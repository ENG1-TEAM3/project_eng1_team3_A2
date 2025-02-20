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

import java.io.IOException;
import java.util.ArrayList;

/**
 * The {@link MenuScreen}, which provides the player with
 * a few options of inputs, which do different things.
 * One of which is to change to the {@link GameScreen} and
 * play the game.
 */
public class MenuScreen extends ScreenAdapter {
    /** The current screen encoded as a state */
    public enum menuState {
        MAIN_MENU,
        MODE_SELECT,
        LOAD_SELECT
    }

    /** The save file choices */
    public enum saveFileSelectionChoice{
        NO_SAVES,
        SAVE1,
        SAVE2,
        SAVE3
    }

    /** The selection choices for the new game screen*/
    public enum modeSelectionState{
        SELECT_DIFFICULTY,
        SELECT_MODE
    }

    /** The modes for the game */
    public enum mode {
        ENDLESS,
        SCENARIO
    }

    /** The difficulties for the game */
    public enum difficulty{
        EASY,
        MEDIUM,
        HARD
    }

    private final ScreenController screenController;
    private final SpriteBatch batch;

    /** Stage used to contain the text for the main menu */
    private Stage mainMenuStage;

    /** Stage used to contain the text for the mode select screen*/
    private Stage modeSelectStage;
    /** State used to swap screens from main menu to mode select */
    private Stage loadSelectStage;
    public menuState currentState;
    /** State used to contain the current user selection for editing in the mode select screen*/
    public modeSelectionState currentSelectionType;
    /** State used to contain the current mode selection (endless or scenario)*/
    public mode currentModeSelection;
    /** State used to contain the current difficulty selection (easy medium or hard)*/
    public difficulty currentDifficultySelection;
    /** State used to contain the current save file selection */
    public saveFileSelectionChoice currentSave;
    private final Sprite backgroundSprite;

    private final Label modeSelectLabel, loadSelectLabel;

    private int customer = 5; // Default to 5 customers if it never gets set somehow

