package interactions;

import game.GameScreen;
import game.ScreenController;

/**
 * The class responsible for mapping keys on the
 * keyboard to enum constants of equivalent meaning.
 */
public class InputKey {

    /** All the different inputs available in the game. */
    public enum InputTypes {
        // MENU
        START_GAME,
        /** Resetting the game, {@link ScreenController#resetGameScreen()} and {@link GameScreen#reset()}. */
        RESET_GAME,
        /** Opening the {@link game.InstructionScreen}. */
        INSTRUCTIONS,
        /** Pausing the game, {@link ScreenController#pauseGameScreen()} .*/
        PAUSE,
        /** Unpausing the game, {@link ScreenController#playGameScreen()} */
        UNPAUSE,
        /** Opening the credits, {@link game.CreditsScreen} */
        CREDITS,
        /** Quitting the game. */
        QUIT,
        /** Open / close the mode select screen */
        MODE_SELECT,
        /** Open / close the load select screen */
        LOAD_SELECT,
        /** Save the game */
        SAVE,
        /** Load the game */
        LOAD,
        // COOK_INTERACT
        /** Put down an item onto a {@link stations.Station} in the {@link cooks.Cook}'s hands. */
        PUT_DOWN,
        /** Pick up an item from a {@link stations.Station} or {@link stations.Pantry}. */
        PICK_UP,
        /** Use the {@link stations.Station} in front of the Cook. */
        USE,
        /** Buy powerUp */
        BUY_POWERUP,
        /** Activate powerUP */
        ACTIVATE_POWERUP,
        // COOK_MOVEMENT
        /** Player moving up. */
        COOK_UP,
        /** Player moving right. */
        COOK_RIGHT,
        /** Player moving down. */
        COOK_DOWN,
        /** Playter moving left. */
        COOK_LEFT,

        // COOK_MISC
        /** Swapping between the {@link cooks.Cook} in the {@link GameScreen}. */
        COOK_SWAP
    }

    /** The key on the keyboard, represented as an int. */
    private final int key;
    /** The enum constant which is representing the key above. */
    private final InputTypes inputType;

    /**
     * The InputKey Constructor
     * @param inputType The {@link InputTypes} enum constant.
     * @param key The key for the {@link InputTypes} input.
     */
    public InputKey(InputTypes inputType, int key) {
        this.key = key;
        this.inputType = inputType;
    }

    /**
     * Getter for the integer.
     * @return The key as an int.
     */
    public int getKey() {
        return key;
    }

    /**
     * Getter for the {@link InputTypes} enum constant.
     * @return The {@link InputTypes} enum constant for this input.
     */
    public InputTypes getType() {
        return inputType;
    }
}
