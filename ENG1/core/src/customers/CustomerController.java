package customers;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import food.Recipe;
import game.GameScreen;
import game.GameSprites;
import game.MenuScreen;
import stations.ServingStation;

import java.util.Random;

/**
 * The class that controls how the {@link Customer}s are
 * added to the stations, what {@link food.Recipe}s they are
 * given and so on.
 */
public class CustomerController {

    /** An {@link Array} of {@link Customer}s currently waiting. */
    public Array<Customer> customers;
    /** The {@link Sprite} of the {@link Customer}. */
    private static Sprite customerSprite;
    /** An array of all {@link ServingStation}s to assign to the {@link Customer}s.*/
    private static Array<ServingStation> servingStations;
    /** The number of {@link Customer}s to spawn. */
    private int customersLeft,
    /** The number of {@link Customer}s served. */
    customersServed;
    /** The {@link game.GameScreen} to send the {@link #customersServed} to. */
    private GameScreen gameScreen;

    private boolean hasAssignedIdsToServingStations = false;

    private int timeCount = 0;
    private int lastCustomerSpawnTime;
    private int timeBetweenSpawnsSeconds;
    /**
     * Constructor for the {@link CustomerController}.
     * <br>It sets up the array that the {@link Customer}s
     * will be stored in.
     * @param gameScreen The {@link GameScreen} that the {@link CustomerController}
     *                   was created by.
     */
    public CustomerController(GameScreen gameScreen) {
        this.customers = new Array<>();
        this.customersLeft = 0;
        this.customersServed = 0;
        customerSprite = GameSprites.getInstance().getSprite(GameSprites.SpriteID.CUSTOMER,"0");
        customerSprite.setSize(42.5F,70);
        servingStations = new Array<>();
        this.gameScreen = gameScreen;
    }

    /**
     * A function with no input that returns whether a
     * {@link Customer} can (true) or not (false) be
     * added to a {@link ServingStation} on the map.
     * a {@link Customer} can be added to a {@link ServingStation}
     * on the {@link game.GameScreen}'s map or not.
     * or not.
     * @return A {@code boolean} that is true when a
     *         {@link Customer} can be added, and false when
     *         they cannot.
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
     * @return {@code int} : The number of instructions in the
     *                       {@link Customer}'s recipe.
     *                       <br>It is -1 if the {@link Customer} fails
     *                       to spawn.
     */
    public void addCustomer(int patience) {
        // Get a deep copy of all the ServingStations.
        Array<ServingStation> emptyStations = new Array<>(servingStations);
        // Loop through and remove all the stations that have a
        // Customer already.
        for (int i = emptyStations.size - 1 ; i >= 0 ; i--) {
            if (emptyStations.get(i).hasCustomer()) {
                emptyStations.removeIndex(i);
            }
        }
        // Now that the only stations left are the ones without Customers,
        // randomly pick one and add a customer to it.
        Random random = new Random();
        int randomStationIndex = random.nextInt(emptyStations.size);
        ServingStation chosenStation = emptyStations.get(randomStationIndex);

        Customer newCustomer = new Customer(customerSprite,
                new Vector2(chosenStation.getCustomerX(),
                        chosenStation.getCustomerY()));


        customers.add(newCustomer);
        this.initialCus(newCustomer, gameScreen.getTotalSecondsRunningGame(), patience);


        newCustomer.index = randomStationIndex;
        newCustomer.randomRecipe();
        chosenStation.setCustomer(newCustomer);

        // Show the Customer's recipe
        gameScreen.getGameHud().addRecipeToRender(servingStations.indexOf(chosenStation,true), Recipe.firstRecipeOption(newCustomer.getRequestName()));

        customersLeft--;
    }

    /**
     * Removes a customer from a {@link ServingStation}.
     * @param station The {@link ServingStation} to remove
     *                the {@link Customer} from.
     */
    public void removeCustomer(ServingStation station) {
        // First make sure the station has a Customer
        if (!station.hasCustomer()) {
            return;
        }
        // Remove the customer from the customers array.
        customers.removeValue(station.getCustomer(),true);
        gameScreen.getGameHud().removeRecipeToRender(servingStations.indexOf(station, true));
        // Then, if it has a customer, set the customer of the station
        // to null.
        station.setCustomer(null);
    }

    /**
     * Sets the number of {@link Customer}s needing to be served.
     * @param customersLeft The number of {@link Customer}s left.
     */
    public void setCustomersLeft(int customersLeft) {
        this.customersLeft = customersLeft;
    }

