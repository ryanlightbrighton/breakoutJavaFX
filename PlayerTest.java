import java.util.ArrayList;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class PlayerTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class PlayerTest
{
    /**
     * Default constructor for test class PlayerTest
     */
    public PlayerTest()
    {
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp()
    {
    }
    public ArrayList<Player> objs = new ArrayList<>();
    
    // getName and getScore used because name and score variables have been made private
    
    @Test
    public void ConstructorTest() {
        for (int i= 0; i < 100; i++) {
            objs.add(new Player());
            assertEquals("none",objs.get(i).getName());
            assertEquals(0,objs.get(i).getScore());
        }
    }
    public ArrayList<Player> objs2 = new ArrayList<>();
    @Test
    public void ConstructorTest2() {
        for (int i= 0; i < 10000; i++) {
            objs2.add(new Player(" " + i, i));
            assertEquals(" " + i,objs2.get(i).getName());
            assertEquals(i,objs2.get(i).getScore(),0);
        }
    }
    Player guy = new Player("Dave",45);
    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @After
    public void tearDown()
    {
        objs = null;
        objs2 = null;
    }
}
