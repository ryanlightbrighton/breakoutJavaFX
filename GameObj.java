import javafx.scene.paint.*;

/**
    <h2>This sets up the GameObj class</h2>
    
    @author Roger Evans (modified by Ryan Light)
*/
public class GameObj
{
    /**
        Visibility of object
    */
    public boolean visible  = true;
    /**
        Position - top left corner X
    */
    public double topX   = 0;
    /**
        Position - top left corner Y
    */
    public int topY   = 0;
    /**
        Width of object
    */
    public int width  = 0;
    /**
        Height of object
    */
    public int height = 0;
    /**
        Colour of object
    */
    public Color colour;
    /**
        Direction X (1 or -1)
    */
    public double dirX   = 1;
    /**
        Direction Y (1 or -1)
    */
    public int dirY   = 1;
    /**
        <h2>Constructor</h2>
        
        <p>Default constructor</p>
        
        @author Roger Evans
    */
    public GameObj()
    {
    }
    /**
        <h2>Constructor</h2>
        
        <p>Overloaded constructor</p>
        
        @author Roger Evans
        @param x Position - top left corner X
        @param y Position - top left corner Y
        @param w Width of object
        @param h Height of object
        @param c Colour of object
    */
    public GameObj( int x, int y, int w, int h, Color c )
    {
        topX   = x;       
        topY = y;
        width  = w; 
        height = h; 
        colour = c;
    }

    /**
        <h2>Move in X axis</h2>
        
        @author Roger Evans
        @param units How far to move (px)
    */
    public void moveX( int units )
    {
        topX += units * dirX;
    }

    /**
        <h2>Move in Y axis</h2>
        
        @author Roger Evans
        @param units How far to move (px)
    */
    public void moveY( int units )
    {
        topY += units * dirY;
    }
    /**
        <h2>Change direction of movement in X axis</h2>
        <p>(-1, 0 or +1)</p>
        @author Roger Evans
    */
    public void changeDirectionX()
    {
        dirX = -dirX;
    }

    /**
        <h2>Change direction of movement in Y axis</h2>
        <p>(-1, 0 or +1)</p>
        @author Roger Evans
    */
    public void changeDirectionY()
    {
        dirY = -dirY;
    }

    /**
        <h2>Detect collision between this object and the argument object</h2>
        @author Roger Evans
        @return Is object hit or not
    */
    public boolean hitBy( GameObj obj )
    {
        return ! ( topX >= obj.topX+obj.width     ||
            topX+width <= obj.topX         ||
            topY >= obj.topY+obj.height    ||
            topY+height <= obj.topY );

    }
    
    /**
        <h2>Detect if collision is side or below/above</h2>
        @author Ryan Light
        @return "" = no collision, or  "side" or "vert"
    */ 
    public String whereHitBrick( GameObj obj )
    {
        String hitPart = ""; // no collision assumed at this point
        
        // get centre of objects
        
        // caller
        
        double callerX = topX + (width/2);
        double callerY = topY + (height/2);
        
        // passed obj
        
        double objX = obj.topX + (obj.width/2);
        double objY = obj.topY + (obj.height/2);
        
        if (callerY >= objY + (obj.height/2)) {
            // bottom
            hitPart = "vert";
        }
        if (callerY <= objY - (obj.height/2)) {
            // top
            hitPart = "vert";
        }
        if (callerX <= objX - (obj.width/2)) {
            // left
            hitPart = "side";
        }
        if (callerX >= objX + (obj.width/2)) {
            // right
            hitPart = "side";
        }
        return hitPart;
    }
}
