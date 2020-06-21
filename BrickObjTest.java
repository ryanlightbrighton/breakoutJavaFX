import javafx.scene.paint.*;
import java.util.ArrayList;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class BrickObjTest.
 *
 * @author  Ryan Light
 * @version 1
 */
public class BrickObjTest
{
    /**
     * Default constructor for test class BrickObjTest
     */
    public BrickObjTest()
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
    
    public ArrayList<BrickObj> objs = new ArrayList<>();
    @Test
    public void ConstructorTest() {
        for (int i= 0; i < 100; i++) {
            objs.add(new BrickObj(i,i,i,i,Color.GREEN));
            assertEquals(i,objs.get(i).topX,0);
            assertEquals(i,objs.get(i).topY,0);
            assertEquals(i,objs.get(i).width,0);
            assertEquals(i,objs.get(i).height,0);
            assertEquals(Color.GREEN,objs.get(i).colour);
        }
    }
    public ArrayList<BrickObj> objs3 = new ArrayList<>();
    @Test
    public void ConstructorColourTest() {
        for (int i = 0; i < 100; i++) {
            double d = i / 100;
            objs3.add(new BrickObj(0,0,0,0,Color.color(d,d,d)));
            assertEquals(Color.color(d,d,d),objs3.get(i).colour);
        }
    }
   
    BrickObj myTestObj = new BrickObj(500,500,50,30,Color.GREEN);
    @Test
    public void GetDamageTest() {
        int damage = myTestObj.getDamage();
        assertEquals(3,damage,0);
    }
    @Test
    public void SetDamageTest() {
        myTestObj.damage = 3;
        myTestObj.setDamage(-3);
        assertEquals(0,myTestObj.getDamage(),0);
    }
    @Test
    public void SetColourTest() {
        for (int r= 0; r < 256; r++) {
            for (int g= 0; g < 256; g++) {
                for (int b= 0; b < 256; b++) {
                       myTestObj.setColor(Color.rgb(r,g,b));
                       assertEquals(Color.rgb(r,g,b),myTestObj.colour);
                       
                }
            }
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
        myTestObj = null;
    }
}
