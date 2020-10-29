package jimmy.mcgymmy.model.food;

import static jimmy.mcgymmy.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FatTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NumberFormatException.class, () -> new Fat(null));
    }

    @Test
    public void constructor_invalidEmail_throwsIllegalArgumentException() {
        String invalidFat = "12@123";
        assertThrows(NumberFormatException.class, () -> new Fat(Integer.parseInt(invalidFat)));
    }

    @Test
    public void fat_isValid_correct() {
        // null fat
        assertThrows(NullPointerException.class, () -> Fat.isValid(null));

        // blank fat
        Assertions.assertFalse(Fat.isValid("")); // empty string
        Assertions.assertFalse(Fat.isValid(" ")); // spaces only

        // invalid parts
        Assertions.assertFalse(Fat.isValid("12314-")); // invalid fat name
        Assertions.assertFalse(Fat.isValid("123_345")); // underscore in fat name
        Assertions.assertFalse(Fat.isValid(" 1234")); // leading space
        Assertions.assertFalse(Fat.isValid("1234 ")); // trailing space
        Assertions.assertFalse(Fat.isValid("-1234")); // negative value
        Assertions.assertFalse(Fat.isValid("1234")); // Out of range

        // valid fat

        Assertions.assertTrue(Fat.isValid("911"));
        Assertions.assertTrue(Fat.isValid("19"));
        Assertions.assertTrue(Fat.isValid("1"));
    }
}
