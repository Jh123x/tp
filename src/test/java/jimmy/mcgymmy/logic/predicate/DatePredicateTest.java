package jimmy.mcgymmy.logic.predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import jimmy.mcgymmy.commons.exceptions.IllegalValueException;
import jimmy.mcgymmy.logic.parser.exceptions.ParseException;
import jimmy.mcgymmy.model.food.Name;
import jimmy.mcgymmy.testutil.FoodBuilder;

public class DatePredicateTest {

    private static final String VALID_DATE_1 = "24-10-2020";
    private static final String VALID_DATE_DIFFERENT_YEAR = "24-10-2019";
    private static final String VALID_DATE_2 = "25-10-2020";
    private static final String DIFFERENT_FORMAT = "2020-10-24";
    private static final String FOOD_NAME = "Apple";

    @Test
    public void equals() throws ParseException {

        DatePredicate firstPredicate = new DatePredicate(VALID_DATE_1);
        DatePredicate secondPredicate = new DatePredicate(VALID_DATE_2);

        // same object -> returns true
        assertEquals(firstPredicate, firstPredicate);

        // same values -> returns true
        DatePredicate firstPredicateCopy = new DatePredicate(VALID_DATE_1);
        assertEquals(firstPredicateCopy, firstPredicate);

        // different types -> returns false
        assertNotEquals(firstPredicate, 1);

        // null -> returns false
        assertNotEquals(firstPredicate, null);

        // different date -> returns false
        assertNotEquals(secondPredicate, firstPredicate);
    }

    @Test
    public void test_datesEqualMultipleFormats_returnsTrue() throws IllegalValueException {

        // Same format
        DatePredicate predicate = new DatePredicate(VALID_DATE_1);
        assertTrue(predicate.test(new FoodBuilder().withName(new Name(FOOD_NAME)).withDate(VALID_DATE_1).build()));

        // Different format
        predicate = new DatePredicate(DIFFERENT_FORMAT);
        assertTrue(predicate.test(new FoodBuilder().withName(new Name(FOOD_NAME)).withDate(VALID_DATE_1).build()));
    }

    @Test
    public void test_datesNotEqual_returnsFalse() throws IllegalValueException {
        DatePredicate predicate = new DatePredicate(VALID_DATE_1);
        assertFalse(predicate.test(new FoodBuilder()
                .withName(new Name(FOOD_NAME))
                .withDate(VALID_DATE_DIFFERENT_YEAR)
                .build()));
    }

}