    /**
     * The constructor for the {@link MenuScreen}.
     * @param screenController The {@link ScreenController} of the {@link ScreenAdapter}.
     * @param orthographicCamera The {@link OrthographicCamera} that the game should use.
     */
    public MenuScreen(ScreenController screenController, OrthographicCamera orthographicCamera) {
        BitmapFont bitmapFont = new BitmapFont();

        currentState = menuState.MAIN_MENU;
        currentDifficultySelection = difficulty.EASY;
        currentModeSelection = mode.SCENARIO;
        currentSelectionType = modeSelectionState.SELECT_MODE;
        if (findSaves().size() > 0) {
            currentSave = findSaves().get(0);
        }
        else {
            currentSave = saveFileSelectionChoice.NO_SAVES;
        }
        this.backgroundSprite = new Sprite(new Texture("Maps/StartMenuBackground2.png"));
        backgroundSprite.setSize(Constants.V_Width, Constants.V_Height);

        Label.LabelStyle font = new Label.LabelStyle(bitmapFont, Color.WHITE);
        Table table = new Table();
        table.center();
        table.setFillParent(true);

        /*Label welcomeLabel = new Label("UNDERCOOKED", font);
        table.add(welcomeLabel).expandX();
        table.row();*/

        Label startLabel = new Label(String.format("PRESS %s TO SELECT GAME MODE",Interactions.getKeyString(InputKey.InputTypes.MODE_SELECT).toUpperCase()), font);
        table.add(startLabel).expandX();
        table.row();

        Label saveLabel = new Label(String.format("PRESS %s TO LOAD SAVE",Interactions.getKeyString(InputKey.InputTypes.LOAD_SELECT).toUpperCase()), font);
        table.add(saveLabel).expandX();
        table.row();

        Label instructionLabel = new Label(String.format("PRESS %s FOR TUTORIAL",Interactions.getKeyString(InputKey.InputTypes.INSTRUCTIONS).toUpperCase()), font);
        table.add(instructionLabel).expandX();
        table.row();

        Label creditLabel = new Label(String.format("PRESS %s FOR CREDITS",Interactions.getKeyString(InputKey.InputTypes.CREDITS).toUpperCase()), font);
        table.add(creditLabel).expandX();
        table.row();

        Label quitLabel = new Label(String.format("PRESS %s TO QUIT",Interactions.getKeyString(InputKey.InputTypes.QUIT).toUpperCase()), font);
        table.add(quitLabel).expandX();



        //welcomeLabel.setFontScale(4);
        startLabel.setFontScale(2);
        saveLabel.setFontScale(2);
        instructionLabel.setFontScale(2);
        creditLabel.setFontScale(2);
        quitLabel.setFontScale(2);


        this.screenController = screenController;
        this.batch = screenController.getSpriteBatch();

        Viewport viewport = new FitViewport(Constants.V_Width, Constants.V_Height, orthographicCamera);

        // AS2 NEW CHANGE - MODE SELECT SCREEN

        Table table2 = new Table();
        table2.center();
        table2.setFillParent(true);
        Label l00 = new Label("Press the Arrow Keys to Select A Mode", font);
        Label l01 = new Label(String.format("Press %s to Go Back",Interactions.getKeyString(InputKey.InputTypes.MODE_SELECT)), font);
        Label l0 = new Label("           ^                          ^", font);
        modeSelectLabel = new Label(getSelectionString(), font);
        Label l2 = new Label("              V                                V", font);
        Label l3 = new Label(String.format("Press %s to Start Game",Interactions.getKeyString(InputKey.InputTypes.START_GAME)), font);
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

        // AS2 NEW CHANGE - SAVE FILE SELECT SCREEN

        Table table3 =new Table();
        table3.center();
        table3.setFillParent(true);
        loadSelectLabel = new Label("FILE SELECTED: " + currentSave.toString(), font);
        Label l10 = new Label("Press the arrow keys to cycle through existing saves", font);
        Label l11 = new Label(String.format("Press %s to go back",
                Interactions.getKeyString(InputKey.InputTypes.LOAD_SELECT)), font);
        Label l12 = new Label(String.format("Press %s to load save",
                Interactions.getKeyString(InputKey.InputTypes.LOAD)), font);
        table3.add(loadSelectLabel).expandX();
        table3.row();
        table3.add(l10).expandX();
        table3.row();
        table3.add(l11).expandX();
        table3.row();
        table3.add(l12).expandX();
        loadSelectLabel.setFontScale(4);



        // AS2 NEW CHANGE - Addition of null check for testing

        if (this.batch != null) {
            mainMenuStage = new Stage(viewport, batch);
            mainMenuStage.addActor(table);
            modeSelectStage = new Stage(viewport, batch);
            modeSelectStage.addActor(table2);
            loadSelectStage = new Stage(viewport, batch);
            loadSelectStage.addActor(table3);
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
        else if (Interactions.isJustPressed(InputKey.InputTypes.CREDITS)) { // Set the screen to the credits screen
            ((CreditsScreen)screenController.getScreen(ScreenID.CREDITS)).setPrevScreenID(ScreenID.MENU);
            screenController.setScreen(ScreenID.CREDITS);
        }
        else if (Interactions.isJustPressed(InputKey.InputTypes.MODE_SELECT)) { // Set the screen to the mode select screen
            if (currentState != menuState.LOAD_SELECT) {
                setCurrentScreenState(this.getCorrectSwapModeSelect());
            }
        }
        else if (Interactions.isJustPressed(InputKey.InputTypes.LOAD_SELECT)) { // Set the screen to the mode select screen
            if (currentState != menuState.MODE_SELECT) {
                if (findSaves().size() > 0) {
                    currentSave = findSaves().get(0);
                }
                else {
                    currentSave = saveFileSelectionChoice.NO_SAVES;
                }
                setCurrentScreenState(this.getCorrectSwapLoadSelect());
            }
        }
        else if (Interactions.isJustPressed(InputKey.InputTypes.QUIT)) { // Quit the game
            Gdx.app.exit();
        }

        // Extra logic below added for Assessment 2
        if (currentState == menuState.MODE_SELECT){ // If the game is on the mode select screen
            if (Interactions.isJustPressed(InputKey.InputTypes.COOK_LEFT)){ // Then the left key should change the selection
                if (currentSelectionType == modeSelectionState.SELECT_DIFFICULTY){
                    currentSelectionType = modeSelectionState.SELECT_MODE;
                }
            }
            if (Interactions.isJustPressed(InputKey.InputTypes.COOK_RIGHT) && currentModeSelection == mode.SCENARIO){ // The right key should also change the selection
                if (currentSelectionType == modeSelectionState.SELECT_MODE){
                    currentSelectionType = modeSelectionState.SELECT_DIFFICULTY;
                }
            }
            if (Interactions.isJustPressed(InputKey.InputTypes.COOK_UP)) { // The up key should cycle through the options for the selection
                if (currentSelectionType == modeSelectionState.SELECT_MODE) {
                    currentModeSelection = cycleMode();
                }
                if (currentSelectionType == modeSelectionState.SELECT_DIFFICULTY){
                    currentDifficultySelection = cycleDifficulty(1);
                }
            }

            if (Interactions.isJustPressed(InputKey.InputTypes.COOK_DOWN)) { // The down key should also cycle through the options for the selection
                if (currentSelectionType == modeSelectionState.SELECT_MODE) {
                    currentModeSelection = cycleMode();
                }
                if (currentSelectionType == modeSelectionState.SELECT_DIFFICULTY){
                    currentDifficultySelection = cycleDifficulty(-1);
                }
            }

            if (Interactions.isJustPressed(InputKey.InputTypes.START_GAME)) { // The start game key should start the game with selected difficulty and mode
                screenController.setScreen(ScreenID.GAME);
                ((GameScreen) screenController.getScreen(ScreenID.GAME)).startGame(customer, currentDifficultySelection,
                        currentModeSelection, false);
                ((GameScreen) screenController.getScreen(ScreenID.GAME)).getCustomerController().setLastCustomerSpawnTime(0);
                setCurrentScreenState(menuState.MAIN_MENU);
            }
            modeSelectLabel.setText(getSelectionString());
        }
        else if (currentState == menuState.LOAD_SELECT){
            if (Interactions.isJustPressed(InputKey.InputTypes.COOK_UP)){
                currentSave = cycleSaves(currentSave, 1, true);
            }
            else if (Interactions.isJustPressed(InputKey.InputTypes.COOK_DOWN)){
                currentSave = cycleSaves(currentSave, -1, true);
            }
            loadSelectLabel.setText("FILE SELECTED: " + currentSave.toString());
            if (Interactions.isJustPressed(InputKey.InputTypes.LOAD)) {
                if (currentSave != saveFileSelectionChoice.NO_SAVES) {
                    GameScreen gs = ((GameScreen) screenController.getScreen(ScreenID.GAME));
                    try {
                        gs.getSaveHandler().loadFromFile(currentSave.toString().toLowerCase() + ".txt");
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    screenController.setScreen(ScreenID.GAME);
                    setCurrentScreenState(menuState.MAIN_MENU);
                }
            }

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

    ////////////////////////////////ALL BELOW NEW FOR ASSESSMENT 2//////////////////////////////////////////////////////
    /**
     * A method used to swap the state from main menu to the mode select screen
     * @return If on moin menu, return mode select state, otherwise return main menu state
     */
    public menuState getCorrectSwapModeSelect(){
        if (currentState == menuState.MAIN_MENU){
            return menuState.MODE_SELECT;
        }
        else {
            return menuState.MAIN_MENU;
        }
    }

    /**
     * A method used to swap the state from main menu to load select screen
     * @return If on main menu, return load select state, otherwise return main menu state
     */
    public menuState getCorrectSwapLoadSelect(){
        if (currentState == menuState.MAIN_MENU){
            return menuState.LOAD_SELECT;
        }
        else {
            return menuState.MAIN_MENU;
        }
    }

    /**
     * Set the current screen state
     * @param ms The state to set the screen to
     */
    public void setCurrentScreenState(menuState ms){
        if (ms == menuState.MAIN_MENU){
            currentState = menuState.MAIN_MENU;
        }
        else if (ms == menuState.MODE_SELECT){
            currentState = menuState.MODE_SELECT;
        }
        else if (ms == menuState.LOAD_SELECT){
            currentState = menuState.LOAD_SELECT;
        }
    }

    /**
     * Get the stage to render corresponding to the current internal screen state
     * @return The stage defined in line above
     */
    public Stage getCurrentScreenStage(){
        if (currentState == menuState.MAIN_MENU){
            return mainMenuStage;
        }
        else if (currentState == menuState.MODE_SELECT){
            return modeSelectStage;
        }
        else {
            return loadSelectStage;
        }
    }

    /**
     * Get the selection string for new game mode selection depending on internal state
     * @return The string defined in line above
     */
    public String getSelectionString() {
        String outputstring = "";
        outputstring += "MODE:  ";
        if (currentSelectionType == modeSelectionState.SELECT_MODE) {
            outputstring += ("[" + currentModeSelection.toString() + "]       ");
        } else {
            outputstring += currentModeSelection.toString() + "       ";
        }
        if (currentModeSelection == mode.SCENARIO) {
            outputstring += "DIFFICULTY:  ";
            if (currentSelectionType == modeSelectionState.SELECT_DIFFICULTY) {
                outputstring += ("[" + currentDifficultySelection.toString() + "] ");
            } else {
                outputstring += currentDifficultySelection.toString() + " ";
            }
        }
        else {
            outputstring += "                                  ";
        }
        return outputstring;
    }

    /**
     * Return the next / previous difficulty selection
     * @param direction If positive or zero then go for the next hardest difficulty, if negative go for next easiest
     * @return The difficulty defined in the line above
     */
    public difficulty cycleDifficulty(int direction){
        if (direction >= 0){
            if (currentDifficultySelection == difficulty.EASY){
                customer = 10;
                return difficulty.MEDIUM;
            }
            else if (currentDifficultySelection == difficulty.MEDIUM){
                customer = 15;
                return difficulty.HARD;
            }
            else {
                customer = 5;
                return difficulty.EASY;
            }
        }
        else {
            if (currentDifficultySelection == difficulty.EASY){
                customer = 15;
                return difficulty.HARD;
            }
            else if (currentDifficultySelection == difficulty.MEDIUM){
                customer = 5;
                return difficulty.EASY;
            }
            else {
                customer = 10;
                return difficulty.MEDIUM;
            }
        }
    }

    /**
     * Return the other mode selection (There are only 2)
     * @return the other mode selection
     */
    public mode cycleMode(){
        if (currentModeSelection == mode.SCENARIO){
            return mode.ENDLESS;
        }
        else {
            return mode.SCENARIO;
        }
    }


    /**
     * Find the current existing save files, and return an arraylist containing enum values representing them
     * @return The arraylist defined in the line above
     */
    public static ArrayList<saveFileSelectionChoice> findSaves(){
        ArrayList<saveFileSelectionChoice> savesThatExist = new ArrayList<>();
        if (Gdx.files.local("save1.txt").exists()){
            savesThatExist.add(saveFileSelectionChoice.SAVE1);
        }
        if (Gdx.files.local("save2.txt").exists()){
            savesThatExist.add(saveFileSelectionChoice.SAVE2);
        }
        if (Gdx.files.local("save3.txt").exists()){
            savesThatExist.add(saveFileSelectionChoice.SAVE3);
        }
        return savesThatExist;
    }

    /**
     * Return the next / previous save file choice - Can depend on their existence or not
     * @param currentSave - The current save file choice
     * @param direction - If positive or zero then go for the next slot (ascending slot num),
     *                  otherwise the previous slot (descending slot num)
     * @param checkExistence - If true then this can only cycle through saves that exist,
     *                      if false then this can cycle through all 3 saves
     * @return The save file choice defined above
     */
    public static saveFileSelectionChoice cycleSaves(saveFileSelectionChoice currentSave, int direction, boolean checkExistence){
        ArrayList<saveFileSelectionChoice> existingSaves = findSaves();
        if (direction >= 0){
            if (currentSave == saveFileSelectionChoice.SAVE1 &&
                    (!checkExistence || existingSaves.contains(saveFileSelectionChoice.SAVE2))){
                return saveFileSelectionChoice.SAVE2;
            }
            else if (currentSave == saveFileSelectionChoice.SAVE2 &&
                    (!checkExistence || existingSaves.contains(saveFileSelectionChoice.SAVE3))){
                return saveFileSelectionChoice.SAVE3;
            }
            else if (currentSave == saveFileSelectionChoice.SAVE3 &&
                    (!checkExistence || existingSaves.contains(saveFileSelectionChoice.SAVE1))){
                return saveFileSelectionChoice.SAVE1;
            }
        }
        else{
            if (currentSave == saveFileSelectionChoice.SAVE1 &&
                    (!checkExistence || existingSaves.contains(saveFileSelectionChoice.SAVE3))){
                return saveFileSelectionChoice.SAVE3;
            }
            else if (currentSave == saveFileSelectionChoice.SAVE2 &&
                    (!checkExistence || existingSaves.contains(saveFileSelectionChoice.SAVE1))){
                return saveFileSelectionChoice.SAVE1;
            }
            else if (currentSave == saveFileSelectionChoice.SAVE3 &&
                    (!checkExistence || existingSaves.contains(saveFileSelectionChoice.SAVE2))){
                return saveFileSelectionChoice.SAVE2;
            }
        }
        return currentSave;
    }

}
