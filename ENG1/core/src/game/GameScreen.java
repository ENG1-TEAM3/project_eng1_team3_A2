package game;

import customers.Customer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import cooks.Cook;
import cooks.GameEntity;
import customers.CustomerController;
import food.FoodItem;
import helper.*;
import interactions.InputKey;
import interactions.Interactions;
import powerups.PowerUpHandler;
import stations.CookInteractable;
import stations.ServingStation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

/** A {@link ScreenAdapter} containing certain elements of the game. */
public class GameScreen extends ScreenAdapter {

	private final OrthographicCamera camera;
	private long msPast1s = 0;
	private long previousSecond = 0, lastCustomerSecond = 0, nextCustomerSecond = 0;
	private int secondsPassed = 0, minutesPassed = 0, hoursPassed = 0;
	private final GameHud gameHud;
	private final InstructionHud instructionHUD;
	private final SpriteBatch batch;
	private ShapeRenderer shape;
	private final ScreenController screenController;
	// private ShapeRenderer shapeRenderer;
	private World world;
	private Box2DDebugRenderer box2DDebugRenderer;

	private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
	private MapHelper mapHelper;
	private final Array<CookInteractable> interactables;
	private final CollisionHelper collisionHelper;
	private final Array<GameEntity> gameEntities;
	private final DrawQueueComparator drawQueueComparator;
	private Array<ServingStation> servingStations;

	// Objects
	private Array<Cook> cooks;
	private Cook cook;

	private int cookIndex;
	private CustomerController customerController;
	private int customersToServe;

	private int timecopy = 0;

	private int diffculty;

	private int reputation = 3, money = 2000;

	private int patience;

	private SaveHandler sv;

	public PowerUpHandler powerUpHandler;

	/**
	 * The constructor for the {@link GameScreen}.
	 *
	 * @param screenController The {@link ScreenController} of the
	 *                         {@link ScreenAdapter}.
	 * @param camera           The {@link OrthographicCamera} that the game should
	 *                         use.
	 */
	public GameScreen(ScreenController screenController, OrthographicCamera camera) {
		this.previousSecond = TimeUtils.millis();
		this.lastCustomerSecond = -1;
		this.nextCustomerSecond = -1;
		this.cooks = new Array<>();
		this.interactables = new Array<>();
		this.collisionHelper = CollisionHelper.getInstance();
		this.collisionHelper.setGameScreen(this);
		this.cookIndex = -1;
		this.camera = camera;
		this.screenController = screenController;
		this.batch = screenController.getSpriteBatch();

		this.gameEntities = new Array<>();
		this.drawQueueComparator = new DrawQueueComparator();
		this.customerController = new CustomerController(this);
		this.world = new World(new Vector2(0, 0), false);

		this.mapHelper = MapHelper.getInstance();
		this.mapHelper.setGameScreen(this);
		this.mapHelper.setupMap();

		this.gameHud = new GameHud(batch, this);
		this.gameHud.setServingStations(this.customerController.getServingStations());
		this.instructionHUD = new InstructionHud(batch);
		this.sv = new SaveHandler(this);

		if (this.batch != null) {
			this.shape = screenController.getShapeRenderer();
			this.box2DDebugRenderer = new Box2DDebugRenderer();
			this.orthogonalTiledMapRenderer = mapHelper.getOrthoRenderer();
		}
		powerUpHandler = new PowerUpHandler(this);
		gameHud.setMoneyLabel(money);
	}

	public void restoreFromData(long smallTimeDifference, int totalSeconds, int repPoints, int moneyAmount,
			int customersServed, int customersLeft, float[] ckpositions, ArrayList<?> foodstacks,
			Cook.Facing[] cookFacings) {
		this.money = moneyAmount;
		gameHud.setMoneyLabel(moneyAmount);
		this.reputation = repPoints;
		this.customerController.setCustomersLeft(customersLeft);
		this.customerController.setCustomersServed(customersServed);
		gameHud.setCustomerCount(customersLeft);

		System.out.println(this.customerController.getCustomersLeft());
		System.out.println(this.customerController.getCustomersServed());

		int ctr = 0;
		for (Cook ck : cooks) {
			ck.getBody().setTransform(ckpositions[ctr * 2], ckpositions[ctr * 2 + 1], 0);
			ArrayList<?> minilist = (ArrayList<?>) foodstacks.get(ctr);
			ck.foodStack.setFoodStackFromArrayList(minilist);
			ck.setFacing(cookFacings[ctr]);
			ctr++;
		}
	}

