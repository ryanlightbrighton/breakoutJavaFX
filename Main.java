import javafx.application.Application;
import javafx.stage.Stage;
import java.util.ArrayList;

/**
    <h2>This is the Main class of the Breakout project</h2>
    
    @author Roger Evans (modified by Ryan Light)
*/
public class Main extends Application
{
    public boolean newGame = true;
    /**
    	<h2>This is the Main method</h2>
    	
    	<p>The main method only gets used when launching from the command line
    	launch initialises the system and then calls start
    	In BlueJ, BlueJ calls start itself</p>
    	
    	@author Roger Evans
    */
    public static void main( String args[] )
    {
        launch(args);
    }
    /**
    	<p>The {@code start} method does the following:</p>
    	
    	<ul>
    		<li>Sets up height and width of the window</li>
    		<li>Sets up debugging for the project</li>
    		<li>Creates the Model, View and Controller objects</li>
    		<li>Links them all together</li>
    		<li>Loads the intro screen</li>
    	</ul>
    	
    	@author Roger Evans (modified by Ryan Light)
    */
    public void start(Stage window) 
    {
        int H = 800;         // Height of window pixels 
        int W = 600;         // Width  of window pixels 

        Debug.set(true);             
        Debug.trace("breakoutJavaFX starting"); 
        Debug.trace("Main::start"); 

        Model model = new Model(W,H,window);
        View  view  = new View(W,H,window);
        Controller controller  = new Controller();

        model.view = view;
        model.controller = controller;
        controller.model = model;
        controller.view = view;
        view.model = model;
        view.controller = controller;
        
        view.intro(window);

        Debug.trace("breakoutJavaFX running"); 
    }
}
