package testPackage;

import org.junit.runner.RunWith;

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
import stations.PreparationStation;
import stations.Station;

import java.io.IOException;

@RunWith(GdxTestRunner.class)
public class SaveHandlerTests {
    @Test
    public void testSavingSimple(){
        Boot b1 = Boot.getInstance();
        b1.createHeadless();

        MapHelper m1 = MapHelper.getInstance();
        GameScreen gs1 = (GameScreen) b1.getScreenController().getScreen(ScreenController.ScreenID.GAME);
        m1.setGameScreen(gs1);

        Rectangle r1 = new Rectangle(100.0f,100.0f,42.50f,20.00f);
        Cook c1 = new Cook(r1.getWidth(), r1.getHeight() , BodyHelper.createBody(r1.x,r1.y,r1.width,r1.height, false, ((GameScreen) b1.getScreenController().getScreen(ScreenController.ScreenID.GAME)).getWorld()));
        gs1.addCook(c1);

        try {
            gs1.getSaveHandler().saveToFile("TestSave1.txt", 0L);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }




        Boot b2 = Boot.getInstance();
        b2.createHeadless();

        MapHelper m2 = MapHelper.getInstance();
        GameScreen gs2 = (GameScreen) b2.getScreenController().getScreen(ScreenController.ScreenID.GAME);
        m2.setGameScreen(gs2);

        Rectangle r2 = new Rectangle(50.0f,50.0f,42.50f,20.00f);
        Cook c2 = new Cook(r2.getWidth(), r2.getHeight() , BodyHelper.createBody(r2.x,r2.y,r2.width,r2.height, false, ((GameScreen) b2.getScreenController().getScreen(ScreenController.ScreenID.GAME)).getWorld()));
        gs2.addCook(c2);

        try {
            gs2.getSaveHandler().loadFromFile("TestSave1.txt");
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        c2.update(0.01f);

        assertEquals("This test asserts that saving works correctly by checking the position of a saved cook",100.0f, c2.getX(), 0.0001);
        assertEquals("This test asserts that saving works correctly by checking the position of a saved cook",100.0f, c2.getY(), 0.0001);

    }
}
