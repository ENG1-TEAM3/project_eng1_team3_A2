package testPackage;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import cooks.Cook;
import customers.Customer;
import game.Boot;
import game.GameScreen;
import game.ScreenController;
import helper.BodyHelper;
import helper.MapHelper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import stations.ServingStation;


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
public class ServingStationTests {
    @Test
    public void testAddCustomer(){
        Boot b1 = Boot.getInstance();
        b1.createHeadless();

        MapHelper m1 = MapHelper.getInstance();
        m1.setGameScreen((GameScreen) b1.getScreenController().getScreen(ScreenController.ScreenID.GAME));

        Rectangle r1 = new Rectangle(0.0f,0.0f,42.50f,20.00f);
        Cook c1 = new Cook(r1.getWidth(), r1.getHeight() , BodyHelper.createBody(r1.x,r1.y,r1.width,r1.height, false, ((GameScreen) b1.getScreenController().getScreen(ScreenController.ScreenID.GAME)).getWorld()));

        ServingStation ss1 = new ServingStation(new Rectangle(100,100,100,100), false,(GameScreen) b1.getScreenController().getScreen(ScreenController.ScreenID.GAME));
        Customer cust1 = new Customer(null);

        ss1.setCustomer(cust1);
        assertTrue("This test asserts that the Serving Station has a customer", ss1.hasCustomer());
    }
    @Test
    public void testGiveWrongOrder(){
        Boot b1 = Boot.getInstance();
        b1.createHeadless();

        MapHelper m1 = MapHelper.getInstance();
        m1.setGameScreen((GameScreen) b1.getScreenController().getScreen(ScreenController.ScreenID.GAME));

        Rectangle r1 = new Rectangle(0.0f,0.0f,42.50f,20.00f);
        Cook c1 = new Cook(r1.getWidth(), r1.getHeight() , BodyHelper.createBody(r1.x,r1.y,r1.width,r1.height, false, ((GameScreen) b1.getScreenController().getScreen(ScreenController.ScreenID.GAME)).getWorld()));

        ServingStation ss1 = new ServingStation(new Rectangle(100,100,100,100), false,(GameScreen) b1.getScreenController().getScreen(ScreenController.ScreenID.GAME));
        Customer cust1 = new Customer(null);
        cust1.setRequestName("Onion Tomato Salad");
        c1.foodStack.addStack(FoodItem.FoodID.bottomBun);
        ss1.setCustomer(cust1);
        ss1.interact(c1, InputKey.InputTypes.USE);
        assertEquals("This test asserts that nothing happens upon interacting with the wrong recipe", "[bottomBun]",
                c1.foodStack.toString());
        assertTrue("This test asserts that nothing happens upon interacting with the wrong recipe", ss1.hasCustomer());

    }
    @Test
    public void testGiveCorrectOrder(){
        Boot b1 = Boot.getInstance();
        b1.createHeadless();

        MapHelper m1 = MapHelper.getInstance();
        m1.setGameScreen((GameScreen) b1.getScreenController().getScreen(ScreenController.ScreenID.GAME));

        Rectangle r1 = new Rectangle(0.0f,0.0f,42.50f,20.00f);
        Cook c1 = new Cook(r1.getWidth(), r1.getHeight() , BodyHelper.createBody(r1.x,r1.y,r1.width,r1.height, false, ((GameScreen) b1.getScreenController().getScreen(ScreenController.ScreenID.GAME)).getWorld()));

        ServingStation ss1 = new ServingStation(new Rectangle(100,100,100,100), false,(GameScreen) b1.getScreenController().getScreen(ScreenController.ScreenID.GAME));
        Customer cust1 = new Customer(null);
        ((GameScreen) b1.getScreenController().getScreen(ScreenController.ScreenID.GAME)).getCustomerController().addCustomerManual(cust1);
        cust1.setRequestName("Onion Tomato Salad");
        c1.foodStack.addStack(FoodItem.FoodID.onionChop);
        c1.foodStack.addStack(FoodItem.FoodID.tomatoChop);
        ss1.setCustomer(cust1);
        ss1.interact(c1, InputKey.InputTypes.USE);
        assertFalse("This test asserts that the customer leaves upon getting the correct recipe",
                ss1.hasCustomer());
    }

    @Test
    public void testGetCusPos(){
        Boot b1 = Boot.getInstance();
        b1.createHeadless();

        MapHelper m1 = MapHelper.getInstance();
        m1.setGameScreen((GameScreen) b1.getScreenController().getScreen(ScreenController.ScreenID.GAME));

        Rectangle r1 = new Rectangle(0.0f,0.0f,42.50f,20.00f);
        Cook c1 = new Cook(r1.getWidth(), r1.getHeight() , BodyHelper.createBody(r1.x,r1.y,r1.width,r1.height, false, ((GameScreen) b1.getScreenController().getScreen(ScreenController.ScreenID.GAME)).getWorld()));
        Customer cust1 = new Customer(null, 0, new Vector2(0,0));
        ServingStation ss1 = new ServingStation(new Rectangle(100,100,100,100), false,(GameScreen) b1.getScreenController().getScreen(ScreenController.ScreenID.GAME));
        assertEquals("This test asserts that the correct x coordinate is returned", 132F, ss1.getCustomerX(), 0.0001);
        assertEquals("This test asserts that the correct y coordinate is returned",196F, ss1.getCustomerY(), 0.0001);
        assertEquals("This test asserts that the correct x coordinate is returned", 132F, ss1.getX(), 0.0001);
        assertEquals("This test asserts that the correct y coordinate is returned",196F, ss1.getY(), 0.0001);
        ss1.setCustomer(cust1);
        assertEquals("This test asserts that the correct x coordinate is returned", 0F, ss1.getX(), 0.0001);
        assertEquals("This test asserts that the correct y coordinate is returned",0F, ss1.getY(), 0.0001);





    }
}
