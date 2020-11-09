package jimmy.mcgymmy.model.food;

import static jimmy.mcgymmy.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import jimmy.mcgymmy.commons.exceptions.IllegalValueException;

public class FatTest {

    @Test
    public void newDefault_works() throws IllegalValueException {
        // If it doesnt, the thrown runtime exception will break this test.
        Fat fat = Fat.newDefault();

        //Test for equivalence
        Fat fat1 = new Fat(0);
        assertEquals(fat, fat1);
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
