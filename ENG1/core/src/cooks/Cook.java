package cooks;

import static helper.Constants.PPM;


import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import food.FoodItem;
import food.FoodItem.FoodID;
import food.FoodStack;
import game.GameSprites;
import interactions.InputKey;
import interactions.Interactions;
import powerups.PowerUp;
import powerups.PowerUpHandler;

/** A {@link GameEntity} that the player controls to interact with the game. */
public class Cook extends GameEntity{
	/** The cook's current sprite. */
	private Sprite sprite;
	/** The control arrow sprite. */
	private final Sprite controlSprite;
    /** The reference to the gameSprites instance*/
	private final GameSprites gameSprites;
    /** The cookInteractor that corresponds to this cook */
	private final CookInteractor cookInteractor;
	// private GameScreen gameScreen;
	/** The direction this cook is facing. */
	private Facing dir;
	/**
	 * The cook's stack of things, containing all the items they're holding. Index 0
	 * = Top Item
	 */
	public FoodStack foodStack;
	/**
	 * The WASD/movement inputs currently being made. Note that if S and D are being
	 * input at the same time, then inputs = { Facing.RIGHT, Facing.DOWN }
	 */
	private final Array<Facing> inputs;
    /** An ID that can be set after initialization to change the cook's sprite */
	private int ID;

	/** All possible directions the cook can be facing. */
	public enum Facing {
		RIGHT, LEFT, UP, DOWN, NONE
	}

	/**
     * Cook Constructor.
     *
     * @param width  Pixel Width of the {@link Cook}'s {@link Body}.
     * @param height Pixel Height of the {@link Cook}'s {@link Body}.
     * @param body   The {@link World}.{@link Body} which will become the
     *               {@link Cook}
     */
	public Cook(float width, float height, Body body) {
		super(width, height, body);
		this.ID = 0; //Cook defaults to sprite 0
		this.dir = Facing.DOWN;
		this.speed = 10f;
		this.gameSprites = GameSprites.getInstance();
		this.controlSprite = gameSprites.getSprite(GameSprites.SpriteID.COOK, "control");

		// Initialize FoodStack
		this.foodStack = new FoodStack();

		// Input array, with the order of inputs the user has in what direction.
		// The oldest button pressed is the one used. Pressing the opposite key removes
		// them.
		this.inputs = new Array<>();

		// Set the sprite
		this.setSprite();

		float cookInteractorSize = 32;

		this.cookInteractor = new CookInteractor(x, y, cookInteractorSize);
	}

    /**
     * Set the cookID to change cook sprite     //////////////////////////ADDED_FOR_ASSESSMENT2/////////////////////////
     * @param num The number to set the ID to (0,1,2)
     *            Numbers other than these will just result in the ID being set to zero.
     */
	public void setCookID(int num) {
        if (num == 0 || num == 1 || num ==2) {
            this.ID = num;
        }
        else {
            this.ID = 0;
        }
	}

    /**
     * Get the cookId
     * @return cookID
     */
	public int getCookID() {
		return this.ID;
	}

	/**
	 * Responsible for processing user input information into {@link #inputs},
	 * {@link #velX} and {@link #velY}.
	 */
	public void userInput() {
		velX = 0F;
		velY = 0F;
		if (Interactions.isPressed(InputKey.InputTypes.COOK_RIGHT)) {
			velX += 1;
			if (!inputs.contains(Facing.RIGHT, true)) {
				inputs.add(Facing.RIGHT);
			}
		} else {
			inputs.removeValue(Facing.RIGHT, true);
		}
		if (Interactions.isPressed(InputKey.InputTypes.COOK_LEFT)) {
			velX += -1;
			if (!inputs.contains(Facing.LEFT, true)) {
				inputs.add(Facing.LEFT);
			}
		} else {
			inputs.removeValue(Facing.LEFT, true);
		}
		if (Interactions.isPressed(InputKey.InputTypes.COOK_UP)) {
			velY += 1;
			if (!inputs.contains(Facing.UP, true)) {
				inputs.add(Facing.UP);
			}
		} else {
			inputs.removeValue(Facing.UP, true);
		}
		if (Interactions.isPressed(InputKey.InputTypes.COOK_DOWN)) {
			velY += -1;
			if (!inputs.contains(Facing.DOWN, true)) {
				inputs.add(Facing.DOWN);
			}
		} else {
			inputs.removeValue(Facing.DOWN, true);
		}

		setDir();

        for (int i = 0; i < Interactions.getInputTypes(Interactions.InputID.COOK_INTERACT).size; i++){
            if (Interactions.isJustPressed(Interactions.getInputTypes(Interactions.InputID.COOK_INTERACT).get(i))) {
                cookInteractor.checkCollisions(this,
                        Interactions.getInputTypes(Interactions.InputID.COOK_INTERACT).get(i));
            }
        }
		if (PowerUpHandler.activePowerUp() == PowerUp.FASTER_COOKS) { // Double speed if faster cooks is active
			body.setLinearVelocity(velX * speed * 2, velY * speed * 2);
		} else {
			body.setLinearVelocity(velX * speed, velY * speed);
		}

	}

