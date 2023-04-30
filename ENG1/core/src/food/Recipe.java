package food;

import java.util.HashMap;
import java.util.Random;

import com.badlogic.gdx.utils.Array;

import food.FoodItem.FoodID;

/**
 * Contains all the Recipes. Each recipe can be represented by a FoodStack. Many
 * FoodStacks map to the same recipe. Also contains many helper functions useful
 * in generating recipes.
 */
public class Recipe {
	private static final Array<String> recipeNames = new Array<>();
	/**
	 * A HashMap containing how each FoodItem's FoodID, via a station of StationID,
	 * can convert to another foodID.
	 */
	public static final HashMap<String, Array<FoodStack>> recipes = new HashMap<>();
	public static final HashMap<String, Integer> prices = new HashMap<>();

	static {
		generateRecipes("Onion Tomato Salad", allCombos(FoodID.onionChop, FoodID.tomatoChop), 99);
		generateRecipes("Lettuce Tomato Salad", allCombos(FoodID.lettuceChop, FoodID.tomatoChop), 99);
		generateRecipes("Lettuce Onion Salad", allCombos(FoodID.lettuceChop, FoodID.onionChop), 99);
		Array<FoodStack> plainBurger = new Array<>();
		plainBurger.add(new FoodStack(FoodID.topBun, FoodID.meatCook, FoodID.bottomBun));
		recipes.put("Plain Burger", plainBurger);
		prices.put("Plain Burger", 250);

		Array<FoodID> topBunArray = new Array<>();
		topBunArray.add(FoodID.topBun);
		Array<FoodID> bottomBunArray = new Array<>();
		bottomBunArray.add(FoodID.bottomBun);
        // Some new recipes added for pizzas and potatoes
        Array<FoodID> emptyPrependArray = new Array<>();
        Array<FoodID> bottomCookedDough = new Array<>();
        bottomCookedDough.add(FoodID.doughCook);
        Array<FoodID> bottomCookedPotato = new Array<>();
        bottomCookedPotato.add(FoodID.potatoCook);
		generateRecipes("Lettuce Burger", allCombos(topBunArray, bottomBunArray, FoodID.meatCook, FoodID.lettuceChop),
				299);
		generateRecipes("Onion Burger", allCombos(topBunArray, bottomBunArray, FoodID.meatCook, FoodID.onionChop), 299);
		generateRecipes("Tomato Burger", allCombos(topBunArray, bottomBunArray, FoodID.meatCook, FoodID.tomatoChop),
				299);
		generateRecipes("Lettuce Burger", allCombos(topBunArray, bottomBunArray, FoodID.meatCook, FoodID.lettuceChop),
				299);
		generateRecipes("Lettuce Tomato Burger",
				allCombos(topBunArray, bottomBunArray, FoodID.meatCook, FoodID.lettuceChop, FoodID.tomatoChop), 320);
		generateRecipes("Lettuce Onion Burger",
				allCombos(topBunArray, bottomBunArray, FoodID.meatCook, FoodID.lettuceChop, FoodID.onionChop), 320);
		generateRecipes("Tomato Onion Burger",
				allCombos(topBunArray, bottomBunArray, FoodID.meatCook, FoodID.tomatoChop, FoodID.onionChop), 320);
		generateRecipes("Lettuce Tomato Onion Burger", allCombos(topBunArray, bottomBunArray, FoodID.meatCook,
				FoodID.lettuceChop, FoodID.tomatoChop, FoodID.onionChop), 350);
        generateRecipes("Cheese Tomato Pizza",
                allCombos(emptyPrependArray, bottomCookedDough, FoodID.cheeseChop, FoodID.tomatoChop), 300);
        generateRecipes("Pepperoni Pizza",
                allCombos(emptyPrependArray, bottomCookedDough, FoodID.pepperoniChop), 300);
        generateRecipes("Mushroom Pizza",
                allCombos(emptyPrependArray, bottomCookedDough, FoodID.mushroomChop), 300);
        generateRecipes("Beans Cheese Potato",
                allCombos(emptyPrependArray, bottomCookedPotato, FoodID.cheeseChop, FoodID.beansCook), 300);
        generateRecipes("Chili Potato",
                allCombos(emptyPrependArray, bottomCookedPotato, FoodID.chiliCook), 300);



		/*
		 * recipes.put("Onion Tomato Salad", new String[] { new
		 * FoodStack(FoodID.onionChop, FoodID.tomatoChop).toString(), new
		 * FoodStack(FoodID.tomatoChop, FoodID.onionChop).toString() } );
		 * recipes.put("Lettuce Tomato Salad", new String[] { new
		 * FoodStack(FoodID.lettuceChop, FoodID.tomatoChop).toString(), new
		 * FoodStack(FoodID.tomatoChop, FoodID.lettuceChop).toString() } );
		 * recipes.put("Lettuce Onion Salad", new String[] { new
		 * FoodStack(FoodID.lettuceChop, FoodID.onionChop).toString(), new
		 * FoodStack(FoodID.onionChop, FoodID.lettuceChop).toString() } );
		 * recipes.put("Lettuce Burger", new String[] { new FoodStack(FoodID.topBun,
		 * FoodID.meatCook, FoodID.lettuceChop, FoodID.bottomBun).toString(), new
		 * FoodStack(FoodID.topBun, FoodID.lettuceChop, FoodID.meatCook,
		 * FoodID.bottomBun).toString() } ); recipes.put("Onion Burger", new String[] {
		 * new FoodStack(FoodID.topBun, FoodID.meatCook, FoodID.onionChop,
		 * FoodID.bottomBun).toString(), new FoodStack(FoodID.topBun, FoodID.onionChop,
		 * FoodID.meatCook, FoodID.bottomBun).toString() } );
		 * recipes.put("Tomato Burger", new String[] { new FoodStack(FoodID.topBun,
		 * FoodID.meatCook, FoodID.tomatoChop, FoodID.bottomBun).toString(), new
		 * FoodStack(FoodID.topBun, FoodID.tomatoChop, FoodID.meatCook,
		 * FoodID.bottomBun).toString() } ); recipes.put("Lettuce Onion Burger", new
		 * String[] { new FoodStack(FoodID.topBun, FoodID.meatCook, FoodID.lettuceChop,
		 * FoodID.onionChop, FoodID.bottomBun).toString(), new FoodStack(FoodID.topBun,
		 * FoodID.meatCook, FoodID.onionChop, FoodID.lettuceChop,
		 * FoodID.bottomBun).toString(), new FoodStack(FoodID.topBun,
		 * FoodID.lettuceChop, FoodID.meatCook, FoodID.onionChop,
		 * FoodID.bottomBun).toString(), new FoodStack(FoodID.topBun, FoodID.onionChop,
		 * FoodID.meatCook, FoodID.lettuceChop, FoodID.bottomBun).toString(), new
		 * FoodStack(FoodID.topBun, FoodID.onionChop, FoodID.lettuceChop,
		 * FoodID.meatCook, FoodID.bottomBun).toString(), new FoodStack(FoodID.topBun,
		 * FoodID.lettuceChop, FoodID.onionChop, FoodID.meatCook,
		 * FoodID.bottomBun).toString() } );
		 */
	}

