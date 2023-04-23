package helper;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import customers.Customer;
import food.FoodItem;
import food.FoodStack;
import food.Recipe;
import game.GameScreen;
import game.GameSprites;
import stations.ServingStation;

import java.util.HashMap;

// import java.awt.*;

/** Responsible for displaying information above the gameplay GameScreen. */
public class GameHud extends Hud {
    /** The label with the current amount of time played. */
    Label timeLabel;
    /** The label with the number of {@link Customer}s left to serve.  */
    Label CustomerLabel;
    Label CustomerScore;
    /** The {@link SpriteBatch} of the GameHud. Use for drawing {@link food.Recipe}s. */
    private SpriteBatch batch;
    /** The {@link FoodStack} that the {@link GameHud} should render. */
    private HashMap<Integer, FoodStack> recipes;
    private GameScreen gs;
    private Array<ServingStation> servingStations;
    // /** The time, in milliseconds, of the last recipe change. */
    // private long lastChange;

    /**
     * The GameHud constructor.
     * @param batch The {@link SpriteBatch} to render
     * @param gameScreen The {@link GameScreen} to render the GameHud on
     */
    public GameHud(SpriteBatch batch, GameScreen gameScreen)
    {
        super(batch);
        recipes = new HashMap<>();
        this.gs = gameScreen;
        timeLabel = new Label("", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        updateTime(0,0,0);

        CustomerLabel = new Label("CUSTOMERS LEFT:", new Label.LabelStyle(new BitmapFont(), Color.BLACK));

        table.add(CustomerLabel).expandX().padTop(80).padRight(60);
        table.add(timeLabel).expandX().padTop(80).padLeft(60);

        this.batch = batch;
    }

    /**
     * Renders both the {@link Hud} with the game information and
     * the {@link Recipe} required the {@link customers.Customer} selected.
     * <br>The {@link Recipe} displays on the right side of the screen.
     */
    @Override
    public void render() {
        super.render();
        batch.begin();
        GameSprites gameSprites = GameSprites.getInstance();
        float drawX, drawY;
        for (Integer i: recipes.keySet()) {
            drawY = this.servingStations.get(i).getY() + (Constants.V_Height/2.0f - Constants.gameCameraOffset.y) + this.servingStations.get(i).getRectangle().getHeight();
            for (int i2 = (recipes.get(i).getStack().size - 1 ); i2 >= 0; i2 --) {
                drawX = this.servingStations.get(i).getX() + (Constants.V_Width/2.0f - Constants.gameCameraOffset.x);

                Sprite foodSprite = gameSprites.getSprite(GameSprites.SpriteID.FOOD, recipes.get(i).getStack().get(i2).toString());
                foodSprite.setScale(2F);
                foodSprite.setPosition(drawX - foodSprite.getWidth() / 2, drawY - foodSprite.getHeight() / 2);
                foodSprite.draw(batch);
                drawY += 32;
            }
        }
        batch.end();
    }

    public void addRecipeToRender(Integer num, FoodStack fs) {
        System.out.println("Adding recipe at " + num.toString());
        System.out.println("The stack is " + fs.toString());
        recipes.put(num, fs);
    }

    public void removeRecipeToRender(Integer num){
        recipes.remove(num);
    }

    public void setServingStations(Array<ServingStation> srvs) {
        this.servingStations = srvs;
    }



    /* Removed as it was confusing to look at.
    /**
     * Changes the order of the {@link FoodItem}s in the recipe every second
     * to show which {@link FoodItem}s have non-specific places in the
     * {@link Recipe}.
     * /
    public void update() {
        if (recipe != null) {
            if (TimeUtils.timeSinceMillis(lastChange) > 1000) {
                this.recipe = Recipe.randomRecipeOption(recipeName);
                lastChange = TimeUtils.millis();
            }
        }
    }*/

    /**
     * Sets the recipe to be rendered.
     * @param cstrs The {@link Customer} who is requesting the {@link #recipes}.
     */


    //public void setRecipes(Array<Customer> cstrs) {
    //    // this.lastChange = TimeUtils.millis();
    //    this.customers = cstrs;
    //    if (customers == null) {
    //        this.recipes = null;
    //        return;
    //    }
    //
    //    //this.recipes = Recipe.randomRecipeOption(customer.getRequestName());
    //    this.recipes = new Array<>();
    //    for(Customer cs: customers){
    //        recipes.add(Recipe.randomRecipeOption(cs.getRequestName()));
    //    }
    //}

    /**
     * Update the Timer
     * @param secondsPassed The number of seconds passed
     */
    public void updateTime(int secondsPassed) {
        updateTime(0,0,secondsPassed);
    }

    /**
     * Update the Timer
     * @param minutesPassed The number of minutes passed
     * @param secondsPassed The number of seconds passed
     */
    public void updateTime(int minutesPassed, int secondsPassed) {
        updateTime(0,minutesPassed,secondsPassed);
    }

    /**
     * Update the Timer
     * @param hoursPassed The number of hours passed
     * @param minutesPassed The number of minutes passed
     * @param secondsPassed The number of seconds passed
     */
    public void updateTime(int hoursPassed, int minutesPassed, int secondsPassed)
    {
        timeLabel.setText("TIMER: " + String.format(Util.formatTime(hoursPassed,minutesPassed,secondsPassed)));
    }

    /**
     * Set the Customer Count label
     * @param customerCount New Customer Count
     */
    public void setCustomerCount(int customerCount) {
        CustomerLabel.setText(String.format("CUSTOMERS: %d",customerCount));
    }

    /**
     * Getter for the {@link Customer} that has their
     * request being shown.
     * @return {@link Customer} : The {@link Customer} having their
     *                            request shown.
     */
}
