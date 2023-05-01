package customers;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import food.Recipe;
import helper.Constants;

import java.io.Serializable;

/**
 * A {@link Customer} has a request that they want
 * to be served by the player.
 */
public class Customer implements Serializable {

    /** The position of the Customer. */
    public Vector2 position; // TestUPDATE
    /** The {@link Sprite} of the {@link Customer}. */
    public Sprite sprite;
    /** The name of the {@link Recipe} that the {@link Customer}
     * is requesting. */
    private String request;
    /** The spawn times and leave times of this customer*/
    private int spawnTime, deadTime;
    /** The station index that this customer is at */
    private int stationIndex;
    /**
     * The constructor for the {@link Customer}.
     * <br>Randomly picks out a {@link Recipe} as a request.
     * @param sprite The {@link Sprite} of the {@link Customer}.
     */
    public Customer(Sprite sprite)
    {
        this.sprite = sprite;
        this.position = Constants.customerSpawn;
        this.request = Recipe.randomRecipe();
        spawnTime = 0;
        deadTime = 0;
    }

    /**
     * Another constructor for the {@link Customer}, with a specified position.
     * <br>Randomly picks out a {@link Recipe} as a request.
     * @param sprite The {@link Sprite} of the {@link Customer}.
     * @param position A {@link Vector2} position of the {@link Customer}.
     */
    public Customer(Sprite sprite, int index, Vector2 position) {
        this(sprite);
        this.position = position;
        this.stationIndex = index;
    }

    /**
     * Set this customers order to a random recipe
     */
    public void randomRecipe() {
        this.request = Recipe.randomRecipe();
    }

    /**
     * Renders the {@link Customer} at their {@link #position}.
     * @param batch The {@link SpriteBatch} of the game.
     */
    public void render(SpriteBatch batch) {
        sprite.setPosition(position.x-sprite.getWidth()/2, position.y-sprite.getHeight()/2);
        sprite.draw(batch);
    }

    /**
     * Getter for the {@code x} position of the {@link Customer}.
     * @return {@code float} : The {@code x} of the {@link Customer}.
     */
    public float getX() {
        return position.x;
    }

    /**
     * Getter for the {@code y} position of the {@link Customer}.
     * @return {@code float} : The {@code y} of the {@link Customer}.
     */
    public float getY() {
        return position.y;
    }

    /**
     * Getter to get the name of the request of the {@link Customer}.
     * @return {@link String} : The name of the {@link Customer}'s
     *                          {@link Recipe} request.
     */
    public String getRequestName() {
        return request;
    }

    //////////////////////////////////ALL METHODS BELOW ADDED FOR ASSESSMENT 2//////////////////////////////////////////

    /**
     * Set the string request
     * @param req the string
     */
    public void setRequestName(String req){
        this.request = req;
    }

    /**
     * Set the timings for the customer
     * @param spnTime - The time in seconds (of the game running) from game start when the customer appears
     * @param dedTime - The time in seconds (of the game running) from game start when the customer should leave
     */
    public void setTimings(int spnTime, int dedTime){
        this.spawnTime = spnTime;
        this.deadTime = dedTime;
    }

    /**
     * Get the spawn time of the customer
     * @return The time in seconds of the customer spawn
     */
    public int getSpawnTime(){
        return this.spawnTime;
    }

    /**
     * Get the leave time of the customer
     * @return The time in seconds when the customer should leave
     */
    public int getDeadTime(){
        return this.deadTime;
    }

    /**
     * Get the index in the stations list that the customerController uses that this customer corresponds to
     * @return The index specified above
     */
    public int getStationIndex() {return this.stationIndex;}
}
