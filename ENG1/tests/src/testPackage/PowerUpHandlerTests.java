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
import powerups.PowerUp;
import powerups.PowerUpHandler;
import stations.Pantry;
import stations.PreparationStation;
import stations.Station;

import java.io.IOException;

@RunWith(GdxTestRunner.class)
public class PowerUpHandlerTests {
    @Test
    public void testPowerUpActivation(){
        Boot b1 = Boot.getInstance();
        b1.createHeadless();

        MapHelper m1 = MapHelper.getInstance();
        GameScreen gs1 = (GameScreen) b1.getScreenController().getScreen(ScreenController.ScreenID.GAME);
        m1.setGameScreen(gs1);

        Rectangle r1 = new Rectangle(0.0f,0.0f,42.50f,20.00f);
        Cook c1 = new Cook(r1.getWidth(), r1.getHeight() , BodyHelper.createBody(r1.x,r1.y,r1.width,r1.height, false, ((GameScreen) b1.getScreenController().getScreen(ScreenController.ScreenID.GAME)).getWorld()));

        assertNull("This test asserts that no powerUp is stored at the start of the game", gs1.getPowerUpHandler().getCurrentPowerUp(0));
        assertNull("This test asserts that no powerUp is stored at the start of the game", PowerUpHandler.activePowerUp());
        assertNull("This test asserts that a non existent powerUp cannot be activated", gs1.getPowerUpHandler().activatePower(0));

        gs1.getPowerUpHandler().addPowerUp(true);

        assertNotNull("This test asserts that a powerUp is stored once addPowerUp is called", gs1.getPowerUpHandler().getCurrentPowerUp(0));

        gs1.getPowerUpHandler().activatePower(0);

        assertNull("This test asserts that a powerUp is moved from the current to active slot when activated",
                gs1.getPowerUpHandler().getCurrentPowerUp(0));
        assertNotNull("This test asserts that a powerUp is moved from the current to active slot when activated",
                PowerUpHandler.activePowerUp());
        assertNull("This test asserts that null is returned when trying to activate a powerUp slot that does not exist",
                gs1.getPowerUpHandler().activatePower(100));

    }

    @Test
    public void testPowerUpUsage(){
        Boot b1 = Boot.getInstance();
        b1.createHeadless();

        MapHelper m1 = MapHelper.getInstance();
        GameScreen gs1 = (GameScreen) b1.getScreenController().getScreen(ScreenController.ScreenID.GAME);
        m1.setGameScreen(gs1);

        Rectangle r1 = new Rectangle(0.0f,0.0f,42.50f,20.00f);
        Cook c1 = new Cook(r1.getWidth(), r1.getHeight() , BodyHelper.createBody(r1.x,r1.y,r1.width,r1.height, false, ((GameScreen) b1.getScreenController().getScreen(ScreenController.ScreenID.GAME)).getWorld()));

        assertFalse("This test asserts that a non existent powerup cannot be used",
                PowerUpHandler.usePowerUp());

        gs1.getPowerUpHandler().addSpecificPowerUp(PowerUp.SATISFIED_CUSTOMER, true);
        gs1.getPowerUpHandler().activatePower(0);
        assertEquals("This test asserts that the satisfied customer powerup has one usage",1, gs1.getPowerUpHandler().getCooldown());
        assertFalse("This test asserts that the cooldown should not be updated automatically on this powerup", gs1.getPowerUpHandler().updateCoolDown(10));
        assertTrue("This test asserts that the powerUp is used correctly",PowerUpHandler.usePowerUp());
        assertEquals("This test asserts that the uses left drops to zero after usage",0, gs1.getPowerUpHandler().getCooldown());
        assertNull("This test asserts that the active powerup becomes null when all usages used",
                PowerUpHandler.activePowerUp());


        gs1.getPowerUpHandler().addSpecificPowerUp(PowerUp.AUTO_STATION, true);
        gs1.getPowerUpHandler().activatePower(0);
        assertEquals("This test asserts that the autoStation powerUp has 1000 'usages'",1000, gs1.getPowerUpHandler().getCooldown());
        gs1.getPowerUpHandler().updateCoolDown(20);
        assertEquals("This test asserts that the usages drop by the delta provided", 980, gs1.getPowerUpHandler().getCooldown());
        gs1.getPowerUpHandler().updateCoolDown(990);
        assertEquals("This test asserts that the usages drop by the delta provided", 0, gs1.getPowerUpHandler().getCooldown());
        assertNull("This test asserts that the active powerUp becomes null after it is all used up", PowerUpHandler.activePowerUp());


    }
}
