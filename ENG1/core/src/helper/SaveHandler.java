package helper;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import cooks.Cook;
import cooks.GameEntity;
import game.GameScreen;

public class SaveHandler {
    public static Array<String> saveFileNames;
    public GameScreen gameScreen;
    public SaveHandler(GameScreen gs){
        this.gameScreen = gs;
    }
    public void saveToFile(String fileName){
        String tilemap = Constants.mapPath;
        Array<Cook> cks = gameScreen.getCooks();
        Array<GameEntity> ents = gameScreen.getGameEntities();
        long smallTimeDiff = TimeUtils.millis() - gameScreen.getPreviousSecond();
        int totalSecondsPassed = gameScreen.getTime();
        int repPointsLeft = gameScreen.getReputation();
        int money = gameScreen.getMoney();
        int amountCustomersServed = gameScreen.getCustomerController().getCustomersServed();
        int amountCustomersLeft = gameScreen.getCustomerController().getCustomersLeft();

    }
    public void loadFromFile(){
        this.gameScreen.restoreFromSaveFile();
    }

}
