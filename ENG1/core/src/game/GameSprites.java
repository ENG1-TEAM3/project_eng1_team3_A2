package game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import java.util.HashMap;

/** A class which allows for easier controlling of how TextureAtlases are
 * loaded for the game, and so that they can easily be accessed.
 * Allows for avoiding using {@link TextureAtlas#createSprite(String)}
 * everywhere, which may result in problems if the Sprite isn't disposed. */
public class GameSprites {

    /**
     * An enum of the different Sprite IDs for each {@link TextureAtlas}.
     */
    public enum SpriteID {
        /** The {@link cooks.Cook}'s {@link TextureAtlas}.*/
        COOK,
        /** The {@link food.FoodItem}'s {@link TextureAtlas}.*/
        FOOD,
        /** The {@link stations.Station}'s and {@link stations.Pantry}'s {@link TextureAtlas}.*/
        STATION,
        /** The {@link customers.Customer}'s {@link TextureAtlas}.*/
        CUSTOMER,

        POWERUP

    }

    /**
     * A {@link HashMap} containing all of the {@link TextureAtlas}es for each of the
     * {@link SpriteID} IDs.
     */
    public static final HashMap<SpriteID, TextureAtlas> textureAtlases = new HashMap<>();
    static {
        textureAtlases.put(SpriteID.COOK, new TextureAtlas("atlas/cook.atlas"));
        textureAtlases.put(SpriteID.FOOD, new TextureAtlas("atlas/food.atlas"));
        textureAtlases.put(SpriteID.STATION, new TextureAtlas("atlas/station.atlas"));
        textureAtlases.put(SpriteID.CUSTOMER, new TextureAtlas("atlas/customer.atlas"));
    }

    /**
     * A {@link HashMap} of the {@link Sprite}s within the {@link TextureAtlas}es, allowing
     * each {@link Sprite} to be accessed via a simple {@link String}.
     * This is not static, or final, as it can be modified as needed during the game.
     */
    private final HashMap<String, Sprite> spriteMap;

    private static GameSprites INSTANCE;

    /** Private constructor to allow for a Singleton. */
    private GameSprites() {
        this.spriteMap = new HashMap<>();
        createResources();
    }

