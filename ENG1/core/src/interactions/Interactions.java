package interactions;

import java.io.Serializable;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Array;

import food.FoodItem.FoodID;
import stations.Station.StationID;

/**
 * A static class containing all information relating to interactions,
 * including: - The cook and station interactions. - Keys being pressed
 * information.
 */
public class Interactions {
	/**
	 * A HashMap containing how each FoodItem's FoodID, via a station of StationID,
	 * can convert to another foodID.
	 */
	private static final HashMap<String, InteractionResult> interactions = new HashMap<>();
	static {
		interactions.put(InteractionKey(FoodID.lettuce, StationID.cut),
				new InteractionResult(FoodID.lettuceChop, new float[] { 25, 50, 75 }, -1, -1));
		interactions.put(InteractionKey(FoodID.tomato, StationID.cut),
				new InteractionResult(FoodID.tomatoChop, new float[] { 25, 50, 75 }, -1, -1));
		interactions.put(InteractionKey(FoodID.onion, StationID.cut),
				new InteractionResult(FoodID.onionChop, new float[] { 25, 50, 75 }, -1, -1));
        // New interactions added for assessment 2 (not meat)
		interactions.put(InteractionKey(FoodID.mushroom, StationID.cut),
				new InteractionResult(FoodID.mushroomChop, new float[] { 25, 50, 75 }, -1, -1));
		interactions.put(InteractionKey(FoodID.pepperoni, StationID.cut),
				new InteractionResult(FoodID.pepperoniChop, new float[] { 33, 66 }, -1, -1));
		interactions.put(InteractionKey(FoodID.cheese, StationID.cut),
				new InteractionResult(FoodID.cheeseChop, new float[] { 25, 50, 75 }, -1, -1));
		interactions.put(InteractionKey(FoodID.meat, StationID.fry),
				new InteractionResult(FoodID.meatCook, new float[] { 50 }, 13F, 13));
		interactions.put(InteractionKey(FoodID.potato, StationID.fry),
				new InteractionResult(FoodID.potatoCook, new float[] { 50 }, 13F, 13));
		interactions.put(InteractionKey(FoodID.dough, StationID.fry),
				new InteractionResult(FoodID.doughCook, new float[] { 50 }, 13F, 13));
		interactions.put(InteractionKey(FoodID.beans, StationID.fry),
				new InteractionResult(FoodID.beansCook, new float[] { 33, 66 }, 13F, 13));
		interactions.put(InteractionKey(FoodID.chili, StationID.fry),
				new InteractionResult(FoodID.chiliCook, new float[] { 33, 66 }, 13F, 13));
	}

	/**
	 * A subclass used to more easily define what interaction must be made to get a
	 * desired change in an ingredient, as well as the speed at which it progresses
	 * to change, and to specify at what percentages a user input is required.
	 */
	public static class InteractionResult implements Serializable {
		private final FoodID result;
		private final float[] steps;
		private final float speed;
        private final float burnSpeed;

		/**
		 * InteractionResult Constructor
		 * 
		 * @param result - The resulting FoodID
		 * @param steps  - The steps in a range of 0 - 100 of the process where input is
		 *               required
		 * @param speed  - The speed of which the progress bar fills up for a station
		 *               per second. -1 is instant.
		 */
		public InteractionResult(FoodID result, float[] steps, float speed, float burnSpeed) {
			this.result = result;
			this.steps = steps;
			this.speed = speed;
			this.burnSpeed = burnSpeed;
		}

		/**
		 * A getter to return the result of the interaction.
		 * 
		 * @return FoodID result.
		 */
		public FoodID getResult() {
			return result;
		}

		/**
		 * A getter to return the steps of the interaction.
		 * 
		 * @return An array of floats, each indicating a percentage that user input is
		 *         required during the process.
		 */
		public float[] getSteps() {
			return steps;
		}

		/**
		 * A getter to return the speed of the interaction.
		 * 
		 * @return A float that defines the percentage increase in a second.
		 */
		public float getSpeed() {
			return speed;
		}

