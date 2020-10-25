package jimmy.mcgymmy.model.food;

import static jimmy.mcgymmy.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class MacronutrientTest {

    private static final Fat DEFAULT_FAT_1 = new Fat(1);
    private static final Fat DEFAULT_FAT_2 = new Fat(1);
    private static final Fat DEFAULT_FAT_3 = new Fat(2);
    private static final Protein DEFAULT_PROTEIN_1 = new Protein(1);
    private static final Carbohydrate DEFAULT_CARBOHYDRATE_1 = new Carbohydrate(1);
    private static final Macronutrient MACRONUTRIENT_1 = new MacronutrientStub(4, 9);
    private static final Macronutrient MACRONUTRIENT_2 = new MacronutrientStub(9, 4);
    private static final int CORRECT_CARBOHYDRATE_MULTIPLIER = 4;
    private static final int CORRECT_PROTEIN_MULTIPLIER = 4;
    private static final int CORRECT_FATS_MULTIPLIER = 9;
    private static final int INVALID_MULTIPLIER = 5;
    private static final int DEFAULT_AMOUNT = 10;

    @Test
    public void amount_lesserThanZero_throwIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                new MacronutrientStub(-1, 4));
    }

    @Test
    public void equals() {
        // identical -> return true
        assertTrue(DEFAULT_FAT_1.equals(DEFAULT_FAT_1)); // identical

        // same type same amount -> return true
        assertTrue(DEFAULT_FAT_1.equals(DEFAULT_FAT_2)); // same type same amount

        // same type different amount -> return false
        assertFalse(DEFAULT_FAT_1.equals(DEFAULT_FAT_3));

        // different type -> return false
        assertFalse(DEFAULT_FAT_1.equals(DEFAULT_PROTEIN_1));

        // not instanceof Macronutrient -> return false
        assertFalse(DEFAULT_FAT_1.equals("dummy string object"));

        // same type, same totalCalories, different amount -> return false
        assertFalse(MACRONUTRIENT_1.equals(MACRONUTRIENT_2));
    }

    @Test
    public void getTotalCalories() {
        assertEquals(new MacronutrientStub(DEFAULT_AMOUNT, CORRECT_CARBOHYDRATE_MULTIPLIER).getTotalCalories(), 40);
    }

    @Test
    public void getCaloricMultiplierReturnsCorrectMultiplier() {
        assertEquals(new MacronutrientStub(DEFAULT_AMOUNT, CORRECT_PROTEIN_MULTIPLIER).getCaloricMultiplier(),
                CORRECT_PROTEIN_MULTIPLIER);
        assertEquals(DEFAULT_FAT_1.getCaloricMultiplier(), CORRECT_FATS_MULTIPLIER);
        assertEquals(DEFAULT_CARBOHYDRATE_1.getCaloricMultiplier(), CORRECT_CARBOHYDRATE_MULTIPLIER);
        assertEquals(DEFAULT_PROTEIN_1.getCaloricMultiplier(), CORRECT_PROTEIN_MULTIPLIER);
    }

    @Test
    public void constructor_invalidMultiplier_throwsAssertionError() {
        assertThrows(AssertionError.class, InvalidMacronutrientStub::new);
    }

    private static class MacronutrientStub extends Macronutrient {
        MacronutrientStub(int amount, int caloricMultiplier) {
            super(amount, caloricMultiplier);
        }
    }

    private static class InvalidMacronutrientStub extends Macronutrient {
        InvalidMacronutrientStub() {
            super(1, INVALID_MULTIPLIER);
        }
    }

}
