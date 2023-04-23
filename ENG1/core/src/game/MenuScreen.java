package game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import game.ScreenController.ScreenID;
import helper.Constants;
import interactions.InputKey;
import interactions.Interactions;

/**
 * The {@link MenuScreen}, which provides the player with
 * a few options of inputs, which do different things.
 * One of which is to change to the {@link GameScreen} and
 * play the game.
 */
public class MenuScreen extends ScreenAdapter {
    public enum menuState {
        MAIN_MENU,
        MODE_SELECT
    }
    public enum modeSelectionState{
        SELECT_DIFFICULTY,
        SELECT_MODE
    }

    public enum mode {
        ENDLESS,
        SCENARIO
    }

    public enum difficulty{
        EASY,
        MEDIUM,
        HARD
    }

    private ScreenController screenController;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Viewport viewport;

    private Stage mainMenuStage;

    private Stage modeSelectStage;

    private menuState currentState;
    private modeSelectionState currentSelectionType;
    private mode currentModeSelection;
    private difficulty currentDifficultySelection;
    private Sprite backgroundSprite;
    private BitmapFont bitmapFont;

    private Label modeSelectLabel;

    /**
     * The constructor for the {@link MenuScreen}.
     * @param screenController The {@link ScreenController} of the {@link ScreenAdapter}.
     * @param orthographicCamera The {@link OrthographicCamera} that the game should use.
     */
    public MenuScreen(ScreenController screenController, OrthographicCamera orthographicCamera) {
        bitmapFont = new BitmapFont();

        currentState = menuState.MAIN_MENU;
        currentDifficultySelection = difficulty.EASY;
        currentModeSelection = mode.SCENARIO;
        currentSelectionType = modeSelectionState.SELECT_MODE;

        this.backgroundSprite = new Sprite(new Texture("Maps/StartMenuBackground.png"));
        backgroundSprite.setSize(Constants.V_Width, Constants.V_Height);

        Label.LabelStyle font = new Label.LabelStyle(bitmapFont, Color.WHITE);
        Table table = new Table();
        table.center();
        table.setFillParent(true);

        Label welcomeLabel = new Label("UNDERCOOKED", font);
        table.add(welcomeLabel).expandX();
        table.row();

        Label startLabel = new Label(String.format("PRESS %s TO SELECT GAME MODE",Interactions.getKeyString(InputKey.InputTypes.MODE_SELECT).toUpperCase()), font);
        table.add(startLabel).expandX();
        table.row();

        Label instructionLabel = new Label(String.format("PRESS %s FOR INSTRUCTIONS",Interactions.getKeyString(InputKey.InputTypes.INSTRUCTIONS).toUpperCase()), font);
        table.add(instructionLabel).expandX();
        table.row();

        Label creditLabel = new Label(String.format("PRESS %s TO VIEW CREDITS",Interactions.getKeyString(InputKey.InputTypes.CREDITS).toUpperCase()), font);
        table.add(creditLabel).expandX();
        table.row();

        Label quitLabel = new Label(String.format("PRESS %s TO QUIT",Interactions.getKeyString(InputKey.InputTypes.QUIT).toUpperCase()), font);
        table.add(quitLabel).expandX();

        welcomeLabel.setFontScale(4);

        this.screenController = screenController;
        this.camera = orthographicCamera;
        this.batch = screenController.getSpriteBatch();

        viewport = new FitViewport(Constants.V_Width, Constants.V_Height, camera);

        // AS2 NEW CHANGE - MODE SELECT SCREEN


        Table table2 = new Table();
        table2.center();
        table2.setFillParent(true);
        Label l00 = new Label("Press the arrow keys to select a mode", font);
        Label l01 = new Label(String.format("Press %s to go back",Interactions.getKeyString(InputKey.InputTypes.MODE_SELECT)), font);
        Label l0 = new Label("           ^                          ^", font);
        modeSelectLabel = new Label(getSelectionString(), font);
        Label l2 = new Label("              V                                V", font);
        Label l3 = new Label(String.format("Press %s to start game",Interactions.getKeyString(InputKey.InputTypes.START_GAME)), font);
        table2.add(l00).expandX();
        table2.row();
        table2.add(l01).expandX();
        table2.row();
        table2.row();
        table2.add(l0).expandX();
        table2.row();
        table2.add(modeSelectLabel).expandX();
        table2.row();
        table2.add(l2).expandX();
        table2.row();
        table2.add(l3).expandX();
        l0.setFontScale(5);
        modeSelectLabel.setFontScale(3);
        l2.setFontScale(4);

        // AS2 NEW CHANGE - Addition of null check for testing

        if (this.batch != null) {
            mainMenuStage = new Stage(viewport, batch);
            mainMenuStage.addActor(table);
            modeSelectStage = new Stage(viewport, batch);
            modeSelectStage.addActor(table2);
        }
    }

