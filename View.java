
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.input.*;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.util.Random;
import javafx.scene.text.*;
import javafx.util.*;
import javafx.animation.*;
import javafx.scene.media.*;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Arrays; 
import javafx.application.Platform;


/**
    <h2>This manages rendering the canvas and all objects</h2>
    
    <p>The View class creates and manages the GUI for the application.
    It doesn't know anything about the game itself, it just displays
    the current state of the Model, and handles user input</p>
    
    <p>In addition, I constructed some new classes to handle rendering the different windows for the game.  I wanted to have an intro screen 
    which keeps record of high scores and a game over screen so the player could input their name to have it saved to the high score table.</p>
    
    <p>After completing this, I wanted to put some levels in the game, so the player could have a sense of progression.  This necessitated screens 
    to link the levels together, and give the player a small break to prepare for the next level.</p>
    
    <p>As discussed in the {@code Model} Javadocs, I was running code here that ended up in the {@code model.runGame()}.  The only vestige of it is
    some code that ensures the bat does not slip off the side of the window.  I have tried to move it over to the {@code Model} but it errors when 
    it is ran.  I feel it does no harm being in the {@code View}.  If I had more time, I could certainly look at ways of trying to move it out of 
    the {@code View}, but for now it would do more harm than good in trying to solve the problem.</p>
    
    @author Roger Evans (modified by Ryan Light)
 */
public class View implements EventHandler<KeyEvent>
{ 
    /**
        Width of window
    */
    public int width;
    /**
        Height of window
    */
    public int height;
    /**
        The current pane
    */
    public Pane pane;
    /**
        Canvas to draw game on
    */
    public Canvas canvas;
    /**
        Start button in the intro menu
    */
    public Button startBtn;
    /**
        Score info at top of screen
    */
    public Label infoText;
    /**
        Lives info at top of screen
    */
    public Label infoText1;
    /**
        Ready, Steady, Go label
    */
    public Label infoText2;
    /**
        Used by player to input their name
    */
    public TextField name;
    /**
        The controller object
    */
    public Controller controller;
    /**
        The model object
    */
    public Model model;
    /**
        The bat object
    */
    public GameObj bat;
    /**
        The ball object
    */
    public GameObj ball;
    /**
        List of bricks
    */
    public ArrayList<BrickObj> bricks;
    /**
        The players score
    */
    public int score;
    /**
        The players lives
    */
    public int lives;
    /**
        The window object
    */
    public Stage window;
    /**
        Unnecessary variable (will remove if I have time)
    */
    public View thisView = this;
    /**
        The animation thread handle
    */
    public Thread daemon;
    /**
        Checks if the Window has loaded
    */
    public boolean loaded = false;
    /**
        List of players objects, containing their names their high scores
    */
    public ArrayList<Player> topMen = new ArrayList<Player>();
    /**
        <h2>Constructor</h2>
        
        <p>This sets up the window dimensions.</p>

        @author Roger Evans
        
        @param w Width of Window
        @param h Height of Window
        @param win The Window object
    */
    public View(int w, int h, Stage win)
    {
        Debug.trace("View::<constructor>");
        width = w;
        height = h;
        window = win;
    }
    /**
        <h2>This sorts the players into highest score first</h2>
        
        <p>I wanted to use objects to hold the high scores (because there are 2 different data types).
        They are kept in an arrayList, which allows for adding new players.  This code uses a bubble sort to 
        check each player object against the immediate one to their left.  If the one to the left has a smaller score
        then their positions in the array are switched.</p>
        
        <p>I have read about concerns with the efficiency of bubble sort, but for the few objects in this arrayList, 
        I felt it was not worth spending time finding an alternative for the few milliseconds it would save.</p>
        
        <p>With respect to the constant creation of new panes for the different game 'windows'; Roger did question it, 
        but I have not noticed any subsequent problems.  I presume they would be culled by the Garbage Collector, 
        but I don't know enough about it to be sure.  I think it would not be too troublesome to recode it to use the same 
        pane and destroy all the old elements, then make new ones (or hide and unhide as required).  I did not want to do this 
        during development of this small application though because there were enough problems without introducing new ones.  
        I feel though that now the codebase is more stable, it is something that could be looked at 
        (or at least noted for future works).</p>

        @author Ryan Light
        
        @param topMen An array of {@code Player} objects
    */
    public void sortScores(ArrayList<Player> topMen) {
        // sort the topMen arrayList from high score to low using bubble sort
        
        if (topMen.size() > 1) {
            for (int i = 0; i < topMen.size(); i++) {
                for (int j = 0; j < topMen.size(); j++) {
                    // sort to the left neighbour
                    if (j > 0) {
                        Player thisObj = topMen.get(j);
                        Player neighbour = topMen.get(j - 1);
                        if (thisObj.getScore() > neighbour.getScore()) {
                            topMen.set(j, neighbour);
                            topMen.set(j-1,thisObj);
                        }
                    }
                }
            }
        }
    }
    /**
        <h2>This creates labels and positions them on screen</h2>
        
        <p>This is a fairly mundane method to automate the process of making labels.  I just felt it tidied up 
        the code, and makes it easier to read.</p>

        @author Ryan Light
        
        @param pane The current pane
        @param content The message to be added to the label
        @param xPos The x position of the label
        @param yPos The y position of the label
        @return The label object
    */
    public Label addLabel(Pane pane, String content,int xPos,int yPos) {
        Label l = new Label(content);
        l.setTranslateX(xPos);
        l.setTranslateY(yPos);
        pane.getChildren().add(l);
        return l;
    }
    
