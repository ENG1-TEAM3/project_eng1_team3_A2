package game;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.HashMap;

/**
 * A class to control the different {@link ScreenAdapter} that the game
 * switches between.
 */
public class ScreenController {

    private final Boot boot;
    private final GameScreen gameScreen;
    private final InstructionScreen instructionScreen;
    private final HashMap<ScreenID, ScreenAdapter> screens;
    private long playTimeDiff;
    private long pauseStartTime;

    /**
     * Screen Controller Constructor
     * The two cameras must be different as they render different pixel spaces at different places.
     * @param boot The class responsible for initializing the first game elements
     * @param gameCam A camera instance (for viewing the game)
     * @param uiCam A camera instance (for viewing UI elements)
     */
    public ScreenController(Boot boot, OrthographicCamera gameCam, OrthographicCamera uiCam) {
        this.boot = boot;

        this.gameScreen = new GameScreen(this,gameCam);
        MenuScreen menuScreen = new MenuScreen(this, uiCam);
        GameOverScreen gameOverScreen = new GameOverScreen(this, uiCam);
        this.instructionScreen = new InstructionScreen(this,uiCam);
        CreditsScreen creditsScreen = new CreditsScreen(this, uiCam);

        this.screens = new HashMap<>();
        this.screens.put(ScreenID.MENU, menuScreen);
        this.screens.put(ScreenID.GAME,gameScreen);
        this.screens.put(ScreenID.GAMEOVER, gameOverScreen);
        this.screens.put(ScreenID.INSTRUCTIONS,instructionScreen);
        this.screens.put(ScreenID.CREDITS, creditsScreen);

        PauseScreen pauseScreen = new PauseScreen(this, uiCam);
        this.screens.put(ScreenID.PAUSE, pauseScreen);
    }

    /**
     * Change the screen of the game to screenID
     * @param screenID The ID of the new screen you want
     */
    public void setScreen(ScreenID screenID) {
        this.boot.setScreen(this.screens.get(screenID));
    }

    /**
     * An intermediate function to get the SpriteBatch from the {@link Boot}.
     * @return {@link SpriteBatch} : {@link SpriteBatch} for the game.
     */
    public SpriteBatch getSpriteBatch() { return boot.getSpriteBatch(); }
    /**
     * An intermediate function to get the {@link ShapeRenderer} from the {@link Boot}.
     * @return {@link ShapeRenderer} : {@link ShapeRenderer} for the game.
     */
    public ShapeRenderer getShapeRenderer() { return boot.getShapeRenderer(); }

    /** The different states that the game can be in.*/
    public enum ScreenID {
        /** The {@link MenuScreen}, where the program opens to. */
        MENU,
        /** The {@link GameScreen}, where the game is played. */
        GAME,
        /** The {@link PauseScreen}, where the game is paused, and the player can
         *  rest, look at instructions and credits, reset or quit. */
        PAUSE,
        /** The {@link GameOverScreen}, which is opened once the game has finished. */
        GAMEOVER,
        /** The {@link InstructionScreen}, where the instructions for the game are displayed. */
        INSTRUCTIONS,
        /** The {@link CreditsScreen}, where the game shows credit for the assets we used
         *  within the game. */
        CREDITS
    }

    /**
     * Get the desired screen from the {@link ScreenController}.
     * @param screenID The {@link ScreenID} of the screen you want.
     * @return {@link ScreenAdapter} : The requested {@link ScreenAdapter}.
     */
    public ScreenAdapter getScreen(ScreenID screenID) {
        return this.screens.get(screenID);
    }

    /** Reset the game to the initial state. */
    public void resetGameScreen() {
        gameScreen.reset();
        instructionScreen.setPrevScreenID(ScreenID.MENU);
    }

    /** Pause the game. */
    public void pauseGameScreen() {
        playTimeDiff = TimeUtils.millis() - gameScreen.getPreviousSecond();
        pauseStartTime = TimeUtils.millis();
        setScreen(ScreenID.PAUSE);
    }

    /** Resume the game from pause.
     * Only call this AFTER {@link #pauseGameScreen()}. */
    public void playGameScreen() {
        gameScreen.setPreviousSecond(TimeUtils.millis()- playTimeDiff);
        gameScreen.addToTimePaused(TimeUtils.millis() - pauseStartTime);
        setScreen(ScreenID.GAME);
    }

    /**
     * Return the unix time in ms of the last game pause
     * @return The time defined above
     */
    public long getPauseStartTime(){
        return pauseStartTime;
    }

}
