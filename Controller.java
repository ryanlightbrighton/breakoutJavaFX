import javafx.scene.input.KeyEvent;


/**
    <h2>This handles and directs key events</h2>
    
    <p>The breakout controller receives KeyPress events from the GUI (via
    the KeyEventHandler). It maps the keys onto methods in the model and
    calls them appropriately</p>
    
    @author Roger Evans (modified by Ryan Light)
*/
public class Controller {
    public Model model;
    public View  view;
    /**
        <h2>Constructor</h2>
        
        @author Roger Evans
    */
    public Controller() {
        Debug.trace("Controller::<constructor>");
    }
    
    
    /**
        <h2>This inturprets key events</h2>
        
        <p>This is how the View talks to the Controller
        and how the Controller talks to the Model.
        This method is called by the View to respond to key presses in the GUI
        The controller's job is to decide what to do. In this case it converts
        the keypresses into commands which are run in the model</p>
        
        <p>As mentioned in the {@code View} Javadoc, I didn't like the delay when holding down the keys in order to 
        move the bat.  This is caused by Windows default key behaviour so isn't something that can be 'fixed' in the game.
        After doing some research, I decided to implement another key handler, which would detect key released.  When the left or 
        right arrows are pushed, the game will set the {@code BAT_DIR} value to positive or negative accordingly, then the {@code model.moveBat()}
        method will move it.  When the key is released, {@code BAT_DIR} will be set to zero, which will stop the bat.  This stops the inherent delay
        due to Windows pausing to determine if a key is tapped or held down.  I also tied in the {@code CTRL} button, which acts as a speed modifier
        when that is held down as the bat is moved.  I'm happy with the results and the bat feels much more responsive now.</p>
        
        <p>I also removed the fast and slow buttons because I wanted the game to dictate the pace of the ball (by speeding up over time), and I didn't want the
        user to countermand that decision.</p>
        
        @author Roger Evans (modified by Ryan Light)
        
        @param event The key event
    */
    public void userKeyInteraction(KeyEvent event) {
        switch (event.getCode()) {
            case LEFT:
            if (event.getEventType() == KeyEvent.KEY_PRESSED) {
                if (event.isControlDown()) {
                    model.BAT_DIR = -3;
                } else {
                    model.BAT_DIR = -1;
                }
            } else if (event.getEventType() == KeyEvent.KEY_RELEASED) {
                model.BAT_DIR = 0;
            }
            break;
            case RIGHT:
            if (event.getEventType() == KeyEvent.KEY_PRESSED) {
                if (event.isControlDown()) {
                    model.BAT_DIR = 3;
                } else {
                    model.BAT_DIR = 1;
                }
            } else if (event.getEventType() == KeyEvent.KEY_RELEASED) {
                model.BAT_DIR = 0;
            }
            break;
            case S :
            // stop the game
            model.setGameRunning(false);
            break;
        }
    }
}
