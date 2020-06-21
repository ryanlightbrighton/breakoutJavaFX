import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Screen;
import javafx.stage.Stage;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class MainTest.
 *
 * @author  Ryan Light
 * @version 1
 */
public class MainTest
{
    /**
     * Default constructor for test class MainTest
     */
    public MainTest()
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
    // no point checking this because it cannot be created
    Main mainProg = new Main();
    @Test
    public void StartTest() {
        mainProg.start(null);
    }
    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @After
    public void tearDown()
    {
        mainProg = null;
    }
}