    /**
     * Check for user input every frame and act on specified inputs.
     * @param delta The time between frames as a float.
     */
    public void update(float delta, boolean shouldResetKeys) {
        Interactions.updateKeys(shouldResetKeys);

        // Set the screen to the instructions screen
        if (Interactions.isJustPressed(InputKey.InputTypes.INSTRUCTIONS)) {
            screenController.setScreen(ScreenID.INSTRUCTIONS);
        }
        else if (Interactions.isJustPressed(InputKey.InputTypes.CREDITS)) {
            ((CreditsScreen)screenController.getScreen(ScreenID.CREDITS)).setPrevScreenID(ScreenID.MENU);
            screenController.setScreen(ScreenID.CREDITS);
        }
        else if (Interactions.isJustPressed(InputKey.InputTypes.MODE_SELECT)) {
            setCurrentScreenState(this.getOtherScreenState());
        }
        else if (Interactions.isJustPressed(InputKey.InputTypes.QUIT)) {
            Gdx.app.exit();
        }


        if (currentState == menuState.MODE_SELECT){
            if (Interactions.isJustPressed(InputKey.InputTypes.COOK_LEFT)){
                if (currentSelectionType == modeSelectionState.SELECT_DIFFICULTY){
                    currentSelectionType = modeSelectionState.SELECT_MODE;
                }
            }
            if (Interactions.isJustPressed(InputKey.InputTypes.COOK_RIGHT)){
                if (currentSelectionType == modeSelectionState.SELECT_MODE){
                    currentSelectionType = modeSelectionState.SELECT_DIFFICULTY;
                }
            }
            if (Interactions.isJustPressed(InputKey.InputTypes.COOK_UP)) {
                if (currentSelectionType == modeSelectionState.SELECT_MODE) {
                    currentModeSelection = cycleMode();
                }
                if (currentSelectionType == modeSelectionState.SELECT_DIFFICULTY){
                    currentDifficultySelection = cycleDifficulty(1);
                }
            }

            if (Interactions.isJustPressed(InputKey.InputTypes.COOK_DOWN)) {
                if (currentSelectionType == modeSelectionState.SELECT_MODE) {
                    currentModeSelection = cycleMode();
                }
                if (currentSelectionType == modeSelectionState.SELECT_DIFFICULTY){
                    currentDifficultySelection = cycleDifficulty(-1);
                }
            }

            if (Interactions.isJustPressed(InputKey.InputTypes.START_GAME)) {
                screenController.setScreen(ScreenID.GAME);
                ((GameScreen) screenController.getScreen(ScreenID.GAME)).startGame(5);
                setCurrentScreenState(menuState.MAIN_MENU);
            }



            modeSelectLabel.setText(getSelectionString());
        }
    }

    /**
     * The function used to render the {@link MenuScreen}.
     *
     * <br>Draws the current selected stage of the {@link MenuScreen},
     * which contains all the text as {@link Label}s.
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        backgroundSprite.draw(batch);
        batch.end();
        getCurrentScreenStage().draw();
        this.update(delta, true);
    }
    public menuState getOtherScreenState(){
        if (currentState == menuState.MAIN_MENU){
            return menuState.MODE_SELECT;
        }
        else {
            return menuState.MAIN_MENU;
        }
    }

    public void setCurrentScreenState(menuState ms){
        if (ms == menuState.MAIN_MENU){
            currentState = menuState.MAIN_MENU;
        }
        else if (ms == menuState.MODE_SELECT){
            currentState = menuState.MODE_SELECT;
        }
    }


    public Stage getCurrentScreenStage(){
        if (currentState == menuState.MAIN_MENU){
            return mainMenuStage;
        }
        else {
            return modeSelectStage;
        }
    }

    public String getSelectionString(){
        String outputstring = "";
        outputstring += "MODE:  ";
        if (currentSelectionType == modeSelectionState.SELECT_MODE) {
            outputstring += ("[" + currentModeSelection.toString() + "]       ");
        }
        else {
            outputstring += currentModeSelection.toString()+ "       ";
        }
        outputstring += "DIFFICULTY:  ";
        if (currentSelectionType == modeSelectionState.SELECT_DIFFICULTY) {
            outputstring += ("[" + currentDifficultySelection.toString() + "] ");
        }
        else {
            outputstring += currentDifficultySelection.toString()+ " ";
        }
        return outputstring;
    };

    public difficulty cycleDifficulty(int direction){
        if (direction >= 0){
            if (currentDifficultySelection == difficulty.EASY){
                return difficulty.MEDIUM;
            }
            else if (currentDifficultySelection == difficulty.MEDIUM){
                return difficulty.HARD;
            }
            else {
                return difficulty.EASY;
            }
        }
        else {
            if (currentDifficultySelection == difficulty.EASY){
                return difficulty.HARD;
            }
            else if (currentDifficultySelection == difficulty.MEDIUM){
                return difficulty.EASY;
            }
            else {
                return difficulty.MEDIUM;
            }
        }
    }

    public mode cycleMode(){
        if (currentModeSelection == mode.SCENARIO){
            return mode.ENDLESS;
        }
        else {
            return mode.SCENARIO;
        }
    }
}
