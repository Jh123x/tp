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
import jimmy.mcgymmy.testutil.FoodBuilder;

class TagContainsKeywordsPredicateTest {

    private static final List<String> LUNCH_KEYWORD = Collections.singletonList("Lunch");
    private static final List<String> EMPTY_KEYWORD = Collections.singletonList("");
    private static final List<String> ABC_KEYWORD = Collections.singletonList("A b c");
    private static final List<String> FIRST_SECOND_KEYWORD = Arrays.asList("first", "second");
    private static final List<String> LUNCH_DINNER_KEYWORD = Arrays.asList("Lunch", "Dinner");
    private static final List<String> MULTIPLE_KEYWORDS = Arrays.asList("AB C", "Lunch", "", "Breakfast");
    private static final TagContainsKeywordsPredicate ABC_PREDICATE = new TagContainsKeywordsPredicate(ABC_KEYWORD);
    private static final TagContainsKeywordsPredicate LUNCH_PREDICATE = new TagContainsKeywordsPredicate(LUNCH_KEYWORD);
    private static final TagContainsKeywordsPredicate LUNCH_PREDICATE_COPY =
            new TagContainsKeywordsPredicate(LUNCH_KEYWORD);

    private static final TagContainsKeywordsPredicate EMPTY_PREDICATE =
            new TagContainsKeywordsPredicate(EMPTY_KEYWORD);

    private static final TagContainsKeywordsPredicate LUNCH_DINNER_PREDICATE =
            new TagContainsKeywordsPredicate(LUNCH_DINNER_KEYWORD);

    private static final TagContainsKeywordsPredicate FIRST_SECOND_PREDICATE =
            new TagContainsKeywordsPredicate(FIRST_SECOND_KEYWORD);

    private static final TagContainsKeywordsPredicate MULTIPLE_KEYWORDS_PREDICATE =
            new TagContainsKeywordsPredicate(MULTIPLE_KEYWORDS);

    @Test
    public void equals() {


        // same object -> returns true
        assertEquals(LUNCH_PREDICATE, LUNCH_PREDICATE);

        // same values -> returns true
        assertEquals(LUNCH_PREDICATE_COPY, LUNCH_PREDICATE);

        // different types -> returns false
        assertNotEquals(LUNCH_PREDICATE, 1);

        // null -> returns false
        assertNotEquals(LUNCH_PREDICATE, null);

        // different food -> returns false
        assertNotEquals(FIRST_SECOND_PREDICATE, LUNCH_PREDICATE);
    }

    @Test
    public void test_foodContainsTagSameAsKeywords_returnsTrue() throws IllegalValueException {
        // One keyword
        assertTrue(LUNCH_PREDICATE.test(new FoodBuilder().withTags("Lunch").build()));
        assertTrue(LUNCH_PREDICATE.test(new FoodBuilder().withTags("Dinner", "Lunch").build()));

        // Multiple keywords
        assertTrue(LUNCH_DINNER_PREDICATE.test(new FoodBuilder().withTags("Lunch").build()));
        assertTrue(LUNCH_DINNER_PREDICATE.test(new FoodBuilder().withTags("Dinner").build()));
        assertTrue(LUNCH_DINNER_PREDICATE.test(new FoodBuilder().withTags("Dinner", "Lunch").build()));
        assertTrue(LUNCH_DINNER_PREDICATE.test(new FoodBuilder().withTags("Dinner", "Breakfast", "Lunch").build()));
    }

    @Test
    public void test_foodDoesNotContainsTagSameAsKeywords_returnsFalse() throws IllegalValueException {
        // food contains one tag
        assertFalse(LUNCH_PREDICATE.test(new FoodBuilder().withTags("Dinner").build()));
        // food contains multiple tags
        assertFalse(LUNCH_PREDICATE.test(new FoodBuilder().withTags("Breakfast", "Dinner").build()));
    }

    @Test
    public void test_cannotCreateTagWithKeyword_returnFalse() throws IllegalValueException {
        assertFalse(EMPTY_PREDICATE.test(new FoodBuilder().withTags("Dinner").build()));
        assertFalse(ABC_PREDICATE.test(new FoodBuilder().withTags("Dinner").build()));
    }

    @Test
    public void test_cannotCreateTagWithSomeKeywords() throws IllegalValueException {
        // food contains some of the valid tag -> returns false
        assertTrue(MULTIPLE_KEYWORDS_PREDICATE.test(new FoodBuilder().withTags("Dinner", "Lunch").build()));
        // food contains none of the valid tag -> returns false
        assertFalse(MULTIPLE_KEYWORDS_PREDICATE.test(new FoodBuilder().withTags("Dinner").build()));
    }
}