	/**
	 * Creates an entry in {@link #recipes} of recipeName: (listOfFoodStacks as a String)
	 * 
	 * @param recipeName       The name of the recipe
	 * @param listOfFoodStacks All FoodStacks which equal this recipe.
	 */
	private static void generateRecipes(String recipeName, Array<Array<FoodID>> listOfFoodStacks, int price) {
		Array<FoodStack> allValidRecipes = new Array<>();
		for (int i = 0; i < listOfFoodStacks.size; i++) {
			Array<FoodID> recipe = listOfFoodStacks.get(i);
			allValidRecipes.add(new FoodStack(recipe));
		}
		recipes.put(recipeName, allValidRecipes);
		prices.put(recipeName, price);
		recipeNames.add(recipeName);
	}

	/**
	 * Generates all the combinations (think THE1 :D) of the stuff length for the
	 * stuff specified
	 * 
	 * @param <T>   Works for any type.
	 * @param stuff The items you want all combos out of
	 * @return An array of arrays, containing all combos
	 */
	@SafeVarargs
	private static <T> Array<Array<T>> allCombos(T... stuff) {
		return allCombosR(new Array<T>(), new Array<>(Array.with(stuff)));
	}

	/**
	 * Very similar to {@link #allCombos(Object[])}, except every combo is prepended
	 * and appended stuff. E.g. Every Burger has a topBun and bottomBun, so topBun
	 * is in prepend, and bottomBun is in append, while the filling is in T...
	 * stuff.
	 * 
	 * @param <T>     Any type.
	 * @param prepend The stuff which will appear before all combos.
	 * @param append  The stuff which will appear after all combos.
	 * @param stuff   The stuff to generate all combos of stuff length of
	 * @return An array of arrays containing all combinations with prepend and
	 *         append.
	 */
	@SafeVarargs
	private static <T> Array<Array<T>> allCombos(Array<T> prepend, Array<T> append, T... stuff) {
		Array<Array<T>> combos = allCombosR(new Array<T>(), new Array<>(Array.with(stuff)));
		Array<Array<T>> newCombos = new Array<>();
		for (Array<T> combo : combos) {
			Array<T> newCombo = new Array<>();
			newCombo.addAll(prepend);
			newCombo.addAll(combo);
			newCombo.addAll(append);
			newCombos.add(newCombo);
		}
		return newCombos;
	}

