package testPackage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import cooks.Cook;
import game.*;
import helper.BodyHelper;
import org.junit.Assert;
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
    @Test
    public void instructionScreenInputTest(){
        Interactions.resetKeys();
        Boot b1 = Boot.getInstance();
        b1.createHeadless();
        b1.getScreenController().setScreen(ScreenController.ScreenID.INSTRUCTIONS);
        InstructionScreen is1 = (InstructionScreen) b1.getScreenController().getScreen(ScreenController.ScreenID.INSTRUCTIONS);
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.INSTRUCTIONS, Input.Keys.I), false, true);
        is1.update(0.001f, false);
        assertEquals("This test asserts that the game swaps to the MenuScreen after the instructions key is pressed on the InstructionsScreen",
                "MenuScreen", b1.getScreen().getClass().getSimpleName());

        b1.getScreenController().setScreen(ScreenController.ScreenID.INSTRUCTIONS);
        is1.setPrevScreenID(ScreenController.ScreenID.GAME);
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.INSTRUCTIONS, Input.Keys.I), true, true);
        is1.update(0.001f, false);
        assertEquals("This test asserts that InstructionScreen swaps to GameScreen with GameScreen as its previous screen ID",
                "GameScreen", b1.getScreen().getClass().getSimpleName());
    }
    @Test
    public void creditsScreenInputTest(){
        Interactions.resetKeys();
        Boot b1 = Boot.getInstance();
        b1.createHeadless();
        b1.getScreenController().setScreen(ScreenController.ScreenID.CREDITS);
        CreditsScreen cs1 = (CreditsScreen) b1.getScreenController().getScreen(ScreenController.ScreenID.CREDITS);
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.CREDITS, Input.Keys.C), false, true);
        cs1.update(0.001f, false);
        assertEquals("This test asserts that the game swaps to the MenuScreen after the credits key is pressed on the CreditsScreen",
                "MenuScreen", b1.getScreen().getClass().getSimpleName());

        b1.getScreenController().setScreen(ScreenController.ScreenID.CREDITS);
        cs1.setPrevScreenID(ScreenController.ScreenID.GAME);
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.CREDITS, Input.Keys.I), true, true);
        cs1.update(0.001f, false);
        assertEquals("This test asserts that CreditsScreen swaps to GameScreen with GameScreen as its previous screen ID",
                "GameScreen", b1.getScreen().getClass().getSimpleName());
    }

    @Test
    public void pauseScreenInputTest(){
        Interactions.resetKeys();
        Boot b1 = Boot.getInstance();
        b1.createHeadless();
        b1.getScreenController().setScreen(ScreenController.ScreenID.PAUSE);
        PauseScreen ps1 = (PauseScreen) b1.getScreenController().getScreen(ScreenController.ScreenID.PAUSE);
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.CREDITS, Input.Keys.C), false, true);
        ps1.update(0.001f, false);
        assertEquals("This test asserts that the pause screen swaps to the credits screen",
                "CreditsScreen", b1.getScreen().getClass().getSimpleName());

        b1.getScreenController().setScreen(ScreenController.ScreenID.PAUSE);
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.INSTRUCTIONS, Input.Keys.I), true, true);
        ps1.update(0.001f, false);
        assertEquals("This test asserts that the pause screen swaps to the instructions screen",
                "InstructionScreen", b1.getScreen().getClass().getSimpleName());


        b1.getScreenController().setScreen(ScreenController.ScreenID.GAME);
        GameScreen gs1 = (GameScreen) b1.getScreenController().getScreen(ScreenController.ScreenID.GAME);
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.PAUSE, Input.Keys.ESCAPE), true, true);
        gs1.update(0.001f, false);
        assertEquals("This test asserts that the game screen swaps to the pause screen",
                "PauseScreen", b1.getScreen().getClass().getSimpleName());

        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.UNPAUSE, Input.Keys.ESCAPE), true, true);
        ps1.update(0.001f, false);
        assertEquals("This test asserts that the pause screen swaps to the game screen",
                "GameScreen", b1.getScreen().getClass().getSimpleName());

        b1.getScreenController().setScreen(ScreenController.ScreenID.PAUSE);
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.RESET_GAME, Input.Keys.R), true, true);
        ps1.update(0.001f, false);
        assertEquals("This test asserts that the pause screen resets back to the menu screen",
                "MenuScreen", b1.getScreen().getClass().getSimpleName());
    }

    @Test
    public void gameOverScreenInputTest(){
        Interactions.resetKeys();
        Boot b1 = Boot.getInstance();
        b1.createHeadless();
        b1.getScreenController().setScreen(ScreenController.ScreenID.GAMEOVER);
        GameOverScreen gos1 = (GameOverScreen) b1.getScreenController().getScreen(ScreenController.ScreenID.GAMEOVER);
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.RESET_GAME, Input.Keys.R), true, true);
        gos1.update(0.001f, false);
        assertEquals("This test asserts that the Game Over screen swaps to the menu screen on reset",
                "MenuScreen", b1.getScreen().getClass().getSimpleName());
    }

    @Test
    public void MenuScreenInputTest(){
        Interactions.resetKeys();
        Boot b1 = Boot.getInstance();
        b1.createHeadless();
        b1.getScreenController().setScreen(ScreenController.ScreenID.MENU);
        MenuScreen ms1 = (MenuScreen) b1.getScreenController().getScreen(ScreenController.ScreenID.MENU);
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.INSTRUCTIONS, Input.Keys.I), true, true);
        ms1.update(0.001f, false);
        assertEquals("This test asserts that the menu screen swaps to the instruction screen",
                "InstructionScreen", b1.getScreen().getClass().getSimpleName());

        b1.getScreenController().setScreen(ScreenController.ScreenID.MENU);
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.CREDITS, Input.Keys.C), true, true);
        ms1.update(0.001f, false);
        assertEquals("This test asserts that the menu screen swaps to the credits screen",
                "CreditsScreen", b1.getScreen().getClass().getSimpleName());

        b1.getScreenController().setScreen(ScreenController.ScreenID.MENU);
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.START_GAME, Input.Keys.ENTER), true, true);
        ms1.update(0.001f, false);
        assertEquals("This test asserts that the menu screen swaps to the game screen",
                "GameScreen", b1.getScreen().getClass().getSimpleName());
    }

    @Test
    public void testDrawQueueComparator(){
        GameScreen.DrawQueueComparator dq1 = new GameScreen.DrawQueueComparator();
        Rectangle r1 = new Rectangle(0.0f,0.0f,42.50f,20.00f);
        Rectangle r2 = new Rectangle(0.0f,100.0f,42.50f,20.00f);
        Rectangle r3 = new Rectangle(0.0f,200.0f,42.50f,20.00f);
        World w1 = new World(new Vector2(0,0), false);
        Cook c1 = new Cook(r1.getWidth(), r1.getHeight() , BodyHelper.createBody(r1.x,r1.y,r1.width,r1.height, false, w1), null);
        Cook c2 = new Cook(r2.getWidth(), r2.getHeight() , BodyHelper.createBody(r2.x,r2.y,r2.width,r2.height, false, w1), null);
        Cook c3 = new Cook(r3.getWidth(), r3.getHeight() , BodyHelper.createBody(r3.x,r3.y,r3.width,r3.height, false, w1), null);
        Array<Cook> cooksort = new Array<>();
        cooksort.add(c3);
        cooksort.add(c2);
        cooksort.add(c1);
        Array<Cook> cooksunsort = new Array<>();
        cooksunsort.add(c2);
        cooksunsort.add(c1);
        cooksunsort.add(c3);
        cooksunsort.sort(dq1);
        assertEquals(cooksort,cooksunsort);
    }
}
