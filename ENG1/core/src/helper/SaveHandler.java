package helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import cooks.Cook;
import cooks.GameEntity;
import customers.Customer;
import food.FoodItem;
import game.GameScreen;
import game.MenuScreen;
import interactions.Interactions;
import stations.CookInteractable;
import stations.CounterStation;
import stations.PreparationStation;
import stations.Station;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class SaveHandler {
    public static Array<String> saveFileNames;
    public GameScreen gameScreen;
    public SaveHandler(GameScreen gs){
        this.gameScreen = gs;
    }
    public void saveToFile(String fileName, long timeOfPause) throws IOException {
        String localpath = Gdx.files.getLocalStoragePath();
        String totalpath = localpath + fileName;
        String tilemap = Constants.mapPath;
        Array<Cook> cks = gameScreen.getCooks();
        Array<GameEntity> ents = gameScreen.getGameEntities();
        Array<CookInteractable> ints = gameScreen.getInteractables();
        Array<Customer> custs = gameScreen.getCustomerController().getCustomers();

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

        long smallTimeDiff = timeOfPause - gameScreen.getPreviousSecond();
        int totalSecondsPassed = gameScreen.getTotalSecondsRunningGame();
        int repPointsLeft = gameScreen.getReputation();
        int money = gameScreen.getMoney();
        int amountCustomersServed = gameScreen.getCustomerController().getCustomersServed();
        int amountCustomersLeft = gameScreen.getCustomerController().getCustomersLeft();
        int amountCustomersTotal = gameScreen.getCustomerController().getTotalCustomersToServe();

        MenuScreen.difficulty currentDif = gameScreen.getCurrentDifficulty();
        MenuScreen.mode currentMod = gameScreen.getCurrentMode();

        ArrayList<ArrayList<FoodItem.FoodID>> stationFoodStacks = new ArrayList<>();
        System.out.println(ints.size);
        for(int i = 0; i < ints.size; i++){
            if (ints.get(i) instanceof CounterStation){
                stationFoodStacks.add(((CounterStation) ints.get(i)).getFoodStack().toArrayList());
            }
            else {
                stationFoodStacks.add(null);
            }
        }

        int lastCustomerSpawnTime = gameScreen.getCustomerController().getLastCustomerSpawnTime();
        String[] customerOrders = new String[custs.size];
        int[] customerIndices = new int[custs.size];
        int[] customerSpawnTimes = new int[custs.size];
        int[] customerDeadTimes = new int[custs.size];
        for(int i = 0; i < custs.size; i++){
            customerOrders[i] = custs.get(i).getRequestName();
            customerIndices[i] = custs.get(i).getStationIndex();
            customerSpawnTimes[i] = custs.get(i).getSpawnTime();
            customerDeadTimes[i] = custs.get(i).getDeadTime();
        }



        Interactions.InteractionResult[] inters = new Interactions.InteractionResult[ints.size];
        float[] progs = new float[ints.size];
        float[] burnprogs = new float[ints.size];
        int[] stps = new int[ints.size];
        FoodItem.FoodID[] foodItems = new FoodItem.FoodID[ints.size];

        boolean[] lockedStats = new boolean[ints.size];
        for (int i = 0; i < ints.size; i++){
            if (((Station) ints.get(i)).isLocked()) {
                lockedStats[i] = true;
            }
            else {
                lockedStats[i] = false;
            }
            if (ints.get(i) instanceof PreparationStation){
                if (((PreparationStation) ints.get(i)).isInUse()){
                    System.out.println("Saving one interaction at" +ints.get(i).getX()+" "+ints.get(i).getY());
                    inters[i] = ((PreparationStation) ints.get(i)).getInteraction();
                    progs[i] = ((PreparationStation) ints.get(i)).getProgress();
                    burnprogs[i] = ((PreparationStation) ints.get(i)).getBurnProgress();
                    stps[i] = ((PreparationStation) ints.get(i)).getStepNum();
                    foodItems[i] = ((PreparationStation) ints.get(i)).getFoodItem();
                }
            }
        }



        try (FileOutputStream fos = new FileOutputStream(totalpath)){
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(tilemap);
            oos.writeObject(smallTimeDiff);
            oos.writeObject(totalSecondsPassed);
            oos.writeObject(repPointsLeft);
            oos.writeObject(money);
            oos.writeObject(amountCustomersServed);
            oos.writeObject(amountCustomersLeft);
            oos.writeObject(amountCustomersTotal);

            oos.writeObject(currentDif);
            oos.writeObject(currentMod);

            oos.writeObject(cookIDs);
            oos.writeObject(ckfacings);
            oos.writeObject(foodstacks);
            oos.writeObject(cookPositions);

            oos.writeObject(stationFoodStacks);

            oos.writeObject(lastCustomerSpawnTime);
            oos.writeObject(customerOrders);
            oos.writeObject(customerIndices);
            oos.writeObject(customerSpawnTimes);
            oos.writeObject(customerDeadTimes);

            oos.writeObject(lockedStats);

            oos.writeObject(inters);
            oos.writeObject(progs);
            oos.writeObject(burnprogs);
            oos.writeObject(stps);
            oos.writeObject(foodItems);


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
        int amountCustomersTot = (int) ois.readObject();
        System.out.println("Loading seconds amount " + totalSecondsPassed);
        System.out.println("Loading smalltimedif " + smallTimeDiff);

        MenuScreen.difficulty currentDiff = (MenuScreen.difficulty) ois.readObject();
        MenuScreen.mode currentMd = (MenuScreen.mode) ois.readObject();

        int[] cookIDS = (int[]) ois.readObject();
        Cook.Facing[] cookFacings = (Cook.Facing[]) ois.readObject();
        ArrayList<?> foodstacks = (ArrayList<?>) ois.readObject();
        float[] cookPositions = (float[]) ois.readObject();
        for (Object foodstack : foodstacks) {
            ArrayList<?> minilist = (ArrayList<?>) foodstack;
            System.out.println(minilist);
        }

        ArrayList<?> counterStationFoodStacks = (ArrayList<?>) ois.readObject();

        int lastCustomerSpawnTime =(int) ois.readObject();
        String[] custOrder = (String[]) ois.readObject();
        int[] custIndices = (int[]) ois.readObject();
        int[] custSpawnTimes= (int[]) ois.readObject();
        int[] custDeadTimes = (int[]) ois.readObject();

        boolean[] lockdstats = (boolean[]) ois.readObject();

        Interactions.InteractionResult[] inters = (Interactions.InteractionResult[]) ois.readObject();
        float[] progresses = (float[]) ois.readObject();
        float[] burnprogresses = (float[]) ois.readObject();
        int[] stepnums = (int[]) ois.readObject();
        FoodItem.FoodID[] foods = (FoodItem.FoodID[]) ois.readObject();

        ois.close();
        this.gameScreen.restoreFromData(smallTimeDiff, totalSecondsPassed, repPointsLeft, money,
                amountCustomersServed, amountCustomersLeft, amountCustomersTot,
                currentDiff, currentMd,
                cookPositions, foodstacks, cookFacings, counterStationFoodStacks,
                lastCustomerSpawnTime,
                custOrder,custIndices,custSpawnTimes,custDeadTimes,
                lockdstats,
                inters, progresses, burnprogresses, stepnums, foods);
    }

}
