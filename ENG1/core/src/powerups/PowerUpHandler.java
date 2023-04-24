package powerups;

import java.util.ArrayList;
import java.util.Random;

public class PowerUpHandler {

	private static final int POWERUP_SLOTS = 3;

	private static final PowerUp[] potentialPowerups = { PowerUp.AUTOFLIP, PowerUp.DOUBLE_MONEY,
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

	private static PowerUp[] currentPowerUps = new PowerUp[POWERUP_SLOTS];

	public PowerUp activatePower(int slot) {
		if (slot < 0 || slot > POWERUP_SLOTS - 1) {
			return null;
		}

		PowerUp selectedPower = currentPowerUps[slot];
		// Do something with powerup!

		switch (selectedPower) {
		case AUTOFLIP:
			break;
		case DOUBLE_MONEY:
			break;
		case SATISFIED_CUSTOMER:
			break;
		case BONUS_TIME:
			break;
		case FASTER_COOKS:
			break;
		}

		currentPowerUps[slot] = null;
		return selectedPower;
	}

	public void addPowerUp() {
		for (int i = 0; i < POWERUP_SLOTS; i++) {
			if (currentPowerUps[i] == null) {
				currentPowerUps[i] = powerUps.get(random.nextInt(powerUps.size()));
				return;
			}
		}
	}

}

enum PowerUp {

	AUTOFLIP(1, 30), DOUBLE_MONEY(2, 30), SATISFIED_CUSTOMER(3, 30), BONUS_TIME(2, 30), FASTER_COOKS(1, 30);

	private final int weight, duration;

	PowerUp(int weight, int duration) {
		this.weight = weight;
		this.duration = duration;
	}

	public int weight() {
		return weight;
	}

	public int duration() {
		return duration;
	}

}