		public float getBurnSpeed() {
			return burnSpeed;
		}
	}

	/** The different IDs of interaction. Used to get the Arrays. */
	public enum InputID {
		/** Key maps for Cook Interactions controls. */
		COOK_INTERACT,
		/** Key maps for Menu controls. */
		MENU,
		/** Key maps for Menu controls. */
		COOK_MOVEMENT,
		/** Key maps for Miscellaneous Cook controls. */
		COOK_MISC
	}

	/**
	 * A HashMap containing all different forms of user inputs. These can easily be
	 * changed / modified as needed from here, instead of searching through the
	 * code.
	 *
	 * There is two ways to read through these. 1: Call the function
	 * {@link #getInputKeys(InputID)} on the ID you want, and then loop through the
	 * Array, calling the Gdx.input.isKeyPressed function you require.
	 *
	 * 2: At the start of the current update of the game, use the function
	 * {@link #updateKeys(boolean)}. This will automatically check the keys, and
	 * then store the {@link InputKey.InputTypes} that have been pressed down. You
	 * can then use the functions {@link #isPressed(InputKey.InputTypes)} or
	 * {@link #isJustPressed(InputKey.InputTypes)} to check if the user has pressed
	 * the keys. They are named in the same way as the LibGDX functions.
	 */
	private static final HashMap<InputID, Array<InputKey>> inputs = new HashMap<>();
	static {
		inputs.put(InputID.MENU,
				new Array<>(new InputKey[] { new InputKey(InputKey.InputTypes.INSTRUCTIONS, Input.Keys.I),
						new InputKey(InputKey.InputTypes.RESET_GAME, Input.Keys.R),
						new InputKey(InputKey.InputTypes.START_GAME, Input.Keys.ENTER),
						new InputKey(InputKey.InputTypes.PAUSE, Input.Keys.ESCAPE),
						new InputKey(InputKey.InputTypes.UNPAUSE, Input.Keys.ESCAPE),
						new InputKey(InputKey.InputTypes.CREDITS, Input.Keys.C),
						new InputKey(InputKey.InputTypes.QUIT, Input.Keys.Q),
						new InputKey(InputKey.InputTypes.MODE_SELECT, Input.Keys.G),
                        new InputKey(InputKey.InputTypes.LOAD_SELECT, Input.Keys.T),
						new InputKey(InputKey.InputTypes.SAVE, Input.Keys.O),
						new InputKey(InputKey.InputTypes.LOAD, Input.Keys.P) }));
		inputs.put(InputID.COOK_MOVEMENT,
				new Array<>(new InputKey[] { new InputKey(InputKey.InputTypes.COOK_UP, Input.Keys.W),
						new InputKey(InputKey.InputTypes.COOK_LEFT, Input.Keys.A),
						new InputKey(InputKey.InputTypes.COOK_DOWN, Input.Keys.S),
						new InputKey(InputKey.InputTypes.COOK_RIGHT, Input.Keys.D),

						new InputKey(InputKey.InputTypes.COOK_UP, Input.Keys.UP),
						new InputKey(InputKey.InputTypes.COOK_LEFT, Input.Keys.LEFT),
						new InputKey(InputKey.InputTypes.COOK_DOWN, Input.Keys.DOWN),
						new InputKey(InputKey.InputTypes.COOK_RIGHT, Input.Keys.RIGHT) }));
		inputs.put(InputID.COOK_INTERACT,
				new Array<>(new InputKey[] { new InputKey(InputKey.InputTypes.USE, Input.Keys.F),
						new InputKey(InputKey.InputTypes.PICK_UP, Input.Keys.E),
						new InputKey(InputKey.InputTypes.PUT_DOWN, Input.Keys.Q),
						new InputKey(InputKey.InputTypes.BUY_POWERUP, Input.Keys.ALT_LEFT),
                        new InputKey(InputKey.InputTypes.ACTIVATE_POWERUP, Input.Keys.SPACE)}));
		inputs.put(InputID.COOK_MISC,
				new Array<>(new InputKey[] { new InputKey(InputKey.InputTypes.COOK_SWAP, Input.Keys.TAB) }));
	}

