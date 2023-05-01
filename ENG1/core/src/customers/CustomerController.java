package customers;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import food.Recipe;
import game.GameScreen;
import game.GameSprites;
import stations.ServingStation;
import game.MenuScreen;
import powerups.PowerUp;
import powerups.PowerUpHandler;

import java.util.Random;

/**
 * The class that controls how the {@link Customer}s are added to the stations,
 * what {@link food.Recipe}s they are given and so on.
 */
public class CustomerController {

	/** An {@link Array} of {@link Customer}s currently waiting. */
	private final Array<Customer> customers;
	/** The {@link Sprite} of the {@link Customer}. */
	private static Sprite customerSprite;
	/**
	 * An array of all {@link ServingStation}s to assign to the {@link Customer}s.
	 */
	private static Array<ServingStation> servingStations;

	/** Integers corresponding to their names */
	private int customersLeft, customersServed, totalCustomersToServe;

	/** The {@link game.GameScreen} to send the {@link #customersServed} to. */
	private final GameScreen gameScreen;
	/** Time in seconds of the last customer spawn */
	private int lastCustomerSpawnTime;
	/** The cooldown time between customer spawns */
	private int timeBetweenSpawnsSeconds;

	/**
	 * Constructor for the {@link CustomerController}. <br>
	 * It sets up the array that the {@link Customer}s will be stored in.
	 * 
	 * @param gameScreen The {@link GameScreen} that the {@link CustomerController}
	 *                   was created by.
	 */
	public CustomerController(GameScreen gameScreen) {

		this.customers = new Array<>();

		customerSprite = GameSprites.getInstance().getSprite(GameSprites.SpriteID.CUSTOMER, "0");
		customerSprite.setSize(42.5F, 70);
		servingStations = new Array<>();
		this.gameScreen = gameScreen;
	}

