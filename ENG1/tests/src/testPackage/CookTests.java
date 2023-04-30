package testPackage;

import com.badlogic.gdx.Input;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import helper.BodyHelper;
import helper.Constants;
import interactions.InputKey;
import interactions.Interactions;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import cooks.Cook;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


@RunWith(GdxTestRunner.class)
public class CookTests {
    @Test
    public void testFoodRelativeMethods() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Rectangle r1 = new Rectangle(0.0f,0.0f,42.50f,20.00f);
        World w1 = new World(new Vector2(0,0), false);
        Cook c1 = new Cook(r1.getWidth(), r1.getHeight() , BodyHelper.createBody(r1.x,r1.y,r1.width,r1.height, false, w1));
        // gameScreen is not used, needs refactoring
        Method privateFoodRelativeX = Cook.class.getDeclaredMethod("foodRelativeX", Cook.Facing.class);
        privateFoodRelativeX.setAccessible(true);
        assertEquals(30F,privateFoodRelativeX.invoke(c1,Cook.Facing.RIGHT));
        assertEquals(-30F,privateFoodRelativeX.invoke(c1,Cook.Facing.LEFT));
        assertEquals(0F,privateFoodRelativeX.invoke(c1,Cook.Facing.UP));
        assertEquals(0F,privateFoodRelativeX.invoke(c1,Cook.Facing.DOWN));
        assertEquals(0F,privateFoodRelativeX.invoke(c1,Cook.Facing.NONE));

