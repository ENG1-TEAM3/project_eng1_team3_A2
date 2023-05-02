package testPackage;
import com.badlogic.gdx.Gdx;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;


@RunWith(GdxTestRunner.class)
public class AssetExistenceTests {
    @Test
    public void testForAtlasSubfolderFilesExistence(){
        assertTrue("This test asserts that cook.atlas exists",
                Gdx.files.internal("atlas/cook.atlas").exists());

        assertTrue("This test asserts that cook.png exists",
                Gdx.files.internal("atlas/cook.png").exists());

        assertTrue("This test asserts that customer.atlas exists",
                Gdx.files.internal("atlas/customer.atlas").exists());

        assertTrue("This test asserts that customer.png exists",
                Gdx.files.internal("atlas/customer.png").exists());

        assertTrue("This test asserts that food.atlas exists",
                Gdx.files.internal("atlas/food.atlas").exists());

        assertTrue("This test asserts that food.png exists",
                Gdx.files.internal("atlas/food.png").exists());

        assertTrue("This test asserts that station.atlas exists",
                Gdx.files.internal("atlas/station.atlas").exists());

        assertTrue("This test asserts that station.png exists",
                Gdx.files.internal("atlas/station.png").exists());
    }

    @Test
    public void testForCooksSubfolderFilesExistence(){
        assertTrue("This test asserts that control.png exists",
                Gdx.files.internal("cooks/control.png").exists());

        assertTrue("This test asserts that cook atlas.tpproj exists",
                Gdx.files.internal("cooks/cook atlas.tpproj").exists());

        assertTrue("This test asserts that hold down.png exists",
                Gdx.files.internal("cooks/hold down.png").exists());

        assertTrue("This test asserts that hold left.png exists",
                Gdx.files.internal("cooks/hold left.png").exists());

        assertTrue("This test asserts that hold right.png exists",
                Gdx.files.internal("cooks/hold right.png").exists());

        assertTrue("This test asserts that hold up.png exists",
                Gdx.files.internal("cooks/hold up.png").exists());

        assertTrue("This test asserts that normal down.png exists",
                Gdx.files.internal("cooks/normal down.png").exists());

        assertTrue("This test asserts that normal left.png exists",
                Gdx.files.internal("cooks/normal left.png").exists());

        assertTrue("This test asserts that normal right.png exists",
                Gdx.files.internal("cooks/normal right.png").exists());

        assertTrue("This test asserts that normal up.png exists",
                Gdx.files.internal("cooks/normal up.png").exists());
    }

    @Test
    public void testForCustomersSubfolderFilesExistence(){
        assertTrue("This test asserts that Customer.png exists",
                Gdx.files.internal("customers/Customer.png").exists());

        assertTrue("This test asserts that customer.tpproj exists",
                Gdx.files.internal("customers/customer.tpproj").exists());
    }

    @Test
    public void testForFoodsSubfolderFilesExistence(){
        assertTrue("This test asserts that BottomBun.png exists",
                Gdx.files.internal("foods/BottomBun.png").exists());

        assertTrue("This test asserts that Burger.png exists",
                Gdx.files.internal("foods/Burger.png").exists());

        assertTrue("This test asserts that Burger_Buns.png exists",
                Gdx.files.internal("foods/Burger_Buns.png").exists());

        assertTrue("This test asserts that Burger_BunsUpscaled.png exists",
                Gdx.files.internal("foods/Burger_BunsUpscaled.png").exists());

        assertTrue("This test asserts that foods atlas.tpproj exists",
                Gdx.files.internal("foods/foods atlas.tpproj").exists());

        assertTrue("This test asserts that Lettuce.png exists",
                Gdx.files.internal("foods/Lettuce.png").exists());

        assertTrue("This test asserts that LettuceChop.png exists",
                Gdx.files.internal("foods/LettuceChop.png").exists());

        assertTrue("This test asserts that LettuceUpscaled.png exists",
                Gdx.files.internal("foods/LettuceUpscaled.png").exists());

        assertTrue("This test asserts that MargheritaPizza.png exists",
                Gdx.files.internal("foods/MargheritaPizza.png").exists());

        assertTrue("This test asserts that Meat.png exists",
                Gdx.files.internal("foods/Meat.png").exists());

        assertTrue("This test asserts that MeatFried.png exists",
                Gdx.files.internal("foods/MeatFried.png").exists());

        assertTrue("This test asserts that Onion.png exists",
                Gdx.files.internal("foods/Onion.png").exists());

        assertTrue("This test asserts that OnionChop.png exists",
                Gdx.files.internal("foods/OnionChop.png").exists());

        assertTrue("This test asserts that OnionUpscaled.png exists",
                Gdx.files.internal("foods/OnionUpscaled.png").exists());

        assertTrue("This test asserts that Tomato.png exists",
                Gdx.files.internal("foods/Tomato.png").exists());

        assertTrue("This test asserts that TomatoChop.png exists",
                Gdx.files.internal("foods/TomatoChop.png").exists());

        assertTrue("This test asserts that TomatoUpscaled.png exists",
                Gdx.files.internal("foods/TomatoUpscaled.png").exists());

        assertTrue("This test asserts that TopBun.png exists",
                Gdx.files.internal("foods/TopBun.png").exists());
    }

