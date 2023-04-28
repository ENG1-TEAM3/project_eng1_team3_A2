package food;

import java.util.HashMap;

/**
 * A class that controls how {@link FoodID}s are
 * used and handled.
 */
public class FoodItem {

    /** IDs of all the different possible types of food ingredients.*/
    public enum FoodID {
        /** Lettuce */
        lettuce,
        /** Lettuce -&gt; {@link stations.PreparationStation}
         * with type {@link stations.Station.StationID#cut} */
        lettuceChop,
        /** Tomato */
        tomato,
        /** Tomato -&gt; {@link stations.PreparationStation}
         * with type {@link stations.Station.StationID#cut} */
        tomatoChop,
        /** Onion */
        onion,
        /** Onion -&gt; {@link stations.PreparationStation}
         * with type {@link stations.Station.StationID#cut} */
        onionChop,
        /** Meat */
        meat,
        /** Meat -&gt; {@link stations.PreparationStation}
         * with type {@link stations.Station.StationID#fry} */
        meatCook,
        /** Bun — Used only to specify that the {@link stations.Pantry} gives
         * either a {@link #bottomBun} or {@link #topBun}. */
        bun,
        /** Bottom Bun -&gt; Highest bun on {@link FoodStack} is {@code null} or {@link #topBun} */
        bottomBun,
        /** Top Bun -&gt; Highest bun on {@link FoodStack} is {@link #bottomBun} */
        topBun,
        dough,
        doughCook,
        cheese,
        cheeseChop,
        beans,
        beansCook,
        chili,
        chiliCook,
        pepperoni,
        pepperoniChop,
        mushroom,
        mushroomChop,
        potato,
        potatoCook,

        cook,
        /** Default */
        none
    }

    /** A dict of the pixel height for each food. Used when rendering the FoodStack.*/
    public static final HashMap<FoodID, Float> foodHeights = new HashMap<>();

    static {
        foodHeights.put(FoodID.lettuce, 20F);
        foodHeights.put(FoodID.lettuceChop, 4F);
        foodHeights.put(FoodID.tomato, 20F);
        foodHeights.put(FoodID.tomatoChop, 5.8F);
        foodHeights.put(FoodID.onion, 20F);
        foodHeights.put(FoodID.onionChop, 5.8F);
        foodHeights.put(FoodID.meat, 8F);
        foodHeights.put(FoodID.meatCook, 8F);
        foodHeights.put(FoodID.bun, 20F);
        foodHeights.put(FoodID.bottomBun, 10F);
        foodHeights.put(FoodID.topBun, 12F);

        foodHeights.put(FoodID.dough, 20F);
        foodHeights.put(FoodID.doughCook, 20F);
        foodHeights.put(FoodID.cheese, 20F);
        foodHeights.put(FoodID.cheeseChop, 20F);
        foodHeights.put(FoodID.beans, 20F);
        foodHeights.put(FoodID.beansCook, 20F);
        foodHeights.put(FoodID.chili, 20F);
        foodHeights.put(FoodID.chiliCook, 20F);
        foodHeights.put(FoodID.pepperoni, 20F);
        foodHeights.put(FoodID.pepperoniChop, 20F);
        foodHeights.put(FoodID.mushroom, 20F);
        foodHeights.put(FoodID.mushroomChop, 20F);
        foodHeights.put(FoodID.potato, 20F);
        foodHeights.put(FoodID.potatoCook, 20F);
        foodHeights.put(FoodID.cook, 60F);
    }

}

