package helper;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;

import food.FoodStack;
import food.Recipe;
import game.GameScreen;
import game.GameSprites;
import interactions.InputKey;
import interactions.Interactions;
import powerups.PowerUpHandler;
import stations.ServingStation;

// import java.awt.*;

/** Responsible for displaying information above the gameplay GameScreen. */
public class GameHud extends Hud {
	/** The label with the current amount of time played. */

	Label timeLabel, customersLeftLabel, customersServedLabel, waveProgressLabel, reputationLabel, moneyLabel,
			powerupTimerLabel;

	/**
	 * The {@link SpriteBatch} of the GameHud. Use for drawing {@link food.Recipe}s.
	 */
	private final SpriteBatch batch;
	/** The {@link FoodStack} that the {@link GameHud} should render. */
	private final ShapeRenderer shape;
	private final HashMap<Integer, FoodStack> recipes;
	/** The Hashmap that contains all recipes to be rendered. */
	private final GameScreen gs;
	private Array<ServingStation> servingStations;
    private final Sprite activePowerUpSprite;
    private final OrthographicCamera o2 = new OrthographicCamera();

    private Matrix4 worldMatrix;

	// /** The time, in milliseconds, of the last recipe change. */
	// private long lastChange;

	/**
	 * The GameHud constructor.
	 *
	 * @param batch      The {@link SpriteBatch} to render
	 * @param gameScreen The {@link GameScreen} to render the GameHud on
	 */
	public GameHud(SpriteBatch batch, ShapeRenderer shape, GameScreen gameScreen) {
		super(batch);
        o2.setToOrtho(false, Constants.V_Width, Constants.V_Height);
        if (shape != null) {
            worldMatrix = shape.getProjectionMatrix();
        }
		recipes = new HashMap<>();
		this.gs = gameScreen;
        BitmapFont btfont = new BitmapFont();
        timeLabel = new Label("Timer :", new Label.LabelStyle(btfont, Color.BLACK));
		timeLabel.setPosition(10, 90 * Constants.V_Height / 100.0f);
        timeLabel.setFontScale(2);

		updateTime(0, 0, 0);

		customersLeftLabel = new Label("Customers Left: ", new Label.LabelStyle(btfont, Color.BLACK));
		customersLeftLabel.setPosition(10, 86 * Constants.V_Height / 100.0f);
        customersLeftLabel.setFontScale(2);

		customersServedLabel = new Label("Customers Served: ", new Label.LabelStyle(btfont, Color.BLACK));
		customersServedLabel.setPosition(10, 82 * Constants.V_Height / 100.0f);
        customersServedLabel.setFontScale(2);

		waveProgressLabel = new Label("PROGRESS TILL NEXT WAVE", new Label.LabelStyle(btfont, Color.WHITE));
		waveProgressLabel.setPosition(Constants.V_Width / 12.0f - (waveProgressLabel.getWidth() / 2),
				Constants.V_Height - Constants.V_Height / 32.0f);
        waveProgressLabel.setFontScale(2);

		reputationLabel = new Label("Reputation: 3", new Label.LabelStyle(btfont, Color.BLACK));
		reputationLabel.setPosition(10, 78 * Constants.V_Height / 100.0f);
        reputationLabel.setFontScale(2);

		moneyLabel = new Label("Money: £0.00", new Label.LabelStyle(btfont, Color.BLACK));
		moneyLabel.setPosition(10, 74 * Constants.V_Height / 100.0f);
        moneyLabel.setFontScale(2);

		powerupTimerLabel = new Label(String.format("%s to buy powerup!", Interactions.getKeyString(InputKey.InputTypes.BUY_POWERUP)), new Label.LabelStyle(btfont, Color.BLACK));
		powerupTimerLabel.setPosition(Constants.V_Width / 2.0f, Constants.V_Height * .70f + 164);
        powerupTimerLabel.setFontScale(2);

		if (batch != null) {
			stage.addActor(customersLeftLabel);
			stage.addActor(waveProgressLabel);
			stage.addActor(reputationLabel);
			stage.addActor(moneyLabel);
			stage.addActor(timeLabel);
			stage.addActor(customersServedLabel);
			stage.addActor(powerupTimerLabel);
		}
		this.batch = batch;
		this.shape = shape;

		activePowerUpSprite = new Sprite();
	}

