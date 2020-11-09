package jimmy.mcgymmy.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;
import java.util.logging.Logger;

import jimmy.mcgymmy.commons.core.LogsCenter;
import jimmy.mcgymmy.commons.core.Messages;
import jimmy.mcgymmy.logic.parser.parameter.OptionalParameter;
import jimmy.mcgymmy.logic.predicate.DatePredicate;
import jimmy.mcgymmy.logic.predicate.FoodContainsKeywordsPredicate;
import jimmy.mcgymmy.logic.predicate.NameContainsKeywordsPredicate;
import jimmy.mcgymmy.logic.predicate.TagContainsKeywordsPredicate;
import jimmy.mcgymmy.model.Model;
import jimmy.mcgymmy.model.food.Food;

/**
 * Finds and lists all food items in McGymmy which fit the given parameters (criteria).
 * Keyword and name matching is case insensitive, while tag matching is case sensitive.
 */
public class FindCommand extends Command {
    public static final String COMMAND_WORD = "find";
    public static final String SHORT_DESCRIPTION = "Filters the food list by the provided parameters.";
    private static final Logger logger = LogsCenter.getLogger(FindCommand.class);

    private OptionalParameter<FoodContainsKeywordsPredicate> foodPredicateParameter = this.addOptionalParameter(
            "keyword",
            "",
            "keywords (case-insensitive).",
            "chicken beef mutton", (s) -> new FoodContainsKeywordsPredicate(Arrays.asList(s.split("\\s+")))
    );
    private OptionalParameter<NameContainsKeywordsPredicate> namePredicateParameter = this.addOptionalParameter(
            "name",
            "n",
            "Name of the Food (case-insensitive)",
            "Cereal", (s) -> new NameContainsKeywordsPredicate(Arrays.asList(s.split("\\s+")))
    );
    private OptionalParameter<TagContainsKeywordsPredicate> tagPredicateParameter = this.addOptionalParameter(
            "tag",
            "t",
            "Tag associated with the Food (case-sensitive)",
            "Lunch", (s) -> new TagContainsKeywordsPredicate(Arrays.asList(s.split("\\s+")))
    );
    private OptionalParameter<DatePredicate> datePredicateParameter = this.addOptionalParameter(
            "date",
            "d",
            "Date associated with the Food",
            "20-04-2020", DatePredicate::new
    );

    void setParameters(OptionalParameter<FoodContainsKeywordsPredicate> foodPredicateParameter,
                       OptionalParameter<NameContainsKeywordsPredicate> namePredicateParameter,
                       OptionalParameter<TagContainsKeywordsPredicate> tagPredicateParameter,
                       OptionalParameter<DatePredicate> datePredicateParameter) {
        this.foodPredicateParameter = foodPredicateParameter;
        this.namePredicateParameter = namePredicateParameter;
        this.tagPredicateParameter = tagPredicateParameter;
        this.datePredicateParameter = datePredicateParameter;
    }

    @Override
    public CommandResult execute(Model model) {
        logger.fine("Executing find command");
        requireNonNull(model);

        // checks whether the optional parameters were called, else assign the value of null
        FoodContainsKeywordsPredicate foodPredicate = foodPredicateParameter.getValue().orElse(null);
        NameContainsKeywordsPredicate namePredicate = namePredicateParameter.getValue().orElse(null);
        TagContainsKeywordsPredicate tagPredicate = tagPredicateParameter.getValue().orElse(null);
        DatePredicate datePredicate = datePredicateParameter.getValue().orElse(null);

        // create a combined predicate which by default returns true
        Predicate<Food> combinedPredicate = food -> true;
        ArrayList<Predicate<Food>> predicateList = new ArrayList<>(Arrays.asList(
                foodPredicate, namePredicate, tagPredicate, datePredicate));

        // combine the other predicates (parameters) to combinedPredicate if they are not null
        for (Predicate<Food> currentPredicate : predicateList) {
            if (currentPredicate != null) {
                combinedPredicate = combinedPredicate.and(currentPredicate);
            }
        }

        model.updateFilteredFoodList(combinedPredicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_FOOD_LISTED_OVERVIEW, model.getFilteredFoodList().size()));
    }
}
