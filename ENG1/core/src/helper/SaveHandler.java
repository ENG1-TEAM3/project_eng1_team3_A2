package helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import cooks.Cook;
import cooks.GameEntity;
import customers.Customer;
import food.FoodItem;
import game.GameScreen;
import game.MenuScreen;
import interactions.Interactions;
import powerups.PowerUp;
import powerups.PowerUpHandler;
import stations.CookInteractable;
import stations.CounterStation;
import stations.PreparationStation;
import stations.Station;

import java.io.*;
import java.util.ArrayList;


/**
 * A class to handle loading and saving the game
 */
public class SaveHandler {
    /** The gameScreen to save and load to*/
    public GameScreen gameScreen;

    /**
     * The SaveHandler Constructor
     * @param gs The gameScreen to save and load to
     */
    public SaveHandler(GameScreen gs){
        this.gameScreen = gs;
    }

    /**
     * Save the game to a file
     * @param fileName - Should be "save1.txt" or "save2.txt" or "save3.txt"
     * @param timeOfPause - The time in ms when the game was paused last
     * @throws IOException Something goes wrong with the file system
     */
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
        boolean[] autocks = new boolean[ints.size];
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
                autocks[i] = ((PreparationStation) ints.get(i)).hasAutoCook();
                if (((PreparationStation) ints.get(i)).hasAutoCook()){
                    System.out.println("one autocook found");
                }
            }
        }

        int powerUpCooldown = gameScreen.getPowerUpHandler().getCooldown();
        PowerUp activePower = PowerUpHandler.activePowerUp();
        PowerUp[] curPowerUps = gameScreen.getPowerUpHandler().getCurrentPowerUps();


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
            oos.writeObject(autocks);

            oos.writeObject(powerUpCooldown);
            oos.writeObject(activePower);
            oos.writeObject(curPowerUps);

            oos.flush();
            oos.close();
        }

    }

    /**
     * Load the game into the gameScreen this class stores
     * @param filename The filename - Should be one of the options specified in the filename of the saveToFile method description
     * @throws IOException - If something in the file system goes wrong (e.g. file does not exist)
     * @throws ClassNotFoundException - If a class cannot be read from the file correctly
     */
    public void loadFromFile(String filename) throws IOException, ClassNotFoundException{
        FileInputStream fis = new FileInputStream(Gdx.files.getLocalStoragePath() + filename);
        ObjectInputStream ois = new ObjectInputStream(fis);

        String tilemap = (String) ois.readObject();
        long smallTimeDiff = (long) ois.readObject();
        int totalSecondsPassed = (int) ois.readObject();
        int repPointsLeft = (int) ois.readObject();
        System.out.println("Reading rep: "+repPointsLeft);
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
        boolean[] autoCooks = (boolean[]) ois.readObject();

        int cldown = (int) ois.readObject();
        PowerUp activePowerup = (PowerUp) ois.readObject();
        PowerUp[] currentPowerups = (PowerUp[]) ois.readObject();


        ois.close();
        this.gameScreen.restoreFromData(smallTimeDiff, totalSecondsPassed, repPointsLeft, money,
                amountCustomersServed, amountCustomersLeft, amountCustomersTot,
                currentDiff, currentMd,
                cookPositions, foodstacks, cookFacings, counterStationFoodStacks,
                lastCustomerSpawnTime,
                custOrder,custIndices,custSpawnTimes,custDeadTimes,
                lockdstats, autoCooks,
                inters, progresses, burnprogresses, stepnums, foods,
                cldown, activePowerup, currentPowerups);
    }

}
