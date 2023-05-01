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
import powerups.PowerUp;
import powerups.PowerUpHandler;
import stations.*;

import java.util.ArrayList;
import java.util.Comparator;

/** A {@link ScreenAdapter} containing certain elements of the game. */
public class GameScreen extends ScreenAdapter {

	private final OrthographicCamera camera;
	private long msPast1s = 0;
	private long previousSecond, totalTimePaused = 0;
	private int secondsPassed = 0, minutesPassed = 0, hoursPassed = 0;
	private final GameHud gameHud;
	private final InstructionHud instructionHUD;
	private final SpriteBatch batch;
	private ShapeRenderer shape;
	private final ScreenController screenController;
	private World world;
	private Box2DDebugRenderer box2DDebugRenderer;
	private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
	private MapHelper mapHelper;
	private final Array<CookInteractable> interactables;
    private final Array<GameEntity> gameEntities;
	private final DrawQueueComparator drawQueueComparator;

	// Objects
	private final Array<Cook> cooks;
	private Cook cook;
	private int cookIndex;
	private final CustomerController customerController;
	private int reputation = 3, money = Constants.MONEY_START;
	private final SaveHandler sv;
	private MenuScreen.difficulty currentDifficulty;
	private MenuScreen.mode currentMode;
	private PowerUpHandler powerUpHandler;

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
		this.cooks = new Array<>();
		this.interactables = new Array<>();
        CollisionHelper collisionHelper = CollisionHelper.getInstance();
		collisionHelper.setGameScreen(this);
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

		if (this.batch != null) {
			this.shape = screenController.getShapeRenderer();
			this.box2DDebugRenderer = new Box2DDebugRenderer();
			this.orthogonalTiledMapRenderer = mapHelper.getOrthoRenderer();
		}
		this.gameHud = new GameHud(batch, shape, this);
		this.gameHud.setServingStations(this.customerController.getServingStations());
		this.instructionHUD = new InstructionHud(batch);

