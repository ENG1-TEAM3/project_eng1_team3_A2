package testPackage;

import com.badlogic.gdx.Gdx;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class FirstTest {

    @Test
    public void firstTest(){
        System.out.println(Gdx.files.internal("cooks/control.png").exists());
        assertTrue("This test checks whether a file exists", Gdx.files.internal("cooks/control.png").exists());
    }
}
