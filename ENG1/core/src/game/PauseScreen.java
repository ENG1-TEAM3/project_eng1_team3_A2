package game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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

/**
 * A {@link ScreenAdapter} that is used when the game is paused.
 * It renders the {@link GameScreen} behind it, so the user can still
 * see the game.
 */
public class PauseScreen extends ScreenAdapter {
    private final ScreenController screenController;

    private Stage stage;
    private final GameScreen gameScreen;
    private ShapeRenderer shape;

    private MenuScreen.saveFileSelectionChoice currentSave;
    private final Label[] lblLabels;
    private boolean shouldResetSaveLabel;

    /**
     * The constructor for the {@link PauseScreen}.
     * @param screenController The {@link ScreenController} of the {@link ScreenAdapter}.
     * @param orthographicCamera The {@link OrthographicCamera} that the game should use.
     */
    public PauseScreen(ScreenController screenController, OrthographicCamera orthographicCamera) {
        shouldResetSaveLabel = true;
        currentSave = MenuScreen.saveFileSelectionChoice.SAVE1;
        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        Table table = new Table();
        table.center();
        table.setFillParent(true);

        String[] strLabels = new String[] {
                "PAUSED",
                String.format("Press %s to Continue",Interactions.getKeyString(InputKey.InputTypes.UNPAUSE)),
                String.format("Press %s to Save : saving to " + currentSave.toString() + " (Press Arrow Keys to cycle this)",Interactions.getKeyString(InputKey.InputTypes.SAVE)),
                String.format("Press %s for Instructions",Interactions.getKeyString(InputKey.InputTypes.INSTRUCTIONS)),
                String.format("Press %s for Credits",Interactions.getKeyString(InputKey.InputTypes.CREDITS)),
                String.format("Press %s to Reset",Interactions.getKeyString(InputKey.InputTypes.RESET_GAME)),
                String.format("Press %s to Quit",Interactions.getKeyString(InputKey.InputTypes.QUIT))
        };

        // Contains the Labels objects for the PauseScreen
        lblLabels = new Label[strLabels.length];

        for (int j = 0; j < lblLabels.length; j++) {
            String strLabel = strLabels[j];
            lblLabels[j] = new Label(String.format(strLabel), font);
            table.add(lblLabels[j]).expandX();
            table.row();
        }

        lblLabels[0].setFontScale(4);
        this.screenController = screenController;
        SpriteBatch batch = screenController.getSpriteBatch();
        this.gameScreen = ((GameScreen) screenController.getScreen(ScreenID.GAME));
        Viewport viewport = new FitViewport(Constants.V_Width, Constants.V_Height, orthographicCamera);
        if (batch != null) {
            stage = new Stage(viewport, batch);
            stage.addActor(table);
            this.shape = new ShapeRenderer();
            shape.setAutoShapeType(true);
        }
    }

    /**
     * Check for user input every frame and act on specified inputs.
     * @param delta The time between frames as a float.
     */
    public void update(float delta, boolean shouldResetKeys) {
        Interactions.updateKeys(shouldResetKeys);
        // Check if the Unpause key was pressed.
        if (Interactions.isJustPressed(InputKey.InputTypes.UNPAUSE)) {
            screenController.playGameScreen();
            shouldResetSaveLabel = true;
            return;
        }
        if (Interactions.isJustPressed(InputKey.InputTypes.INSTRUCTIONS)) {
            ((InstructionScreen)screenController.getScreen(ScreenID.INSTRUCTIONS)).setPrevScreenID(ScreenID.PAUSE);
            screenController.setScreen(ScreenID.INSTRUCTIONS);
        }
        else if (Interactions.isJustPressed(InputKey.InputTypes.CREDITS)) {
            ((CreditsScreen)screenController.getScreen(ScreenID.CREDITS)).setPrevScreenID(ScreenID.PAUSE);
            screenController.setScreen(ScreenID.CREDITS);
        }
        else if (Interactions.isJustPressed(InputKey.InputTypes.RESET_GAME)) {
            screenController.
                    resetGameScreen();
            screenController.setScreen(ScreenID.MENU);
        }

        // Saving logic added for assessment 2
        else if (Interactions.isJustPressed(InputKey.InputTypes.COOK_UP)){
            currentSave = MenuScreen.cycleSaves(currentSave,1, false);
        }
        else if (Interactions.isJustPressed(InputKey.InputTypes.COOK_DOWN)){
            currentSave = MenuScreen.cycleSaves(currentSave,-1, false);
        }
        else if (Interactions.isJustPressed(InputKey.InputTypes.SAVE) && shouldResetSaveLabel) {
            try {
                gameScreen.getSaveHandler().saveToFile(currentSave.toString().toLowerCase() + ".txt", screenController.getPauseStartTime());
            } catch (IOException io) {
                throw new RuntimeException(io);
            }
            shouldResetSaveLabel = false;
        }

        else if (Interactions.isJustPressed(InputKey.InputTypes.QUIT)) {
            Gdx.app.exit();
        }
        if (shouldResetSaveLabel){
            lblLabels[2].setText(String.format("Press %s to save : saving to " + currentSave.toString() + " (Press arrow keys to cycle this)", Interactions.getKeyString(InputKey.InputTypes.SAVE)));
        }
        else {
            lblLabels[2].setText("Game Saved");
        }

    }

    /**
     * The function used to render the {@link PauseScreen}.
     *
     * <br>Draws the {@link GameScreen} underneath using the
     * {@link GameScreen#renderGame(float)} function, and then
     * renders the {@link PauseScreen} over it.
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {

        gameScreen.renderGame(delta);

        shape.begin(ShapeRenderer.ShapeType.Filled);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shape.setColor(0,0,0,0.5F);
        shape.rect(0,0, Constants.V_Width,Constants.V_Height);
        shape.setColor(Color.WHITE);
        shape.end();

        stage.draw();

        Gdx.gl.glDisable(GL20.GL_BLEND);

        this.update(delta, true);
    }
}
