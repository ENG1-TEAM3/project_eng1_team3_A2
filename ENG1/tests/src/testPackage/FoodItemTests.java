package testPackage;

import food.FoodItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class FoodItemTests {
    @Test
    public void testPixelHeights(){
        assertEquals("These tests assert that the correct pixel height is returned for each food item", (Float) 20F, FoodItem.foodHeights.get(FoodItem.FoodID.lettuce));
        assertEquals("These tests assert that the correct pixel height is returned for each food item", (Float) 4F, FoodItem.foodHeights.get(FoodItem.FoodID.lettuceChop));
        assertEquals("These tests assert that the correct pixel height is returned for each food item", (Float) 20F, FoodItem.foodHeights.get(FoodItem.FoodID.tomato));
        assertEquals("These tests assert that the correct pixel height is returned for each food item", (Float) 5.8F, FoodItem.foodHeights.get(FoodItem.FoodID.tomatoChop));
        assertEquals("These tests assert that the correct pixel height is returned for each food item", (Float) 20F, FoodItem.foodHeights.get(FoodItem.FoodID.onion));
        assertEquals("These tests assert that the correct pixel height is returned for each food item", (Float) 5.8F, FoodItem.foodHeights.get(FoodItem.FoodID.onionChop));
        assertEquals("These tests assert that the correct pixel height is returned for each food item", (Float) 8F, FoodItem.foodHeights.get(FoodItem.FoodID.meat));
        assertEquals("These tests assert that the correct pixel height is returned for each food item", (Float) 8F, FoodItem.foodHeights.get(FoodItem.FoodID.meatCook));
        assertEquals("These tests assert that the correct pixel height is returned for each food item", (Float) 20F, FoodItem.foodHeights.get(FoodItem.FoodID.bun));
        assertEquals("These tests assert that the correct pixel height is returned for each food item", (Float) 10F, FoodItem.foodHeights.get(FoodItem.FoodID.bottomBun));
        assertEquals("These tests assert that the correct pixel height is returned for each food item", (Float) 12F, FoodItem.foodHeights.get(FoodItem.FoodID.topBun));
    }
}
