import javafx.scene.paint.*;
import java.util.ArrayList;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class BatObjTest.
 *
 * @author  Ryan Light
 * @version 1
 */
public class BatObjTest
{
    /**
     * Default constructor for test class BatObjTest
     */
    public BatObjTest()
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
    public ArrayList<BatObj> objs = new ArrayList<>();
    @Test
    public void ConstructorTest() {
        for (int i= 0; i < 100; i++) {
            objs.add(new BatObj(i,i,i,i,Color.GREEN));
            assertEquals(i,objs.get(i).topX,0);
            assertEquals(i,objs.get(i).topY,0);
            assertEquals(i,objs.get(i).width,0);
            assertEquals(i,objs.get(i).height,0);
            assertEquals(Color.GREEN,objs.get(i).colour);
        }
    }
    public ArrayList<BatObj> objs3 = new ArrayList<>();
    @Test
    public void ConstructorColourTest() {
        for (int i = 0; i < 100; i++) {
            double d = i / 100;
            objs3.add(new BatObj(0,0,0,0,Color.color(d,d,d)));
            assertEquals(Color.color(d,d,d),objs3.get(i).colour);
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
        objs = null;
        objs3 = null;
    }
}
