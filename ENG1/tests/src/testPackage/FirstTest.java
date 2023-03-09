package testPackage;

import com.badlogic.gdx.Gdx;
import cooks.Cook;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class FirstTest {
    @Test
    public void firstTest(){
        assertTrue("This test checks whether a file exists", Gdx.files.internal("cooks/control.png").exists());
    }
    @Test
    public void secondTest(){
        assertTrue("This test checks whether a second file exists", Gdx.files.internal("cooks/hold left.png").exists());
    }
    @Test
    public void thirdTest(){
        assertEquals(Cook.testTest(1), 2);
    }
}
