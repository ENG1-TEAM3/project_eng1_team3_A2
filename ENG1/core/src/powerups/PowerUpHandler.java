package powerups;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import food.FoodStack;
import food.Recipe;
import game.GameScreen;
import game.GameSprites;
import interactions.InputKey.InputTypes;
import stations.CookInteractable;
import stations.PreparationStation;
import stations.ServingStation;

public class PowerUpHandler {

	private static final int POWERUP_SLOTS = 1;

	private static final PowerUp[] potentialPowerups = { PowerUp.AUTO_STATION, PowerUp.DOUBLE_MONEY,
			PowerUp.SATISFIED_CUSTOMER, PowerUp.BONUS_TIME, PowerUp.FASTER_COOKS };
	public static final ArrayList<PowerUp> powerUps = new ArrayList<PowerUp>();
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

	private static PowerUp activePowerUp = null;

	private GameScreen gameScreen;

	public PowerUpHandler(GameScreen gameScreen) {
		this.gameScreen = gameScreen;
		activePowerUpSprite = new Sprite();
	}

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

	Sprite activePowerUpSprite;

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

		switch (activePowerUp) {
		case AUTO_STATION:
			if (cooldown % (activePowerUp.duration() / 20) == 0) {
				for (CookInteractable interactable : gameScreen.getInteractables()) {
					if (interactable instanceof PreparationStation) {
						if (!((PreparationStation) interactable).isLocked()) {
							interactable.interact(gameScreen.getCurrentCook(), InputTypes.USE);
						}
					}
				}
			}
			break;
		default:
			break;
		}

		return false;
	}

	public void render(SpriteBatch batch, Vector2 pos) {
		batch.draw(activePowerUpSprite, pos.x * 32 - 16, pos.y * 32 - 16, 32, 32);
	}

	public void addPowerUp(boolean reset) {
		for (int i = 0; i < POWERUP_SLOTS; i++) {
			if (currentPowerUps[i] == null || reset) {
				currentPowerUps[i] = powerUps.get(random.nextInt(powerUps.size()));
				return;
			}
		}
	}

	public static PowerUp activePowerUp() {

		return activePowerUp;
	}
	
	public int cooldown() {
		return cooldown;
	}

}