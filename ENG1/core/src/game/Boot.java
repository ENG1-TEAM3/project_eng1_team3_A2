package game;

// import com.badlogic.gdx.ApplicationAdapter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import helper.Constants;

/**
 * Responsible for starting the game. The singleton used to initialize all game
 * elements.
 */
public class Boot extends Game {

	/** The Boot Singleton Instance. */
	public static Boot INSTANCE;
	// The screen width and height.

	// private int widthScreen, heightScreen;

	/** The camera for the game. */
	private OrthographicCamera uiCamera;
	private OrthographicCamera gameCamera;

	/** Boot Singleton Constructor */
	private Boot() {
		INSTANCE = this;
	}

	/**
	 * Returns the Singleton Boot instance
	 * 
	 * @return The Boot INSTANCE
	 */
	public static Boot getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new Boot();
		}
		return INSTANCE;
	}

	/** The Viewport for the game. */
	private Viewport uiPort;
	private Viewport gamePort;
	/** The spriteBatch for the game. */
	private SpriteBatch spriteBatch;
	/** The shapeRenderer for the game. */
	private ShapeRenderer shapeRenderer;
	/** The screenController used by the game. */
	private ScreenController screenController;

	@Override
	public void create() {
        System.out.println(Gdx.files.getLocalStoragePath() + "egg.txt");
		// this.widthScreen = Gdx.graphics.getWidth();
		// this.heightScreen = Gdx.graphics.getHeight();
		this.uiCamera = new OrthographicCamera();
		this.gameCamera = new OrthographicCamera();
		this.spriteBatch = new SpriteBatch();
		this.shapeRenderer = new ShapeRenderer();
		this.shapeRenderer.setAutoShapeType(true);
		this.gamePort = new FitViewport(Constants.V_Width, Constants.V_Height, gameCamera);
		this.uiPort = new FitViewport(Constants.V_Width, Constants.V_Height, uiCamera);
		this.screenController = new ScreenController(this, gameCamera, uiCamera);
		this.screenController.setScreen(ScreenController.ScreenID.MENU);
	}

	/**
	 * Set up the boot instance in headless mode. This is used for testing.
	 */
	public void createHeadless() { // AS2 NEW CHANGE - this method allows for a game to be created headlessly for
									// testing.
		this.uiCamera = new OrthographicCamera();
		this.gameCamera = new OrthographicCamera();
		this.spriteBatch = null;
		this.shapeRenderer = null;
		this.uiPort = new FitViewport(Constants.V_Width, Constants.V_Height, uiCamera);
		this.gamePort = new FitViewport(Constants.V_Width, Constants.V_Height, gameCamera);
		this.screenController = new ScreenController(this, gameCamera, uiCamera);
		this.screenController.setScreen(ScreenController.ScreenID.MENU);
	}

	public void resize(int width, int height) {
		gamePort.update(width, height);
	}

	/**
	 * The {@link SpriteBatch} getter
	 * 
	 * @return {@link SpriteBatch} : The {@link SpriteBatch} for the game.
	 */
	public SpriteBatch getSpriteBatch() {
		return spriteBatch;
	}

	/**
	 * The {@link ShapeRenderer} getter.
	 * 
	 * @return {@link ShapeRenderer} : The {@link ShapeRenderer} for the game.
	 */
	public ShapeRenderer getShapeRenderer() {
		return shapeRenderer;
	}

	/**
	 * The {@link ScreenController} getter.
	 * 
	 * @return {@link ScreenController} : The {@link ScreenController} for the game.
	 */
	public ScreenController getScreenController() {
		return screenController;
	}

}
