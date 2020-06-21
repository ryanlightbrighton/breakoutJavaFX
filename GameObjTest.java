import javafx.scene.paint.*;
import java.util.ArrayList;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class GameObjTest.
 *
 * @author  Ryan Light
 * @version 1
 */
public class GameObjTest
{
    /**
     * Default constructor for test class GameObjTest
     */
    public GameObjTest()
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
    public ArrayList<GameObj> objs = new ArrayList<>();
    @Test
    public void ConstructorTest() {
        for (int i= 0; i < 100; i++) {
            objs.add(new GameObj(i,i,i,i,Color.GREEN));
            assertEquals(i,objs.get(i).topX,0);
            assertEquals(i,objs.get(i).topY,0);
            assertEquals(i,objs.get(i).width,0);
            assertEquals(i,objs.get(i).height,0);
            assertEquals(Color.GREEN,objs.get(i).colour);
        }
    }
    public ArrayList<GameObj> objs3 = new ArrayList<>();
    @Test
    public void ConstructorColourTest() {
        for (int i = 0; i < 100; i++) {
            double d = i / 100;
            objs3.add(new GameObj(0,0,0,0,Color.color(d,d,d)));
            assertEquals(Color.color(d,d,d),objs3.get(i).colour);
        }
    }
    GameObj myTestObj = new GameObj(0,0,50,30,Color.GREEN);
    @Test
    public void DirectionChangeTest() {
        myTestObj.changeDirectionX();
        myTestObj.changeDirectionY();
        assertEquals(-1,myTestObj.dirX,0);
        assertEquals(-1,myTestObj.dirY,0);
    }
    @Test
    public void MovementTest() {
        // reset position of myGameObj
        myTestObj.topX = 0;
        myTestObj.topY = 0;
        myTestObj.moveX(1000);
        myTestObj.moveY(50);
        assertEquals(1000,myTestObj.topX,0);
        assertEquals(50,myTestObj.topY,0);
    }
    GameObj myTestObj2 = new GameObj(500,500,50,30,Color.GREEN);
    @Test
    public void CollisionTest () {
        myTestObj.topX = 0;
        myTestObj.topY = 0;
        boolean collision = myTestObj.hitBy(myTestObj2);
        assertEquals(false,collision);
    }
    @Test
    public void CollisionTest2 () {
        myTestObj.topX = 500;
        myTestObj.topY = 500;
        boolean collision = myTestObj.hitBy(myTestObj2);
        assertEquals(true,collision);
    }
    @Test
    public void CollisionTestMiss () {
        myTestObj.topX = 0;
        myTestObj.topY = 0;
        String hitPart = "";
        if (myTestObj.hitBy(myTestObj2)) {
            hitPart = myTestObj.whereHitBrick(myTestObj2);
        }
        assertEquals("",hitPart);
    }
    @Test
    public void CollisionTestSide () {
        myTestObj.topX = 540; // 20 pixels on the right of myTestObj2
        myTestObj.topY = 500;
        String hitPart = "";
        if (myTestObj.hitBy(myTestObj2)) {
            hitPart = myTestObj.whereHitBrick(myTestObj2);
        }
        assertEquals("side",hitPart);
    }
    @Test
    public void CollisionTestBelow () {
        myTestObj.topX = 500;
        myTestObj.topY = 520; // 20 pixels below myTestObj2
        String hitPart = "";
        if (myTestObj.hitBy(myTestObj2)) {
            hitPart = myTestObj.whereHitBrick(myTestObj2);
        }
        assertEquals("vert",hitPart);
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
        myTestObj2 = null;
    }
}
