package testPackage;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import cooks.Cook;
import game.*;
import helper.BodyHelper;
import helper.Constants;

import helper.MapHelper;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import org.junit.Test;

import customers.Customer;
import stations.ServingStation;

@RunWith(GdxTestRunner.class)
public class CustomerTests {
    @Test
    public void testCustomerDefaultPositioning(){
        Customer cust1 = new Customer(GameSprites.getInstance().getSprite(GameSprites.SpriteID.CUSTOMER, "0"));
        assertEquals("This test asserts that the customer x position is set to the default when none specified",
                Constants.customerSpawn.x, cust1.getX(), 0.0001f);
        assertEquals("This test asserts that the customer y position is set to the default when none specified",
                Constants.customerSpawn.y, cust1.getY(), 0.0001f);
    }

    @Test
    public void testCustomerTimings(){
        Customer cust1 = new Customer(GameSprites.getInstance().getSprite(GameSprites.SpriteID.CUSTOMER, "0"));
        cust1.setTimings(0,45);
        assertEquals("This test asserts that timings are set correctly ",45, cust1.getDeadTime() );
        assertEquals("This test asserts that timings are set correctly ",0, cust1.getSpawnTime());
    }

    @Test
    public void testCustomerIndex(){
        Customer cust1 = new Customer(null, 1, new Vector2(0,0));
        assertEquals("This test asserts that the index is set correctly ", 1, cust1.getStationIndex());

    }
    @Test
    public void testRestoreCustomer(){
        Boot b1 = Boot.getInstance();
        b1.createHeadless();

        GameScreen gs1 = (GameScreen) b1.getScreenController().getScreen(ScreenController.ScreenID.GAME);

        ServingStation ss1 = new ServingStation(new Rectangle(100,100,100,100), false,gs1);
        gs1.addInteractable(ss1);

        gs1.getCustomerController().addServingStation(ss1);
        gs1.getCustomerController().restoreCustomerFromSave(0,45,90,"egg");

        Customer newCust = gs1.getCustomerController().getCustomers().get(0);
        assertEquals("This test asserts that the restored customer has the correct time saved", 45,newCust.getSpawnTime());
        assertEquals("This test asserts that the restored customer has the correct time saved", 90,newCust.getDeadTime());
    }

    @Test
    public void testAddRemoveCustomer(){
        Boot b1 = Boot.getInstance();
        b1.createHeadless();

        GameScreen gs1 = (GameScreen) b1.getScreenController().getScreen(ScreenController.ScreenID.GAME);

        ServingStation ss1 = new ServingStation(new Rectangle(100,100,100,100), false,gs1);
        gs1.addInteractable(ss1);

        assertTrue(gs1.getCustomerController().canAddCustomer());
        assertEquals(0, gs1.getCustomerController().getCustomers().size);
        gs1.getCustomerController().addCustomer(21);
        assertEquals(1, gs1.getCustomerController().getCustomers().size);
        assertEquals(0, gs1.getCustomerController().getCustomers().get(0).getSpawnTime());
        assertEquals(21, gs1.getCustomerController().getCustomers().get(0).getDeadTime());
        gs1.setTiming(22);
        gs1.getCustomerController().removeCustomerIfExpired(gs1.getCustomerController().getCustomers().get(0));
        assertEquals(0, gs1.getCustomerController().getCustomers().size);
        assertEquals(2, gs1.getReputation());

        gs1.getCustomerController().tryToSpawnCustomer(MenuScreen.difficulty.EASY, MenuScreen.mode.SCENARIO);
        assertEquals(0, gs1.getCustomerController().getCustomers().size);
        gs1.setTiming(70);
        gs1.getCustomerController().tryToSpawnCustomer(MenuScreen.difficulty.EASY, MenuScreen.mode.SCENARIO);
        assertEquals(1, gs1.getCustomerController().getCustomers().size);


    }

}
