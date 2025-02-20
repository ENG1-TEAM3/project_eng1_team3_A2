package stations;

import com.badlogic.gdx.math.Rectangle;
import cooks.Cook;
import food.FoodItem;
import game.GameScreen;
import helper.Constants;
import interactions.InputKey;

/** The Bin Station Class.
 * Contains what happens when the station is interacted with.
 */
public class BinStation extends Station {

    /**
     * The constructor for the {@link BinStation}.
     * @param rectangle The collision and interaction area of the {@link Station}.
     */
    public BinStation(Rectangle rectangle, boolean locked, GameScreen gameScreen) {
        super(rectangle, locked, gameScreen);
    }

    /**
     * The interact function for the {@link BinStation}.
     *
     * This takes the top item from the {@link Cook}'s {@link food.FoodStack}
     * if they use either the {@link InputKey.InputTypes#USE} or
     * {@link InputKey.InputTypes#PUT_DOWN} keys.
     * @param cook The cook that interacted with the {@link CookInteractable}.
     * @param inputType The type of {@link InputKey.InputTypes} the player made with
     *                  the {@link CookInteractable}.
     */
    @Override
    public void interact(Cook cook, InputKey.InputTypes inputType) {
        // Only bin if user inputs USE or PUT_DOWN
        if (inputType == InputKey.InputTypes.USE || inputType == InputKey.InputTypes.PUT_DOWN) {
            if (cook.foodStack.popStack() == FoodItem.FoodID.cook){ // Binning the cook item should refund the player
                gameScreen.addMoney(Constants.STAFF_COST);
            }
        }
    }
}