        Method privateFoodRelativeY = Cook.class.getDeclaredMethod("foodRelativeY", Cook.Facing.class);
        privateFoodRelativeY.setAccessible(true);
        assertEquals(-14F,privateFoodRelativeY.invoke(c1,Cook.Facing.UP));
        assertEquals(-24F,privateFoodRelativeY.invoke(c1,Cook.Facing.LEFT));
        assertEquals(-24F,privateFoodRelativeY.invoke(c1,Cook.Facing.RIGHT));
        assertEquals(-25F,privateFoodRelativeY.invoke(c1,Cook.Facing.DOWN));
        assertEquals(0F,privateFoodRelativeY.invoke(c1,Cook.Facing.NONE));
    }
    @Test
    public void testOpposite() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException{
        Rectangle r1 = new Rectangle(0.0f,0.0f,42.50f,20.00f);
        World w1 = new World(new Vector2(0,0), false);
        Cook c1 = new Cook(r1.getWidth(), r1.getHeight() , BodyHelper.createBody(r1.x,r1.y,r1.width,r1.height, false, w1));

        Method privateOpposite = Cook.class.getDeclaredMethod("opposite", Cook.Facing.class);
        privateOpposite.setAccessible(true);
        assertEquals(Cook.Facing.UP, privateOpposite.invoke(c1,Cook.Facing.DOWN));
        assertEquals(Cook.Facing.DOWN, privateOpposite.invoke(c1,Cook.Facing.UP));
        assertEquals(Cook.Facing.RIGHT, privateOpposite.invoke(c1,Cook.Facing.LEFT));
        assertEquals(Cook.Facing.LEFT, privateOpposite.invoke(c1,Cook.Facing.RIGHT));
        assertEquals(Cook.Facing.NONE, privateOpposite.invoke(c1,Cook.Facing.NONE));
    }
    @Test
    public void testRotate() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException{
        Rectangle r1 = new Rectangle(0.0f,0.0f,42.50f,20.00f);
        World w1 = new World(new Vector2(0,0), false);
        Cook c1 = new Cook(r1.getWidth(), r1.getHeight() , BodyHelper.createBody(r1.x,r1.y,r1.width,r1.height, false, w1));

        Method privateRotate = Cook.class.getDeclaredMethod("rotate90c", Cook.Facing.class);
        privateRotate.setAccessible(true);
        assertEquals(Cook.Facing.UP, privateRotate.invoke(c1,Cook.Facing.LEFT));
        assertEquals(Cook.Facing.DOWN, privateRotate.invoke(c1,Cook.Facing.RIGHT));
        assertEquals(Cook.Facing.RIGHT, privateRotate.invoke(c1,Cook.Facing.UP));
        assertEquals(Cook.Facing.LEFT, privateRotate.invoke(c1,Cook.Facing.DOWN));
        assertEquals(Cook.Facing.NONE, privateRotate.invoke(c1,Cook.Facing.NONE));
    }

    @Test
    public void testMoveDistance(){
        Interactions.resetKeys();
        Rectangle r1 = new Rectangle(0.0f,100.0f,42.50f,20.00f);
        World w1 = new World(new Vector2(0,0), false);
        Cook c1 = new Cook(r1.getWidth(), r1.getHeight() , BodyHelper.createBody(r1.x,r1.y,r1.width,r1.height, false, w1));

        float prevpos = c1.getBody().getPosition().y;
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_UP, Input.Keys.W), false, false);
        c1.userInput();
        w1.step(1/60f,6,2);
        assertEquals(prevpos + 10 * (1/60f), c1.getBody().getPosition().y, 0.0001);
        Interactions.resetKeys();

        float prevpos2 = c1.getBody().getPosition().y;
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_DOWN, Input.Keys.S), false, false);
        c1.userInput();
        w1.step(1/60f,6,2);
        assertEquals(prevpos2 - 10 * (1/60f), c1.getBody().getPosition().y, 0.0001);
        Interactions.resetKeys();

        float prevpos3 = c1.getBody().getPosition().x;
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_RIGHT, Input.Keys.D), false, false);
        c1.userInput();
        w1.step(1/60f,6,2);
        assertEquals(prevpos3 + 10 * (1/60f), c1.getBody().getPosition().x, 0.0001);
        Interactions.resetKeys();

        float prevpos4 = c1.getBody().getPosition().x;
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_LEFT, Input.Keys.A), false, false);
        c1.userInput();
        w1.step(1/60f,6,2);
        assertEquals(prevpos4 - 10 * (1/60f), c1.getBody().getPosition().x, 0.0001);
        Interactions.resetKeys();

    }
    @Test
    public void testUpdatePosition(){
        Interactions.resetKeys();
        Rectangle r1 = new Rectangle(0.0f,100.0f,42.50f,20.00f);
        World w1 = new World(new Vector2(0,0), false);
        Cook c1 = new Cook(r1.getWidth(), r1.getHeight() , BodyHelper.createBody(r1.x,r1.y,r1.width,r1.height, false, w1));

        assertEquals("This test asserts that the X position (pixel space) is calculated correctly on initialization", 0.0f, c1.getX(), 0.001);
        assertEquals("This test asserts that the Y position (pixel space) is calculated correctly on initialization",100.0f, c1.getY(), 0.001);

        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_UP, Input.Keys.W), false, false);
        c1.userInput();
        w1.step(1/60f,6,2);
        c1.update(0.01f);
        Interactions.resetKeys();

        assertEquals("This test asserts that the X position (pixel space) is calculated correctly after 1 step upwards", 0.0f, c1.getX(), 0.001);
        assertEquals("This test asserts that the Y position (pixel space) is calculated correctly after 1 step upwards",100.0f + ((1/60f) * 10 * Constants.PPM), c1.getY(), 0.001);

        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_RIGHT, Input.Keys.D), false, false);
        c1.userInput();
        w1.step(1/60f,6,2);
        c1.update(0.01f);
        Interactions.resetKeys();

        assertEquals("This test asserts that the X position (pixel space) is calculated correctly after 1 step up and 1 right", 0.0f + ((1/60f) * 10 * Constants.PPM), c1.getX(), 0.001);
        assertEquals("This test asserts that the Y position (pixel space) is calculated correctly after 1 step up and 1 right",100.0f + ((1/60f) * 10 * Constants.PPM), c1.getY(), 0.001);

    }

    @Test
    public void testCookDirection(){
        Rectangle r1 = new Rectangle(0.0f,0.0f,42.50f,20.00f);
        World w1 = new World(new Vector2(0,0), false);
        Cook c1 = new Cook(r1.getWidth(), r1.getHeight() , BodyHelper.createBody(r1.x,r1.y,r1.width,r1.height, false, w1));

        //============================================ONE=KEYPRESS======================================================
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_UP, Input.Keys.W), false, false);
        c1.userInput();
        assertEquals("This test asserts that the cook is facing up, when a COOK_UP input type is input.",Cook.Facing.UP, c1.getDir());
        Interactions.resetKeys();

        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_LEFT, Input.Keys.A), false, false);
        c1.userInput();
        assertEquals("This test asserts that the cook is facing left, when a COOK_LEFT input type is input.",Cook.Facing.LEFT, c1.getDir());
        Interactions.resetKeys();

        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_RIGHT, Input.Keys.D), false, false);
        c1.userInput();
        assertEquals("This test asserts that the cook is facing right, when a COOK_RIGHT input type is input.",Cook.Facing.RIGHT, c1.getDir());
        Interactions.resetKeys();

        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_DOWN, Input.Keys.S), false, false);
        c1.userInput();
        assertEquals("This test asserts that the cook is facing down, when a COOK_DOWN input type is input.",Cook.Facing.DOWN, c1.getDir());
        Interactions.resetKeys();

        // These tests also assert that cooks swap direction accordingly as the inputs change.


        //=============================================TWO=KEYPRESSES===================================================
        //=============================================90==DEGREES======================================================

        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_UP, Input.Keys.W), false, false);
        c1.userInput();
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_LEFT, Input.Keys.A), false, false);
        c1.userInput();
        assertEquals("This test asserts that the cook will switch directions while strafing (Holding up and then holding left)",
                Cook.Facing.LEFT, c1.getDir());
        Interactions.resetKeys();

        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_UP, Input.Keys.W), false, false);
        c1.userInput();
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_RIGHT, Input.Keys.D), false, false);
        c1.userInput();
        assertEquals("This test asserts that the cook will switch directions while strafing (Holding up and then holding right)",
                Cook.Facing.RIGHT, c1.getDir());
        Interactions.resetKeys();


        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_DOWN, Input.Keys.S), false, false);
        c1.userInput();
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_LEFT, Input.Keys.A), false, false);
        c1.userInput();
        assertEquals("This test asserts that the cook will switch directions while strafing (Holding down and then holding left)",
                Cook.Facing.LEFT, c1.getDir());
        Interactions.resetKeys();

        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_DOWN, Input.Keys.S), false, false);
        c1.userInput();
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_RIGHT, Input.Keys.D), false, false);
        c1.userInput();
        assertEquals("This test asserts that the cook will switch directions while strafing (Holding down and then holding right)",
                Cook.Facing.RIGHT, c1.getDir());
        Interactions.resetKeys();


        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_LEFT, Input.Keys.A), false, false);
        c1.userInput();
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_UP, Input.Keys.W), false, false);
        c1.userInput();
        assertEquals("This test asserts that the cook will switch directions while strafing (Holding left and then holding up)",
                Cook.Facing.UP, c1.getDir());
        Interactions.resetKeys();

        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_LEFT, Input.Keys.A), false, false);
        c1.userInput();
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_DOWN, Input.Keys.S), false, false);
        c1.userInput();
        assertEquals("This test asserts that the cook will switch directions while strafing (Holding left and then holding down)",
                Cook.Facing.DOWN, c1.getDir());
        Interactions.resetKeys();


        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_RIGHT, Input.Keys.D), false, false);
        c1.userInput();
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_UP, Input.Keys.W), false, false);
        c1.userInput();
        assertEquals("This test asserts that the cook will switch directions while strafing (Holding right and then holding up)",
                Cook.Facing.UP, c1.getDir());
        Interactions.resetKeys();

        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_RIGHT, Input.Keys.D), false, false);
        c1.userInput();
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_DOWN, Input.Keys.S), false, false);
        c1.userInput();
        assertEquals("This test asserts that the cook will switch directions while strafing (Holding right and then holding down)",
                Cook.Facing.DOWN, c1.getDir());
        Interactions.resetKeys();

        //============================================180==DEGREES======================================================

        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_UP, Input.Keys.W), false, false);
        c1.userInput();
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_DOWN, Input.Keys.S), false, false);
        c1.userInput();
        assertEquals("This test asserts that the cook will not switch directions when the opposite key to the previous movement is held (Holding up then down)",
                Cook.Facing.UP, c1.getDir());
        Interactions.resetKeys();

        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_DOWN, Input.Keys.S), false, false);
        c1.userInput();
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_UP, Input.Keys.W), false, false);
        c1.userInput();
        assertEquals("This test asserts that the cook will not switch directions when the opposite key to the previous movement is held (Holding down then up)",
                Cook.Facing.DOWN, c1.getDir());
        Interactions.resetKeys();

        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_LEFT, Input.Keys.A), false, false);
        c1.userInput();
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_RIGHT, Input.Keys.D), false, false);
        c1.userInput();
        assertEquals("This test asserts that the cook will not switch directions when the opposite key to the previous movement is held (Holding left then right)",
                Cook.Facing.LEFT, c1.getDir());
        Interactions.resetKeys();

        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_RIGHT, Input.Keys.D), false, false);
        c1.userInput();
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_LEFT, Input.Keys.A), false, false);
        c1.userInput();
        assertEquals("This test asserts that the cook will not switch directions when the opposite key to the previous movement is held (Holding right then left)",
                Cook.Facing.RIGHT, c1.getDir());
        Interactions.resetKeys();

        //=======================================THREE==KEYPRESSES======================================================

        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_UP, Input.Keys.W), false, false);
        c1.userInput();
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_LEFT, Input.Keys.A), false, false);
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_RIGHT, Input.Keys.D), false, false);
        c1.userInput();
        assertEquals("This test asserts that the cook will face the direction that does not have its opposite in the inputs array (WAD opposite inputs added last)",
                Cook.Facing.UP, c1.getDir());
        Interactions.resetKeys();

        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_LEFT, Input.Keys.A), false, false);
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_RIGHT, Input.Keys.D), false, false);
        c1.userInput();
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_UP, Input.Keys.W), false, false);
        c1.userInput();
        assertEquals("This test asserts that the cook will face the direction that does not have its opposite in the inputs array (ADW opposite inputs added first)",
                Cook.Facing.UP, c1.getDir());
        Interactions.resetKeys();


        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_DOWN, Input.Keys.S), false, false);
        c1.userInput();
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_LEFT, Input.Keys.A), false, false);
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_RIGHT, Input.Keys.D), false, false);
        c1.userInput();
        assertEquals("This test asserts that the cook will face the direction that does not have its opposite in the inputs array (SAD opposite inputs added last)",
                Cook.Facing.DOWN, c1.getDir());
        Interactions.resetKeys();

        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_LEFT, Input.Keys.A), false, false);
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_RIGHT, Input.Keys.D), false, false);
        c1.userInput();
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_DOWN, Input.Keys.S), false, false);
        c1.userInput();
        assertEquals("This test asserts that the cook will face the direction that does not have its opposite in the inputs array (ADS opposite inputs added first)",
                Cook.Facing.DOWN, c1.getDir());
        Interactions.resetKeys();


        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_LEFT, Input.Keys.A), false, false);
        c1.userInput();
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_UP, Input.Keys.W), false, false);
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_DOWN, Input.Keys.S), false, false);
        c1.userInput();
        assertEquals("This test asserts that the cook will face the direction that does not have its opposite in the inputs array (AWS opposite inputs added last)",
                Cook.Facing.LEFT, c1.getDir());
        Interactions.resetKeys();

        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_UP, Input.Keys.W), false, false);
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_DOWN, Input.Keys.S), false, false);
        c1.userInput();
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_LEFT, Input.Keys.A), false, false);
        c1.userInput();
        assertEquals("This test asserts that the cook will face the direction that does not have its opposite in the inputs array (WSA opposite inputs added first)",
                Cook.Facing.LEFT, c1.getDir());
        Interactions.resetKeys();


        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_RIGHT, Input.Keys.D), false, false);
        c1.userInput();
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_UP, Input.Keys.W), false, false);
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_DOWN, Input.Keys.S), false, false);
        c1.userInput();
        assertEquals("This test asserts that the cook will face the direction that does not have its opposite in the inputs array (DWS opposite inputs added last)",
                Cook.Facing.RIGHT, c1.getDir());
        Interactions.resetKeys();

        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_UP, Input.Keys.W), false, false);
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_DOWN, Input.Keys.S), false, false);
        c1.userInput();
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_RIGHT, Input.Keys.D), false, false);
        c1.userInput();
        assertEquals("This test asserts that the cook will face the direction that does not have its opposite in the inputs array (WSD opposite inputs added first)",
                Cook.Facing.RIGHT, c1.getDir());
        Interactions.resetKeys();

        //=======================================FOUR==KEYPRESSES=======================================================

        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_UP,Input.Keys.W), false, false);
        c1.userInput();
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_LEFT,Input.Keys.A), false, false);
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_RIGHT,Input.Keys.S), false, false);
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_DOWN,Input.Keys.D), false, false);
        c1.userInput();
        assertEquals("This test asserts that the cook will not change directions if all directional inputs are pressed (W then ASD)",Cook.Facing.UP, c1.getDir());
        Interactions.resetKeys();

        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_LEFT,Input.Keys.A), false, false);
        c1.userInput();
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_UP,Input.Keys.W), false, false);
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_DOWN,Input.Keys.S), false, false);
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_RIGHT,Input.Keys.D), false, false);
        c1.userInput();
        assertEquals("This test asserts that the cook will not change directions if all directional inputs are pressed (A then WSD)",Cook.Facing.LEFT, c1.getDir());
        Interactions.resetKeys();

        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_RIGHT,Input.Keys.D), false, false);
        c1.userInput();
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_LEFT,Input.Keys.A), false, false);
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_DOWN,Input.Keys.S), false, false);
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_UP,Input.Keys.W), false, false);
        c1.userInput();
        assertEquals("This test asserts that the cook will not change directions if all directional inputs are pressed (D then ASW)",Cook.Facing.RIGHT, c1.getDir());
        Interactions.resetKeys();

        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_DOWN,Input.Keys.S), false, false);
        c1.userInput();
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_UP,Input.Keys.W), false, false);
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_LEFT,Input.Keys.A), false, false);
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_RIGHT,Input.Keys.D), false, false);
        c1.userInput();
        assertEquals("This test asserts that the cook will not change directions if all directional inputs are pressed (S then WAD",Cook.Facing.DOWN, c1.getDir());
        Interactions.resetKeys();

    }


}
