package jimmy.mcgymmy.model;

import static jimmy.mcgymmy.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import jimmy.mcgymmy.commons.exceptions.IllegalValueException;
import jimmy.mcgymmy.model.food.Food;
import jimmy.mcgymmy.model.food.Fridge;
import jimmy.mcgymmy.testutil.FoodBuilder;
import jimmy.mcgymmy.testutil.McGymmyBuilder;
import jimmy.mcgymmy.testutil.TypicalFoods;

public class McGymmyTest {

    private final McGymmy mcGymmy = new McGymmy();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), mcGymmy.getFoodList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> mcGymmy.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlyMcGymmy_replacesData() {
        McGymmy newData = TypicalFoods.getTypicalMcGymmy();
        mcGymmy.resetData(newData);
        assertEquals(newData, mcGymmy);
    }

    @Test
    public void resetData_withDuplicateFoods_replacesData() {
        // Two foods with the same identity fields
        McGymmy newData = TypicalFoods.getTypicalDuplicateMcGymmy();
        mcGymmy.resetData(newData);

        assertEquals(mcGymmy, newData);
    }

    @Test
    public void hasFood_nullFood_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> mcGymmy.hasFood(null));
    }

    @Test
    public void hasFood_foodNotInMcGymmy_returnsFalse() {
        assertFalse(mcGymmy.hasFood(TypicalFoods.getChickenRice()));
    }

    @Test
    public void hasFood_foodInMcGymmy_returnsTrue() {
        mcGymmy.addFood(TypicalFoods.getChickenRice());
        assertTrue(mcGymmy.hasFood(TypicalFoods.getChickenRice()));
    }

    @Test
    public void hasFood_foodWithSameIdentityFieldsInMcGymmy_returnsTrue() throws IllegalValueException {
        mcGymmy.addFood(TypicalFoods.getChickenRice());
        Food editedAlice = new FoodBuilder(TypicalFoods.getChickenRice()).build();
        assertTrue(mcGymmy.hasFood(editedAlice));
    }

    @Test
    public void getFoodList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> mcGymmy.getFoodList().remove(0));
    }

    @Test
    public void hashCode_returnsCorrectHashCode() {
        Food chickenRice = TypicalFoods.getChickenRice();
        Food apple = TypicalFoods.getApple();
        Fridge fridge = new Fridge();
        fridge.add(chickenRice);
        fridge.add(apple);
        McGymmy mcGymmy = new McGymmyBuilder().withFood(chickenRice).withFood(apple).build();
        assertEquals(fridge.hashCode(), mcGymmy.hashCode());
    }
}
