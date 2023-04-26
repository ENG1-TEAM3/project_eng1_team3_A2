package powerups;

import java.util.ArrayList;
import java.util.Random;

import game.GameScreen;
import interactions.InputKey.InputTypes;
import stations.CookInteractable;
import stations.PreparationStation;

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

	private int cooldown = 0;

	private PowerUp activePowerUp = null;

	private GameScreen gameScreen;

	public PowerUpHandler(GameScreen gameScreen) {
		this.gameScreen = gameScreen;
	}

	public PowerUp activatePower(int slot) {
		if (slot < 0 || slot > POWERUP_SLOTS - 1 || cooldown > 0) {
			return null;
		}

		if (currentPowerUps[slot] == null) {
			// Just for testing set the current power up to auto station.
			activePowerUp = PowerUp.AUTO_STATION;
			cooldown = activePowerUp.duration();
			return null;
		}

		activePowerUp = currentPowerUps[slot];
		cooldown = activePowerUp.duration();

		currentPowerUps[slot] = null;
		return activePowerUp;
	}

	public boolean updateCoolDown(float dt) {
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

	public void addPowerUp() {
		for (int i = 0; i < POWERUP_SLOTS; i++) {
			if (currentPowerUps[i] == null) {
				currentPowerUps[i] = powerUps.get(random.nextInt(powerUps.size()));
				return;
			}
		}
	}

	public PowerUp activePowerUp() {
		return activePowerUp;
	}

}