package jimmy.mcgymmy.model.food;

import static jimmy.mcgymmy.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import jimmy.mcgymmy.commons.exceptions.IllegalValueException;

public class NameTest {

    private static final String BLANK_NAME = "";
    private static final String SPACES_ONLY = "   ";
    private static final String NON_ALPHANUMERIC = "^";
    private static final String SINGLE_CHARACTER = "a";
    private static final String NON_ALPHANUMERIC_AND_ALPHANUMERIC = "ASFASGAS^";
    private static final String VALID_NAME = "Cheese Fries";
    private static final String DIFFERENT_NAME = "cHeese FrIes";
    private static final String ALPHABETS_ONLY = "abcdefgh";
    private static final String NUMBERS_ONLY = "1234567890";
    private static final String ALPHANUMERIC = "abcdefghijkl1234567890";
    private static final String ALPHANUMERIC_WITH_CAPS = "abcdefghiJKL1234567890";
    private static final String ALPHANUMERIC_WITH_SPACES = "french fries";
    private static final String ALPHANUMBERIC_WITH_CAPS_AND_SPACES = "French Fried Beef Fries";
    private static final String LONG_NAME = "qwertyuiopasdfgh jklzxcvbnm1234567890";


    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Name(null));
    }

    @Test
    public void constructor_invalidName_throwsIllegalValueException() {
        assertThrows(IllegalValueException.class, () -> new Name(BLANK_NAME));
    }

    @Test
    public void isValidName() {
        // null name
        assertThrows(NullPointerException.class, () -> Name.isValidName(null));

        // invalid name
        assertFalse(Name.isValidName(BLANK_NAME)); // empty string
        assertFalse(Name.isValidName(SPACES_ONLY)); // spaces only
        assertFalse(Name.isValidName(NON_ALPHANUMERIC)); // only non-alphanumeric characters
        assertFalse(Name.isValidName(NON_ALPHANUMERIC_AND_ALPHANUMERIC)); // contains non-alphanumeric characters

        // valid name
        assertTrue(Name.isValidName(LONG_NAME)); // long names
        assertTrue(Name.isValidName(ALPHANUMERIC)); // alphanumeric
        assertTrue(Name.isValidName(NUMBERS_ONLY)); // numbers only
        assertTrue(Name.isValidName(ALPHABETS_ONLY)); // alphabets only
        assertTrue(Name.isValidName(SINGLE_CHARACTER)); // one character
        assertTrue(Name.isValidName(ALPHANUMERIC_WITH_CAPS)); // with capital letters
        assertTrue(Name.isValidName(ALPHANUMERIC_WITH_SPACES)); // alphabets and spaces
        assertTrue(Name.isValidName(ALPHANUMBERIC_WITH_CAPS_AND_SPACES)); // Alphanumeric with caps and spaces
    }

    @Test
    public void testEquals() throws IllegalValueException {
        Name name = new Name(VALID_NAME);

        //Same object -> Equals
        assertEquals(name, name);

        //Different object, same name -> Equals
        Name name2 = new Name(VALID_NAME);
        assertEquals(name, name2);

        //Different object, different name -> Not equals
        Name name3 = new Name(DIFFERENT_NAME);
        assertNotEquals(name, name3);

    }

    @Test
    public void hashCode_returnsCorrectHashCode() throws IllegalValueException {
        Name name = new Name(VALID_NAME);

        //Same name same object -> same hashcode
        assertEquals(name.hashCode(), name.hashCode());

        //Same name different object -> same hashcode
        Name name2 = new Name(VALID_NAME);
        assertEquals(name.hashCode(), name2.hashCode());

        //Different name different object -> different hashcode
        Name name3 = new Name(DIFFERENT_NAME);
        assertNotEquals(name, name3);
    }
}
