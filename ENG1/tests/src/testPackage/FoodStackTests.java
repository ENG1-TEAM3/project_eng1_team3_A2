package testPackage;

import food.FoodItem;
import org.junit.runner.RunWith;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import com.badlogic.gdx.utils.Array;
import helper.BodyHelper;
import helper.Constants;
import interactions.InputKey;
import interactions.Interactions;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import cooks.Cook;
import food.FoodStack;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


@RunWith(GdxTestRunner.class)
public class FoodStackTests {
    @Test
    public void testFoodStackEmpty(){
        FoodStack fs1 = new FoodStack();
        assertNull("This asserts that null is returned when calling peekStack on an empty foodstack", fs1.peekStack());
        assertNull("This asserts that null is returned when calling popStack on an empty foodstack",fs1.popStack());
    }
    @Test
    public void testFoodStackConstructNonEmpty(){
        FoodStack fs1 = new FoodStack(FoodItem.FoodID.bun, FoodItem.FoodID.lettuce, FoodItem.FoodID.lettuceChop);
        assertEquals("This test asserts that peekStack returns the correct item",
                FoodItem.FoodID.bun, fs1.peekStack()
                );
        assertEquals("This test asserts that popStack returns the correct item",
                FoodItem.FoodID.bun, fs1.popStack()
                );
        assertEquals("This test asserts that an item has been removed from the stack",
                FoodItem.FoodID.lettuce, fs1.peekStack());
    }
    @Test
    public void testFoodStackAddFood(){
        FoodStack fs1 = new FoodStack();
        assertNull(fs1.peekStack());
        fs1.addStack(FoodItem.FoodID.bun);
        assertEquals("This test asserts that a bun was added to the top of the stack",
                FoodItem.FoodID.bun, fs1.peekStack());
    }

    @Test
    public void testFoodStackClearStack(){
        FoodStack fs1 = new FoodStack(FoodItem.FoodID.lettuce, FoodItem.FoodID.lettuce, FoodItem.FoodID.bun);
        assertEquals(FoodItem.FoodID.lettuce, fs1.peekStack());
        fs1.clearStack();
        assertNull("This test asserts that the stack has been cleared", fs1.peekStack());
    }

    @Test
    public void testFoodStackSize(){
        FoodStack fs1 = new FoodStack();
        assertEquals("This test asserts that an empty foodstack has size zero", 0, fs1.size());
        fs1.addStack(FoodItem.FoodID.bun);
        assertEquals("This test asserts that a stack with one item in it has size one", 1,
                fs1.size());
        fs1.addStack(FoodItem.FoodID.lettuce);
        assertEquals("This test asserts that a stack with two items in it has size two", 2,
                fs1.size());
    }

    @Test
    public void testFoodStackGetStack(){
        FoodStack fs1 = new FoodStack(FoodItem.FoodID.lettuce, FoodItem.FoodID.meat, FoodItem.FoodID.bottomBun);
        Array<FoodItem.FoodID> ar1 = new Array<>();
        ar1.add(FoodItem.FoodID.lettuce,FoodItem.FoodID.meat, FoodItem.FoodID.bottomBun);
        assertEquals("This test asserts that getStack returns the correct Array", ar1, fs1.getStack());
        fs1.clearStack();
        assertEquals("This test asserts that getStack returns the correct empty Array", new Array<>(), fs1.getStack());
    }

    @Test
    public void testFoodStackSetStack(){
        FoodStack fs1 = new FoodStack();
        Array<FoodItem.FoodID> ar1 = new Array<>();
        ar1.add(FoodItem.FoodID.lettuce,FoodItem.FoodID.meat, FoodItem.FoodID.bottomBun);
        assertEquals(0, fs1.size());
        fs1.setStack(ar1);
        assertEquals("This test asserts that setStack sets the correct stack", ar1, fs1.getStack());
    }

    @Test
    public void testFoodStackToString(){
        FoodStack fs1 = new FoodStack();
        assertEquals("This test asserts that the return value of toString is correct for an empty list",
                "[]", fs1.toString());
        fs1.addStack(FoodItem.FoodID.bun);
        assertEquals("This test asserts that the return value of toString is correct for a list only containing bun",
                "[bun]", fs1.toString());
        fs1.addStack(FoodItem.FoodID.bottomBun);
        fs1.addStack(FoodItem.FoodID.meatCook);
        fs1.addStack(FoodItem.FoodID.onion);
        assertEquals("This test asserts that the return value of toString is correct for a multi value list",
                "[onion, meatCook, bottomBun, bun]", fs1.toString());
    }










}
