import java.util.ArrayList;
import javafx.scene.paint.*;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.media.*;
import java.io.File;
import java.util.Random;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.util.*;
import javafx.scene.control.*;
import javafx.animation.*;
import javafx.scene.transform.Rotate;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.Scene;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
/**
    <h2>This manages the state of the game</h2>
    
    <p>The model represents all the actual content and functionality of the app
    For Breakout, it manages all the game objects that the View needs
    (the bat, ball, bricks, and the score), provides methods to allow the Controller
    to move the bat (and a couple of other functions - change the speed or stop 
    the game), and runs a background process (a 'thread') that moves the ball 
    every 20 milliseconds and checks for collisions.</p>
    
    @author Roger Evans (modified by Ryan Light)
*/
public class Model 
{
    /**
        Border round the edge of the panel
    */
    public int B = 6;
    /**
        Height of the menu bar area at the top of the screen
    */
    public int M = 40;
    /**
        Ball size
    */
    public int BALL_SIZE = 30;
    /**
        Bat width
    */
    public int BAT_WIDTH = 50;
    /**
        Bat height
    */
    public int BAT_HEIGHT = 30;
    /**
        Brick width
    */
    public int BRICK_WIDTH = 50;
    /**
        Brick height
    */
    public int BRICK_HEIGHT = 30;
    /**
        Distance to move the bat on each update
    */
    public int BAT_MOVE = 5;
    /**
        Units to move the ball on each step
    */
    public int BALL_MOVE = 8;
    /**
        Score for hitting a brick
    */
    public int HIT_BRICK = 50;
    /**
        Score (penalty) for hitting the bottom of the screen
    */
    public int HIT_BOTTOM = -200;
    /**
        Indicates if bat is travelling Left or Right
    */
    public int BAT_DIR = 0;
    /**
        Reference to the view object
    */
    View view;
    /**
        Reference to the controller object
    */
    Controller controller;
    /**
        The ball object
    */
    public GameObj ball;
    /**
        Array of the bricks
    */
    public ArrayList<BrickObj> bricks = new ArrayList<>();
    /**
        The bat object
    */
    public BatObj bat;
    /**
        The score
    */
    public int score = 0;
    /**
        Player lives remaining
    */
    public int lives = 3;
    /**
        Which level is being played
    */
    public int level = 1;
    /**
        {@code True} if game is running {@code false} if not.  Can be set to stop the game
    */ 
    public boolean gameRunning = true;
    /**
        Depreciated
    */
    public boolean fast = false;
    /**
        System time in milliseconds when the level commenced
    */
    public long start = System.currentTimeMillis();
    /**
        Interval in seconds until the game next speeds the ball up
    */
    public int nextSpeedup = 30;
    /**
        Width of game window
    */
    public int width;
    /**
        Height of game window
    */
    public int height;
    /**
        The model needs access to window to launch "game over" screen
    */
    public Stage window;
    

    /**
        <h2>Constructor</h2>
        
        <p>The constructor saves the window dimensions.</p>
        
        @author Roger Evans
        
        @param w Width of window
        @param h Height of window
        @param st Window object
    */
    public Model( int w, int h, Stage st )
    {
        Debug.trace("Model::<constructor>");  
        width = w; 
        height = h;
        window = st;
    }
    
    /**
        <h2>Plays sound file</h2>
        
        <p>Uses string as path to sound file</p>
        
        @author Ryan Light
        
        @param musicFile path in local folder to music file
    */
    
