package helper;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;

/**
 * A class for variables that remain Constant
 * that are needed all across the code.
 */
public class Constants {

    /** Pixels Per Metre. */
    public static final float PPM = 32.0f;
    /** The ViewPort / Window Width. */
    public static final int V_Width = 1920;
    /** The ViewPort / Window Height. */
    public static final int V_Height = 1080;

    public static final String mapPath = "Maps/StationsMap.tmx";

    /** The {@link customers.Customer} default spawn position */

    private static TiledMapTileLayer tl = ((TiledMapTileLayer) (new TmxMapLoader().load(mapPath).getLayers().get(0)));
    public static final int mapwidth = tl.getTileWidth() * tl.getWidth();
    public static final int mapheight = tl.getTileHeight() * tl.getHeight();

    public static final Vector2 customerSpawn= new Vector2(425,470);
    public static final Vector2 gameCameraOffset= new Vector2(mapwidth/2.0f,mapheight/2.0f + Constants.V_Height/8.0f);

}
