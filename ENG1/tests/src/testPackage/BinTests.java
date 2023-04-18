package testPackage;

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

@RunWith(GdxTestRunner.class)
public class BinTests {
    @Test
    public void testInteractBinStation(){
        Boot b1 = Boot.getInstance();
        b1.createHeadless();

        MapHelper m1 = MapHelper.getInstance();
        m1.setGameScreen((GameScreen) b1.getScreenController().getScreen(ScreenController.ScreenID.GAME));

        Rectangle r1 = new Rectangle(0.0f,0.0f,42.50f,20.00f);
        Cook c1 = new Cook(r1.getWidth(), r1.getHeight() , BodyHelper.createBody(r1.x,r1.y,r1.width,r1.height, false, ((GameScreen) b1.getScreenController().getScreen(ScreenController.ScreenID.GAME)).getWorld()), null);


        c1.foodStack.addStack(FoodItem.FoodID.bottomBun);
        assertEquals(1, c1.foodStack.size());

        BinStation bs1 = new BinStation(new Rectangle(100,100,100,100));
        bs1.interact(c1, InputKey.InputTypes.USE);
        assertEquals("This test asserts that the cooks stack is popped when interacting with a bin, using the use key",
                0, c1.foodStack.size());

        c1.foodStack.addStack(FoodItem.FoodID.bottomBun);
        c1.foodStack.addStack(FoodItem.FoodID.bottomBun);
        assertEquals(2, c1.foodStack.size());

        bs1.interact(c1, InputKey.InputTypes.PUT_DOWN);
        assertEquals("This test asserts that the cooks stack is popped when interacting with a bin, using the put down key",1, c1.foodStack.size());

    }
}