	/**
	 * The update function for the {@link Cook}, which updates the {@link Cook}'s
	 * {@link #x} and {@link #y} values, and updates the position of the
	 * {@link Cook}'s {@link CookInteractor}.
	 * 
	 * @param delta The time between frames as a float.
	 */
	@Override
	public void update(float delta) {
		x = body.getPosition().x * PPM;
		y = body.getPosition().y * PPM;
		this.cookInteractor.updatePosition(x, y, dir);
	}

	/**
	 * Update the current {@link Sprite} of the {@link Cook}.
	 */
	private void setSprite() {
		// Set up sprite string
        String spriteName;
        if (this.ID == 1) {
            spriteName = "c1";
        }
        else if (this.ID == 2) {
            spriteName = "c2";
        }
        else{
            spriteName = "";
        }
		// If holding something, add "h" to the start of the sprite name.
		if (foodStack.size() > 0) {
			spriteName += "h";
		}
		sprite = gameSprites.getSprite(GameSprites.SpriteID.COOK, spriteName + dir);

	}

    /**
     * Set the facing of the Cook - This method is used to restore from a save file
     * @param face The facing to set the cook to
     */
	public void setFacing(Cook.Facing face) {
		this.dir = face;
	}

	/**
	 * Render the {@link Cook} and their {@link FoodStack}.
	 * 
	 * @param batch The {@link SpriteBatch} that the {@link Cook} will render using.
	 */
	@Override
	public void render(SpriteBatch batch) {
		setSprite();
		sprite.setPosition(x - width / 2 - 2.5F, y - height / 2); // -2.5 for a similar reason to the below one
		this.sprite.setSize(47.5F, 70);

		// If the cook is looking anywhere but down, draw the food first
		if (dir != Facing.DOWN) {
			renderFood(batch);
			sprite.draw(batch);
		} else {
			sprite.draw(batch);
			renderFood(batch);
		}
	}

    /**
     * Render the control arrow over the currently controlled cook
     * @param batch Spritebatch to render with
     */
	public void renderControlArrow(SpriteBatch batch) {
		controlSprite.setSize(32, 22F);
		controlSprite.setPosition(x - controlSprite.getWidth() / 2,
				y - controlSprite.getHeight() / 4 + sprite.getHeight());
		controlSprite.draw(batch);
	}

	/**
	 * Debug rendering using the {@link SpriteBatch}. Unused.
	 * 
	 * @param batch The {@link SpriteBatch} used to render.
	 */
	@Override
	public void renderDebug(SpriteBatch batch) {

	}

	/**
	 * Rendering using the {@link ShapeRenderer}. Unused.
	 * 
	 * @param shape The {@link ShapeRenderer} used to draw.
	 */
	@Override
	public void renderShape(ShapeRenderer shape) {

	}

	/**
	 * Debug rendering using the {@link ShapeRenderer}. Unused.
	 * 
	 * @param shape The {@link ShapeRenderer} used to draw.
	 */
	@Override
	public void renderShapeDebug(ShapeRenderer shape) {
		cookInteractor.renderDebug(shape);
	}

	/**
	 * Return the X pixel offset from the cook's position that the cook's FoodStack
	 * requires for rendering.
	 */
	private float foodRelativeX(Cook.Facing dir) {
		switch (dir) {
		case RIGHT:
			return 30F;
		case LEFT:
			return -30F;
		default:
			return 0F;
		}
	}

	/**
	 * Return the Y pixel offset from the cook's position that the cook's FoodStack
	 * requires for rendering.
	 */
	private float foodRelativeY(Cook.Facing dir) {
		switch (dir) {
		case UP:
			return -14F;
		case DOWN:
			return -25F;
		case LEFT:
		case RIGHT:
			return -24F;
		default:
			return 0F;
		}
	}