	private static final HashMap<InputID, Array<InputKey.InputTypes>> inputGroups = new HashMap<>();
	static {
		inputGroups.put(InputID.MENU,
				new Array<>(new InputKey.InputTypes[] { InputKey.InputTypes.INSTRUCTIONS,
						InputKey.InputTypes.RESET_GAME, InputKey.InputTypes.START_GAME, InputKey.InputTypes.PAUSE,
						InputKey.InputTypes.UNPAUSE, InputKey.InputTypes.CREDITS, InputKey.InputTypes.QUIT,
						InputKey.InputTypes.MODE_SELECT, InputKey.InputTypes.LOAD_SELECT,InputKey.InputTypes.SAVE, InputKey.InputTypes.LOAD }));
		inputGroups.put(InputID.COOK_MOVEMENT, new Array<>(new InputKey.InputTypes[] { InputKey.InputTypes.COOK_UP,
				InputKey.InputTypes.COOK_LEFT, InputKey.InputTypes.COOK_DOWN, InputKey.InputTypes.COOK_RIGHT, }));
		inputGroups.put(InputID.COOK_INTERACT, new Array<>(new InputKey.InputTypes[] { InputKey.InputTypes.USE,
				InputKey.InputTypes.PICK_UP, InputKey.InputTypes.PUT_DOWN, InputKey.InputTypes.BUY_POWERUP }));
		inputGroups.put(InputID.COOK_MISC, new Array<>(new InputKey.InputTypes[] { InputKey.InputTypes.COOK_SWAP }));
	}

	/**
	 * A list of all keys that were being pressed when {@link #updateKeys(boolean)}
	 * was called.
	 */
	public static Array<InputKey.InputTypes> keysPressed = new Array<>();
	/**
	 * A list of all keys that were pressed the same frame that
	 * {@link #updateKeys(boolean)} was called.
	 */
	public static Array<InputKey.InputTypes> keysJustPressed = new Array<>();

	/**
	 * Get the input key assigned to the enum constant inputID
	 * 
	 * @param inputID {@link InputID} enum Constant
	 * @return The key on the keyboard correlated to it.
	 */
	public static Array<InputKey> getInputKeys(InputID inputID) {
		return inputs.get(inputID);
	}

	public static Array<InputKey.InputTypes> getInputTypes(InputID inputID) {
		return inputGroups.get(inputID);
	}

	/** Remove all current keyPressed info. */
	public static void resetKeys() {
		keysPressed.clear();
		keysJustPressed.clear();
	}

	/**
	 * This function updates the internal arrays in Interactions for the array
	 * variables that record what {@link InputKey.InputTypes} are pressed, based on
	 * the {@link Interactions#inputs} HashMap, such as
	 * {@link Interactions#keysPressed}.
	 */
	public static void updateKeys(boolean shouldResetKeys) {
		if (shouldResetKeys) {
			resetKeys();
		}
		for (InputID inputID : InputID.values()) {
			for (InputKey inputKey : new Array.ArrayIterator<>(inputs.get(inputID))) {
				// ^ The new ... stuff was added to remove warnings. RHS is the same as
				// inputs.get(inputID)
				// LibGDX doesn't like nested iteration on arrays without this.
				if (Gdx.input.isKeyPressed(inputKey.getKey())) {
					keysPressed.add(inputKey.getType());
				}
				if (Gdx.input.isKeyJustPressed(inputKey.getKey())) {
					keysJustPressed.add(inputKey.getType());
				}
			}
		}
	}

	/**
	 * This function is used to manually add key presses to the keys pressed arrays.
	 * 
	 * @param inputKey         The inputKey to add
	 * @param shouldResetKeys  Should this method clear current key presses
	 * @param isKeyJustPressed Should this method add this key press to the once per
	 *                         hold key press list.
	 */
	public static void manualAddKey(InputKey inputKey, boolean shouldResetKeys, boolean isKeyJustPressed) {
		if (shouldResetKeys) {
			resetKeys();
		}
		if (isKeyJustPressed) {
			keysJustPressed.add(inputKey.getType());
		} else {
			keysPressed.add(inputKey.getType());
		}

	}

