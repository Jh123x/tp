package jimmy.mcgymmy.model.tag;

import static jimmy.mcgymmy.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class TagTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Tag(null));
    }

    @Test
    public void constructor_invalidTagName_throwsIllegalArgumentException() {
        String invalidTagName = "";
        assertThrows(IllegalArgumentException.class, () -> new Tag(invalidTagName));
    }

    @Test
    public void isValidTagName() {

        //Invalid testcases
        assertThrows(NullPointerException.class, () -> new Tag(null)); // null tag name
        assertFalse(Tag.isValidTagName("123456789012345678901")); // 21 chars
        assertFalse(Tag
                .isValidTagName("123456789012345678901234567890123456789012345678901234567890")); // 60 chars
        assertFalse(Tag.isValidTagName("asdsafdfs12345$$$@#$!^#$")); //Alphanumeric with symbol
        assertFalse(Tag.isValidTagName("@#$!^#$")); //Symbols

        //ValidTagNames
        assertTrue(Tag.isValidTagName("asdsafdfs")); //Alphabets
        assertTrue(Tag.isValidTagName("asdsafdfs12345")); //Alphanumeric
        assertTrue(Tag.isValidTagName("12345678901234567890")); //20 chars

    }

}
