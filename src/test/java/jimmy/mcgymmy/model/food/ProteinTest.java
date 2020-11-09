package jimmy.mcgymmy.model.food;

import static jimmy.mcgymmy.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import jimmy.mcgymmy.commons.exceptions.IllegalValueException;

public class ProteinTest {

    @Test
    public void newDefault_works() throws IllegalValueException {
        // If it doesnt, the thrown runtime exception will break this test.
        Protein protein = Protein.newDefault();
        //Test for equivalence
        Protein protein1 = new Protein(0);
        assertEquals(protein, protein1);
    }

    @Test
    public void protein_isValid_correct() {
        // null protein number
        assertThrows(NullPointerException.class, () -> Protein.isValid(null));

        // invalid protein numbers
        Assertions.assertFalse(Protein.isValid("")); // empty string
        Assertions.assertFalse(Protein.isValid(" ")); // spaces only
        Assertions.assertFalse(Protein.isValid("protein")); // non-numeric
        Assertions.assertFalse(Protein.isValid("9011p041")); // alphabets within digits
        Assertions.assertFalse(Protein.isValid("9312 1534")); // spaces within digits
        Assertions.assertFalse(Protein.isValid("9111")); // Out of range

        // valid protein numbers
        Assertions.assertTrue(Protein.isValid("911"));
        Assertions.assertTrue(Protein.isValid("93"));
        Assertions.assertTrue(Protein.isValid("1"));
    }
}