	/**
	 * A function with no input that returns whether a {@link Customer} can (true)
	 * or not (false) be added to a {@link ServingStation} on the map. a
	 * {@link Customer} can be added to a {@link ServingStation} on the
	 * {@link game.GameScreen}'s map or not. or not.
	 *
	 * @return A {@code boolean} that is true when a {@link Customer} can be added,
	 *         and false when they cannot.
	 */
	public boolean canAddCustomer() {
		Array<ServingStation> stations = servingStations;
		for (ServingStation station : stations) {
			if (station.getCustomer() == null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Add a {@link Customer} to a {@link ServingStation}.
	 */
	public void addCustomer(int patience) {
		System.out.println("adding cust inside");
		// Get a deep copy of all the ServingStations.
		Array<ServingStation> emptyStations = new Array<>(servingStations);
		// Loop through and remove all the stations that have a
		// Customer already.
		for (int i = emptyStations.size - 1; i >= 0; i--) {
			if (emptyStations.get(i).hasCustomer()) {
				emptyStations.removeIndex(i);
			}
		}
		// Now that the only stations left are the ones without Customers,
		// randomly pick one and add a customer to it.

		Random random = new Random();
		int randomStationIndex = random.nextInt(emptyStations.size);
		ServingStation chosenStation = emptyStations.get(randomStationIndex);
		int actualStationIndex = servingStations.indexOf(chosenStation, true);

		Customer newCustomer = new Customer(customerSprite, actualStationIndex,
				new Vector2(chosenStation.getCustomerX(), chosenStation.getCustomerY()));

		customers.add(newCustomer);

		this.setupCustomer(newCustomer, gameScreen.getTotalSecondsRunningGame(), patience);

		newCustomer.randomRecipe();
		chosenStation.setCustomer(newCustomer);

		// Show the Customer's recipe
		gameScreen.getGameHud().addRecipeToRender(servingStations.indexOf(chosenStation, true),
				Recipe.firstRecipeOption(newCustomer.getRequestName()));
		setCustomersLeft(customersLeft - 1);
	}

	/////////////////////////////////////////// NEW ASSESSMENT 2
	/////////////////////////////////////////// ADDITION////////////////////////////////////////////////

	/**
	 * Restore a customer from save
	 * 
	 * @param stationIndex The index of the serving station that this customer
	 *                     corresponds to
	 * @param spawnTime    The spawn time of the customer in seconds
	 * @param deadTime     The leaving time of the customer in seconds
	 * @param order        The string order name of the customer
	 */
	public void restoreCustomerFromSave(int stationIndex, int spawnTime, int deadTime, String order) {
		Customer newCust = new Customer(customerSprite, stationIndex, new Vector2(
				servingStations.get(stationIndex).getCustomerX(), servingStations.get(stationIndex).getCustomerY()));
		customers.add(newCust);

		this.setupCustomer(newCust, spawnTime, deadTime - spawnTime);
		newCust.setRequestName(order);
		servingStations.get(stationIndex).setCustomer(newCust);
		gameScreen.getGameHud().addRecipeToRender(stationIndex, Recipe.firstRecipeOption(newCust.getRequestName()));
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Removes a customer from a {@link ServingStation}.
	 *
	 * @param station The {@link ServingStation} to remove the {@link Customer}
	 *                from.
	 */
	public void removeCustomer(ServingStation station) {
		// First make sure the station has a Customer
		if (!station.hasCustomer()) {
			return;
		}
		// Remove the customer from the customers array.
		customers.removeValue(station.getCustomer(), true);
		gameScreen.getGameHud().removeRecipeToRender(servingStations.indexOf(station, true));
		// Then, if it has a customer, set the customer of the station
		// to null.
		station.setCustomer(null);
	}

	/**
	 * Sets the number of {@link Customer}s needing to be served.
	 *
	 * @param customersLeft The number of {@link Customer}s left.
	 */
	public void setCustomersLeft(int customersLeft) {
		this.customersLeft = customersLeft;
		gameScreen.getGameHud().updateCustomersLeftLabel(customersLeft);
	}

	/**
	 * Gets the number of {@link Customer}s needing to be served.
	 *
	 * @return {@code int} : The number of {@link Customer}s left.
	 */
	public int getCustomersLeft() {
		return customersLeft;
	}

	/**
	 * Sets the number of {@link Customer}s needing to be served.
	 * 
	 * @param customersServed The number of {@link Customer}s served.
	 */
	public void setCustomersServed(int customersServed) {
		this.customersServed = customersServed;
		gameScreen.getGameHud().updateCustomersServedLabel(customersServed);

	}

	/**
	 * Gets the number of {@link Customer}s that have been served.
	 *
	 * @return {@code int} : The number of {@link Customer}s served.
	 */
	public int getCustomersServed() {
		return customersServed;
	}

	/**
	 * Adds a {@link ServingStation} to the {@link Array} of {@link ServingStation}s
	 * so that {@link Customer}s can be assigned to them.
	 *
	 * @param station The {@link ServingStation} to add as an option for
	 *                {@link Customer}'s to be assigned to.
	 */
	public void addServingStation(ServingStation station) {
		servingStations.add(station);
	}

	/**
	 * An {@link Array} of all of the {@link ServingStation}s within the game.
	 *
	 * @return {@link Array}&lt;{@link ServingStation}&gt; : The {@link Array} of
	 *         {@link ServingStation}s.
	 */
	public Array<ServingStation> getServingStations() {
		return servingStations;
	}

	/**
	 * Called when a {@link Customer} has been sucessfully served at a
	 * {@link ServingStation} by a {@link cooks.Cook}.
	 *
	 * @param station The {@link ServingStation} that the {@link Customer} was
	 *                served at.
	 */
	public void customerServed(ServingStation station) {
		int customerInd = customers.indexOf(station.getCustomer(), true);
		if (customerInd < 0) {
			return;
		}
		removeCustomer(station);
		setCustomersServed(customersServed + 1);
	}

	/**
	 * Clears the {@link Array} of {@link ServingStation}. Should be called when the
	 * game is reset.
	 */
	public void clearServingStations() {

		servingStations.clear();
		gameScreen.getGameHud().clearRecipes();
	}

	////////////////////////////////////////// ALL METHODS BELOW ADDED FOR
	////////////////////////////////////////// ASSESSMENT
	////////////////////////////////////////// 2//////////////////////////////////

	/**
	 * Set the total amount of customers to serve
	 * 
	 * @param amount An integer amount of customers
	 */
	public void setTotalCustomersToServe(int amount) {
		this.totalCustomersToServe = amount;
	}

	/**
	 * Get the total amount of customers to serve
	 * 
	 * @return The integer amount of customers defined above
	 */
	public int getTotalCustomersToServe() {
		return this.totalCustomersToServe;
	}

	/**
	 * Get the Array of customers that currently exist
	 * 
	 * @return The array defined above
	 */
	public Array<Customer> getCustomers() {
		return this.customers;
	}

	public int timeMultiplier = 1;

	public void multiplyTimeBetweenServes() {
		timeMultiplier = 2;
	}

	public int timeBetweenSpawnsSeconds() {
		return timeBetweenSpawnsSeconds / timeMultiplier;
	}

	/**
	 * Try to spawn a certain amount of customers every certain amount of time
	 * dictated by difficulty and mode
	 * 
	 * @param msd The difficulty of the game - If the mode is endless this has no
	 *            effect
	 * @param md  The mode of the game - If it is endless the difficulty has no
	 *            effect
	 */
	public void tryToSpawnCustomer(MenuScreen.difficulty msd, MenuScreen.mode md) {
		int patience;
		if (md == MenuScreen.mode.SCENARIO) {
			switch (msd) {
			case MEDIUM:
				timeBetweenSpawnsSeconds = 50;
				patience = 50;
				break;
			case HARD:
				timeBetweenSpawnsSeconds = 40;
				patience = 40;
				break;
			default:
				timeBetweenSpawnsSeconds = 60;
				patience = 60;
			}
		} else {
			if (this.customersServed < 5) { // Less than 5 customers spawn rules easy
				timeBetweenSpawnsSeconds = 60;
				patience = 60;
			} else if (this.customersServed < 10) { // 5>= c > 10 customers spawn rules medium
				timeBetweenSpawnsSeconds = 60;
				patience = 60;
			} else { // 10 >= c spawn rules hard
				timeBetweenSpawnsSeconds = 60;
				patience = 60;
			}
		}
		timeBetweenSpawnsSeconds *= timeMultiplier;
//		if (PowerUpHandler.activePowerUp() == PowerUp.BONUS_TIME) {
//			PowerUpHandler.usePowerUp();
//			timeMultiplier = 2;
//		} else {
//			timeMultiplier = 1;
//		}

		if ((gameScreen.getTotalSecondsRunningGame() - lastCustomerSpawnTime >= timeBetweenSpawnsSeconds)
				&& canAddCustomer()) {
			timeMultiplier = 1;
			addCustomer(patience);
			if (md == MenuScreen.mode.ENDLESS) {
				if (this.customersServed > 8) {
					addCustomer(patience); // If more than 8 customers served make wave of 2
				}
				if (this.customersServed > 13) {
					addCustomer(patience); // If more than 13 customers served make wave of 3
				}
			}
			if (md == MenuScreen.mode.SCENARIO) {
				if (msd == MenuScreen.difficulty.MEDIUM) {
					addCustomer(patience);
				}
				if (msd == MenuScreen.difficulty.HARD) {
					addCustomer(patience);
					addCustomer(patience);
				}
			}

			lastCustomerSpawnTime = gameScreen.getTotalSecondsRunningGame();
		}
	}

	/**
	 * Set up the timings for a customer
	 * 
	 * @param cus       The customer to set up timings for
	 * @param spawnTime The time when the customer was spawned
	 * @param patience  The amount of time the customer should wait before leaving
	 */
	public void setupCustomer(Customer cus, int spawnTime, int patience) {
		cus.setTimings(spawnTime, spawnTime + patience);
		// System.out.println("Customer added with spawntime " + spawnTime + " deadtime
		// " + (spawnTime + patience));
	}

	/**
	 * Remove specified customer if it is past their leave time
	 * 
	 * @param customer The customer to check for removal
	 */
	public void removeCustomerIfExpired(Customer customer) {
		if (gameScreen.getTotalSecondsRunningGame() >= customer.getDeadTime()) {
			// System.out.println("Removing customer");
			removeCustomer(servingStations.get(customer.getStationIndex()));
			gameScreen.loseReputation();
		}
	}

	/**
	 * Return a float between 0.0 and 1.0, corresponding to the progress until next
	 * customer spawn
	 * 
	 * @return The float specified in the line above
	 */
	public float returnFractionProgressUntilNextCustomer() {
		return (gameScreen.getTotalSecondsRunningGame() - lastCustomerSpawnTime) / (float) (timeBetweenSpawnsSeconds);
	}

	/**
	 * Set the last customer spawn time
	 * 
	 * @param tm Time in seconds since game start
	 */
	public void setLastCustomerSpawnTime(int tm) {
		this.lastCustomerSpawnTime = tm;
	}

	/**
	 * Get the last customer spawn time
	 * 
	 * @return The time in seconds specified in the line above
	 */
	public int getLastCustomerSpawnTime() {
		return lastCustomerSpawnTime;
	}
}