	public void updateTiming() {
		long diffInMillis = TimeUtils.timeSinceMillis(previousSecond);
		if (diffInMillis >= (1000 - msPast1s)) {
			msPast1s = diffInMillis - (1000 - msPast1s);
			previousSecond += 1000;
			secondsPassed += 1;
			timecopy += 1;
			if (secondsPassed >= 60) {
				secondsPassed = 0;
				minutesPassed += 1;
				if (minutesPassed >= 60) {
					minutesPassed = 0;
					hoursPassed += 1;
				}
			}
		}
		gameHud.updateTime(hoursPassed, minutesPassed, secondsPassed);
	}

	public int getTime() {
		return hoursPassed * 60 * 60 + minutesPassed * 60 + secondsPassed;
	}

	public void setTime(int seconds) {
	}

	public int getReputation() {
		return reputation;
	}

	public int getMoney() {
		return money;
	}

	/**
	 * Update the game's values, {@link GameEntity}s and so on.
	 *
	 * @param delta The time between frames as a float.
	 */
	public void update(float delta, boolean shouldResetKeys) {

		// First thing, update all inputs
		Interactions.updateKeys(shouldResetKeys);

		updateTiming();

		for (Cook thisCook : cooks) {
			thisCook.getBody().setLinearVelocity(0F, 0F);
			if (thisCook == cook) {
				thisCook.userInput();
			}
		}
		if (Interactions.isJustPressed(InputKey.InputTypes.COOK_SWAP)) {
			setCook((cookIndex + 1) % cooks.size);
		}

		// Spawning code to spawn a customer after an amount of time.
		/*
		 * if(TimeUtils.millis() >= nextCustomerSecond) { int recipeComplexity =
		 * customerController.addCustomer(); if (recipeComplexity == -1) { // If
		 * customer couldn't be added, then wait 2 seconds. nextCustomerSecond += 2000;
		 * } else { // Wait longer if the recipe has more steps. lastCustomerSecond =
		 * TimeUtils.millis(); nextCustomerSecond += 1000 * Math.floor(9 + 5.4F *
		 * Math.log(recipeComplexity - 0.7)); } }
		 */

		if (Interactions.isJustPressed(InputKey.InputTypes.PAUSE)) {
			screenController.pauseGameScreen();
		}
		if (Interactions.isJustPressed(InputKey.InputTypes.SAVE)) {
			try {
				sv.saveToFile("save1.txt");
			} catch (IOException io) {
				throw new RuntimeException(io);
			}
		}
		if (Interactions.isJustPressed(InputKey.InputTypes.LOAD)) {
			try {
				sv.loadFromFile("save1.txt");
			} catch (IOException | ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
		world.step(1 / 60f, 6, 2);

		for (GameEntity entity : gameEntities) {
			entity.update(delta);
		}
	}

	/**
	 * Update the {@link #camera}.
	 */
	private void cameraUpdate() {
		camera.position.set(new Vector3(Constants.gameCameraOffset.x, Constants.gameCameraOffset.y, 0));
		camera.update();
	}

	/**
	 * The next frame of the game.
	 *
	 * @param delta The time between frames as a float.
	 */
	@Override
	public void render(float delta) {
		this.update(delta, true);

		renderGame(delta);

		setDifficulties(patience);

		for (Customer cus : customerController.customers) {
			boolean B = customerController.checkRemove(cus, minutesPassed, secondsPassed, cookIndex);
			if (B == true) {
				customerController.NoPatience(B, cus);
				reputation--;
				gameHud.setReputationPoints(reputation);
			}
		}

		if (customersToServe <= customerController.getCustomersServed() || reputation == 0) {
			screenController.setScreen((ScreenController.ScreenID.GAMEOVER));
			((GameOverScreen) screenController.getScreen(ScreenController.ScreenID.GAMEOVER)).setTime(hoursPassed,
					minutesPassed, secondsPassed);
		}
	}

	/**
	 * Render the {@link GameScreen}. It is a separate function to allow rendering
	 * of the game from the {@link PauseScreen}.
	 *
	 * @param delta The time between frames as a float.
	 */
	public void renderGame(float delta) {

		cameraUpdate();
		orthogonalTiledMapRenderer.setView(camera);
		batch.setProjectionMatrix(camera.combined);
		shape.setProjectionMatrix(camera.combined);

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		orthogonalTiledMapRenderer.render();
		batch.begin();
		gameEntities.sort(drawQueueComparator);

		for (GameEntity entity : gameEntities) {
			entity.render(batch);
			if (entity == cook) {
				((Cook) entity).renderControlArrow(batch);
			}
			entity.renderDebug(batch);
		}

		batch.end();
		shape.begin(ShapeRenderer.ShapeType.Filled);

		for (GameEntity entity : gameEntities) {
			entity.renderShape(shape);
			entity.renderShapeDebug(shape);
		}

		shape.end();
		// box2DDebugRenderer.render(world, camera.combined.scl(Constants.PPM));
		gameHud.render();
		instructionHUD.render();

	}

	/**
	 * A {@link Comparator} used to compare the Y height of two {@link GameEntity}s.
	 * If it is negative, then the left entity is higher. If it is positive, then
	 * the right entity is higher. If it is 0, then both are at the same height.
	 */
	public static class DrawQueueComparator implements Comparator<GameEntity> {
		@Override
		public int compare(GameEntity o1, GameEntity o2) {
			float o1Y = o1.getY(), o2Y = o2.getY();
			if (o1Y > o2Y) {
				return -1;
			} else if (o2Y > o1Y) {
				return 1;
			}
			return 0;
		}
	}

	/**
	 * Get the world that the game is using.
	 *
	 * @return {@link World} : The {@link GameScreen}'s {@link World}.
	 */
	public World getWorld() {
		return world;
	}

	public Array<GameEntity> getGameEntities() {
		return this.gameEntities;
	}

	public Array<Cook> getCooks() {
		return this.cooks;
	}

	/**
	 * Sets the currently active {@link #cook} that the game is using.
	 *
	 * @param cookIndex The index of {@link #cook} in the {@link #cooks} array.
	 * @return {@link Cook} : The {@link Cook} that the game has swapped to.
	 */
	public Cook setCook(int cookIndex) {
		if (cookIndex < 0 || cookIndex > cooks.size) {
			throw new ArrayIndexOutOfBoundsException();
		}
		this.cook = cooks.get(cookIndex);
		this.cookIndex = cookIndex;
		return this.cook;
	}

	/**
	 * Adds a new {@link Cook} to the {@link #cooks} {@link Array} for the game to
	 * swap between.
	 *
	 * @param newCook The {@link Cook} to be added to the {@link Array}.
	 * @return {@code int} : The index of the new cook in the cooks array.
	 */
	public int addCook(Cook newCook) {
		gameEntities.add(newCook);
		cooks.add(newCook);
		return cooks.size - 1;
	}

	/**
	 * Updates the {@link GameHud} with the correct number of {@link Customer}s.
	 *
	 * @param customerCount The {@code int} number to set the number of
	 *                      {@link Customer}s to.
	 */
	public void setCustomerHud(int customerCount) {
		gameHud.setCustomerCount(customersToServe - customerCount);
	}

	public void loseReputation() {
		if (reputation > 1) {
			reputation--;
		} else {
			reputation = 3;
			screenController.setScreen((ScreenController.ScreenID.GAMEOVER));
			((GameOverScreen) screenController.getScreen(ScreenController.ScreenID.GAMEOVER)).setTime(hoursPassed,
					minutesPassed, secondsPassed);
		}

		gameHud.setReputationPoints(reputation);
	}

	public void addMoney(int amount) {
		money += amount;
		gameHud.setMoneyLabel(money);
	}

	public boolean spendMoney(int amount) {
		if (money - amount < 0) {
			return false;
		}
		money -= amount;

		gameHud.setMoneyLabel(money);

		return true;
	}

	public void setMoney(int amount) {
		money = amount;
	}

	public Cook getCurrentCook() {
		return cook;
	}

	/**
	 * Returns the number of customers remaining before the game is finished.
	 *
	 * @return {@code int} : The value of
	 *         {@link CustomerController#getCustomersLeft()}.
	 */
	public int getCustomerCount() {
		return customerController.getCustomersLeft();
	}

	/**
	 * A getter to get the {@link #previousSecond}. <br>
	 * The {@link #previousSecond} is used for the timer, by checking when the
	 * previous second was so that the game can check if it has been another second
	 * or not.
	 *
	 * @return {@code long} : The {@link #previousSecond}.
	 */
	public long getPreviousSecond() {
		return previousSecond;
	}

	/**
	 * A setter to set the {@link #previousSecond} to the {@code long} provided.
	 *
	 * @param newSecond What to set the {@link #previousSecond} to as a
	 *                  {@code long}.
	 */
	public void setPreviousSecond(long newSecond) {
		previousSecond = newSecond;
	}

	/**
	 * A getter to get the {@link #nextCustomerSecond}. <br>
	 * The {@link #nextCustomerSecond} is used for spawning the {@link Customer}s
	 * after a short delay.
	 *
	 * @return {@code long} : The {@link #nextCustomerSecond}.
	 */
	public long getNextCustomerSecond() {
		return nextCustomerSecond;
	}

	/**
	 * A setter to set the {@link #nextCustomerSecond} to the {@code long} provided.
	 *
	 * @param newSecond What to set the {@link #nextCustomerSecond} to as a
	 *                  {@code long}.
	 */
	public void setNextCustomerSecond(long newSecond) {
		nextCustomerSecond = newSecond;
	}

	/**
	 * {@link #interactables} getter. Contains all the {@link #interactables} in the
	 * {@link GameScreen}.
	 *
	 * @return {@link Array}&lt;{@link CookInteractable}&gt; :
	 *         {@link #interactables}.
	 */
	public Array<CookInteractable> getInteractables() {
		return interactables;
	}

	/**
	 * Adds a {@link CookInteractable} that a {@link Cook} can interact with to
	 * {@link #interactables}.
	 *
	 * @param cookInteractable The {@link CookInteractable} object that the
	 *                         {@link Cook} should be able to interact with.
	 */
	public void addInteractable(CookInteractable cookInteractable) {
		interactables.add(cookInteractable);
	}

	/**
	 * Adds a game entity to the GameScreen to be rendered and updated.
	 *
	 * @param entity The {@link GameEntity} to be added.
	 */
	public void addGameEntity(GameEntity entity) {
		gameEntities.add(entity);
	}

	/**
	 * Intermediate function to allow the {@link MapHelper} to add the
	 * {@link ServingStation}s to the {@link CustomerController}.
	 *
	 * @param station The {@link ServingStation} to add to the
	 *                {@link CustomerController}.
	 */
	public void addServingStation(ServingStation station) {
		customerController.addServingStation(station);
	}

	/** Reset the game variables, map and world. */
	public void reset() {
		// Reset all variables
		secondsPassed = 0;
		minutesPassed = 0;
		hoursPassed = 0;
		cooks.clear();
		gameEntities.clear();
		interactables.clear();
		mapHelper.dispose();
		customerController.clearServingStations();
		dispose();
		mapHelper = MapHelper.newInstance();
		mapHelper.setGameScreen(this);
		world.dispose();
		this.world = new World(new Vector2(0, 0), false);
		this.mapHelper.setupMap();
		if (this.batch != null) {
			this.orthogonalTiledMapRenderer = mapHelper.getOrthoRenderer();
		}
		cookIndex = -1;
	}

	/**
	 * A variable for setting up the game when it starts.
	 *
	 * @param customers The number of customers that need to be served in the game
	 *                  to finish.
	 */
	public void startGame(int customers) {
		secondsPassed = 0;
		minutesPassed = 0;
		hoursPassed = 0;

		previousSecond = TimeUtils.millis();
		lastCustomerSecond = TimeUtils.millis();
		nextCustomerSecond = TimeUtils.millis() + 2000;
		msPast1s = 0;

		customersToServe = customers;
		diffculty = customers;
		patience = setPatience(diffculty);
		customerController.setCustomersLeft(customers);
		customerController.setCustomersServed(0);
		customerController.addCustomer(minutesPassed, secondsPassed, patience);

		gameHud.setCustomerCount(customers);
	}

	/**
	 * A getter for the {@link CustomerController} of the game.
	 *
	 * @return {@link CustomerController} : The {@link CustomerController} for the
	 *         game.
	 */
	public CustomerController getCustomerController() {
		return this.customerController;
	}

	/**
	 * Getter to get the {@link GameHud}.
	 *
	 * @return {@link GameHud} : The game's {@link GameHud}.
	 */
	public GameHud getGameHud() {
		return gameHud;
	}

	public InstructionHud getInstructionHUD() {
		return instructionHUD;
	}

	public void setDifficulties(int patience) {
		if (diffculty == 5) {
			customerController.spawnCutomer_Scenario(timecopy, minutesPassed, secondsPassed, patience);
		} else if (diffculty == 10) {
			customerController.spawnCutomer_Normal(timecopy, minutesPassed, secondsPassed, patience);
		} else if (diffculty == 15) {
			customerController.spawnCutomer_Hard(timecopy, minutesPassed, secondsPassed, patience);
		} else if (diffculty > 15) {
			customerController.spawnCutomer_Endless(timecopy, minutesPassed, secondsPassed, patience);
		}
	}

	public int setPatience(int numOfCus) {
		if (diffculty == 5) {
			patience = 30;
		} else if (diffculty == 10) {
			patience = 25;
		} else if (diffculty == 15) {
			patience = 20;
		} else {
			patience = 30;
		}
		return patience;
	}
}
