import javafx.scene.paint.*;
/**
 * Class for brick objects
 *
 * @author Ryan Light
 * @version (27/02/2019)
 */
public class BrickObj extends GameObj
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
    public BrickObj( int x, int y, int w, int h, Color c )
    {
        topX   = x;       
        topY = y;
        width  = w; 
        height = h; 
        colour = c;
    }
    int damage = 3;
    /**
    	<h2>Gets damage level of brick</h2>
    	
    	@author Ryan Light
    	@return how much health they have
    */
    public int getDamage() {
        return damage;
    }
    /**
    	<h2>Sets damage level of brick</h2>
    	
    	@author Ryan Light
    	@param d Damage to add or subtract
    */
    public void setDamage(int d) {
        damage += d;
    }
    /**
    	<h2>Sets colour of brick</h2>
    	
    	@author Ryan Light
    	@param a colour
    */
    public void setColor(Color c) {
        colour = c;
    }
}
