package testPackage;

import game.GameSprites;
import helper.Constants;

import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import org.junit.Test;

import customers.Customer;

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
}
