package game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;

import game.ScreenController.ScreenID;
import helper.Constants;
import interactions.InputKey;
import interactions.Interactions;

/**
 * The {@link InstructionScreen}, which provides the
 * player with instructions on how to play the game.
 */
public class InstructionScreen extends ScreenAdapter {

    private ScreenID prevScreenID = ScreenID.MENU;
    private final ScreenController screenController;
    private Stage stage;

    /**
     * The constructor for the {@link PauseScreen}.
     * @param screenController The {@link ScreenController} of the {@link ScreenAdapter}.
     * @param orthographicCamera The {@link OrthographicCamera} that the game should use.
     */
    public InstructionScreen(ScreenController screenController, OrthographicCamera orthographicCamera) {
        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        Table table = new Table();
        table.center();
        table.setFillParent(true);

        Label gameOverLabel = new Label("Instructions", font);
        gameOverLabel.setFontScale(3);
        table.add(gameOverLabel).expandX();

        table.row();

        String[] instructions = new String[] {
                "",String.format("Move using the WASD keys or the Arrow Keys. Swap the cook you are controlling by pressing %s.",Interactions.getKeyString(InputKey.InputTypes.COOK_SWAP)),
                "",
                String.format("To use a station, press %s while facing it.", Interactions.getKeyString(InputKey.InputTypes.USE)),
                "",
                String.format("Take items from the Pantries (the top tables with ingredients) or a Station by pressing %s.", Interactions.getKeyString(InputKey.InputTypes.PICK_UP)),
                "Pantries have an infinite number of resources, and the Cook has no carry limit.",
                "",
                "There is a special cook pantry that lets you buy more staff members, place these cooks down on a station to gain automatic cooking.",
                "Items that auto cooks finish will magically teleport to the current selected cook!",
                "",
                String.format("You can put an Item down on a Station (Table) by pressing %s.", Interactions.getKeyString(InputKey.InputTypes.PUT_DOWN)),
                "Counters can do this for any Item, but preparation stations require valid ingredients.",
                "",
                String.format("You progress ingredient preparation by using (%s) the station when the bar is yellow.", Interactions.getKeyString(InputKey.InputTypes.USE)),
                // // I feel the below are unnecessary and the player can figure this out themselves.
                // "",
                // "Buns are added to the stack by giving you the opposite of the highest bun,",
                // "This means if your highest bun is a bottom bun, then you'll get a top bun.",
                // "",
                "",
                String.format("The bin allows you to dispose of items you no longer need. (%s OR %s).",
                        Interactions.getKeyString(InputKey.InputTypes.USE),
                        Interactions.getKeyString(InputKey.InputTypes.PUT_DOWN)),
                "",
                "Each customer can request different meals with different ingredient combos.",
                "Fulfil their order by remaking the recipe shown above them,",
                String.format("and then using (%s) the serving counter while holding it.",Interactions.getKeyString(InputKey.InputTypes.USE)),
                "",
                String.format("Some of the recipes don't follow a specific order. As long as the bread/potato/pizza base is in the correct place, the customer will accept it"),
                "",
                String.format("Unlock bonus stations to aid you by pressing %s, but they will cost you some money.",Interactions.getKeyString(InputKey.InputTypes.USE)),
                "",
                String.format("Powerups can be unlocked using %s and obtained with %s, you'll randomly be given 1 out of 5 different powerups.",
                        Interactions.getKeyString(InputKey.InputTypes.BUY_POWERUP),Interactions.getKeyString(InputKey.InputTypes.ACTIVATE_POWERUP)),
                "","You can reroll for a new one but it will cost money.","",
                String.format("You can pause the game by pressing %s.", Interactions.getKeyString(InputKey.InputTypes.PAUSE)),
                "",
                "Your goal is to successfully serve all your customers in time and if you don't, for every customer, you'll lose a reputation point.",
                "",
                "If you run out of reputation points, the game will end. You win once you've served your customers and still have points.",
                "","Have Fun!",""
        };

        for (String instruction : instructions) {
            Label instLabel = new Label(instruction, font);
            instLabel.setFontScale(1.2f);
            table.add(instLabel).expandX();
            table.row();
        }

        Label extraText = new Label("To go back, press I", font);
        extraText.setFontScale(1.5F);
        table.add(extraText);

        this.screenController = screenController;
        SpriteBatch batch = screenController.getSpriteBatch();

        FitViewport viewport = new FitViewport(Constants.V_Width, Constants.V_Height, orthographicCamera);
        if (batch != null) {
            stage = new Stage(viewport, batch);
            stage.addActor(table);
        }
    }

    /**
     * Check for user input every frame and act on specified inputs.
     * @param delta The time between frames as a float.
     */
    public void update(float delta, boolean shouldResetKeys) {
        // Check for input.
        Interactions.updateKeys(shouldResetKeys);
        if (Interactions.isJustPressed(InputKey.InputTypes.INSTRUCTIONS)) {
            screenController.setScreen(prevScreenID);
        }
    }

    /**
     * The function used to render the {@link PauseScreen}.
     *
     * <br>Draws the {@link #stage} of the {@link PauseScreen},
     * which contains all the text as {@link Label}s.
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
        this.update(delta, true);
    }

    /**
     * Sets the variable {@link #prevScreenID} to the input,
     * which allows the {@link PauseScreen} to return the
     * player to the screen they opened it from.
     * @param scID The {@link ScreenController.ScreenID} of the previous {@link ScreenAdapter}.
     */
    public void setPrevScreenID(ScreenID scID) {
        prevScreenID = scID;
    }
}
