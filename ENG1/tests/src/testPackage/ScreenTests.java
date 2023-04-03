package testPackage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import cooks.Cook;
import game.Boot;
import game.ScreenController;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import interactions.Interactions;
import interactions.InputKey;
@RunWith(GdxTestRunner.class)
public class ScreenTests {
    @Test
    public void testFirstScreen(){
        Boot b1 = Boot.getInstance();
        b1.createHeadless();
        assertEquals("This test asserts that the game opens to the MenuScreen", "MenuScreen",b1.getScreen().getClass().getSimpleName());
    }
    @Test
    public void swapScreenTest(){
        Boot b1 = Boot.getInstance();
        b1.createHeadless();
        b1.getScreenController().setScreen(ScreenController.ScreenID.CREDITS);
        assertEquals("This test asserts that the swaps to the MenuScreen", "CreditsScreen",b1.getScreen().getClass().getSimpleName());
        b1.getScreenController().setScreen(ScreenController.ScreenID.GAMEOVER);
        assertEquals("This test asserts that the swaps to the MenuScreen", "GameOverScreen",b1.getScreen().getClass().getSimpleName());
        b1.getScreenController().setScreen(ScreenController.ScreenID.GAME);
        assertEquals("This test asserts that the swaps to the MenuScreen", "GameScreen",b1.getScreen().getClass().getSimpleName());
        b1.getScreenController().setScreen(ScreenController.ScreenID.INSTRUCTIONS);
        assertEquals("This test asserts that the swaps to the MenuScreen", "InstructionScreen",b1.getScreen().getClass().getSimpleName());
        b1.getScreenController().setScreen(ScreenController.ScreenID.MENU);
        assertEquals("This test asserts that the swaps to the MenuScreen", "MenuScreen",b1.getScreen().getClass().getSimpleName());
        b1.getScreenController().setScreen(ScreenController.ScreenID.PAUSE);
        assertEquals("This test asserts that the swaps to the MenuScreen", "PauseScreen",b1.getScreen().getClass().getSimpleName());

    }



}