        // New classes for assessment 2
		this.sv = new SaveHandler(this);
		powerUpHandler = new PowerUpHandler(this);
		gameHud.updateMoneyLabel(money);
        this.cooks.get(1).setCookID(1);
        this.cooks.get(2).setCookID(2);
	}

    //Fixed for assessment 2 - originally the timer ran too slow

    /**
     * Update the seconds, minutes and hours passed.
     */
	public void updateTiming() {
		long diffInMillis = TimeUtils.timeSinceMillis(previousSecond);
		if (diffInMillis >= (1000 - msPast1s)) {
			msPast1s = diffInMillis - (1000 - msPast1s);
			previousSecond += 1000;
			secondsPassed += 1;
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

    /**
     * Set the seconds, minutes and hours passed based on an amount of seconds
     * @param totalSecs The amount of seconds passed
     */
    public void setTiming(int totalSecs){
        hoursPassed = totalSecs / 3600;
        minutesPassed = (totalSecs - (3600*hoursPassed)) / 60;
        secondsPassed = totalSecs - (3600 * hoursPassed) - (60* minutesPassed);
    }

    /**
     * Get the amount of time the game has spent running (this stops while paused)
     * @return The amount of time specified in line above
     */
	public int getTotalSecondsRunningGame() {
		return hoursPassed * 60 * 60 + minutesPassed * 60 + secondsPassed;
	}

    /**
     * Get the current reputation
     * @return the reputation value
     */
	public int getReputation() {
		return reputation;
	}

    /**
     * Get the current amount of money
     * @return the money value
     */
	public int getMoney() {
		return money;
	}

    /**
     * Get the current selected cook
     * @return the cook selected
     */
	public Cook getCurrentCook() {
		return cook;
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

		if (customerController.getTotalCustomersToServe() != -1
				&& (customerController.getTotalCustomersToServe() <= customerController.getCustomersServed())) {
			screenController.setScreen((ScreenController.ScreenID.GAMEOVER));
			((GameOverScreen) screenController.getScreen(ScreenController.ScreenID.GAMEOVER)).setTime(hoursPassed,
					minutesPassed, secondsPassed);
			((GameOverScreen) screenController.getScreen(ScreenController.ScreenID.GAMEOVER)).setTextLabel("YOU WIN!");
		}

        // A copy is taken here as the below for loop may remove elements from the array.
		Array<Customer> customersCopy = new Array<>(customerController.getCustomers());
		for (Customer cus : customersCopy) {
			customerController.removeCustomerIfExpired(cus);
		}

		customerController.tryToSpawnCustomer(this.currentDifficulty, this.currentMode);

		for (Cook thisCook : cooks) {
			thisCook.getBody().setLinearVelocity(0F, 0F);
			if (thisCook == cook) {
				thisCook.userInput();
			}
		}

		if (Interactions.isJustPressed(InputKey.InputTypes.COOK_SWAP)) {
			setCook((cookIndex + 1) % cooks.size);
		}

		if (Interactions.isJustPressed(InputKey.InputTypes.PAUSE)) {
			screenController.pauseGameScreen();
		}


		world.step(1 / 60f, 6, 2);

		for (GameEntity entity : gameEntities) {
			entity.update(delta);
		}

        //Added for Assessment 2 - Power up related code

		if (PowerUpHandler.activePowerUp() != null) {
			powerUpHandler.updateCoolDown(delta);

		} else if (Interactions.isJustPressed(InputKey.InputTypes.BUY_POWERUP) && money >= Constants.POWERUP_COST) {
			spendMoney(Constants.POWERUP_COST);
			powerUpHandler.addPowerUp(true);

		} else if (Interactions.isJustPressed(InputKey.InputTypes.ACTIVATE_POWERUP)) {
            powerUpHandler.activatePower(0);
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
		//box2DDebugRenderer.render(world, camera.combined.scl(Constants.PPM));
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

    /**
     * Get the saveHandler
     * @return the saveHandler instance
     */
    public SaveHandler getSaveHandler(){
        return this.sv;
    }

    /**
     * Get the array of GameEntities
     * @return the Array of GameEntities
     */
	public Array<GameEntity> getGameEntities() {
		return this.gameEntities;
	}

    /**
     * Get the array of Cooks
     * @return the array of Cooks
     */
	public Array<Cook> getCooks() {
		return this.cooks;
	}

	/**
	 * Sets the currently active {@link #cook} that the game is using.
	 *
	 * @param cookIndex The index of {@link #cook} in the {@link #cooks} array.
	 */
	public void setCook(int cookIndex) {
		if (cookIndex < 0 || cookIndex > cooks.size) {
			throw new ArrayIndexOutOfBoundsException();
		}
		this.cook = cooks.get(cookIndex);
		this.cookIndex = cookIndex;
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
     * Lose a reputation point if possible. If we cant then the game ends
     */
    public void loseReputation() {
		if (reputation > 1) {
			reputation--;
		} else {
			reputation = 3;
			screenController.setScreen((ScreenController.ScreenID.GAMEOVER));
			((GameOverScreen) screenController.getScreen(ScreenController.ScreenID.GAMEOVER)).setTime(hoursPassed,
					minutesPassed, secondsPassed);
		}

		gameHud.updateReputationLabel(reputation);
	}

    /**
     * Add a certain amount of money to the current amount
     * @param amount The amount of money to add
     */
	public void addMoney(int amount) {
		if (PowerUpHandler.activePowerUp() == PowerUp.DOUBLE_MONEY) {
			amount *= 2;
		}
		money += amount;
		gameHud.updateMoneyLabel(money);
	}

    /**
     * Spend a certain amount of money
     * @param amount The amount money to spend
     * @return true if the money could be spent, false otherwise
     */
	public boolean spendMoney(int amount) {
		System.out.println("AMOUNT OF MONEY SPENT: " + amount);
		if (money - amount < 0) {
			return false;
		}
		money -= amount;

		gameHud.updateMoneyLabel(money);

		return true;
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
     * Add an amount of milliseconds to the total time paused
     * @param amount The amount defined in the line above
     */
	public void addToTimePaused(long amount) {
		totalTimePaused += amount;
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
        reputation = 3;

        money = Constants.MONEY_START;
        gameHud.updateMoneyLabel(money);

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
        this.cooks.get(1).setCookID(1);
        this.cooks.get(2).setCookID(2);
	}

	/**
	 * A variable for setting up the game when it starts.
	 *
	 * @param customers The number of customers that need to be served in the game
	 *                  to finish.
	 */
	public void startGame(int customers, MenuScreen.difficulty diff, MenuScreen.mode md, boolean calledFromLoad) {

        this.currentDifficulty = diff;
        this.currentMode = md;

        if (!calledFromLoad) {
            secondsPassed = 0;
            minutesPassed = 0;
            hoursPassed = 0;

            previousSecond = TimeUtils.millis();
            totalTimePaused = 0;
            msPast1s = 0;

            int customersToServe;
            if (md == MenuScreen.mode.SCENARIO) {
                customersToServe = customers;
            } else {
                customersToServe = -1;
                System.out.println("Endless mode selected");
            }

            customerController.setCustomersLeft(customersToServe);
            customerController.setCustomersServed(0);
            customerController.setTotalCustomersToServe(customersToServe);
        }
	}

    // Added for Assessment 2
    /**
     * Restore the gameScreen to another state using the passed in data - Called by SaveHandler
     * @param smallTimeDifference - The amount of milliseconds past the amount of seconds in the next parameter
     * @param totalSeconds - Amount of seconds passed of the game running
     * @param repPoints - Reputation amount
     * @param moneyAmount - Amount of money
     * @param customersServed - Amount of customers served
     * @param customersLeft - Amount of customers left to serve
     * @param customersToServe - Amount of customers total to serve
     * @param loadedDiff - The difficulty (only makes a difference if the mode is scenario)
     * @param loadedMode - The mode
     * @param cookPositions - An array of cook positions, in form [x,y,x1,y1,x2,y2]
     * @param cookFoodStacks - An Arraylist of ArrayLists that stores the cooks' food stacks
     * @param cookFacings - An array of cook facings
     * @param stationFoodStacks - An Arraylist of ArrayLists that stores the counter stations inventories
     * @param lastCustomerSpawnTime - The time in seconds of the last customer spawn
     * @param custOrders - An array of strings corresponding to the customers orders
     * @param cusIndices - An array of integers corresponding to the customers indexes
     * @param cusSpawnTimes - An array of integers corresponding to the customers spawn times in seconds
     * @param custDeadTimes - An array of integers corresponding to the customers leave times in seconds
     * @param lockedStations - An array of booleans indicating whether a station is locked or not
     * @param autoCooks - An array of booleans indicating whether a station has an AutoCook or not
     * @param inters - An array of InteractionResults corresponding to current food items being prepared on stations
     * @param progresses - An array of floats corresponding to the progress of existing food item preparations
     * @param burnprogresses - An array of floats corresponding to the burn levels of current cooking preparations
     * @param stepnums - An array of integers corresponding to the current cooking steps of preparations
     * @param foods - An array of FoodItems corresponding to the current storage of preparation stations
     * @param powerUpCooldown - The cooldown of the current powerUp
     * @param activePowerup- The current active powerUp
     * @param currentPowerUps - The array of current stored powerUps (just one value in this implementation)
     */
    public void restoreFromData(long smallTimeDifference, int totalSeconds, int repPoints, int moneyAmount,
                                int customersServed, int customersLeft, int customersToServe,
                                MenuScreen.difficulty loadedDiff, MenuScreen.mode loadedMode,
                                float[] cookPositions, ArrayList<?> cookFoodStacks,
                                Cook.Facing[] cookFacings,
                                ArrayList<?> stationFoodStacks, int lastCustomerSpawnTime,
                                String[] custOrders, int[] cusIndices, int[] cusSpawnTimes, int[] custDeadTimes,
                                boolean[] lockedStations, boolean[] autoCooks,
                                Interactions.InteractionResult[] inters, float[] progresses,
                                float[] burnprogresses, int[] stepnums, FoodItem.FoodID[] foods,
                                int powerUpCooldown, PowerUp activePowerup, PowerUp[] currentPowerUps) {

        this.money = moneyAmount;
        gameHud.updateMoneyLabel(moneyAmount);
        this.reputation = repPoints;
        this.customerController.setCustomersLeft(customersLeft);
        this.customerController.setCustomersServed(customersServed);
        this.customerController.setTotalCustomersToServe(customersToServe);
        gameHud.updateCustomersLeftLabel(customersLeft);

        int ctr = 0;
        for (Cook ck : cooks) {
            ck.getBody().setTransform(cookPositions[ctr * 2], cookPositions[ctr * 2 + 1], 0);
            ArrayList<?> minilist = (ArrayList<?>) cookFoodStacks.get(ctr);
            ck.foodStack.setFoodStackFromArrayList(minilist);
            ck.setFacing(cookFacings[ctr]);
            ctr++;
        }



        for (int i = 0; i < this.interactables.size; i++){
            if (!lockedStations[i]){
                ((Station) this.interactables.get(i)).setLocked(false);
            }
            if (stationFoodStacks.get(i) != null) {
                ArrayList<?> minilist = (ArrayList<?>) stationFoodStacks.get(i);
                ((CounterStation) this.interactables.get(i)).getFoodStack().setFoodStackFromArrayList(minilist);
            }
            if (this.interactables.get(i) instanceof PreparationStation){
                ((PreparationStation) this.interactables.get(i)).restoreStationFromSave(inters[i], progresses[i],
                        burnprogresses[i], stepnums[i], foods[i], autoCooks[i]);

            }
        }

        customerController.setLastCustomerSpawnTime(lastCustomerSpawnTime);
        for (int i =0; i< custOrders.length;i++){
            this.customerController.restoreCustomerFromSave(cusIndices[i],
                     cusSpawnTimes[i], custDeadTimes[i],custOrders[i]);
        }

        this.powerUpHandler.restoreFromData(powerUpCooldown, activePowerup, currentPowerUps);

        this.previousSecond = TimeUtils.millis() - smallTimeDifference;
        this.setTiming(totalSeconds);
        startGame(customersToServe, loadedDiff, loadedMode, true);
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

    /**
     * Get the camera that uses the game space coordinates
     * @return The gameScreens camera
     */
    public OrthographicCamera getGameCoordsCamera() {
        camera.update();
        return camera;
    }

    public MenuScreen.difficulty getCurrentDifficulty(){
        return currentDifficulty;
    }
    public MenuScreen.mode getCurrentMode(){
        return currentMode;
    }

    public PowerUpHandler getPowerUpHandler(){
        return powerUpHandler;
    }
    public int getSecondsPassed(){
        return secondsPassed;
    }
    public int getMinutesPassed(){
        return minutesPassed;
    }
    public int getHoursPassed(){
        return hoursPassed;
    }

}
