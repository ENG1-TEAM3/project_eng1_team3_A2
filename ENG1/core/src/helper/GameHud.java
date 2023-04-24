package helper;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;

import customers.Customer;
import food.FoodStack;
import food.Recipe;
import game.GameScreen;
import game.GameSprites;
import stations.ServingStation;

// import java.awt.*;

/** Responsible for displaying information above the gameplay GameScreen. */
public class GameHud extends Hud {
	/** The label with the current amount of time played. */
	Label timeLabel;
	/** The label with the number of {@link Customer}s left to serve. */
	Label CustomerLabel;
	Label CustomerScore;

	Label reputationLabel;
	/**
	 * The {@link SpriteBatch} of the GameHud. Use for drawing {@link food.Recipe}s.
	 */
	private SpriteBatch batch;
	/** The {@link FoodStack} that the {@link GameHud} should render. */
	private HashMap<Integer, FoodStack> recipes;
	/** The Hashmap that contains all recipes to be rendered. */
	private GameScreen gs;
	private Array<ServingStation> servingStations;
	// /** The time, in milliseconds, of the last recipe change. */
	// private long lastChange;

	/**
	 * The GameHud constructor.
	 * 
	 * @param batch      The {@link SpriteBatch} to render
	 * @param gameScreen The {@link GameScreen} to render the GameHud on
	 */
	public GameHud(SpriteBatch batch, GameScreen gameScreen) {
		super(batch);
		recipes = new HashMap<>();
		this.gs = gameScreen;
		timeLabel = new Label("", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
		updateTime(0, 0, 0);

		CustomerLabel = new Label("CUSTOMERS LEFT: ", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
		reputationLabel = new Label("Reputation: 3", new Label.LabelStyle(new BitmapFont(), Color.BLACK));

		table.add(CustomerLabel).expandX().padTop(80).padRight(60);
		table.add(timeLabel).expandX().padTop(80).padLeft(60);
		table.add(reputationLabel).expandX().padTop(120).padRight(60);

		this.batch = batch;
	}

	/**
	 * Renders both the {@link Hud} with the game information and the {@link Recipe}
	 * required the {@link customers.Customer} selected. <br>
	 * The {@link Recipe} displays on the right side of the screen.
	 */
	@Override
	public void render() {
		super.render();
		batch.begin();
		GameSprites gameSprites = GameSprites.getInstance();
		// AS2 CHANGE - Rewrote scaling code to allow for any map to render correctly in
		// the middle of the screen.
		float drawX, drawY;
		for (Integer i : recipes.keySet()) {
			drawY = this.servingStations.get(i).getY() + (Constants.V_Height / 2.0f - Constants.gameCameraOffset.y)
					+ this.servingStations.get(i).getRectangle().getHeight();
			for (int i2 = (recipes.get(i).getStack().size - 1); i2 >= 0; i2--) { // Render from the bottom up, for
																					// consistent distance.
				drawX = this.servingStations.get(i).getX() + (Constants.V_Width / 2.0f - Constants.gameCameraOffset.x);
				Sprite foodSprite = gameSprites.getSprite(GameSprites.SpriteID.FOOD,
						recipes.get(i).getStack().get(i2).toString());
				foodSprite.setScale(2F);
				foodSprite.setPosition(drawX - foodSprite.getWidth() / 2, drawY - foodSprite.getHeight() / 2);
				foodSprite.draw(batch);
				drawY += 32;
			}
		}
		batch.end();
	}

	/**
	 * Adds a recipe to the rendering hashmap
	 * 
	 * @param num The integer key for the recipe in the hashmap - Corresponds to the
	 *            index of the serving station
	 * @param fs  The FoodStack to be rendered.
	 */
	public void addRecipeToRender(Integer num, FoodStack fs) {
		recipes.put(num, fs);
	}

	/**
	 * Removes a recipe from the rendering hashmap
	 * 
	 * @param num The integer key for the recipe in the hashmap
	 */
	public void removeRecipeToRender(Integer num) {
		recipes.remove(num);
	}

	/**
	 * Gives the GameHud an array of all serving stations.
	 * 
	 * @param srvs The array of serving stations
	 */
	public void setServingStations(Array<ServingStation> srvs) {
		this.servingStations = srvs;
	}

	/**
	 * Update the Timer
	 * 
	 * @param secondsPassed The number of seconds passed
	 */
	public void updateTime(int secondsPassed) {
		updateTime(0, 0, secondsPassed);
	}

	/**
	 * Update the Timer
	 * 
	 * @param minutesPassed The number of minutes passed
	 * @param secondsPassed The number of seconds passed
	 */
	public void updateTime(int minutesPassed, int secondsPassed) {
		updateTime(0, minutesPassed, secondsPassed);
	}

	/**
	 * Update the Timer
	 * 
	 * @param hoursPassed   The number of hours passed
	 * @param minutesPassed The number of minutes passed
	 * @param secondsPassed The number of seconds passed
	 */
	public void updateTime(int hoursPassed, int minutesPassed, int secondsPassed) {
		timeLabel.setText("TIMER: " + String.format(Util.formatTime(hoursPassed, minutesPassed, secondsPassed)));
	}

	/**
	 * Set the Customer Count label
	 * 
	 * @param customerCount New Customer Count
	 */
	public void setCustomerCount(int customerCount) {
		CustomerLabel.setText(String.format("CUSTOMERS: %d", customerCount));
	}

	public void setReputationPoints(int reputation) {
		reputationLabel.setText(String.format("Reputation: %d", reputation));
	}

}