	/**
	 * Renders both the {@link Hud} with the game information and the {@link Recipe}
	 * required the {@link customers.Customer} selected. <br>
	 * The {@link Recipe} displays on the right side of the screen.
	 */
	@Override
	public void render() {
        shape.setProjectionMatrix(o2.combined);
        shape.begin(ShapeRenderer.ShapeType.Filled);
        // Debug square
        // shape.rect(0,0,10,10,Color.GREEN,Color.GREEN,Color.GREEN,Color.GREEN); //DEBUG SQUARE 1

        shape.rect(0, Constants.V_Height - Constants.V_Height / 16f, Constants.V_Width, Constants.V_Height / 16f,
                Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK);
        shape.rect(0, Constants.V_Height - Constants.V_Height / 16f,
                Constants.V_Width * gs.getCustomerController().returnFractionProgressUntilNextCustomer(),
                Constants.V_Height / 16f, Color.GREEN, Color.GREEN, Color.GREEN, Color.GREEN);

        shape.end();
        shape.setProjectionMatrix(gs.getGameCoordsCamera().combined);
        shape.begin(ShapeRenderer.ShapeType.Filled);
        // shape.rect(0,0,10,10,Color.RED,Color.RED,Color.RED,Color.RED); //DEBUG SQUARE 2

        for (ServingStation ss : this.servingStations) {
            if (ss.hasCustomer()) {
                int dedtime = ss.getCustomer().getDeadTime();
                int spawntime = ss.getCustomer().getSpawnTime();
                float fractional = (gs.getTotalSecondsRunningGame() - spawntime) / (float) (dedtime - spawntime);
                shape.rect(ss.getX() + 22, ss.getY() - 32, 5, 64 - (64 * fractional), Color.RED, Color.RED, Color.RED,
                        Color.RED);
            }
        }
        shape.end();

		if (gs.getPowerUpHandler().getCurrentPowerUps()[0] != null) {
            batch.setProjectionMatrix(o2.combined);
			batch.begin();
			activePowerUpSprite.setTexture(
					new Texture(Gdx.files.internal("powerups/" + gs.getPowerUpHandler().getCurrentPowerUp(0).spritePath())));
			batch.draw(activePowerUpSprite.getTexture(), Constants.V_Width / 2.0f, Constants.V_Height * .75f, 64, 64);
			batch.end();
			powerupTimerLabel.setText(String.format("%s : Press [%s] to activate \nPress [%s to reroll] \n(Costs %s)", gs.getPowerUpHandler().getCurrentPowerUp(0).spritePath().replace(".png", "").replace("_", " ").toUpperCase(),
                    Interactions.getKeyString(InputKey.InputTypes.ACTIVATE_POWERUP),
                    Interactions.getKeyString(InputKey.InputTypes.BUY_POWERUP),
                    Constants.POWERUP_COST));
            powerupTimerLabel.setFontScale(2);

        } else if (PowerUpHandler.activePowerUp() != null) {
            batch.setProjectionMatrix(o2.combined);
            batch.begin();
            activePowerUpSprite.setTexture(
                    new Texture(Gdx.files.internal("powerups/" + PowerUpHandler.activePowerUp().spritePath())));
            batch.draw(activePowerUpSprite.getTexture(), Constants.V_Width / 2.0f, Constants.V_Height * .75f, 64, 64);
            batch.end();
            powerupTimerLabel.setText(String.format("%s \nuses: %d",
                    PowerUpHandler.activePowerUp().spritePath().replace(".png", "").replace("_", " ").toUpperCase(),
                    gs.getPowerUpHandler().getCooldown()));
        } else {
			powerupTimerLabel.setText(String.format("[%s] to buy powerup! (Costs %s)", Interactions.getKeyString(InputKey.InputTypes.BUY_POWERUP) , Constants.POWERUP_COST));
		}

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
     * Removes all recipes from rendering
     */
	public void clearRecipes() {
		recipes.clear();
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
		timeLabel.setText("Timer: " + String.format(Util.formatTime(hoursPassed, minutesPassed, secondsPassed)));
	}

	/**
	 * Set the customers left - If this is negative assume endless mode
	 * @param amountCustomers The amount of customers left
	 */
	public void updateCustomersLeftLabel(int amountCustomers) {
		if (amountCustomers >= 0) {
			customersLeftLabel.setText(String.format("Customers Left: %d", amountCustomers));
		} else {
			customersLeftLabel.setText("ENDLESS MODE");
		}
	}

    /**
     * Set the customers served label
     * @param amountCustomers The amount of customers served
     */
	public void updateCustomersServedLabel(int amountCustomers) {
		customersServedLabel.setText(String.format("Customers Served: %d", amountCustomers));
	}

    /**
     * Update the reputation label
     * @param reputation The amount of reputation
     */
	public void updateReputationLabel(int reputation) {
		reputationLabel.setText(String.format("Reputation: %d", reputation));
	}

    /**
     * Update the money label
     * @param amount The amount of money
     */
	public void updateMoneyLabel(int amount) {
		int pounds = amount / 100;
		int pennies = amount - pounds * 100;
		moneyLabel.setText(String.format("Money: £%d.%d", pounds, pennies));
	}

}
