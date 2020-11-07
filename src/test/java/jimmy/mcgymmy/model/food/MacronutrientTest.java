package jimmy.mcgymmy.model.food;

import static jimmy.mcgymmy.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import jimmy.mcgymmy.commons.exceptions.IllegalValueException;

class MacronutrientTest {

    private static Fat defaultFat1;
    private static Fat defaultFat2;
    private static Fat defaultFat3;
    private static Protein defaultProtein1;
    private static Carbohydrate defaultCarbohydrate1;
    private static Macronutrient macronutrient1;
    private static Macronutrient macronutrient2;

    static {
        try {
            initialiseVariables();
        } catch (IllegalValueException e) {
            assert false : "Error in food";
        }
    }

    private static final int CORRECT_CARBOHYDRATE_MULTIPLIER = 4;
    private static final int CORRECT_PROTEIN_MULTIPLIER = 4;
    private static final int CORRECT_FATS_MULTIPLIER = 9;
    private static final int INVALID_MULTIPLIER = 5;
    private static final int DEFAULT_AMOUNT = 10;

    public static void initialiseVariables() throws IllegalValueException {
        defaultFat1 = new Fat(1);
        defaultFat2 = new Fat(1);
        defaultFat3 = new Fat(2);
        defaultProtein1 = new Protein(1);
        defaultCarbohydrate1 = new Carbohydrate(1);
        macronutrient1 = new MacronutrientStub(4, 9);
        macronutrient2 = new MacronutrientStub(9, 4);
    }

    @Test
    public void amount_lesserThanZero_throwIllegalValueException() {
        assertThrows(IllegalValueException.class, () ->
                new MacronutrientStub(-1, 4));
    }

    @Test
    public void equals() {
        // identical -> return true
        assertTrue(defaultFat1.equals(defaultFat1)); // identical

        // same type same amount -> return true
        assertTrue(defaultFat1.equals(defaultFat2)); // same type same amount

        // same type different amount -> return false
        assertFalse(defaultFat1.equals(defaultFat3));

        // different type -> return false
        assertFalse(defaultFat1.equals(defaultProtein1));

        // not instanceof Macronutrient -> return false
        assertFalse(defaultFat1.equals("dummy string object"));

        // same type, same totalCalories, different amount -> return false
        assertFalse(macronutrient1.equals(macronutrient2));
    }

    @Test
    public void getTotalCalories() throws IllegalValueException {
        assertEquals(new MacronutrientStub(DEFAULT_AMOUNT, CORRECT_CARBOHYDRATE_MULTIPLIER).getTotalCalories(), 40);
    }

    @Test
    public void getCaloricMultiplierReturnsCorrectMultiplier() throws IllegalValueException {
        assertEquals(new MacronutrientStub(DEFAULT_AMOUNT, CORRECT_PROTEIN_MULTIPLIER).getCaloricMultiplier(),
                CORRECT_PROTEIN_MULTIPLIER);
        assertEquals(defaultFat1.getCaloricMultiplier(), CORRECT_FATS_MULTIPLIER);
        assertEquals(defaultCarbohydrate1.getCaloricMultiplier(), CORRECT_CARBOHYDRATE_MULTIPLIER);
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

    private static class InvalidMacronutrientStub extends Macronutrient {
        InvalidMacronutrientStub() throws IllegalValueException {
            super(1, INVALID_MULTIPLIER);
        }

        @Override
        String getMessageConstraint() {
            return null;
        }
    }

}
