package testPackage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import cooks.Cook;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import interactions.Interactions;
import interactions.InputKey;
@RunWith(GdxTestRunner.class)
public class FirstTest {
    @Test
    public void firstTest(){
        assertTrue("This test checks whether a file exists", Gdx.files.internal("cooks/control.png").exists());
    }
    @Test
    public void secondTest(){
        assertTrue("This test checks whether a second file exists", Gdx.files.internal("cooks/hold left.png").exists());
    }
    @Test
    public void thirdTest(){
        assertEquals(Cook.testTest(1), 2);
    }
    @Test
    public void fourthTest(){
        assertTrue("This test shouldnt fail", Gdx.files.internal("cooks/normal left.png").exists());
    }
    @Test
    public void fifthTest(){
        assertTrue("This test shouldnt also fail", Gdx.files.internal("cooks/normal right.png").exists());
    }
    @Test
    public void sixthTest(){
        assertFalse(Interactions.isPressed(InputKey.InputTypes.COOK_UP));
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.COOK_UP, Input.Keys.W), false, false);
        assertTrue(Interactions.isPressed(InputKey.InputTypes.COOK_UP));
        Interactions.resetKeys();
        assertFalse(Interactions.isPressed(InputKey.InputTypes.COOK_UP));
    }

}
