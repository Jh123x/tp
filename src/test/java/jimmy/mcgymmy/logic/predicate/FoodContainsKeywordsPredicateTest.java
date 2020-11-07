package jimmy.mcgymmy.logic.predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import jimmy.mcgymmy.commons.exceptions.IllegalValueException;
import jimmy.mcgymmy.model.food.Name;
import jimmy.mcgymmy.testutil.FoodBuilder;

class FoodContainsKeywordsPredicateTest {

    private static final List<String> CHICKEN_LUNCH_LIST = Arrays.asList("Chicken", "Lunch");
    private static final List<String> CHICKEN_DINNER_LIST = Arrays.asList("Chicken", "Dinner");
    private static final List<String> SECOND_PREDICATE_KEYWORD_LIST = Arrays.asList("first", "second");
    private static final List<String> FIRST_PREDICATE_KEYWORD_LIST = Collections.singletonList("first");
    private static final FoodContainsKeywordsPredicate CHICKEN_DINNER_PREDICATE =
            new FoodContainsKeywordsPredicate(CHICKEN_DINNER_LIST);

    @Test
    public void equals() {

        FoodContainsKeywordsPredicate firstPredicate = new FoodContainsKeywordsPredicate(FIRST_PREDICATE_KEYWORD_LIST);
        FoodContainsKeywordsPredicate secondPredicate =
                new FoodContainsKeywordsPredicate(SECOND_PREDICATE_KEYWORD_LIST);

        // same object -> returns true
        assertEquals(firstPredicate, firstPredicate);

        // same values -> returns true
        FoodContainsKeywordsPredicate firstPredicateCopy =
                new FoodContainsKeywordsPredicate(FIRST_PREDICATE_KEYWORD_LIST);
        assertEquals(firstPredicateCopy, firstPredicate);

        // different types -> returns false
        assertNotEquals(firstPredicate, 1);

        // null -> returns false
        assertNotEquals(firstPredicate, null);

        // different food -> returns false
        assertNotEquals(secondPredicate, firstPredicate);
    }

    @Test
    public void test_nameFoundTagNotFound_returnsTrue() throws IllegalValueException {
        assertTrue(CHICKEN_DINNER_PREDICATE
                .test(new FoodBuilder()
                        .withName(new Name("Chicken McWing"))
                        .withTags("Lunch")
                        .build()));
    }

    @Test
    public void test_nameFoundTagFound_returnsTrue() throws IllegalValueException {
        assertTrue(CHICKEN_DINNER_PREDICATE
                .test(new FoodBuilder()
                        .withName(new Name("Chicken McWing"))
                        .withTags("Dinner")
                        .build()));
    }

    @Test
    public void test_nameNotFoundTagFound_returnsTrue() throws IllegalValueException {
        assertTrue(CHICKEN_DINNER_PREDICATE
                .test(new FoodBuilder()
                        .withName(new Name("Big Mac"))
                        .withTags("Dinner")
                        .build()));
    }

    @Test
    public void test_nameNotFoundTagNotFound_returnsFalse() throws IllegalValueException {
        FoodContainsKeywordsPredicate predicate =
                new FoodContainsKeywordsPredicate(CHICKEN_LUNCH_LIST);
        assertFalse(predicate.test(new FoodBuilder().withName(new Name("Big Mac")).withTags("Dinner").build()));
    }
}
