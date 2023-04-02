package testPackage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import game.Boot;
import game.GameScreen;
import game.ScreenController;
import helper.BodyHelper;
import helper.Constants;
import interactions.InputKey;
import interactions.Interactions;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.mock;
import com.badlogic.gdx.graphics.GL20;

import static org.junit.Assert.*;
import cooks.Cook;
import helper.MapHelper;
import org.mockito.Mockito;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;

@RunWith(GdxTestRunner.class)
public class CookTests {
    @Test
    public void testFoodRelativeMethods() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Rectangle r1 = new Rectangle(0.0f,0.0f,42.50f,20.00f);
        World w1 = new World(new Vector2(0,0), false);
        Cook c1 = new Cook(r1.getWidth(), r1.getHeight() , BodyHelper.createBody(r1.x,r1.y,r1.width,r1.height, false, w1), null);
        // gameScreen is not used, needs refactoring
        Method privateFoodRelativeX = Cook.class.getDeclaredMethod("foodRelativeX", Cook.Facing.class);
        privateFoodRelativeX.setAccessible(true);
        assertEquals(privateFoodRelativeX.invoke(c1,Cook.Facing.RIGHT), 30F);
        assertEquals(privateFoodRelativeX.invoke(c1,Cook.Facing.LEFT), -30F);
        assertEquals(privateFoodRelativeX.invoke(c1,Cook.Facing.UP), 0F);
        assertEquals(privateFoodRelativeX.invoke(c1,Cook.Facing.DOWN), 0F);
        assertEquals(privateFoodRelativeX.invoke(c1,Cook.Facing.NONE), 0F);

        Method privateFoodRelativeY = Cook.class.getDeclaredMethod("foodRelativeY", Cook.Facing.class);
        privateFoodRelativeY.setAccessible(true);
        assertEquals(privateFoodRelativeY.invoke(c1,Cook.Facing.UP), -14F);
        assertEquals(privateFoodRelativeY.invoke(c1,Cook.Facing.LEFT), -24F);
        assertEquals(privateFoodRelativeY.invoke(c1,Cook.Facing.RIGHT), -24F);
        assertEquals(privateFoodRelativeY.invoke(c1,Cook.Facing.DOWN), -25F);
        assertEquals(privateFoodRelativeY.invoke(c1,Cook.Facing.NONE), 0F);
    }
    @Test
    public void testMoveDistance(){
        Interactions.resetKeys();
        Rectangle r1 = new Rectangle(0.0f,100.0f,42.50f,20.00f);
        World w1 = new World(new Vector2(0,0), false);
        Cook c1 = new Cook(r1.getWidth(), r1.getHeight() , BodyHelper.createBody(r1.x,r1.y,r1.width,r1.height, false, w1), null);

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
    public void testCookDirection(){
        Rectangle r1 = new Rectangle(0.0f,0.0f,42.50f,20.00f);
        World w1 = new World(new Vector2(0,0), false);
        Cook c1 = new Cook(r1.getWidth(), r1.getHeight() , BodyHelper.createBody(r1.x,r1.y,r1.width,r1.height, false, w1), null);

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
        assertEquals("This test asserts that the cook will face the direction that does not have its opposite in the inputs array (opposite inputs added last)",
                Cook.Facing.UP, c1.getDir());
        Interactions.resetKeys();

        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_LEFT, Input.Keys.A), false, false);
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_RIGHT, Input.Keys.D), false, false);
        c1.userInput();
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_UP, Input.Keys.W), false, false);
        c1.userInput();
        assertEquals("This test asserts that the cook will face the direction that does not have its opposite in the inputs array (opposite inputs added first)",
                Cook.Facing.UP, c1.getDir());
        Interactions.resetKeys();
        // Could add the other 6 combinations for a more concrete test

        //=======================================FOUR==KEYPRESSES=======================================================

        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_UP,Input.Keys.W), false, false);
        c1.userInput();
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_LEFT,Input.Keys.A), false, false);
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_RIGHT,Input.Keys.S), false, false);
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_DOWN,Input.Keys.D), false, false);
        c1.userInput();
        assertEquals("This test asserts that the cook will not change directions if all directional inputs are pressed",Cook.Facing.UP, c1.getDir());
        // Could add the other 3 combinations for a more concrete test
    }


}