    /**
     * Gets the number of {@link Customer}s needing to be served.
     * @return {@code int} : The number of {@link Customer}s left.
     */
    public int getCustomersLeft() {
        return customersLeft;
    }

    /**
     * Sets the number of {@link Customer}s needing to be served.
     * @param customersServed The number of {@link Customer}s served.
     */
    public void setCustomersServed(int customersServed) {
        this.customersServed = customersServed;
        gameScreen.setCustomerHud(customersServed);
    }

    /**
     * Gets the number of {@link Customer}s that have been served.
     * @return {@code int} : The number of {@link Customer}s served.
     */
    public int getCustomersServed() {
        return customersServed;
    }

    /**
     * Adds a {@link ServingStation} to the {@link Array} of
     * {@link ServingStation}s so that {@link Customer}s can
     * be assigned to them.
     * @param station The {@link ServingStation} to add as
     *                an option for {@link Customer}'s to
     *                be assigned to.
     */
    public void addServingStation(ServingStation station) {
        servingStations.add(station);
    }

    /**
     * An {@link Array} of all of the {@link ServingStation}s
     * within the game.
     * @return {@link Array}&lt;{@link ServingStation}&gt; :
     *                  The {@link Array} of {@link ServingStation}s.
     */
    public Array<ServingStation> getServingStations() {
        return servingStations;
    }

    /**
     * Called when a {@link Customer} has been sucessfully served at
     * a {@link ServingStation} by a {@link cooks.Cook}.
     * @param station The {@link ServingStation} that the {@link Customer}
     *                was served at.
     */
    public void customerServed(ServingStation station) {
        int customerInd = customers.indexOf(station.getCustomer(),true);
        if (customerInd < 0) {
            return;
        }
        removeCustomer(station);
        customersServed++;
        gameScreen.setCustomerHud(customersServed);

        // BELOW IS CODE FOR CUSTOMER SPAWNING.

        // If there is no more customers on the stations, and
        // the time for the next customer to arrive is above 2 seconds,
        // lower the time until the next customer to 2.
        /*if (customers.size == 0) {
            if (TimeUtils.timeSinceMillis(gameScreen.getNextCustomerSecond()) > 2000) {
                gameScreen.setNextCustomerSecond(TimeUtils.millis() + 2000);
            }
        }*/
    }

    /**
     * Clears the {@link Array} of {@link ServingStation}. Should
     * be called when the game is reset.
     */
    public void clearServingStations() {

        servingStations.clear();
        gameScreen.getGameHud().clearRecipes();
    }
	/**
	 * Clears the {@link Array} of {@link ServingStation}. Should be called when the
	 * game is reset.
	 */


    public void tryToSpawnCustomer(MenuScreen.difficulty msd, MenuScreen.mode md){

        int patience;
        if (md == MenuScreen.mode.SCENARIO) {
            switch (msd) {
                case MEDIUM:
                    timeBetweenSpawnsSeconds = 25;
                    patience = 25;
                    break;
                case HARD:
                    timeBetweenSpawnsSeconds = 20;
                    patience = 20;
                    break;
                default:
                    timeBetweenSpawnsSeconds = 45;
                    patience = 45;
            }
        }
        else {
            if (this.customersServed < 5){     // Less than 5 customers spawn rules easy
                timeBetweenSpawnsSeconds = 30;
                patience = 30;
            }
            else if (this.customersServed < 10){ // 5>= c > 10 customers spawn rules medium
                timeBetweenSpawnsSeconds = 25;
                patience = 25;
            }
            else {  // 10 >= c spawn rules hard
                timeBetweenSpawnsSeconds = 20;
                patience = 20;
            }
        }
        if ((gameScreen.getTotalSecondsRunningGame() - lastCustomerSpawnTime >= timeBetweenSpawnsSeconds) && canAddCustomer()) {
            addCustomer(patience);
            lastCustomerSpawnTime = gameScreen.getTotalSecondsRunningGame();
        }
    }

    public void initialCus(Customer cus, int spawnTime, int patience){
        cus.setTimings(spawnTime,  spawnTime + patience);
    }
    public void removeCustomerIfExpired(Customer customer){
        if (gameScreen.getTotalSecondsRunningGame() >= customer.getDeadTime()){
            removeCustomer(servingStations.get(customer.index));
            System.out.println("Removing expired customer");
            gameScreen.loseReputation();
        }
    }

    public float returnFractionProgressUntilNextCustomer(){
        return (gameScreen.getTotalSecondsRunningGame() - lastCustomerSpawnTime) / (float) (timeBetweenSpawnsSeconds);
    }
}

