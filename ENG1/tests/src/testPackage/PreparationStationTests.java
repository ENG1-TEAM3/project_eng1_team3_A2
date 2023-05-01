package testPackage;

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
import stations.PreparationStation;
import stations.Station;

@RunWith(GdxTestRunner.class)
public class PreparationStationTests {
    @Test
    public void testInteractPrepStationChop(){
        Boot b1 = Boot.getInstance();
        b1.createHeadless();

        MapHelper m1 = MapHelper.getInstance();
        m1.setGameScreen((GameScreen) b1.getScreenController().getScreen(ScreenController.ScreenID.GAME));

        Rectangle r1 = new Rectangle(0.0f,0.0f,42.50f,20.00f);
        Cook c1 = new Cook(r1.getWidth(), r1.getHeight() , BodyHelper.createBody(r1.x,r1.y,r1.width,r1.height, false, ((GameScreen) b1.getScreenController().getScreen(ScreenController.ScreenID.GAME)).getWorld()));

        PreparationStation ps1 = new PreparationStation(new Rectangle(100,100,100,100), false, (GameScreen) b1.getScreenController().getScreen(ScreenController.ScreenID.GAME) );
        assertFalse("This test asserts that a new prep station starting unlocked starts unlocked",
                ps1.isLocked());
        assertFalse("This test asserts that a new prep station is not in use",
                ps1.isInUse());



        ps1.setID(Station.StationID.cut);
        c1.foodStack.addStack(FoodItem.FoodID.meat);


        ps1.interact(c1, InputKey.InputTypes.PUT_DOWN);



        assertEquals("This test asserts that the cook cannot cut meat (trying to use cutting station for non cuttable item)",
                "[meat]", c1.foodStack.toString());
        c1.foodStack.clearStack();
        c1.foodStack.addStack(FoodItem.FoodID.lettuce);

        ps1.interact(c1, InputKey.InputTypes.PUT_DOWN);
        assertEquals("This test asserts that the prep station will take lettuce when set to cutting mode",
                "[]", c1.foodStack.toString());

        ps1.interact(c1, InputKey.InputTypes.PICK_UP);
        assertEquals("This test asserts that the cook can pick the unprocessed lettuce back up",
                "[lettuce]", c1.foodStack.toString());

        ps1.interact(c1, InputKey.InputTypes.PUT_DOWN);

        ps1.update(0.05f); // not required for the test, just making sure it does not crash + code cov on cutting

        assertEquals("This test asserts that the lettuce starts at progress 0.25",
                25.0f, ps1.getProgress(), 0.001f);


        ps1.interact(c1, InputKey.InputTypes.USE);
        assertEquals("This test asserts that the lettuce jumps to progress 0.5 after an interaction",
                50.0f, ps1.getProgress(), 0.001f);
        ps1.interact(c1, InputKey.InputTypes.USE);
        assertEquals("This test asserts that the lettuce jumps to progress 0.75 after another interaction",
                75.0f, ps1.getProgress(), 0.001f);

        ps1.interact(c1, InputKey.InputTypes.USE);

        assertEquals("This test asserts that the lettuce jumps to progress 1.00 after another interaction",
                100.0f, ps1.getProgress(), 0.001f);

        ps1.interact(c1, InputKey.InputTypes.USE);

        assertEquals("This test asserts that the cook can pick up a finished chopped lettuce using the use key",
                "[lettuceChop]",c1.foodStack.toString());


        //===========================================================This time with the pick up key=========================
        c1.foodStack.clearStack();
        c1.foodStack.addStack(FoodItem.FoodID.lettuce);

        ps1.interact(c1, InputKey.InputTypes.PUT_DOWN);
        assertEquals("This test asserts that the prep station will take lettuce when set to cutting mode",
                "[]", c1.foodStack.toString());

        assertEquals("This test asserts that the lettuce starts at progress 0.25",
                25.0f, ps1.getProgress(), 0.001f);


        ps1.interact(c1, InputKey.InputTypes.USE);
        assertEquals("This test asserts that the lettuce jumps to progress 0.5 after an interaction",
                50.0f, ps1.getProgress(), 0.001f);
        ps1.interact(c1, InputKey.InputTypes.USE);
        assertEquals("This test asserts that the lettuce jumps to progress 0.75 after another interaction",
                75.0f, ps1.getProgress(), 0.001f);

        ps1.interact(c1, InputKey.InputTypes.USE);

        assertEquals("This test asserts that the lettuce jumps to progress 1.00 after another interaction",
                100.0f, ps1.getProgress(), 0.001f);

        ps1.interact(c1, InputKey.InputTypes.PICK_UP);

        assertEquals("This test asserts that the cook can pick up a finished chopped lettuce using the pick up key",
                "[lettuceChop]",c1.foodStack.toString());
    }
    @Test
    public void testInteractPrepStationCook(){
        Boot b1 = Boot.getInstance();
        b1.createHeadless();

        MapHelper m1 = MapHelper.getInstance();
        m1.setGameScreen((GameScreen) b1.getScreenController().getScreen(ScreenController.ScreenID.GAME));

        Rectangle r1 = new Rectangle(0.0f,0.0f,42.50f,20.00f);
        Cook c1 = new Cook(r1.getWidth(), r1.getHeight() , BodyHelper.createBody(r1.x,r1.y,r1.width,r1.height, false, ((GameScreen) b1.getScreenController().getScreen(ScreenController.ScreenID.GAME)).getWorld()));

        PreparationStation ps1 = new PreparationStation(new Rectangle(100,100,100,100), false, (GameScreen) b1.getScreenController().getScreen(ScreenController.ScreenID.GAME));
        ps1.setID(Station.StationID.fry);
        c1.foodStack.addStack(FoodItem.FoodID.meat);

        ps1.interact(c1, InputKey.InputTypes.PUT_DOWN);

        assertEquals("This test asserts that cook can put meat down on a cooking station", "[]", c1.foodStack.toString());
        assertEquals("This test asserts that the cooking progress starts at 0.0", 0.0, ps1.getProgress(), 0.0001f);
        ps1.interact(c1, InputKey.InputTypes.USE);
        assertEquals("This test asserts that cooking progress will stay at 0.0 after a use interaction", 0.0f, ps1.getProgress(), 0.0001f);
        ps1.update(0.5f);
        Interactions.InteractionResult meatinter = Interactions.interaction(FoodItem.FoodID.meat, Station.StationID.fry);
        float spd = meatinter.getSpeed(); // This is 13
        assertEquals("This test asserts that cooking progress will increment by increase by cooking speed * delta", 0.0f + (spd * 0.5f), ps1.getProgress(), 0.0001f);
        ps1.update(3.0f);
        assertEquals("This test asserts that cooking progress will increment by increase by cooking speed * delta", 0.0f + (spd * 0.5f) + (spd * 3.0f), ps1.getProgress(), 0.0001f);
        // Progress here is 45.5
        ps1.update(0.5f);
        ps1.update(0.5f);
        ps1.update(0.5f);
        ps1.update(0.5f);
        ps1.update(0.5f);
        // (0.5 * 5 * 13) + 45.5 is more than 50, yet the cooking progress should not be able to pass 50.
        float[] steps = meatinter.getSteps();
        assertEquals("This test asserts that cooking progress cannot progress past a certain point without flipping", steps[0] , ps1.getProgress(), 0.0001f);
        ps1.interact(c1, InputKey.InputTypes.USE);
        assertEquals("This test asserts that cooking progress will not change after just inputting the flip", steps[0] , ps1.getProgress(), 0.0001f);
        ps1.update(0.5f);
        assertEquals("This test asserts that cooking progress can begin again after the flip", steps[0] + (0.5f * spd) , ps1.getProgress(), 0.0001f);
        ps1.update(0.5f);
        ps1.update(0.5f);
        ps1.update(0.5f);
        ps1.update(0.5f);
        ps1.update(0.5f);
        ps1.update(0.5f);
        ps1.update(0.5f);
        ps1.update(0.5f);
        assertEquals("This test asserts that cooking progress cannot exceed 100", 100.0f , ps1.getProgress(), 0.0001f);
        ps1.interact(c1, InputKey.InputTypes.PICK_UP);
        assertEquals("This test asserts that the cook can pick up the cooked meat", "[meatCook]", c1.foodStack.toString());
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

        PreparationStation ps1 = new PreparationStation(new Rectangle(100,100,100,100), true,(GameScreen) b1.getScreenController().getScreen(ScreenController.ScreenID.GAME));
        int oldmoney = gs1.getMoney();
        ps1.interact(c1, InputKey.InputTypes.USE);
        assertEquals("This test asserts that money is spent to buy a locked station",
                oldmoney - Constants.STAFF_COST, gs1.getMoney());
        assertFalse("This test asserts that the station unlocks",
                ps1.isLocked());
    }
    @Test
    public void testDeployAutoCook(){
        Boot b1 = Boot.getInstance();
        b1.createHeadless();

        MapHelper m1 = MapHelper.getInstance();
        GameScreen gs1 = (GameScreen) b1.getScreenController().getScreen(ScreenController.ScreenID.GAME);
        m1.setGameScreen(gs1);

        Rectangle r1 = new Rectangle(0.0f,0.0f,42.50f,20.00f);
        Cook c1 = new Cook(r1.getWidth(), r1.getHeight() , BodyHelper.createBody(r1.x,r1.y,r1.width,r1.height, false, ((GameScreen) b1.getScreenController().getScreen(ScreenController.ScreenID.GAME)).getWorld()));

        PreparationStation ps1 = new PreparationStation(new Rectangle(100,100,100,100), false,(GameScreen) b1.getScreenController().getScreen(ScreenController.ScreenID.GAME));
        c1.foodStack.addStack(FoodItem.FoodID.cook);
        assertFalse("This test asserts that a station does not start with an autocook",
                ps1.hasAutoCook());
        ps1.interact(c1, InputKey.InputTypes.PUT_DOWN);
        assertTrue("This test asserts that the station internal state changes when an autocook is placed",
                ps1.hasAutoCook());

    }


}
