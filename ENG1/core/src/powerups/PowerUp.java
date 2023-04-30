package powerups;

/**
 * An enum containing all the power up types
 */
public enum PowerUp {

	AUTO_STATION(1, 1000, "auto_station.png"), DOUBLE_MONEY(1, 500, "double_money.png"),
	SATISFIED_CUSTOMER(1, -1, "satisfied_customer.png"), BONUS_TIME(1, -2, "bonus_time.png"),
	FASTER_COOKS(1, 500, "faster_cooks.png");

    /** Weight specifies chance of getting the powerUp compared to others, duration is how long it will last*/
	private final int weight, duration;
	private final String spritePath;

    /**
     * Enum constructor
     * @param weight relative chance of getting powerUp
     * @param duration duration of powerUp
     * @param spritePath spritePath of powerup
     */
	PowerUp(int weight, int duration, String spritePath) {
		this.weight = weight;
		this.duration = duration;
		this.spritePath = spritePath;
	}

    /**
     * Get the weight of this powerUp
     * @return The weight
     */
	public int weight() {
		return weight;
	}

    /**
     * Get the duration of this powerUp
     * @return The duration
     */
	public int duration() {
		return duration;
	}

    /**
     * Get the spritePath of this powerUP
     * @return The spritePath
     */
	public String spritePath() {
		return spritePath;
	}

}