    /**
     * The getter function to get the {@link #INSTANCE} of the {@link GameSprites}.
     * @return {@link GameSprites}: The single {@link GameSprites} instance.
     */
    public static GameSprites getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GameSprites();
        }
        return INSTANCE;
    }

    /**
     * Puts together the {@link #spriteMap} using the {@link TextureAtlas}es
     * provided in the {@link #textureAtlases} {@link HashMap}.
     * It creates the {@link Sprite}s here, so they
     * only ever have to be created once.
     */
    public void createResources() {
        for (SpriteID spriteID : SpriteID.values()) {
            if (spriteID != SpriteID.POWERUP) {
                TextureAtlas thisAtlas = textureAtlases.get(spriteID);
                for (TextureAtlas.AtlasRegion spriteRegion : thisAtlas.getRegions()) {
                    spriteMap.put(spriteKey(spriteID, spriteRegion.name), thisAtlas.createSprite(spriteRegion.name));
                }
            }
        }
        createNonAtlasResources();
    }

    /**
     * A function to convert a {@link SpriteID} and {@link Sprite} name
     * into a {@link String} key for the {@link #spriteMap}.
     * @param spriteID The {@link SpriteID} of the {@link Sprite}.
     * @param spriteName The name of the {@link Sprite} as a {@link String}.
     * @return  {@link String}: The key to use for the {@link #spriteMap}.
     */
    public String spriteKey(SpriteID spriteID, String spriteName) {
        return String.format("%s-%s", spriteID.ordinal(), spriteName);
    }

    /**
     * Get a {@link Sprite} from the {@link #spriteMap}.
     * @param spriteID The {@link SpriteID} of the {@link Sprite}.
     * @param spriteName The name of the {@link Sprite} as a {@link String}.
     * @return  {@link Sprite}: The retrieved from the {@link #spriteMap}.
     */
    public Sprite getSprite(SpriteID spriteID, String spriteName) {
        return spriteMap.get(spriteKey(spriteID,spriteName));
    }

    /**
     * A function to dispose of unneeded resources to free up space.
     */
    public void dispose() {
        spriteMap.clear();
    }


    //Added for Assessment 2
    /**
     * Add resources to the spriteMap that are not contained in atlas files
     */
    public void createNonAtlasResources(){
        Texture ts0 = new Texture(Gdx.files.internal("foods/Beans_Canned.png"));
        Sprite ns0 = new Sprite(ts0);
        spriteMap.put(spriteKey(SpriteID.FOOD, "beans"), ns0);

        Texture ts1 = new Texture(Gdx.files.internal("foods/Beans_Cooked.png"));
        Sprite ns1 = new Sprite(ts1);
        spriteMap.put(spriteKey(SpriteID.FOOD, "beansCook"), ns1);

        Texture ts2 = new Texture(Gdx.files.internal("foods/Dough.png"));
        Sprite ns2 = new Sprite(ts2);
        spriteMap.put(spriteKey(SpriteID.FOOD, "dough"), ns2);

        Texture ts3 = new Texture(Gdx.files.internal("foods/Pizza.png"));
        Sprite ns3 = new Sprite(ts3);
        spriteMap.put(spriteKey(SpriteID.FOOD, "doughCook"), ns3);

        Texture ts4 = new Texture(Gdx.files.internal("foods/Cheese.png"));
        Sprite ns4 = new Sprite(ts4);
        spriteMap.put(spriteKey(SpriteID.FOOD, "cheese"), ns4);

        Texture ts5 = new Texture(Gdx.files.internal("foods/Cheese_Chopped.png"));
        Sprite ns5 = new Sprite(ts5);
        spriteMap.put(spriteKey(SpriteID.FOOD, "cheeseChop"), ns5);

        Texture ts6 = new Texture(Gdx.files.internal("foods/Chilli_Canned.png"));
        Sprite ns6 = new Sprite(ts6);
        spriteMap.put(spriteKey(SpriteID.FOOD, "chili"), ns6);

        Texture ts7 = new Texture(Gdx.files.internal("foods/Chilli_Cooked.png"));
        Sprite ns7 = new Sprite(ts7);
        spriteMap.put(spriteKey(SpriteID.FOOD, "chiliCook"), ns7);

        Texture ts8 = new Texture(Gdx.files.internal("foods/Pepperoni.png"));
        Sprite ns8 = new Sprite(ts8);
        spriteMap.put(spriteKey(SpriteID.FOOD, "pepperoni"), ns8);

        Texture ts9 = new Texture(Gdx.files.internal("foods/SlicedPepperoni.png"));
        Sprite ns9 = new Sprite(ts9);
        spriteMap.put(spriteKey(SpriteID.FOOD, "pepperoniChop"), ns9);

        Texture ts10 = new Texture(Gdx.files.internal("foods/Mushrooms.png"));
        Sprite ns10 = new Sprite(ts10);
        spriteMap.put(spriteKey(SpriteID.FOOD, "mushroom"), ns10);

        Texture ts11 = new Texture(Gdx.files.internal("foods/Mushrooms_Chopped.png"));
        Sprite ns11 = new Sprite(ts11);
        spriteMap.put(spriteKey(SpriteID.FOOD, "mushroomChop"), ns11);

        Texture ts12 = new Texture(Gdx.files.internal("foods/Potato.png"));
        Sprite ns12 = new Sprite(ts12);
        spriteMap.put(spriteKey(SpriteID.FOOD, "potato"), ns12);

        Texture ts13 = new Texture(Gdx.files.internal("foods/Potato_Cooked.png"));
        Sprite ns13 = new Sprite(ts13);
        spriteMap.put(spriteKey(SpriteID.FOOD, "potatoCook"), ns13);

        Texture ts14 = new Texture(Gdx.files.internal("cooks/normal down2.png"));
        Sprite ns14 = new Sprite(ts14);
        spriteMap.put(spriteKey(SpriteID.FOOD, "cook"), ns14);



        Texture ts15 = new Texture(Gdx.files.internal("powerups/satisfied_customer.png"));
        Sprite ns15 = new Sprite(ts15);
        spriteMap.put(spriteKey(SpriteID.POWERUP, "satisfiedCustomer"), ns15);

        Texture ts16 = new Texture(Gdx.files.internal("powerups/bonus_time.png"));
        Sprite ns16= new Sprite(ts16);
        spriteMap.put(spriteKey(SpriteID.POWERUP, "bonusTime"), ns16);

        Texture ts17 = new Texture(Gdx.files.internal("powerups/double_money.png"));
        Sprite ns17 = new Sprite(ts17);
        spriteMap.put(spriteKey(SpriteID.POWERUP, "doubleMoney"), ns17);

        Texture ts18 = new Texture(Gdx.files.internal("powerups/faster_cooks.png"));
        Sprite ns18 = new Sprite(ts18);
        spriteMap.put(spriteKey(SpriteID.POWERUP, "fasterCooks"), ns18);

        Texture ts19 = new Texture(Gdx.files.internal("powerups/auto_station.png"));
        Sprite ns19 = new Sprite(ts19);
        spriteMap.put(spriteKey(SpriteID.POWERUP, "autoStation"), ns19);





        String path = "cooks/";
        String[] directions = new String[] {"up", "left", "right", "down"};
        String[] types = new String[] {"normal","hold"};
        String[] hashTypes = new String[] {"","h"};
        String[] cookFileNums = new String[] {"1","3"};
        String[] cookIdents = new String[] {"c1","c2"};
        String direction;
        String type;
        String cookNumber;


        for (int i = 0; i < 2; i++){
            cookNumber = cookFileNums[i];
            for (int j = 0; j<4; j++) {
                direction = directions[j];
                for (int k = 0; k < 2; k++) {
                    type = types[k];
                    Texture text1 = new Texture(path+type+" "+direction+cookNumber+".png");
                    Sprite spr1 = new Sprite(text1);
                    spriteMap.put(spriteKey(SpriteID.COOK, cookIdents[i] + hashTypes[k] + direction.toUpperCase()),spr1);
                }
            }
        }


    }


}
