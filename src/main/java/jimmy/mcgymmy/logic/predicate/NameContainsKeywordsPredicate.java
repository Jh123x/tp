package jimmy.mcgymmy.logic.predicate;

import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;

import jimmy.mcgymmy.commons.core.LogsCenter;
import jimmy.mcgymmy.commons.util.StringUtil;
import jimmy.mcgymmy.model.food.Food;

/**
 * Tests that a {@code Food}'s {@code Name} matches any of the keywords given.
 */
public class NameContainsKeywordsPredicate implements Predicate<Food> {
    private static final Logger logger = LogsCenter.getLogger(NameContainsKeywordsPredicate.class);
    private final List<String> keywords;

    public NameContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Food food) {
        assert food != null : "NameContainsKeywordsPredicate -> Name cannot be null";
        try {
            return keywords.stream()
                    .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(food.getName().fullName, keyword));
        } catch (IllegalArgumentException e) {
            logger.info("ILE surfaced : " + e);
            return true;
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof NameContainsKeywordsPredicate // instanceof handles nulls
                && keywords.equals(((NameContainsKeywordsPredicate) other).keywords)); // state check
    }

}