	/**
	 * Returns the Keys assigned to the enum constant {@link InputKey.InputTypes} as
	 * an array of strings.
	 * 
	 * @param inputType {@link InputKey.InputTypes} enum constant
	 * @return A LibGDX Array of the string names for the keys assigned to
	 *         {@link InputKey.InputTypes}
	 */
	public static Array<String> getKeyStrings(InputKey.InputTypes inputType) {
		Array<String> validKeys = new Array<>();
		for (Array<InputKey> inputKeys : inputs.values()) {
			for (InputKey inputKey : new Array.ArrayIterator<>(inputKeys)) {
				// ^ The new ... stuff was added to remove warnings. RHS is the same as just
				// inputKeys
				// LibGDX doesn't like nested iteration on arrays without this.
				if (inputKey.getType() == inputType) {
					validKeys.add(getKeyString(inputKey));
				}
			}
		}
		return validKeys;
	}

	/**
	 * Returns the Keys assigned to the enum constant {@link InputKey.InputTypes} as
	 * a string. Uses {@link #getKeyStrings(InputKey.InputTypes) getKeyStrings}
	 * 
	 * @param inputType {@link InputKey.InputTypes} enum constant
	 * @return A string of all valid keys for the {@link InputKey.InputTypes} key.
	 */
	public static String getKeyString(InputKey.InputTypes inputType) {
		Array<String> validKeys = getKeyStrings(inputType);
		// If there are no results, return "undefined"
		if (validKeys.size == 0) {
			return "undefined";
			// If there is 1 result, output that result alone
		} else if (validKeys.size == 1) {
			return validKeys.first();
		}

		// Otherwise, return them in an array format.
		String output = "[";
		for (int i = 0; i < validKeys.size - 1; i++) {
			output += validKeys.get(i) + (i == validKeys.size - 2 ? "" : ",");
		}
		output += " or " + validKeys.get(validKeys.size - 1);
		return output + "]";
	}

	/**
	 * Turn an {@link InputKey} into string format.
	 * 
	 * @param inputKey Desired InputKey
	 * @return String version of the input key.
	 */
	public static String getKeyString(InputKey inputKey) {
		return Input.Keys.toString(inputKey.getKey());
	}

	/**
	 * Checks to see if a inputType is being pressed. Eg. Is COOK_RIGHT being
	 * pressed? This is equivalent to LibGDX's Gdx.input.isKeyPressed()
	 * 
	 * @param inputType The inputType to check for.
	 * @return boolean : true if inputType is being pressed.
	 */
	public static boolean isPressed(InputKey.InputTypes inputType) {
		return keysPressed.contains(inputType, true);
	}

	/**
	 * Checks to see if a inputType has just been pressed. Eg. Has COOK_RIGHT JUST
	 * been pressed? This is equivalent to LibGDX's Gdx.input.isKeyJustPressed()
	 * 
	 * @param inputType The inputType to check for.
	 * @return boolean : true if inputType has been pressed.
	 */
	public static boolean isJustPressed(InputKey.InputTypes inputType) {
		return keysJustPressed.contains(inputType, true);
	}

	/**
	 * Convert a FoodItem's foodID into another foodID using station of stationID.
	 * 
	 * @param foodID    : The FoodID of the input ingredient.
	 * @param stationID : The StationID of the station being used.
	 * @return FoodID of the new ingredient, OR null if the station cannot interact
	 *         with this foodID.
	 */
	public static InteractionResult interaction(FoodID foodID, StationID stationID) {
		return interactions.get(InteractionKey(foodID, stationID));
	}

	/**
	 * Creates an interaction key out of foodID and stationID.
	 * 
	 * @return The key made out of both arguments.
	 */
	private static String InteractionKey(FoodID foodID, StationID stationID) {
		return String.format("%s-%s", foodID.ordinal(), stationID.ordinal());
	}
}
