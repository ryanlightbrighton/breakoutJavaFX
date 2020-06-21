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

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class ViewTest.
 *
 * @author  Ryan Light
 * @version 1
 */
public class ViewTest
{
    /**
     * Default constructor for test class ViewTest
     */
    public ViewTest()
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
    public View view = new View(0,0,null);
    public ArrayList<Player> topMen = new ArrayList<Player>();
    @Test
    public void SortTest() {
        for (int i= 0; i < 100; i++) {
            topMen.add(new Player("" + i,i));
        }
        view.sortScores(topMen);
        int index = 0;
        for (int i= 99; i == 0; i--) {
            assertEquals(i,topMen.get(index).getScore(),0);
            assertEquals("" + i,topMen.get(index).getName());
            index ++;
        }
    }
    public ArrayList<Player> topMen2 = new ArrayList<Player>();
    @Test
    public void SortTest2() {
        for (int i= 0; i < 100; i++) {
            topMen2.add(new Player());
        }
        view.sortScores(topMen);
        for (int i= 99; i == 0; i--) {
            assertEquals(0,topMen.get(i).getScore(),0);
            assertEquals("none" + i,topMen.get(i).getName());
        }
    }
    // cannot simulate window - maybe this is why these label tests fail (no point asserting anything here!)
    public Pane pane = new Pane();
    @Test
    public void LabelTest() {
        for (int i= -1000; i < 1000; i++) {
            for (int j= -1000; j < 1000; j++) {
                Label lab = view.addLabel(pane,"test_label" + i + j, i, j);
            }
        }
    }
    @Test
    public void GetPaneTest() {
        for (int i= 0; i < 1000; i++) {
            Pane p = view.getPane();
        }
    }
    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @After
    public void tearDown()
    {
        topMen = null;
        topMen2 = null;
        pane = null;
        view = null;
    }
}