	/**
	 * The recursive helper function responsible for creating all combos. Used
	 * exclusively by allCombos atm.
	 * 
	 * @param <T>       Any type.
	 * @param myList    The combo being generated.
	 * @param remaining The remaining elements to add to the combo.
	 * @return An array containing all combos (each combo is in an array too).
	 */
	private static <T> Array<Array<T>> allCombosR(Array<T> myList, Array<T> remaining) {
		// If there's no remaining, add myList to storage by returning it in storage
		// form
		if (remaining.size == 0) {
			// return new Array<Array<T>>(Array.with(myList));
			Array<Array<T>> retArray = new Array<>();
			retArray.add(myList);
			return retArray;
		}
		// We want to create a new branch for every single remaining item
		// The newList and newRemaining is carried into each branch
		Array<Array<T>> storage = new Array<>();
		for (int i = 0; i < remaining.size; i++) {
			// Create the newList for branch i
			Array<T> newList = new Array<>();
			newList.addAll(myList);
			newList.add(remaining.get(i));
			// Create the newRemaining for branch i
			Array<T> newRemaining = new Array<>();
			newRemaining.addAll(remaining);
			newRemaining.removeIndex(i);
			// Create branch i and add its storage onto our storage
			storage.addAll(allCombosR(newList, newRemaining));
		}
		return storage;
	}

	public static boolean matchesRecipe(FoodStack foodStack, String recipeName) {
		// First get the recipe
		Array<FoodStack> validStacks = recipes.get(recipeName);
		// If it doesn't exist, return false
		if (validStacks == null) {
			return false;
		}

		// For each validStack, check if the foodStack matches.
		for (FoodStack validStack : validStacks) {
			if (validStack.toString().equals(foodStack.toString())) {
				return true;
			}
		}
		return false;
	}


	public static String randomRecipe() {
		Random random = new Random();
		return recipeNames.get(random.nextInt(recipeNames.size));
	}

	/**
	 * Outputs a random option from the {@link #recipes} of the recipe for
	 * {@link #recipeNames}.
	 * 
	 * @param recipeName The name of the {@link Recipe#recipes}.
	 * @return {@link FoodStack} : A random {@link FoodStack} from the different
	 *         ways of making the recipe for {@code recipeName}. <br>
	 *         Returns {@code null} if nothing is found.
	 */
	public static FoodStack randomRecipeOption(String recipeName) {
		Array<FoodStack> options = recipes.get(recipeName);
		// If null, return null
		if (options == null) {
			return null;
		}
		// If not null, then return a random option.
		Random random = new Random();
		return options.get(random.nextInt(options.size));
	}

	/**
	 * Outputs the first option added to the {@link #recipes} for the recipe name
	 * input.
	 * 
	 * @param recipeName The name of the {@link Recipe} to get the first option of.
	 * @return {@link FoodStack} : The first {@link FoodStack} available for the
	 *         recipe named {@code requestName}. <br>
	 *         Returns {@code null} if nothing is found.
	 */
	public static FoodStack firstRecipeOption(String recipeName) {
		Array<FoodStack> options = recipes.get(recipeName);
		// If null, return null
		if (options == null) {
			return null;
		}
		return options.get(0);
	}
}
