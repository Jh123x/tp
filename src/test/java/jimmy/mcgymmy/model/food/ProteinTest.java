package jimmy.mcgymmy.model.food;

import static jimmy.mcgymmy.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProteinTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NumberFormatException.class, () -> new Protein(null));
    }

    @Test
    public void constructor_invalidProtein_throwsIllegalArgumentException() {
        String invalidProtein = "";
        assertThrows(IllegalArgumentException.class, () -> new Protein(invalidProtein));
    }

    @Test
    public void isValidProtein() {
        // null phone number
        assertThrows(NullPointerException.class, () -> Protein.isValid(null));

        // invalid phone numbers
        Assertions.assertFalse(Protein.isValid("")); // empty string
        Assertions.assertFalse(Protein.isValid(" ")); // spaces only
        Assertions.assertFalse(Protein.isValid("phone")); // non-numeric
        Assertions.assertFalse(Protein.isValid("9011p041")); // alphabets within digits
        Assertions.assertFalse(Protein.isValid("9312 1534")); // spaces within digits

        // valid phone numbers
        Assertions.assertTrue(Protein.isValid("911"));
        Assertions.assertTrue(Protein.isValid("93121534"));
        Assertions.assertTrue(Protein.isValid("124293842033123"));
    }
}