    @Test
    public void testForIndividualStationSubfolderFilesExistence(){
        assertTrue("This test asserts that Bin.png exists",
                Gdx.files.internal("Individual_Stations/Bin.png").exists());

        assertTrue("This test asserts that Cutting_Station.png exists",
                Gdx.files.internal("Individual_Stations/Cutting_Station.png").exists());

        assertTrue("This test asserts that Fryer.png exists",
                Gdx.files.internal("Individual_Stations/Fryer.png").exists());

        assertTrue("This test asserts that stations.tpproj exists",
                Gdx.files.internal("Individual_Stations/stations.tpproj").exists());

        assertTrue("This test asserts that Table.png exists",
                Gdx.files.internal("Individual_Stations/Table.png").exists());

        assertTrue("This test asserts that TableWithChairs.png exists",
                Gdx.files.internal("Individual_Stations/TableWithChairs.png").exists());
    }

    @Test
    public void testForMapsSubfolderFilesExistence(){
        assertTrue("This test asserts that StartMenuBackground.png exists",
                Gdx.files.internal("Maps/StartMenuBackground.png").exists());

        assertTrue("This test asserts that Stations.png exists",
                Gdx.files.internal("Maps/Stations.png").exists());

        assertTrue("This test asserts that Stations.tsx exists",
                Gdx.files.internal("Maps/Stations.tsx").exists());

        assertTrue("This test asserts that Stations.xcf exists",
                Gdx.files.internal("Maps/Stations.xcf").exists());

        assertTrue("This test asserts that StationsMap.png exists",
                Gdx.files.internal("Maps/StationsMap.png").exists());

        assertTrue("This test asserts that StationsMap.tmx exists",
                Gdx.files.internal("Maps/StationsMap.tmx").exists());

        assertTrue("This test asserts that StationsMap1.tmx exists",
                Gdx.files.internal("Maps/StationsMap1.tmx").exists());

        assertTrue("This test asserts that StationsMap2.tmx exists",
                Gdx.files.internal("Maps/StationsMap2.tmx").exists());
    }

    @Test
    public void testForPowerupsSubfolderFilesExistence(){
        assertTrue("This test asserts that auto_station.png exists",
                Gdx.files.internal("powerups/auto_station.png").exists());

        assertTrue("This test asserts that bonus_time.png exists",
                Gdx.files.internal("powerups/bonus_time.png").exists());

        assertTrue("This test asserts that double_money.png exists",
                Gdx.files.internal("powerups/double_money.png").exists());

        assertTrue("This test asserts that faster_cooks.png exists",
                Gdx.files.internal("powerups/faster_cooks.png").exists());

        assertTrue("This test asserts that satisfied_customer.png exists",
                Gdx.files.internal("powerups/satisfied_customer.png").exists());

    }
    @Test
    public void testForSpritesSubfolderFilesExistence(){
        assertTrue("This test asserts that Burger.xcf exists",
                Gdx.files.internal("Sprites/xcfs/Burger.xcf").exists());

        assertTrue("This test asserts that Burger_Buns.xcf exists",
                Gdx.files.internal("Sprites/xcfs/Burger_Buns.xcf").exists());

        assertTrue("This test asserts that Cutting_Station.xcf exists",
                Gdx.files.internal("Sprites/xcfs/Cutting_Station.xcf").exists());

        assertTrue("This test asserts that Fryer.xcf exists",
                Gdx.files.internal("Sprites/xcfs/Fryer.xcf").exists());

        assertTrue("This test asserts that Lettuce.xcf exists",
                Gdx.files.internal("Sprites/xcfs/Lettuce.xcf").exists());

        assertTrue("This test asserts that MargheritaPizza.xcf exists",
                Gdx.files.internal("Sprites/xcfs/MargheritaPizza.xcf").exists());

        assertTrue("This test asserts that Meat.xcf exists",
                Gdx.files.internal("Sprites/xcfs/Meat.xcf").exists());

        assertTrue("This test asserts that Onion.xcf exists",
                Gdx.files.internal("Sprites/xcfs/Onion.xcf").exists());

        assertTrue("This test asserts that Table.xcf exists",
                Gdx.files.internal("Sprites/xcfs/Table.xcf").exists());

        assertTrue("This test asserts that Tomato.xcf exists",
                Gdx.files.internal("Sprites/xcfs/Tomato.xcf").exists());
    }
    @Test
    public void testForMiscFilesExistence(){
        assertTrue("This test asserts that badlogic.jpg exists",
                Gdx.files.internal("badlogic.jpg").exists());
    }
}
