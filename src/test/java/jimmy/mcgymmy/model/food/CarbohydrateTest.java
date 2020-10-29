package jimmy.mcgymmy.model.food;

import static jimmy.mcgymmy.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class CarbohydrateTest {


    @Test
    public void constructor_invalidCarbohydrate_throwsIllegalArgumentException() {
        String invalidCarb = "";
        assertThrows(NumberFormatException.class, () -> new Carbohydrate(Integer.parseInt(invalidCarb)));
    }

    @Test
    public void carbohydrate_isValid_correct() {
        // null carbohydrate
        assertThrows(NullPointerException.class, () -> Carbohydrate.isValid(null));

        // invalid carbohydrates
        assertFalse(Carbohydrate.isValid("")); // empty string
        assertFalse(Carbohydrate.isValid(" ")); // spaces only
        assertFalse(Carbohydrate.isValid("132454897")); // Out of range
        assertFalse(Carbohydrate.isValid("1000")); // Out of range

        // valid carbohydrates
        assertTrue(Carbohydrate.isValid("123"));
        assertTrue(Carbohydrate.isValid("12"));
        assertTrue(Carbohydrate.isValid("1")); // one character

    }
}
