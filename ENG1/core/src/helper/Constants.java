package helper;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;

/**
 * A class for variables that remain Constant that are needed all across the
 * code.
 */
public class Constants {

	/** Pixels Per Metre. */
	public static final float PPM = 32.0f;
	/** The ViewPort / Window Width. */
	public static final int V_Width = 1920;
	/** The ViewPort / Window Height. */
	public static final int V_Height = 1080;

    public static final int POWERUP_COST = 200;

    public static final int STAFF_COST = 1000;

    public static final int MONEY_START = 2000;
	public static final String mapPath = "Maps/StationsMap3.tmx";

	/** Layer used to retrieve the map width and height in pixels */
	private static TiledMapTileLayer tl = ((TiledMapTileLayer) (new TmxMapLoader().load(mapPath).getLayers().get(0)));
	/** Map width in pixels */
	public static final int mapwidth = tl.getTileWidth() * tl.getWidth();
	/** Map height in pixels */
	public static final int mapheight = tl.getTileHeight() * tl.getHeight();

	/** The {@link customers.Customer} default spawn position */
	public static final Vector2 customerSpawn = new Vector2(425, 470);
	/**
	 * The amount to move the map camera by, given the fact the map will by default
	 * render with its bottom left corner in the center of the screen
	 */
	public static final Vector2 gameCameraOffset = new Vector2(mapwidth / 2.0f,
			mapheight / 2.0f + Constants.V_Height / 8.0f);

}
