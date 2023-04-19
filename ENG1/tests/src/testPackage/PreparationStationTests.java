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
import stations.PreparationStation;
import stations.Station;

@RunWith(GdxTestRunner.class)
public class PreparationStationTests {
    @Test
    public void testInteractPrepStation(){
        Boot b1 = Boot.getInstance();
        b1.createHeadless();

        MapHelper m1 = MapHelper.getInstance();
        m1.setGameScreen((GameScreen) b1.getScreenController().getScreen(ScreenController.ScreenID.GAME));

        Rectangle r1 = new Rectangle(0.0f,0.0f,42.50f,20.00f);
        Cook c1 = new Cook(r1.getWidth(), r1.getHeight() , BodyHelper.createBody(r1.x,r1.y,r1.width,r1.height, false, ((GameScreen) b1.getScreenController().getScreen(ScreenController.ScreenID.GAME)).getWorld()), null);

        PreparationStation ps1 = new PreparationStation(new Rectangle(100,100,100,100));
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
        ps1.update(0.05f);
        assertEquals("This test asserts that the lettuce starts at progress 0.25",
                25.0f, ps1.getProgress(), 0.001f);


        ps1.interact(c1, InputKey.InputTypes.USE);
        System.out.println(ps1.getProgress());
        ps1.interact(c1, InputKey.InputTypes.USE);
        System.out.println(ps1.getProgress());
        ps1.interact(c1, InputKey.InputTypes.USE);
        System.out.println(ps1.getProgress());
        System.out.println(c1.foodStack);
        ps1.interact(c1, InputKey.InputTypes.USE);
        System.out.println(ps1.getProgress());
        System.out.println(c1.foodStack);
    }
}