    public void playSound(String musicFile) {
        Media sound = new Media(new File(musicFile).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }

    /**
        <h2>This sets up the game world</h2>
        
        <ul>
          <li>Initialises game variables</li>
          <li>Spawns all game objects</li>
          <li>Selects level</li>
        </ul>
                
        @author Roger Evans (modified by Ryan Light)
    */
    
    public void initialiseGame()
    {       
        BAT_DIR = 0;
        start = System.currentTimeMillis();
        nextSpeedup = 30;
        BALL_MOVE = 8;
        ball = new GameObj(
            width/2,
            height / 4,
            BALL_SIZE,
            BALL_SIZE,
            Color.RED
        );
        bat = new BatObj(
            width/2,
            height - BAT_HEIGHT*3/2,//height - 20,
            BAT_WIDTH*3, 
            BRICK_HEIGHT/4,
            Color.GRAY
        );
        int rows = 200;
        switch (level) {
            case 1: score = 0;
                    lives = 3;
                    Debug.trace("MODEL Setting up L1");
                    break;
            case 2: rows = 300;
                    Debug.trace("MODEL Setting up L2");
                    break;
            case 3: rows = 400;
                    Debug.trace("MODEL Setting up L3");
                    break;
        }
        bricks = new ArrayList<>();
        for (int i = B; i < width; i+= 60)
        {
            for (int j =  2 * M; j < rows; j+= 100)
            {
                bricks.add(new BrickObj(i, j/2, BRICK_WIDTH, BRICK_HEIGHT, Color.GREEN ));
            }
        }
        Debug.trace("count bricks: " + bricks.size());
    }

    /**
        <h2>Starts animation thread</h2>
        
        <p>Starts animation thread and returns the thread handle.  I added the return so I could have access to the thread when I was runnning the game stop code in the 
        {@code View}.  Now the code to halt the game is back in the {@code Model}, the return is now kept as legacy code but largely unneeded.</p>
        
        @author Roger Evans (modified by Ryan Light)
        
        @return The thread handle
    */
    
    public Thread startGame()
    {
        
        Thread t = new Thread( this::runGame );
        t.setDaemon(true);
        t.start();
        
        return t;
    }
       
    /**
        Random number generator
        
        @author Ryan Light
    */
    public Random generator = new Random();
    
    /**
        <h2>This spawns a label as a particle</h2>
        
        <p>Spawns a message on screen as a particle effect.
        The message will rise up for the duration set in the params, then delete itself.</p>
        
        @author Ryan Light
        
        @param p The pane to register this particle effect to
        @param xPos The x position on the window to place this particle
        @param yPos The y position on the window to place this particle
        @param message The contents of the message
        @duration The length of time in milliseconds the particle stays before being deleted 
    */
    
    public synchronized void brickScore(Pane p, double xPos, double yPos,String message, int duration) {
        Label score = new Label(message);
        score.getStyleClass().clear();
        score.setFont(Font.font("Arial", FontWeight.BOLD, 10));
        score.setScaleX(4);
        score.setScaleY(4);
        Color scoreColor = Color.rgb(250,100,39);
        DropShadow ds = new DropShadow();
        ds.setOffsetY(3);
        ds.setColor(Color.rgb(0,0,0));
        score.setEffect(ds);
        InnerShadow is = new InnerShadow();
        is.setOffsetX(2);
        is.setOffsetY(2);
        is.setColor(Color.rgb(250,237,39));
        score.setEffect(is);
        score.setTextFill(scoreColor);
        score.setTranslateX(xPos);
        score.setTranslateY(yPos);
        p.getChildren().add(score);
        Timeline timeline = new Timeline();
        KeyFrame move = new KeyFrame(
            Duration.millis(duration),
            new KeyValue(score.translateXProperty(), xPos + 5 - generator.nextInt(10)),
            new KeyValue(score.translateYProperty(), yPos - 70)
        );
        timeline.getKeyFrames().add(move);
        timeline.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                p.getChildren().remove(score);
            }
        });
        timeline.play();
    }
    
    /**
        <h2>This spawns an asterisk label as a particle</h2>
    
        <p>Spawns a 'spark' (asterisk) on screen with a random colour and direction.
        The spark will move towards this direction, then delete itself once the move is complete.</p>
        
        @author Ryan Light
        
        @param p The pane to register this particle effect to
        @param xPos The x position on the window to place this particle
        @param yPos The y position on the window to place this particle
        @param dur The length of time in milliseconds the particle takes to complete the move 
        @param scale The base size of this particle
        @param scaleVariance Number to randomly select the size of the particle
        @param color the r g b values for the spark in the form of an array
    */
    
    public synchronized void spark(Pane p, double xPos, double yPos, int dur, int scale, int scaleVariance, int[] color) {
        Label spark = new Label("*");
        spark.getStyleClass().clear();
        spark.setScaleX(scale + generator.nextInt(scaleVariance) / 10);
        spark.setScaleY(scale + generator.nextInt(scaleVariance) / 10);
        Color sparkColor = Color.rgb(color[0],color[1],color[2]);
        spark.setTextFill(sparkColor);
        spark.setTranslateX(xPos);
        spark.setTranslateY(yPos);
        p.getChildren().add(spark);
        Timeline timeline = new Timeline();
        KeyFrame move = new KeyFrame(
            Duration.millis(300),
            new KeyValue(spark.translateXProperty(), xPos + 50 - generator.nextInt(100)),
            new KeyValue(spark.translateYProperty(), yPos + 50 - generator.nextInt(100))
        );
        timeline.getKeyFrames().add(move);
        timeline.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                p.getChildren().remove(spark);
            }
        });
        timeline.play();
    }
    
    /**
        <h2>Creates a cluster of particle effects</h2>
        
        <p>This is used to spawn impact sparks when the ball collides with a brick, and also to make a trail for the ball
        When called, it makes 10 calls to the {@code spark()} function and generates a randomised colour for each one.</p>
        
        @author Ryan Light
        
        @param p The pane to register this particle effect to
        @param xPos The x position on the window to place this particle
        @param yPos The y position on the window to place this particle
        @param dur The length of time in milliseconds the particle takes to complete the move 
        @param scale The base size of this particle
        @param scaleVariance Number to randomly select the size of the particle
        @param color the r g b values for the spark in the form of an array
    */
    
    public synchronized void addSparks(GameObj obj, int duration, int scale, int scaleVariance, int[] rgb) {
        for (int i=0; i<10; i++) {
            double xPos;
            double yPos;
            Pane p = view.getPane();
            /*
                if this is a brick, select the centre.
                If it is the ball then select somewhere random on the ball object
            */
            if (obj instanceof BrickObj) { 
                xPos = obj.topX + (obj.width / 2);
                yPos = obj.topY + (obj.height / 2);
            } else {
                xPos = obj.topX + generator.nextInt(obj.width);
                yPos = obj.topY + generator.nextInt(obj.height);
            }
            int[] color = {generator.nextInt(rgb[0]),generator.nextInt(rgb[1]),generator.nextInt(rgb[2])};
            
            spark(
                p,
                xPos,
                yPos,
                duration,
                scale,
                scaleVariance,
                color
            );
        }
    }
   
    
    /**
        <h2>This is the main animation loop for the game.</h2>
        
        <p>This runs the {@code initialiseGame()} method, then updates the game state and screen in a loop every few milliseconds by running 
        {@code updateGame()} and {@code modelChanged()}.
        The section after was added by me to select the appropriate 'new level' pane or 'game over' pane based on the players outcome.
        Initially, I had this code running in the View object, but there were synchronisation issues and sometimes a second instance of the game 
        would spawn.  After checking with Roger, he suggested relocating the code back to the {@code Model} because the {@code View} should only be for rendering 
        the changes, and not making them.</p>
        
        <p>Moving the code to here allows the game to finish completely before the new pane is loaded.  I managed to debug this using a combination of breakpoints and 
        {@code Debug.trace()} calls.  By placing debug messages at the start of every function, it is possible to see which ones have not been called when they should 
        and (in this case) which ones are firing multiple times.  Once I found the culprits, I then set breakpoints in likely places to follow the chain of events.<p>
        
        <p>I made a test build of the game for doing this because it can be quite destructive when commenting out code and adding in test code.  Due to the nature of the 
        problem (that it only happened after the first level was complete and the game had been played through at least once), I had to destroy the bricks by script in 
        order to finish the level quicker and inspect the results.  I feel is quite hard to even discover these kind of problems, as they require in depth testing 
        (often in a live environment), which means playtesting the game over and over.  However, I did manage to uncover some other minor issues while trying to reproduce 
        this problem, which made the endeavour more fruitful.</p>
        
        @author Roger Evans (modified by Ryan Light)
    */
        
    public void runGame()
    {
        Debug.trace("runGame called");
        initialiseGame();
        try
        {
            setGameRunning(true);
            while (getGameRunning())
            {
                updateGame();
                modelChanged();
                Thread.sleep( getFast() ? 10 : 20 );
            }
            // section added to select level
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if (lives == 0) {
                        Debug.trace("Game over man, Game over!"); 
                        setLevel(1);
                        setGameRunning(false);
                        view.gameOver(window);
                        playSound("sounds/game_over.wav");
                    } else if (bricks.size() == 0) {
                        setGameRunning(false);
                        int level = getLevel();
                        Debug.trace("Ended current level: " + level);
                        switch (level) {
                        case 1: Debug.trace("Running L1 end code");
                                setLevel(2);
                                addToLives(1);
                                view.nextLevel(window);
                                break;
                        case 2: Debug.trace("Running L2 end code");
                                setLevel(3);
                                addToLives(1);
                                view.nextLevel(window);
                                break;
                        case 3: Debug.trace("Running L3 end code");
                                setLevel(1);
                                view.gameOver(window);
                                playSound("sounds/winner.wav");
                                break;
                        }  
                    }
                }
            });
        } catch (Exception e) 
        {
            Debug.error("Model::runAsSeparateThread error: " + e.getMessage() );
        }
    }
  
    /**
        <h2>This updates the state of the game world.</h2>
        
        <p>This method deals with the following:</p>
        
        <ul>
          <li>Checks if the ball needs to be sped up (every 30 seconds)</li>
          <li>Moves the ball</li>
          <li>Checks for collision with walls</li>
          <li>Checks for collision with the bat</li>
          <li>Checks for collision with walls</li>
          <li>Checks for collision with the floor</li>
          <li>Checks for collision with the roof</li>
          <li>Checks for collision with any bricks</li>
          <li>Stops the game if the player is out of lives</li>
          <li>Ends the level if the bricks are all destroyed</li>
        </ul>
        
        <p>I decided to add some code to speed up the ball periodically.  This increases a sense of urgency in the game and adds challenge for the player.  I added an 
        audio cue, and also a particle label so they can see the game has decided to speed up the ball in case they miss the audio cue in the clutter.
        Additionally a visual cue was added when the ball strikes the floor, so players can see instantly that they have lost a life.  I found during testing that if I 
        glanced up at the status bar, then I would lose track of the ball and make a mistake.</p>
        
        <p>During testing, I found that the ball could wedge itself in the walls/ceiling/floor and appear to hover.  This became more apparent as the speed of the 
        game was increased.  I deduced (from some debug hints) that the ball had gone so far into the surface that every frame it was changing direction, but never 
        moving enough to be free of the surface it was trapped in.</p>
        
        <p>I decided to add some additional checks to make sure that the ball only ever reversed direction if it was heading into the surface.  For example, if the ball 
        strikes the left wall, and is moving left, then flip the x direction.  But if the ball is in the left wall and moving right, then do nothing so it can move clear
        of the wall.</p>
        
        <p>I also moved the code that checks for collision with the bat above the block of code that checks for collision with the floor to give it priority.  
        Although this is personal preference, I wanted to give players the benefit of the doubt if there was a contestable decision.  There was an issue where the ball 
        would get trapped under the bat and drain multiple lives from the player, but this change in conjunction with the checks for direction and collision seem to have 
        allieviated the problem.</p>
        
        <p>The code to check collisions with the bricks is a standard loop to go through all the brick objects and check for intersection with the ball object.  
        If a collision is found, then a further function determines if the brick was hit on the side or on the (top/bottom).  I added this in because the ball would
        flip its Y direction if it struck the side of a brick, which seemed counter-intuitive for gameplay.</p>
        
        <p>I feel this could be improved in terms of performance however.  It seems wasteful to check for collisions with every object, especially when some of them may 
        be on the other side of the pane.  I was trialling a 'safe zone' which is laid out after the bricks spawn and therefore if the ball is within it, then there is no 
        need to check for collisions with the bricks.  An alternative would be to store the positions of the bricks immediate neighbours in the brick object, so if a 
        collision was determined to have happened with that certain brick, then only the other brick objects near it would need to be checked.  Unfortunately though, I ran 
        out of time and had to prioritise making making the game system work as a whole, and not just one really good aspect at the detriment of everything else.</p>
        
        <p>The bricks have an armour value which degrades every time the ball strikes them.  This is stored as a property in each brick object.  
        They are coloured green initially, then as their armour degrades, it turns orange, then red.  
        Once it is red, the next successive hit will destroy them.  When a brick is hit by the ball, a sound effect will play and some impact sparks 
        will be spawned at the brick position.  I wanted to add this to add some atmosphere and fun for the player as well as giving them some audial and visual feedback 
        on their actions.  Additionally, a label particle is spawned which informs the player how many points they have accrued.</p>
        
        <p>I also made a modification to the way the ball responds to the bat.  Normally, the movement vector would be something like [1,1], [1,-1],[-1,-1] or [-1,1].  
        This is to say that the ball only ever moves in diagonals.  I added some code into the method, so the ball would respond to the bat direction.  This allows the 
        player to change the direction of the ball when it is struck, much the same as a snooker player might try and slice a shot.  I would have liked to expand on
        this more and replace the entire movement system with vectors, but my experiments were not fruitful enough in the time I had available.</p>
        
        <p>The last modification was to run a check to see if the bricks have been totally destoyed, or if the player is out of lives.  In either case, it sets 
        {@code setGameRunning(false)} so the loop can quit then run the pane select code after.</p>
        
        @author Roger Evans (modified by Ryan Light)
    */
    

    public synchronized void updateGame()
    {
        // speed game up as time goes on
        
        long currentTime = System.currentTimeMillis();
        float sec = (currentTime - start) / 1000F;
        int secInt = (int)sec;
        if (nextSpeedup == secInt) {
            BALL_MOVE += 3;
            nextSpeedup += 30;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    playSound("sounds/speedup.wav");
                    brickScore(view.getPane(),ball.topX,ball.topY,"Speedup!",1000);
                }
            });
        }
        
        // move the ball one step (the ball knows which direction it is moving in)
        
        ball.moveX(BALL_MOVE);                      
        ball.moveY(BALL_MOVE);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                int[] rgb = {255,50,50};
                addSparks(ball,(generator.nextInt(75)+25)*10,3,2,rgb);
            }
        });
        
        // get the current ball position (top left corner)
        
        double x = ball.topX;  
        int y = ball.topY;
        
        // check for collision with walls
        // Attempt to stop ball bouncing left and right continuously off the same wall
        
        boolean hitRightWall = x >= width - B - BALL_SIZE;
        boolean hitLeftWall = x <= 0 + B;
        if (hitLeftWall && ball.dirX < 0 || hitRightWall && ball.dirX > 0)  {
            ball.changeDirectionX();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    playSound("sounds/coin.wav");
                }
            });
        }
        
        // check whether ball has hit the bat.  Moved this above checking if it hit the floor
        
        if ( ball.hitBy(bat) ) {
            if (ball.dirY > 0) {
                ball.changeDirectionY();
            }
            // try some code to alter vector
            // Debug.trace("bat dir" + this.BAT_DIR);
            if (this.BAT_DIR > 0) {
                ball.dirX = ball.dirX - 0.3;  
                if (ball.dirX < -1) {ball.dirX = -1;}
            } else if (this.BAT_DIR < 0) {
                ball.dirX = ball.dirX + 0.3;  
                if (ball.dirX > 1) {ball.dirX = 1;}
            }
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    playSound("sounds/hit.wav");
                }
            });
        }

        // check if ball has hit the floor
        
        if (y >= height - B - BALL_SIZE && ball.dirY > 0) { 
            ball.changeDirectionY();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    playSound("sounds/lose_life.wav");
                    brickScore(view.getPane(),ball.topX,ball.topY,"-1 Life",1000);
                }
            });
            addToScore( HIT_BOTTOM );
            addToLives(-1);
            if (getLives() == 0) {
                setGameRunning(false);
            }
        }
        
        // check if ball has hit the roof

        if (y <= 0 + M && ball.dirY < 0)  {
            ball.changeDirectionY();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    playSound("sounds/coin.wav");
                }
            });
        }
        
        // loop through all bricks to check if the ball is in contact
        // If so, damage the brick
        
        for (int i = 0; i < bricks.size(); i++) {
            BrickObj brick = bricks.get(i);
            if (ball.hitBy(brick)) {
                String hitPart = ball.whereHitBrick(brick);
                if (hitPart.equals("side")) {
                    ball.changeDirectionX();
                } else if (hitPart.equals("vert")) { 
                    ball.changeDirectionY();
                }
                int damage = brick.getDamage(); 
                switch(damage) {
                    case 3: brick.setDamage(-1);
                            brick.setColor(Color.ORANGE);
                            break;
                    case 2: brick.setDamage(-1);
                            brick.setColor(Color.RED);
                            break;
                    default:bricks.remove(brick);
                            break;
                }
                
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        int[] rgb = {255,255,255};
                        addSparks(brick,(generator.nextInt(75)+25)*10,8,5,rgb);
                        brickScore(view.getPane(),brick.topX,brick.topY,"+" + HIT_BRICK,500);
                        playSound("sounds/blast.wav");
                    }
                });
                
                addToScore( HIT_BRICK );
                break;
            }
        }
        
        // stop the game if there are no more bricks or the player has run out of lives
       
        if (lives == 0 || bricks.size() == 0) {
            setGameRunning(false);
        }
    }

    /**
        <h2>This is how the Model talks to the View</h2>
        
        <p>Whenever the {@code Model} changes, this method calls the {@code update} method in the {@code View}. It needs to run in the JavaFX event thread, and {@code Platform.runLater} 
        is a utility that makes sure this happens even if called from the {@code runGame} thread</p>

        @author Roger Evans
    */

    public synchronized void modelChanged()
    {
        Platform.runLater(view::update);
    }
    
    /*
        Methods for accessing and updating values
        these are all synchronized so that the can be called by the main thread 
        or the animation thread safely
    */
    
    /**
        <h2>Change game running state</h2>
        
        <p>set to {@code false} to stop the game</p>

        @author Roger Evans
    */
    public synchronized void setGameRunning(Boolean value)
    {  
        gameRunning = value;
    }
    
    /**
        <h2>Return game running state</h2>

        @author Roger Evans
    */
    // Return game running state
    public synchronized Boolean getGameRunning()
    {  
        return gameRunning;
    }

    /**
        <h2>Change game speed</h2>
        
        <p>{@code false} is normal speed, {@code true} is fast</p>

        @author Roger Evans
    */
    public synchronized void setFast(Boolean value)
    {  
        fast = value;
    }
    

    /**
        <h2>Return game speed</h2>
        
        <p>{@code false} is normal speed, {@code true} is fast</p>

        @author Roger Evans
        
        @return game speed
    */
    public synchronized Boolean getFast()
    {  
        return(fast);
    }

    /**
        <h2>Return bat object</h2>

        @author Roger Evans (modified by Ryan Light)
        
        @return bat object
    */
    public synchronized BatObj getBat()
    {
        return(bat);
    }
    
    /**
        <h2>Return ball object</h2>

        @author Roger Evans
        
        @return ball object
    */
    public synchronized GameObj getBall()
    {
        return(ball);
    }
    
    /**
        <h2>Return bricks</h2>

        @author Roger Evans (modified by Ryan Light)
        
        @return arraylist of bricks
    */
    public synchronized ArrayList<BrickObj> getBricks()
    {
        return(bricks);
    }
    
    /**
        <h2>Return score</h2>

        @author Roger Evans
        
        @return score
    */
    public synchronized int getScore()
    {
        return(score);
    }
    
    /**
        <h2>Updates the score</h2>

        @author Roger Evans
    */
    public synchronized void addToScore(int n)    
    {
        score += n;        
    }
    
    /**
        <h2>Gets the remaining lives</h2>

        @author Ryan Light
        
        @return number of lives
    */
    public synchronized int getLives()
    {
        return lives;
    }
    /**
        <h2>Adds to remaining lives</h2>
        
        <p>Can be positive or negative</p>

        @author Ryan Light
    */
    public synchronized void addToLives(int n)
    {
        lives += n;        
    }
    /**
        <h2>Move the bat one step</h2>
        
        <p>-1 is left, +1 is right</p>

        @author Roger Evans
    */
    public synchronized void moveBat( int direction )
    {        
        int dist = direction * BAT_MOVE;
        bat.moveX(dist);
    }
    /**
        <h2>Gets the level</h2>

        @author Ryan Light
        
        @return level (numerical)
    */
    public int getLevel() {
        return level;
    }
    /**
        <h2>Sets the level</h2>
        
        <p>Sets the level to the numerical input</p>

        @author Ryan Light
    */
    public void setLevel(int lev) {
        level = lev;
    }
    
}   
    