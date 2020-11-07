package jimmy.mcgymmy.model.food;

import static jimmy.mcgymmy.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import jimmy.mcgymmy.commons.exceptions.IllegalValueException;
import jimmy.mcgymmy.model.tag.Tag;
import jimmy.mcgymmy.testutil.Assert;
import jimmy.mcgymmy.testutil.FoodBuilder;
import jimmy.mcgymmy.testutil.TypicalFoods;


public class FoodTest {
    public static final String INVALID_FOOD_NAME = "";

    private static Name validFoodName;
    private static Name validFoodName2;
    private static Protein protein;
    private static Protein protein1;
    private static Carbohydrate carbohydrate;
    private static Carbohydrate carbohydrate1;
    private static Fat fat;
    private static Fat fat1;
    private static Food comparedFood;
    private static Food sameAsComparedFood;
    private static Food foodWDifferentName;
    private static Food foodWDifferentProtein;
    private static Food foodWDifferentCarbs;
    private static Food foodWDifferentFat;

    static {
        try {
            initialiseFoods();
        } catch (IllegalValueException e) {
            assert false : "Error in food name";
        }
    }

    static void initialiseFoods() throws IllegalValueException {
        validFoodName = new Name("test food");
        validFoodName2 = new Name("test food 2");
        protein = new Protein(2);
        protein1 = new Protein(3);
        carbohydrate = new Carbohydrate(3);
        carbohydrate1 = new Carbohydrate(4);
        fat = new Fat(4);
        fat1 = new Fat(5);
        comparedFood = new Food(validFoodName, protein, fat, carbohydrate);
        sameAsComparedFood = new Food(validFoodName, protein, fat, carbohydrate);
        foodWDifferentName = new Food(validFoodName2, protein, fat, carbohydrate);
        foodWDifferentProtein = new Food(validFoodName, protein1, fat, carbohydrate);
        foodWDifferentFat = new Food(validFoodName, protein, fat1, carbohydrate);
        foodWDifferentCarbs = new Food(validFoodName, protein, fat, carbohydrate1);
    }

    @Test
    public void constructor_nullProtein_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new Food(validFoodName, null, fat, carbohydrate));
    }

    @Test
    public void constructor_nullCarbohydrate_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new Food(validFoodName, protein, fat, null));
    }

    @Test
    public void constructor_nullFat_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new Food(validFoodName, protein, null, carbohydrate));
    }

    @Test
    public void constructor_invalidName_throwsIllegalValueException() {
        assertThrows(IllegalValueException.class, () ->
                new Food(new Name(INVALID_FOOD_NAME), protein, fat, carbohydrate));
    }

    @Test
    public void toStringTest() throws IllegalValueException {
        String expected1 = "Food: test food\n"
                + "Protein: 2\n"
                + "Carbs: 3\n"
                + "Fat: 4\n";
        assertEquals(comparedFood.toString(), expected1);
        String expected2 = "Food: test food2\n"
                + "Protein: 100\n"
                + "Carbs: 20\n"
                + "Fat: 10\n";
        assertEquals(new Food("test food2", 100, 10, 20).toString(), expected2);
    }

    @Test
    public void getCaloriesTest() throws IllegalValueException {
        assertEquals(new Food("water", 0, 0, 0).getCalories(), 0);
        assertEquals(new Food("chimkenbreast", 30, 0, 0).getCalories(), 120);
        assertEquals(new Food("chimkenRice", 0, 0, 30).getCalories(), 120);
        assertEquals(new Food("sesameOil", 0, 10, 0).getCalories(), 90);
        assertEquals(new Food("chimkenRiceSet", 30, 10, 30).getCalories(), 330);
    }


    @Test
    public void equals() {
        // identical -> returns true
        assertEquals(comparedFood, comparedFood);

        // different object all field are the same -> returns true
        assertEquals(comparedFood, sameAsComparedFood);

        // different name -> returns false
        assertFalse(comparedFood.equals(foodWDifferentName));

        // different protein -> returns false
        assertFalse(comparedFood.equals(foodWDifferentProtein));

        // different carbohydrate -> returns false
        assertFalse(comparedFood.equals(foodWDifferentCarbs));

        // different fat -> returns false
        assertFalse(comparedFood.equals(foodWDifferentFat));

        // different type -> returns false
        assertFalse(comparedFood.equals(protein));
    }

    @Test
    public void hasTag_tagInFood_returnsTrue() throws IllegalValueException {
        Food testFood = new FoodBuilder(TypicalFoods.getChickenRice()).withTags("Lunch").build();
        assertTrue(testFood.hasTag(new Tag("Lunch")));
    }

    @Test
    public void hasTag_tagNotInFood_returnsFalse() throws IllegalValueException {
        Food testFood = new FoodBuilder(TypicalFoods.getChickenRice()).withTags("Lunch").build();
        assertFalse(testFood.hasTag(new Tag("Dinner")));
    }

    @Test
    public void addTag_returnsNewFoodWithTag_oldFoodUnChanged() throws IllegalValueException {
        Tag initialTag = new Tag("Lunch");
        Tag toBeAdded = new Tag("Dinner");
        Food testFood = new FoodBuilder(TypicalFoods.getChickenRice()).withTags("Lunch").build();
        Food newFood = testFood.addTag(toBeAdded);
        assertTrue(newFood.hasTag(toBeAdded));
        assertTrue(newFood.hasTag(initialTag));
        assertTrue(testFood.hasTag(initialTag));
        assertFalse(testFood.hasTag(toBeAdded));
    }

    @Test
    public void removeTag_returnsNewFoodWithoutTag_oldFoodUnChanged() throws IllegalValueException {
        Tag initialTag = new Tag("Lunch");
        Food testFood = new FoodBuilder(TypicalFoods.getChickenRice()).withTags("Lunch").build();
        Food newFood = testFood.removeTag(initialTag);
        assertFalse(newFood.hasTag(initialTag));
        assertTrue(testFood.hasTag(initialTag));
    }

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() throws IllegalValueException {
        Food food = new FoodBuilder().build();
        Assert.assertThrows(UnsupportedOperationException.class, () -> food.getTags().remove(0));
    }


}
