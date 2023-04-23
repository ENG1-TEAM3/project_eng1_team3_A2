package helper;

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

    /** The {@link customers.Customer} default spawn position */
    public static final Vector2 customerSpawn= new Vector2(425,470);
    public static final Vector2 gameCameraOffset= new Vector2(Constants.V_Width/4.0f,Constants.V_Height/4.0f + Constants.V_Height/8.0f);
    /** The location that the {@link food.Recipe} being checked is rendered. */
    public static final float RECIPE_X = (4f/5f) * (float) V_Width , RECIPE_Y = (6f/8f) * (float) V_Height;
}
