package jimmy.mcgymmy.model.food;

import static jimmy.mcgymmy.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import jimmy.mcgymmy.commons.exceptions.IllegalValueException;

class MacronutrientTest {

    private static final int CORRECT_CARBOHYDRATE_MULTIPLIER = 4;
    private static final int CORRECT_PROTEIN_MULTIPLIER = 4;
    private static final int CORRECT_FATS_MULTIPLIER = 9;
    private static final int DEFAULT_AMOUNT = 10;

    private static Carbohydrate defaultCarbohydrate1;
    private static Macronutrient macronutrient1;
    private static Macronutrient macronutrient2;
    private static Macronutrient macronutrient3;
    private static Protein defaultProtein1;
    private static Fat defaultFat1;
    private static Fat defaultFat2;
    private static Fat defaultFat3;

    static {
        try {
            initialiseVariables();
        } catch (IllegalValueException e) {
            assert false : "Error in food";
        }
    }

    public static void initialiseVariables() throws IllegalValueException {
        defaultFat1 = new Fat(1);
        defaultFat2 = new Fat(1);
        defaultFat3 = new Fat(2);
        defaultProtein1 = new Protein(1);
        defaultCarbohydrate1 = new Carbohydrate(1);
        macronutrient1 = new MacronutrientStub(4, 5);
        macronutrient2 = new MacronutrientStub(9, 6);
        macronutrient3 = new MacronutrientStub(9, 7);
    }

    @Test
    public void amount_lesserThanZero_throwIllegalValueException() {
        // Boundary value
        assertThrows(IllegalValueException.class, () -> new MacronutrientStub(-1, 4));

        // Middle of boundary
        assertThrows(IllegalValueException.class, () -> new MacronutrientStub(-100, 4));

        //Other boundary value
        assertThrows(IllegalValueException.class, () -> new MacronutrientStub(Integer.MIN_VALUE, 4));
    }

    @Test
    public void amount_greaterThan1000_throwIllegalValueException() {
        // Boundary value
        assertThrows(IllegalValueException.class, () -> new MacronutrientStub(1000, 4));

        // Middle of boundary
        assertThrows(IllegalValueException.class, () -> new MacronutrientStub(100000, 4));

        //Other boundary value
        assertThrows(IllegalValueException.class, () -> new MacronutrientStub(Integer.MAX_VALUE, 4));
    }

    @Test
    public void multiplier_lessThan1_throwAssertionException() {

        // Boundary value
        assertThrows(AssertionError.class, () -> new MacronutrientStub(10, 0));

        // Middle of boundary
        assertThrows(AssertionError.class, () -> new MacronutrientStub(10, -100));

        //Other boundary value
        assertThrows(AssertionError.class, () -> new MacronutrientStub(10, Integer.MIN_VALUE));
    }

    @Test
    public void setMacronutrient1_isValid_correct() {
        // null protein number
        assertThrows(NullPointerException.class, () -> Protein.isValid(null));

        // invalid protein numbers
        assertFalse(Macronutrient.isValid("")); // empty string
        assertFalse(Macronutrient.isValid("   ")); // spaces only
        assertFalse(Macronutrient.isValid("Macronutrient")); // non-numeric
        assertFalse(Macronutrient.isValid("9011p041")); // alphabets within digits
        assertFalse(Macronutrient.isValid("9312 1534")); // spaces within digits
        assertFalse(Macronutrient.isValid("9111")); // Out of range

        // valid protein numbers
        assertTrue(Macronutrient.isValid("911"));
        assertTrue(Macronutrient.isValid("911   "));
        assertTrue(Macronutrient.isValid("   911"));
        assertTrue(Macronutrient.isValid("93"));
        assertTrue(Macronutrient.isValid("1"));
    }

    @Test
    public void equals() {
        // identical -> return true
        assertEquals(defaultFat1, defaultFat1); // identical

        // same type same amount -> return true
        assertEquals(defaultFat2, defaultFat1); // same type same amount

        // same type different amount -> return false
        assertNotEquals(defaultFat3, defaultFat1);

        // different type -> return false
        assertNotEquals(defaultProtein1, defaultFat1);

        // not instanceof Macronutrient -> return false
        assertNotEquals(defaultFat1, "dummy string object");

        // same type, same totalCalories, different amount -> return false
        assertNotEquals(macronutrient2, macronutrient1);

        // same type, different total Calories, same amount -> return false
        assertNotEquals(macronutrient2, macronutrient3);
    }

    @Test
    public void getTotalCalories() throws IllegalValueException {
        // Check for Carbs
        assertEquals(new MacronutrientStub(DEFAULT_AMOUNT,
                CORRECT_CARBOHYDRATE_MULTIPLIER).getTotalCalories(), 40);

        // Check for fat
        assertEquals(new MacronutrientStub(DEFAULT_AMOUNT,
                CORRECT_FATS_MULTIPLIER).getTotalCalories(), 90);

        // Check for protein
        assertEquals(new MacronutrientStub(DEFAULT_AMOUNT,
                CORRECT_PROTEIN_MULTIPLIER).getTotalCalories(), 40);

        // Check for other multiplier result
        assertEquals(macronutrient3.getTotalCalories(), 63);
    }

    @Test
    public void getCaloricMultiplierReturnsCorrectMultiplier() throws IllegalValueException {
        assertEquals(new MacronutrientStub(DEFAULT_AMOUNT, CORRECT_PROTEIN_MULTIPLIER).getCaloricMultiplier(),
                CORRECT_PROTEIN_MULTIPLIER);

        // Fat multiplier
        assertEquals(defaultFat1.getCaloricMultiplier(), CORRECT_FATS_MULTIPLIER);

        // Carb multiplier
        assertEquals(defaultCarbohydrate1.getCaloricMultiplier(), CORRECT_CARBOHYDRATE_MULTIPLIER);

        //Protein Multiplier
        assertEquals(defaultProtein1.getCaloricMultiplier(), CORRECT_PROTEIN_MULTIPLIER);
    }

    private static class MacronutrientStub extends Macronutrient {
        MacronutrientStub(int amount, int caloricMultiplier) throws IllegalValueException {
            super(amount, caloricMultiplier);
        }

        @Override
        String getMessageConstraint() {
            return null;
        }
    }

}