    /**
        <h2>This creates the Intro Screen</h2>
        
        <p>This method creates the screen that greets the player when they load the game, or exit their current one.
        It houses the high score table.  The top 5 player objects will have their details displayed, with the names on the 
        left and corresponding score on the right (via iterating through {@code topMen} with a {@code for} loop).</p>

        @author Ryan Light
        
        @param window The current Window object
    */
    public void intro(Stage window)
    {
        // create a new pane and set it's ID for the CSS file
        pane = new Pane();
        pane.setId("Breakout");
        
        // canvas object - we set the width and height here (from the constructor), 
        // and the pane and window set themselves up to be big enough
        canvas = new Canvas(width,height);  
        pane.getChildren().add(canvas);
        
        addLabel(pane, "Breakout!", 225, 10);
        addLabel(pane, "Donald Trump Mexican Wall edition", 40, 90);
        
        // start button
        startBtn = new Button("Start game");
        startBtn.setTranslateX(250);
        startBtn.setTranslateY(170);
        startBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                nextLevel(window);
            }
        });
        pane.getChildren().add(startBtn);
        
        // High scores here
        
        addLabel(pane, "High scores", 200, 250);
        
        sortScores(topMen);
        
        int separator = 300;
        for (int i = 0; i < 5 && i < topMen.size(); i++) {
            addLabel(pane, topMen.get(i).getName(), 50, separator);
            addLabel(pane, "" + topMen.get(i).getScore(), 470, separator);
            separator += 40;
        }

        // add the complete GUI to the scene
        Scene scene = new Scene(pane);   
        scene.getStylesheets().add("breakout.css"); // tell the app to use our css file

        // put the scene in the window and display it
        window.setScene(scene);
        if (! loaded) {
            window.show();
            loaded = true;
        }
    }
    
    /**
        <h2>This creates the 'Level ...' Screen</h2>
        
        <p>This method creates the screen that The player sees when they complete a level (or start the game).  It offeres them a small countdown so 
        they have a few seconds to prepare for the next level.  I had some trouble implementing a timer to run the countdown, so I used the following as reference:
        See <a href="Stack Overflow">https://stackoverflow.com/questions/26916640/javafx-not-on-fx-application-thread-when-using-timer</a></p>

        @author Ryan Light
        
        @param window The current Window object
    */
    public void nextLevel(Stage window)
    {
        Debug.trace("nextLevel() called");
        
        pane = new Pane();
        pane.setId("Breakout");
        
        canvas = new Canvas(width,height);  
        pane.getChildren().add(canvas);
        
        addLabel(pane, "Level " + model.getLevel(), 225, 300);
        
        infoText2 = addLabel(pane, "", 225, 350);
        
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    String txt = infoText2.getText();
                    if (txt.equals("")) {
                        infoText2.setText("Ready");
                        infoText2.setStyle("-fx-text-fill: red");
                        model.playSound("sounds/beep.wav");
                    } else if (txt.equals("Ready")) {
                        infoText2.setText("Steady");
                        infoText2.setStyle("-fx-text-fill: orange");
                        model.playSound("sounds/beep.wav");
                    } else if (txt.equals("Steady")) {
                        infoText2.setText("Go!");
                        infoText2.setStyle("-fx-text-fill: green");
                        model.playSound("sounds/beep.wav");
                    } else if (txt.equals("Go!")) {
                        timer.cancel();
                        Debug.trace("timer stopping");
                        thisView.start(window); 
                        daemon = model.startGame();
                    }
                });
            }
        }, 1000, 1000);
        
        Scene scene = new Scene(pane);   
        scene.getStylesheets().add("breakout.css");

        window.setScene(scene);
        if (! loaded) {
            window.show();
            loaded = true;
        }
    }
    
    /**
        <h2>This creates the 'Game Over' Screen</h2>
        
        <p>This method creates the screen that The player uses to see their score and input their name so it can be saved to the 
        high score table.  The button has an eventhandler attached to load the {@code intro()} method and add the player details to  
        a new object in the {@code topMen} arraylist.  It seemed prudent to use event-driven code to achieve this rather than attempt 
        another method.</p>

        @author Ryan Light
        
        @param window The current Window object
    */
    public void gameOver(Stage window)
    {
        pane = new Pane();
        pane.setId("Breakout");
        
        canvas = new Canvas(width,height);  
        pane.getChildren().add(canvas);
        
        addLabel(pane, "Game over", 220, 50);
        addLabel(pane, "Your score", 220, 100);
        addLabel(pane, "" + model.getScore(), 220, 150);
        
        name = new TextField("John Doe");
        name.setTranslateX(220);
        name.setTranslateY(200);
        pane.getChildren().add(name);
        
        startBtn = new Button("Continue");
        startBtn.setTranslateX(225);
        startBtn.setTranslateY(250);
        startBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                topMen.add(new Player(name.getText(),model.getScore()));
                thisView.intro(window);
            }
        });
        pane.getChildren().add(startBtn);

        Scene scene = new Scene(pane);   
        scene.getStylesheets().add("breakout.css");
        
        window.setScene(scene);
        if (! loaded) {
            window.show();
            loaded = true;
        }
    }

    /**
        <h2>This sets the scene for the game</h2>
        
        <p>This method creates the screen that hosts the game objects.  I added a label to hold the player lives in it.
        This accompanies the score label at the top of the screen.</p>
        
        <p>I decided to change the way the key press system works.  Due to how Windows handling key presses, there is some lag between 
        pressing left or right and the bat responding.  I felt this was detrimental to the handling of the game.  This is covered
        in greater detail in the Javadocs for the {@code controller}.  The new system uses a handler to detect {@code KEY_PRESSED}, then sets the 
        bat to move immediately until {@code KEY_RELEASED} is detected.  To make this possible, a {@code setOnKeyReleased} was added here.</p>

        @author Roger Evans (modified by Ryan Light)
        
        @param window The current Window object
    */
    public void start(Stage window) 
    {
        pane = new Pane();
        pane.setId("Breakout");
        
        canvas = new Canvas(width,height);  
        pane.getChildren().add(canvas);
        
        // infoText box for the score - a label which we position on 
        //the canvas with translations in X and Y coordinates
        infoText = addLabel(pane, "Score: " + model.getScore(), 50, 10);
        infoText1 = addLabel(pane, "Lives: " + model.getLives(), 250, 10);

        Scene scene = new Scene(pane);   
        scene.getStylesheets().add("breakout.css"); // tell the app to use our css file

        // Add an event handler for key presses. We use the View object itself
        // and provide a handle method to be called when a key is pressed.
        scene.setOnKeyPressed(this);
        scene.setOnKeyReleased(this);  // EH for stopping bat

        window.setScene(scene);
        if (! loaded) {
            window.show();
            loaded = true;
        }
    }

    /**
        <h2>This passes the key event to the controller</h2>
        
        <p>This method passes the key event to the {@code userKeyInteraction} method in the {@code Controller}.</p>

        @author Roger Evans
        
        @param event The key event
    */
    public void handle(KeyEvent event) {
        controller.userKeyInteraction( event );
    }
    
    /**
        <h2>This draws the game</h2>
        
        <p>This method redraws the canvas, updates the score and lives labels then uses {@code displayGameObj} to redraw:</p>
        
        <ul>
          <li>The ball</li>
          <li>The bat</li>
          <li>The bricks</li>
        </ul>

        @author Roger Evans
    */
    public void drawPicture()
    {
        // the ball movement is runnng 'in the background' so we have
        // add the following line to make sure
        synchronized( Model.class )
        {
            GraphicsContext gc = canvas.getGraphicsContext2D();

            // clear the canvas to redraw
            gc.setFill( Color.BLACK );
            gc.fillRect( 0, 0, width, height );
            
            // update score
            infoText.setText("Score: " + model.getScore());
            infoText1.setText("Lives: " + model.getLives());
            // draw the bat and ball
            displayGameObj( gc, ball );   // Display the Ball
            displayGameObj( gc, bat  );   // Display the Bat

            // display bricks
            
            for (int i = 0; i < bricks.size(); i++)
            {
                displayGameObj( gc, bricks.get(i)); 
            }
        }
    }

    /**
        <h2>This draws an object on the canvas</h2>

        @author Roger Evans
        
        @param gc The graphics context
        @param go An object
    */
    public void displayGameObj( GraphicsContext gc, GameObj go )
    {
        gc.setFill( go.colour );
        gc.fillRect( go.topX, go.topY, go.width, go.height );
    }

    /**
        <h2>This is how the Model talks to the View</h2>
        
        <p>This method gets called BY THE MODEL, whenever the model changes.  It has to do whatever is required to update the GUI to show the new model status</p>
        
        <p>As mentioned at the top of this Javadoc, I have added a section to make the bat respect the sides of the screen.  It checks if the bat is trying to go left
        and that the position of it's top left corner is not less than zero.  If it is, it sets it to zero.  If not travelling left, it repeats the check for the right hand side
        with respect to the right hand edge of the game window and limits it accordingly.</p>

        @author Roger Evans (modified by Ryan Light)
    */
    public void update()
    {
        // Get from the model the ball, bat, bricks & score
        ball    = model.getBall();              // Ball
        bricks  = model.getBricks();            // Bricks
        bat     = model.getBat();               // Bat
        score   = model.getScore();             // Score
        lives = model.getLives();
        
        
        // limit bat position to respect sides of screen
        
        if (model.BAT_DIR < 0) {
            // get position of bat
            if (bat.topX / 2 < 0) {
                model.BAT_DIR = 0;
            }
        } else if (model.BAT_DIR > 0) {
            if (bat.topX + bat.width > model.width) {
                model.BAT_DIR = 0;
            }
        }
        
        model.moveBat(model.BAT_DIR);
        drawPicture();
    }
    /**
        <h2>Gets the current pane</h2>
        
        <p>This method is called by the model, when it needs to spawn a label (particle effect).</p>

        @author Ryan Light
        @return The current pane
    */
    public Pane getPane() {
        return pane;
    }

}
