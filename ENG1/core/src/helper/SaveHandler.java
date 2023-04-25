package helper;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import cooks.Cook;
import cooks.GameEntity;
import food.FoodItem;
import food.FoodStack;
import game.GameScreen;
import stations.CookInteractable;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class SaveHandler {
    public static Array<String> saveFileNames;
    public GameScreen gameScreen;
    public SaveHandler(GameScreen gs){
        this.gameScreen = gs;
    }
    public void saveToFile(String fileName) throws IOException {
        String localpath = Gdx.files.getLocalStoragePath();
        String totalpath = localpath + fileName;
        String tilemap = Constants.mapPath;
        Array<Cook> cks = gameScreen.getCooks();
        Array<GameEntity> ents = gameScreen.getGameEntities();
        Array<CookInteractable> ints = gameScreen.getInteractables();


        int[] cookIDs = new int[cks.size];
        Cook.Facing[] ckfacings = new Cook.Facing[cks.size];
        ArrayList<ArrayList<FoodItem.FoodID>> foodstacks = new ArrayList<>();
        float[] cookPositions = new float[cks.size * 2];

        int ctr = 0;
        for (Cook ck: cks){
            cookIDs[ctr] = ck.getCookID();
            ckfacings[ctr] = ck.getDir();
            ArrayList<FoodItem.FoodID> minilist = ck.foodStack.toArrayList();
            foodstacks.add(minilist);
            cookPositions[ctr*2] = ck.getBody().getPosition().x;
            cookPositions[ctr*2 + 1] = ck.getBody().getPosition().y;
            ctr ++;
        }

        long smallTimeDiff = TimeUtils.millis() - gameScreen.getPreviousSecond();
        int totalSecondsPassed = gameScreen.getTime();
        int repPointsLeft = gameScreen.getReputation();
        int money = gameScreen.getMoney();
        int amountCustomersServed = gameScreen.getCustomerController().getCustomersServed();
        int amountCustomersLeft = gameScreen.getCustomerController().getCustomersLeft();
        try (FileOutputStream fos = new FileOutputStream(totalpath)){
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(tilemap);
            oos.writeObject(smallTimeDiff);
            oos.writeObject(totalSecondsPassed);
            oos.writeObject(repPointsLeft);
            oos.writeObject(money);
            oos.writeObject(amountCustomersServed);
            oos.writeObject(amountCustomersLeft);

            oos.writeObject(cookIDs);
            oos.writeObject(ckfacings);
            oos.writeObject(foodstacks);
            oos.writeObject(cookPositions);


            oos.flush();
            oos.close();
        }



    }
    public void loadFromFile(String filename) throws IOException, ClassNotFoundException{
        FileInputStream fis = new FileInputStream(Gdx.files.getLocalStoragePath() + filename);
        ObjectInputStream ois = new ObjectInputStream(fis);

        String tilemap = (String) ois.readObject();
        long smallTimeDiff = (long) ois.readObject();
        int totalSecondsPassed = (int) ois.readObject();
        int repPointsLeft = (int) ois.readObject();
        int money = (int) ois.readObject();
        int amountCustomersServed = (int) ois.readObject();
        int amountCustomersLeft = (int) ois.readObject();




        int[] cookIDS = (int[]) ois.readObject();
        Cook.Facing[] cookFacings = (Cook.Facing[]) ois.readObject();
        ArrayList<?> foodstacks = (ArrayList<?>) ois.readObject();
        float[] cookPositions = (float[]) ois.readObject();
        System.out.println(Arrays.toString(cookIDS));
        System.out.println(Arrays.toString(cookFacings));
        System.out.println(Arrays.toString(cookPositions));
        for (Object foodstack : foodstacks) {
            ArrayList<?> minilist = (ArrayList<?>) foodstack;
            System.out.println(minilist);
        }
        ois.close();
        this.gameScreen.restoreFromData(smallTimeDiff, totalSecondsPassed, repPointsLeft,
                money, amountCustomersServed, amountCustomersLeft,
                cookPositions, foodstacks, cookFacings);
    }

}
