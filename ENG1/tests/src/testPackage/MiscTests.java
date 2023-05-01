package testPackage;

import com.badlogic.gdx.Gdx;
import cooks.Cook;
import helper.Util;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class MiscTests {
    @Test
    public void testUtils(){

        assertEquals("This test asserts that the distance between two points is calculated correctly",
                5.0, Util.distancePoints(0,3,4,0), 0.001);


        assertEquals("This test asserts that the timestring is constructed properly",
                "4:2:13", Util.formatTime(4,2,13));
    }
}
