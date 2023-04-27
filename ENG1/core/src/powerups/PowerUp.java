package powerups;

public enum PowerUp {

	AUTO_STATION(1, 500), DOUBLE_MONEY(2, 500), SATISFIED_CUSTOMER(3, -1), BONUS_TIME(2, -2), FASTER_COOKS(1, 500);

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
