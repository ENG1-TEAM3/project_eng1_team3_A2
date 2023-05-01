package stations;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import cooks.Cook;
import customers.Customer;
import customers.CustomerController;
import food.Recipe;
import game.GameScreen;
import interactions.InputKey;
import powerups.PowerUp;
import powerups.PowerUpHandler;

/**
 * The {@link ServingStation} class, where the {@link cooks.Cook} provide the
 * {@link customers.Customer}s with their orders.
 */
public class ServingStation extends Station {

	private Customer customer;
	private final float customerX;
	private final float customerY;
	private final CustomerController customerController;

	/**
	 * The constructor for the {@link ServingStation}.
	 * 
	 * @param rectangle The collision and interaction area of the
	 *                  {@link ServingStation}.
	 *//*
		 * //* @param customerController The {@link CustomerController} for the game.
		 */
	public ServingStation(Rectangle rectangle, boolean locked, GameScreen gameScreen) {
		super(rectangle, locked, gameScreen);
		this.customerController = gameScreen.getCustomerController();
		// The below x and y can be changed wherever needed.
		this.customerX = rectangle.x + 32;
		this.customerY = rectangle.y + 96;
	}

	/**
	 * Returns whether the {@link ServingStation} does
	 * 
	 * @return {@code boolean} : Whether there is ({@code true}) or isn't
	 *         ({@code false}) a {@link Customer}.
	 */
	public boolean hasCustomer() {
		return customer != null;
	}

	/*
	 * public void setCustomer(Customer customer) { this.customer = customer; }
	 */

	/**
	 * The interact function for the {@link ServingStation}.
	 *
	 * <br>
	 * This checks that the {@link Cook} has the right {@link food.Recipe}, and then
	 * acts based on if the {@link Cook} does or does not.
	 * 
	 * @param cook      The cook that interacted with the {@link CookInteractable}.
	 * @param inputType The type of {@link InputKey.InputTypes} the player made with
	 *                  the {@link CookInteractable}.
	 */
	@Override
	public void interact(Cook cook, InputKey.InputTypes inputType) {
		// USE to see request, or submit request
		if (inputType == InputKey.InputTypes.USE) {
			// First make sure there is actually a request on this counter.
			if (hasCustomer()) {
				// If there is a request, then compare the two.
				String request;
				if (Recipe.matchesRecipe(cook.foodStack, customer.getRequestName())) {
					gameScreen.addMoney(Recipe.prices.get(customer.getRequestName()));
					// If it's correct, then the customer will take the food and leave.
					request = null;
					cook.foodStack.clearStack();
					customerController.customerServed(this);
				} else if (PowerUpHandler.activePowerUp() == PowerUp.SATISFIED_CUSTOMER) {
					PowerUpHandler.usePowerUp();
					gameScreen.addMoney(Recipe.prices.get(customer.getRequestName()));
					request = null;
					customerController.customerServed(this);
				} else if (PowerUpHandler.activePowerUp() == PowerUp.BONUS_TIME) {
					PowerUpHandler.usePowerUp();
					customer.deadTime += customerController.timeBetweenSpawnsSeconds();
					if (customerController.timeMultiplier == 1) {
						customerController.multiplyTimeBetweenServes();
					}
				}
			}
		}
	}

	/**
	 * Renders the {@link Customer} that is at the {@link ServingStation}.
	 * 
	 * @param batch The {@link SpriteBatch} used to render.
	 */
	@Override
	public void render(SpriteBatch batch) {
		if (hasCustomer()) {
			customer.render(batch);
		}
	}

	/**
	 * Set the {@link #customer} of the {@link ServingStation} to a new
	 * {@link Customer}.
	 * 
	 * @param customer The {@link Customer} to set the {@link ServingStation} to.
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	/**
	 * Getter for the {@link Customer} that is tied to the {@link ServingStation}.
	 * If there is no {@link Customer}, then {@code null} is returned.
	 * 
	 * @return {@link Customer} : The {@link Customer} at the
	 *         {@link ServingStation}.
	 */
	public Customer getCustomer() {
		return this.customer;
	}

	/**
	 * Getter for the {@link Customer}'s {@code x} position at the
	 * {@link ServingStation}.
	 * 
	 * @return {@code float} : The {@link Customer}'s {@code x}.
	 */
	public float getCustomerX() {
		return customerX;
	}

	/**
	 * Getter for the {@link Customer}'s {@code y} position at the
	 * {@link ServingStation}.
	 * 
	 * @return {@code float} : The {@link Customer}'s {@code y}.
	 */
	public float getCustomerY() {
		return customerY;
	}

	/**
	 * Getter for the {@code x} of the {@link Customer}. This is because the
	 * {@link Customer} is drawn by the {@link ServingStation}, and not the
	 * {@link ServingStation} itself. <br>
	 * If there is no {@link Customer} assigned, it uses the {@link #customerX}
	 * variable.
	 * 
	 * @return {@code float} : The {@code x} of the {@link Customer}.
	 */
	public float getX() {
		if (!hasCustomer()) {
			return customerX;
		}
		return customer.getX();
	}

	/**
	 * Getter for the {@code y} of the {@link Customer}. This is because the
	 * {@link Customer} is drawn by the {@link ServingStation}, and not the
	 * {@link ServingStation} itself. <br>
	 * If there is no {@link Customer} assigned, it uses the {@link #customerY}
	 * variable.
	 * 
	 * @return {@code float} : The {@code y} of the {@link Customer}.
	 */
	public float getY() {
		if (!hasCustomer()) {
			return customerY;
		}
		return customer.getY();
	}
}
