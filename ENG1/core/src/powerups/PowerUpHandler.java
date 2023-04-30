package powerups;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import game.GameScreen;
import interactions.InputKey.InputTypes;
import stations.CookInteractable;
import stations.PreparationStation;


/**
 * Class to handle usage of powerUps
 */
public class PowerUpHandler {
    /** Amount of powerups that can be stored */
	private static final int POWERUP_SLOTS = 1;
    /** The powerUp names */
	private static final PowerUp[] potentialPowerups = { PowerUp.AUTO_STATION, PowerUp.DOUBLE_MONEY,
			PowerUp.SATISFIED_CUSTOMER, PowerUp.BONUS_TIME, PowerUp.FASTER_COOKS };
    /** Construct an Arraylist that has more of the powerUps with higher weights than those with lower weights*/
	public static final ArrayList<PowerUp> powerUps = new ArrayList<>();
	static {
		for (PowerUp powerUp : potentialPowerups) {
			for (int i = 0; i < powerUp.weight(); i++) {
				powerUps.add(powerUp);
			}
		}
	}

	private final Random random = new Random();

	private PowerUp[] currentPowerUps = new PowerUp[POWERUP_SLOTS];

	private static int cooldown = 0;
    /** The currently active powerUp*/
	private static PowerUp activePowerUp = null;

	private final GameScreen gameScreen;

    private final Sprite activePowerUpSprite;

    /**
     * The PowerUpHandler Constructor
     * @param gameScreen The gameScreen on which these powerUps should affect
     */
	public PowerUpHandler(GameScreen gameScreen) {
		this.gameScreen = gameScreen;
		activePowerUpSprite = new Sprite();
	}


    /**
     * Activate a powerUp out of the stores ones
     * @param slot The index in the stored powerUp array list to access
     * @return null if that powerUp could not be activated, the powerUp instance if it can be activated
     */
	public PowerUp activatePower(int slot) {
		if (slot < 0 || slot > POWERUP_SLOTS - 1 || cooldown > 0) {
			return null;
		}

		if (currentPowerUps[slot] == null) {
			return null;
		}

		activePowerUp = currentPowerUps[slot];
		activePowerUpSprite.setTexture(new Texture(Gdx.files.internal("powerups/" + activePowerUp.spritePath())));
		cooldown = Math.abs(activePowerUp.duration());

		currentPowerUps[slot] = null;
		return activePowerUp;
	}

    /**
     * Use up a powerUp that does not automatically remove itself
     * @return true if the powerUp could be removed, false if there is no powerUp to remove
     */
	public static boolean usePowerUp() {
		cooldown--;

		if (activePowerUp == null) {
			return false;
		}
		if (cooldown <= 0) {
			activePowerUp = null;
			cooldown = 0;
		}

		return true;
	}


    /**
     * Update the coolDown of a powerUp
     * @param dt The amount of time passed
     * @return True if the powerUp could be updated, false otherwise
     */
	public boolean updateCoolDown(float dt) {

		if (activePowerUp.duration() < 0) {
			return false;
		}

		cooldown -= dt;
		if (cooldown <= 0) {
			cooldown = 0;
			activePowerUp = null;
			return true;
		}

        if (activePowerUp == PowerUp.AUTO_STATION) {
            if (cooldown % (activePowerUp.duration() / 20) == 0) {
                for (CookInteractable interactable : gameScreen.getInteractables()) {
                    if (interactable instanceof PreparationStation) {
                        if (!((PreparationStation) interactable).isLocked()) {
                            interactable.interact(gameScreen.getCurrentCook(), InputTypes.USE);
                        }
                    }
                }
            }
        }

		return false;
	}

    /**
     * Render the current stored / active powerUp
     * @param batch The batch to render with
     * @param pos The vector of where to render
     */
	public void render(SpriteBatch batch, Vector2 pos) {
		batch.draw(activePowerUpSprite, pos.x * 32 - 16, pos.y * 32 - 16, 32, 32);
	}

    /**
     * Add a powerUp to the storage
     * @param reset Whether the currently stored powerUp should be replaced
     */
	public void addPowerUp(boolean reset) {
		for (int i = 0; i < POWERUP_SLOTS; i++) {
			if (currentPowerUps[i] == null || reset) {
				currentPowerUps[i] = powerUps.get(random.nextInt(powerUps.size()));
				return;
			}
		}
	}

    /**
     * Return the active PowerUp
     * @return the active PowerUp
     */
	public static PowerUp activePowerUp() {
		return activePowerUp;
	}

    /**
     * Get the powerUp in storage at a certain index
     * @param index The index of the powerUp to return
     * @return the powerUp defined above
     */
    public PowerUp getCurrentPowerUp(int index){
        return currentPowerUps[index];
    }


    /**
     * Get the cooldown of the current powerUp
     * @return the cooldown of the current powerUp
     */
	public int getCooldown() {
		return cooldown;
	}

    /**
     * Get the whole array of stored powerUps
     * @return the array of stored powerUps
     */
    public PowerUp[] getCurrentPowerUps() {
        return currentPowerUps;
    }

    /**
     * Restore the powerUpHandler from data
     * @param cldown - cooldown
     * @param activPowerUp - The active powerup
     * @param puSlots - The array of stored powerUps
     */
    public void restoreFromData(int cldown, PowerUp activPowerUp, PowerUp[] puSlots){
        cooldown = cldown;
        activePowerUp = activPowerUp;
        this.currentPowerUps = puSlots;
    }





}