package helper;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import interactions.InputKey;
import interactions.Interactions;

/** Responsible for displaying the controls on the {@link game.GameScreen}. */
public class InstructionHud extends Hud{

    Label instructionsLabel;

    /**
     * The constructor for the {@link InstructionHud}.
     * @param batch The SpriteBatch to render on this Hud.
     */
    public InstructionHud(SpriteBatch batch) {
        super(batch);
        instructionsLabel = new Label(String.format("Press %s to USE \nPress %s to PICK UP \nPress %s to PUT DOWN \nPress %s to SWAP COOKS",
                Interactions.getKeyString(InputKey.InputTypes.USE),
                Interactions.getKeyString(InputKey.InputTypes.PICK_UP),
                Interactions.getKeyString(InputKey.InputTypes.PUT_DOWN),
                Interactions.getKeyString(InputKey.InputTypes.COOK_SWAP)
                ), new Label.LabelStyle(new BitmapFont(), Color.SLATE));
        table.add(instructionsLabel).expandX().padTop(90).padRight(950);
        instructionsLabel.setFontScale(1.25f);
    }

    /**
     * Renders the {@link Hud} with the game's controls.
     */
    @Override
    public void render() {
        super.render();
    }


}
