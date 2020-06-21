import javafx.scene.paint.*;
/**
 * Class for bat objects
 *
 * @author (Ryan Light)
 * @version (27/02/2019)
 */
public class BatObj extends GameObj
{
    /**
    	<h2>Constructor</h2>
    			
    	@author Ryan Light
    	@param x Position - top left corner X
    	@param y Position - top left corner Y
    	@param w Width of object
    	@param h Height of object
    	@param c Colour of object
    */
    public BatObj( int x, int y, int w, int h, Color c )
    {
        topX   = x;       
        topY = y;
        width  = w; 
        height = h; 
        colour = c;
    }
    //int batDirection = 0;
}