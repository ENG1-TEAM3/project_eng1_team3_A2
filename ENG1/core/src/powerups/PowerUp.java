package powerups;

public enum PowerUp {

	AUTO_STATION(1, 1000, "auto_station.png"), DOUBLE_MONEY(1, 500, "double_money.png"),
	SATISFIED_CUSTOMER(1, -1, "satisfied_customer.png"), BONUS_TIME(1, -2, "bonus_time.png"),
	FASTER_COOKS(1, 500, "faster_cooks.png");

	private final int weight, duration;
	private final String spritePath;

	PowerUp(int weight, int duration, String spritePath) {
		this.weight = weight;
		this.duration = duration;
		this.spritePath = spritePath;
	}

	public int weight() {
		return weight;
	}

	public int duration() {
		return duration;
	}

	public String spritePath() {
		return spritePath;
	}

}
