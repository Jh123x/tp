package jimmy.mcgymmy.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Set;

import jimmy.mcgymmy.commons.core.Messages;
import jimmy.mcgymmy.commons.core.index.Index;
import jimmy.mcgymmy.logic.commands.exceptions.CommandException;
import jimmy.mcgymmy.logic.parser.ParserUtil;
import jimmy.mcgymmy.logic.parser.parameter.OptionalParameter;
import jimmy.mcgymmy.logic.parser.parameter.Parameter;
import jimmy.mcgymmy.model.Model;
import jimmy.mcgymmy.model.date.Date;
import jimmy.mcgymmy.model.food.Carbohydrate;
import jimmy.mcgymmy.model.food.Fat;
import jimmy.mcgymmy.model.food.Food;
import jimmy.mcgymmy.model.food.Name;
import jimmy.mcgymmy.model.food.Protein;
import jimmy.mcgymmy.model.tag.Tag;

/**
 * Edits the details of an existing food in mcgymmy.
 */
public class EditCommand extends Command {
    public static final String COMMAND_WORD = "edit";
    public static final String SHORT_DESCRIPTION = "Edit the selected food item.";

    public static final String MESSAGE_FOOD_NO_CHANGE = "The food item does not change:\n%1$s";
    public static final String MESSAGE_EDIT_FOOD_SUCCESS = "Edited Food:\n%1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";

    private Parameter<Index> indexParameter = this.addParameter(
            "index",
            "",
            "index number used in the displayed food list.",
            "2",
            ParserUtil::parseIndex
    );
    private OptionalParameter<Name> nameParameter = this.addOptionalParameter(
            "name",
            "n",
            "Name of food to add",
            "John Doe",
            ParserUtil::parseName
    );
    private OptionalParameter<Protein> proteinParameter = this.addOptionalParameter(
            "protein value",
            "p",
            "Protein value of food (g)",
            "10",
            ParserUtil::parseProtein
    );
    private OptionalParameter<Fat> fatParameter = this.addOptionalParameter(
            "fat value",
            "f",
            "Fat value of food (g)",
            "10",
            ParserUtil::parseFat
    );
    private OptionalParameter<Carbohydrate> carbParameter = this.addOptionalParameter(
            "carb value",
            "c",
            "Carbohydrate value of food (g)",
            "10",
            ParserUtil::parseCarb
    );
    private OptionalParameter<Date> dateParameter = this.addOptionalParameter(
        "date",
        "d",
        "Date on which the food is consumed",
        "20-04-2020",
        ParserUtil::parseDate
    );

    void setParameters(Parameter<Index> indexParameter, OptionalParameter<Name> nameParameter,
                       OptionalParameter<Protein> proteinParameter, OptionalParameter<Fat> fatParameter,
                       OptionalParameter<Carbohydrate> carbParameter, OptionalParameter<Date> dateParameter) {
        this.indexParameter = indexParameter;
        this.nameParameter = nameParameter;
        this.proteinParameter = proteinParameter;
        this.fatParameter = fatParameter;
        this.carbParameter = carbParameter;
        this.dateParameter = dateParameter;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Food> lastShownList = model.getFilteredFoodList();
        Index index = indexParameter.consume();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_FOOD_DISPLAYED_INDEX);
        }

        Food foodToEdit = lastShownList.get(index.getZeroBased());

        if (nameParameter.getValue().isEmpty() && proteinParameter.getValue().isEmpty()
                && fatParameter.getValue().isEmpty() && carbParameter.getValue().isEmpty()
                && dateParameter.getValue().isEmpty()) {
            throw new CommandException(MESSAGE_NOT_EDITED);
        }

        Name newName = this.nameParameter.getValue().orElseGet(foodToEdit::getName);
        Protein newProtein = this.proteinParameter.getValue().orElseGet(foodToEdit::getProtein);
        Fat newFat = this.fatParameter.getValue().orElseGet(foodToEdit::getFat);
        Carbohydrate newCarb = this.carbParameter.getValue().orElseGet(foodToEdit::getCarbs);
        Date newDate = this.dateParameter.getValue().orElseGet(foodToEdit::getDate);
        Set<Tag> tags = foodToEdit.getTags();

        Food editedFood = new Food(newName, newProtein, newFat, newCarb, tags, newDate);

        if (foodToEdit.equals(editedFood)) {
            return new CommandResult(String.format(MESSAGE_FOOD_NO_CHANGE, editedFood));
        }

        model.setFood(index, editedFood);
        return new CommandResult(String.format(MESSAGE_EDIT_FOOD_SUCCESS, editedFood));
    }
}
