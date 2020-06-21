
/**
 * <h2>Holds player name and their score</h2>
 *
 * @author Ryan Light
 * @version 23/04/2019
 */
public class Player
{
    /**
    	Players name
    */
    private String name;
    /**
    	Players score
    */
    private int score;

    /**
    	<h2>Constructor</h2>
    	
    	<p>Default constructor</p>
    	
    	@author Ryan Light
    */
    public Player()
    {
        name = "none";
        score = 0;
    }
    /**
    	<h2>Constructor</h2>
    	
    	<p>Overloaded constructor</p>
    	
    	@author Ryan Light
    	@param x Player name
    	@param y score
    */
    public Player(String x, int y)
    {
        name = x;
        score = y;
    }
	
	// note there are no methods to set score or name because it is done in the constuctor

    /**
    	<h2>Gets the name of the player</h2>
    	
    	@author Ryan Light
    	@return players name
    */
    public String getName()
    {
        return name;
    }
    /**
    	<h2>Gets the score of the player</h2>
    	
    	@author Ryan Light
    	@return players score
    */
    public int getScore()
    {
        return score;
    }
}
