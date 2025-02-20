package stations;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import cooks.Cook;
import food.FoodItem;
import game.GameScreen;
import helper.Constants;
import interactions.InputKey;

/**
 * A {@link Pantry} class used to create Pantries that allow
 * {@link Cook}s to take the {@link FoodItem} that is assigned
 * to the {@link Pantry}.
 */
public class Pantry extends Station {

    FoodItem.FoodID foodID;

    /**
     * The constructor for the {@link Pantry}.
     * @param rectangle The collision and interaction area of the {@link Pantry}.
     */
    public Pantry(Rectangle rectangle, boolean locked, GameScreen gameScreen) {
        super(rectangle, locked, gameScreen);
    }

    /**
     * Assigns a {@link FoodItem} to the {@link Pantry} that the
     * {@link Cook} will take from it.
     * @param foodID The {@link FoodItem} to assign to the {@link Pantry}.
     */
    public void setItem(FoodItem.FoodID foodID) {
        this.foodID = foodID;
    }

    /**
     * The function that allows a {@link Cook} to interact with the {@link Pantry}.
     * <br>If the {@link Cook} uses either the {@link InputKey.InputTypes#USE} or
     * {@link InputKey.InputTypes#PICK_UP}, then they will pick up the
     * {@link FoodItem} from it.
     * @param cook The cook that interacted with the {@link CookInteractable}.
     * @param inputType The type of {@link InputKey.InputTypes} the player made with
     *                  the {@link CookInteractable}.
     */
    public void interact(Cook cook, InputKey.InputTypes inputType) {
        // If the input is to pick up:
        if (inputType == InputKey.InputTypes.PICK_UP || inputType == InputKey.InputTypes.USE) {
            // Add the new FoodItem onto the stack.
            FoodItem.FoodID addedFood = foodID;
            if (addedFood == FoodItem.FoodID.cook){
                if (gameScreen.getMoney() >= Constants.STAFF_COST) { // Buying cooks costs money
                    gameScreen.spendMoney(Constants.STAFF_COST);
                }
                else {
                    return;
                }
            }
            // If the foodID is "bun", check which bun it should add.
            if (foodID == FoodItem.FoodID.bun) {
                boolean bottom = true;
                // Look through the stack, and alternate between top bun or bottom bun.
                Array<FoodItem.FoodID> foodItems = cook.foodStack.getStack();
                for (FoodItem.FoodID foodItem : foodItems) {
                    if (foodItem == FoodItem.FoodID.bottomBun) {
                        bottom = false;
                        break;
                    }
                    if (foodItem == FoodItem.FoodID.topBun) {
                        bottom = true;
                        break;
                    }
                }
                addedFood = bottom ? FoodItem.FoodID.bottomBun : FoodItem.FoodID.topBun;
            }
            cook.foodStack.addStack(addedFood);
        }
    }
}
