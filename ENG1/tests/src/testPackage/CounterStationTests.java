package testPackage;

import com.badlogic.gdx.Input;
import helper.CollisionHelper;
import helper.Constants;
import interactions.Interactions;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import com.badlogic.gdx.math.Rectangle;
import cooks.Cook;
import food.FoodItem;
import game.Boot;
import game.GameScreen;
import game.ScreenController;
import helper.BodyHelper;
import helper.MapHelper;
import interactions.InputKey;
import org.junit.Test;
import stations.CounterStation;

@RunWith(GdxTestRunner.class)
public class CounterStationTests {
    @Test
    public void testInteractCounterStation(){
        Boot b1 = Boot.getInstance();
        b1.createHeadless();

        MapHelper m1 = MapHelper.getInstance();
        m1.setGameScreen((GameScreen) b1.getScreenController().getScreen(ScreenController.ScreenID.GAME));

        Rectangle r1 = new Rectangle(0.0f,0.0f,42.50f,20.00f);
        Cook c1 = new Cook(r1.getWidth(), r1.getHeight() , BodyHelper.createBody(r1.x,r1.y,r1.width,r1.height, false, ((GameScreen) b1.getScreenController().getScreen(ScreenController.ScreenID.GAME)).getWorld()));

        CounterStation cs1 = new CounterStation(new Rectangle(100,100,100,100), false,(GameScreen) b1.getScreenController().getScreen(ScreenController.ScreenID.GAME));

        c1.foodStack.addStack(FoodItem.FoodID.bottomBun);

        cs1.interact(c1, InputKey.InputTypes.PUT_DOWN);

        assertEquals("This test asserts that the bun leaves the cooks inventory when putting it down on a counter",
                "[]", c1.foodStack.toString());

        assertEquals("This test asserts that the bun enters the counter's inventory when putting it down on a counter",
                "[bottomBun]", cs1.getFoodStack().toString());

        c1.foodStack.addStack(FoodItem.FoodID.meat);
        c1.foodStack.addStack(FoodItem.FoodID.lettuce);

        cs1.interact(c1, InputKey.InputTypes.PUT_DOWN);

        assertEquals("This test asserts that the item on the top of the cooks stack removed when putting it down on a counter",
                "[meat]", c1.foodStack.toString());

        assertEquals("This test asserts that the new item is inserted on the top of the stack of the counter",
                "[lettuce, bottomBun]", cs1.getFoodStack().toString());

        cs1.interact(c1, InputKey.InputTypes.USE);

        assertEquals("This test asserts that the cook and the counter swap stacks when pressing the use key",
                "[lettuce, bottomBun]", c1.foodStack.toString());

        assertEquals("This test asserts that the cook and the counter swap stacks when pressing the use key",
                "[meat]", cs1.getFoodStack().toString());

        cs1.interact(c1, InputKey.InputTypes.USE);
        cs1.interact(c1, InputKey.InputTypes.PICK_UP);

        assertEquals("This test asserts that the cook will pick up one item from the top of the stack on the counter",
                "[lettuce, meat]",c1.foodStack.toString());

        assertEquals("This test asserts that the cook will pick up one item from the top of the stack on the counter",
                "[bottomBun]", cs1.getFoodStack().toString());
    }
    @Test
    public void testBuyLockedStation(){
        Boot b1 = Boot.getInstance();
        b1.createHeadless();

        MapHelper m1 = MapHelper.getInstance();
        GameScreen gs1 = (GameScreen) b1.getScreenController().getScreen(ScreenController.ScreenID.GAME);
        m1.setGameScreen(gs1);

        Rectangle r1 = new Rectangle(0.0f,0.0f,42.50f,20.00f);
        Cook c1 = new Cook(r1.getWidth(), r1.getHeight() , BodyHelper.createBody(r1.x,r1.y,r1.width,r1.height, false, ((GameScreen) b1.getScreenController().getScreen(ScreenController.ScreenID.GAME)).getWorld()));

        CounterStation cs1 = new CounterStation(new Rectangle(100,100,100,100), true,(GameScreen) b1.getScreenController().getScreen(ScreenController.ScreenID.GAME));
        int oldmoney = gs1.getMoney();
        cs1.interact(c1, InputKey.InputTypes.USE);
        assertEquals("This test asserts that money is spent to buy a locked station",
                oldmoney - Constants.STAFF_COST, gs1.getMoney());
        assertFalse("This test asserts that the station unlocks",
                cs1.isLocked());
    }

    @Test
    public void testCookInteraction(){
        Boot b1 = Boot.getInstance();
        b1.createHeadless();

        MapHelper m1 = MapHelper.getInstance();
        GameScreen gs1 = (GameScreen) b1.getScreenController().getScreen(ScreenController.ScreenID.GAME);
        m1.setGameScreen(gs1);

        Rectangle r1 = new Rectangle(75.0f,100.0f,42.50f,20.00f);
        Cook c1 = new Cook(r1.getWidth(), r1.getHeight() , BodyHelper.createBody(r1.x,r1.y,r1.width,r1.height, false, ((GameScreen) b1.getScreenController().getScreen(ScreenController.ScreenID.GAME)).getWorld()));

        CounterStation cs1 = new CounterStation(new Rectangle(100,100,100,100), false,(GameScreen) b1.getScreenController().getScreen(ScreenController.ScreenID.GAME));
        gs1.addInteractable(cs1);

        assertEquals("This test asserts that no station are found within the interaction bounds",0, CollisionHelper.getInstance().stationCollisions(new Rectangle(0,0,1,1)).size);
        assertEquals("This test asserts that a station is found within the interaction bounds",1, CollisionHelper.getInstance().stationCollisions(new Rectangle(150,150,1,1)).size);

        assertNull("This test asserts that an interaction is not found",CollisionHelper.getInstance().getInteract(c1, new Rectangle(0, 0, 1, 1)));
        assertNotNull("This test asserts that an interaction is found",CollisionHelper.getInstance().getInteract(c1, new Rectangle(150,150,1, 1)));

        c1.foodStack.addStack(FoodItem.FoodID.bun);
        c1.setFacing(Cook.Facing.RIGHT);
        c1.update(0.001f);
        Interactions.manualAddKey(new InputKey(InputKey.InputTypes.PUT_DOWN, Input.Keys.Q), false, true);
        c1.userInput();
        assertEquals("This test asserts that the cook interacts and puts the bun down","[]", c1.foodStack.toString());
        assertEquals("This test asserts that the station getst the bun","[bun]", cs1.getFoodStack().toString());


    }

}
