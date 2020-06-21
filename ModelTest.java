import java.util.ArrayList;
import javafx.scene.paint.*;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.media.*;
import java.io.File;
import java.util.Random;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.util.*;
import javafx.scene.control.*;
import javafx.animation.*;
import javafx.scene.transform.Rotate;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.Scene;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class ModelTest.
 *
 * @author  Ryan Light
 * @version 1
 */
public class ModelTest
{
    /**
     * Default constructor for test class ModelTest
     */
    public ModelTest()
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
    @Test
    public void ConstructorTest() {
        for (int i= 0; i < 100; i++) {
            Model model = new Model(i,i,null);
            assertNotNull(model);
            assertEquals(i,model.width,0);
            assertEquals(i,model.height,0);
        }
    }
    Model model = new Model(0,0,null);
    // cannot create sounds so don't test or it would fail anyway
    @Test
    public void SoundTest() {
        model.playSound("sounds/speedup.wav");
    }
    @Test
    public void SoundTest2() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                model.playSound("sounds/speedup.wav");
            }
        });
    }
    // cannot create particles so don't test or it would fail anyway
    Pane p = new Pane();
    @Test
    public void MessageTest() {
        model.brickScore(p, 0, 0,"test", 1000);
    }
    @Test
    public void MessageTest2() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                model.brickScore(p, 0, 0,"test", 1000);
            }
        });
    }
    @Test
    public void SetGameRunningTest() {
        for (int i = 0; i < 1000; i++) {
            if (i % 2 == 0) {
                model.setGameRunning(true);
                assertEquals(true,model.gameRunning);
            } else {
                model.setGameRunning(false);
                assertEquals(false,model.gameRunning);
            }
        }
    }
    @Test
    public void GetGameRunningTest() {
        model.setGameRunning(false);
        boolean b = model.getGameRunning();
        assertEquals(false,b);
    }
    @Test
    public void GetScoreTest() {
        model.score = 69;
        int i = model.getScore();
        assertEquals(69,i,0);
    }
    @Test
    public void AddToScoreTest() {
        model.score = 1;
        for (int i = 1; i < 10; i++) {
            model.addToScore(1);
            assertEquals(i + 1,model.getScore(),0);
        }
    }
    @Test
    public void GetLivesTest() {
        for (int i = 1; i < 10; i++) {
            model.lives = i;
            int l = model.getLives();
            assertEquals(i,l,0);
        }
        
    }
    @Test
    public void AddToLivesTest() {
        model.lives = 0;
        for (int i = 0; i < 10; i++) {
            model.addToLives(1);
            assertEquals(i + 1,model.getLives(),0);
        }
    }
    @Test
    public void GetLevelTest() {
        for (int i = 1; i < 10; i++) {
            model.level = i;
            int l = model.getLevel();
            assertEquals(i,l,0);
        }
    }
    @Test
    public void SetLevelTest() {
        for (int i = -1000; i < 1000; i++) {
            model.setLevel(i);
            assertEquals(i,model.getLevel(),0);
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
        model = null;
        p = null;
    }
}
