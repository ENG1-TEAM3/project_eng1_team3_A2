package testPackage;

import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import cooks.Cook;
import food.FoodItem;
import game.Boot;
import game.GameScreen;
import game.ScreenController;
import helper.BodyHelper;
import helper.MapHelper;
import interactions.InputKey;
import org.junit.Test;
import org.junit.runner.RunWith;
import stations.BinStation;
import stations.Pantry;

@RunWith(GdxTestRunner.class)
public class PantryStationTests {
    @Test
    public void testInteractPantry(){
        Boot b1 = Boot.getInstance();
        b1.createHeadless();

        MapHelper m1 = MapHelper.getInstance();
        m1.setGameScreen((GameScreen) b1.getScreenController().getScreen(ScreenController.ScreenID.GAME));

        Rectangle r1 = new Rectangle(0.0f,0.0f,42.50f,20.00f);
        Cook c1 = new Cook(r1.getWidth(), r1.getHeight() , BodyHelper.createBody(r1.x,r1.y,r1.width,r1.height, false, ((GameScreen) b1.getScreenController().getScreen(ScreenController.ScreenID.GAME)).getWorld()), null);

        Pantry p1 = new Pantry(new Rectangle(100,100,100,100), false,(GameScreen) b1.getScreenController().getScreen(ScreenController.ScreenID.GAME));
        p1.setItem(FoodItem.FoodID.meat);

        assertEquals("This test asserts that a cook starts out with an empty stack",
                "[]",c1.foodStack.toString());

        p1.interact(c1, InputKey.InputTypes.PICK_UP);

        assertEquals("This test asserts that a cook picks up the correct item from the pantry, using the pick up input type",
                "[meat]",c1.foodStack.toString());

        p1.interact(c1, InputKey.InputTypes.USE);

        assertEquals("This test asserts that a cook picks up another item from the pantry, this time using the use input type",
                "[meat, meat]", c1.foodStack.toString());

        p1.setItem(FoodItem.FoodID.bun);
        p1.interact(c1, InputKey.InputTypes.USE);
        assertEquals("This test asserts that the cook picks up a bottom bun first",
                "[bottomBun, meat, meat]", c1.foodStack.toString());
        p1.interact(c1, InputKey.InputTypes.USE);
        assertEquals("This test asserts that the cook will pick up a top bun after a bottom bun",
                "[topBun, bottomBun, meat, meat]", c1.foodStack.toString());
        p1.interact(c1, InputKey.InputTypes.USE);
        assertEquals("This test asserts that the cook will pick up another bottom bun after picking up a set of buns",
                "[bottomBun, topBun, bottomBun, meat, meat]", c1.foodStack.toString());
    }

}
