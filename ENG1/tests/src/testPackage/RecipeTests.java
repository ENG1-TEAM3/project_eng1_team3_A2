package testPackage;

import food.FoodItem;
import food.FoodStack;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import food.Recipe;

@RunWith(GdxTestRunner.class)
public class RecipeTests {
    @Test
    public void testMatchesRecipe(){
        assertFalse("This test asserts that matchesRecipe returns false when provided a recipe that does not exist",
                Recipe.matchesRecipe(new FoodStack(), "Doesnt exist"));
        assertTrue("This test asserts that matchesRecipe returns true when a valid recipe is provided and a valid foodstack",
                Recipe.matchesRecipe(new FoodStack(FoodItem.FoodID.lettuceChop, FoodItem.FoodID.onionChop), "Lettuce Onion Salad"));
        assertFalse("This test asserts that matchesRecipe returns false when a valid recipe is provided but incorrect foodstack",
                Recipe.matchesRecipe(new FoodStack(FoodItem.FoodID.lettuce, FoodItem.FoodID.onion), "Lettuce Onion Salad"));
    }
    @Test
    public void testEmptyFoodStackInputs(){
        assertNull("This test asserts that randomRecipeOption returns null when passed a recipe that does not exist",
                Recipe.randomRecipeOption("Doesnt exist"));
        assertNull("This test asserts that firstRecipeOption returns null when passed a recipe that does not exist",
                Recipe.firstRecipeOption("Doesnt exist"));

    }
}