	/**
	 * Renders the {@link FoodStack} of the {@link Cook} visually.
	 * 
	 * @param batch The {@link SpriteBatch} that the {@link Cook} will render using.
	 */
	private void renderFood(SpriteBatch batch) {
		// Loop through the items in the food stack.
		// It is done from the end of the stack to the start because the stack's top is
		// at 0, and the bottom at the end.
		Array<FoodID> foodList = foodStack.getStack();
		float xOffset = foodRelativeX(dir), yOffset = foodRelativeY(dir);
		// Get offset based on direction.

		float drawX = x, drawY = y + 27F;
		for (int i = foodList.size - 1; i >= 0; i--) {
			Sprite foodSprite = gameSprites.getSprite(GameSprites.SpriteID.FOOD, String.valueOf(foodList.get(i)));
			Float drawInc = FoodItem.foodHeights.get(foodList.get(i));
			if (drawInc == null) {
				drawY += 5F;
				continue;
			}
            if (foodList.get(i) != FoodID.cook) {
                foodSprite.setScale(2F);
                foodSprite.setPosition(drawX - foodSprite.getWidth() / 2 + xOffset,
                        drawY + foodSprite.getHeight() / 2F + yOffset);
                foodSprite.draw(batch);
            }
            else{
                foodSprite.setSize(47.5F, 70);
                foodSprite.setPosition(drawX - foodSprite.getWidth() / 2F + xOffset,
                        drawY + (foodSprite.getHeight() / (280F/47.5F)) + yOffset);
                foodSprite.draw(batch);
            }

			drawY += drawInc;
		}
	}

	/**
	 * Return the opposite direction to the input direction.
	 * 
	 * @param direction The input direction you want an opposite for.
	 * @return The opposite direction to the input.
	 */
	private Facing opposite(Facing direction) {
		switch (direction) {
		case UP:
			return Facing.DOWN;
		case DOWN:
			return Facing.UP;
		case RIGHT:
			return Facing.LEFT;
		case LEFT:
			return Facing.RIGHT;
		default:
			return Facing.NONE;
		}
	}

	/**
	 * Return the direction 90 degrees clockwise to the input direction.
	 * 
	 * @param direction The input direction you want a 90c rotation for.
	 * @return The 90c rotation direction to the input.
	 */
	private Facing rotate90c(Facing direction) {
		switch (direction) {
		case UP:
			return Facing.RIGHT;
		case DOWN:
			return Facing.LEFT;
		case RIGHT:
			return Facing.DOWN;
		case LEFT:
			return Facing.UP;
		default:
			return Facing.NONE;
		}
	}

	/**
	 * A function to find where the {@link Cook} should be facing depending on the
	 * order of inputs, the latest being prioritized, and ignoring any inputs that
	 * are input with their opposite. For example, pressing {Left, Up, Right} in the
	 * same order. Right is prioritized as it is the newest input, but the opposite
	 * Left was pressed, so Up is the final choice of direction.
	 */
	private void setDir() {
		// If the size of inputs is 0, just return and change nothing.
		if (inputs.size == 0) {
			return;
		}

		// Possible next direction is the direction that was just input
		Facing possibleNext = inputs.get(inputs.size - 1);
		Facing possibleOpp = opposite(possibleNext);
		// If there is the opposite input...

		if (inputs.contains(possibleOpp, true)) {
			// Now check that the same does not apply to the other directions.
			boolean hasPossibleRotated = inputs.contains(rotate90c(possibleNext), true),
					hasOppRotated = inputs.contains(rotate90c(possibleOpp), true);
			if (hasPossibleRotated ^ hasOppRotated) {
				// If it doesn't, set the direction to the one that is there.
				if (hasPossibleRotated) {
					dir = rotate90c(possibleNext);
				} else {
					dir = rotate90c(possibleOpp);
				}
			}
			// If both or neither of them are there, then change nothing.
		} else {
			// If the opposite isn't there, it's fine to switch.
			dir = possibleNext;
		}
	}


    /**
     * Get the current facing - Used in saving the game
     * @return The current facing
     */
	public Facing getDir() {
		return dir;
	}

    /**
     * Get the cook width
     * @return The current width
     */
	public float getWidth() {
		return this.width;
	}

    /**
     * Get the cook height
     * @return The current height
     */
	public float getHeight() {
		return this.height;
	}
}
