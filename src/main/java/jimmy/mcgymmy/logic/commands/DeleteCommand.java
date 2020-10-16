package jimmy.mcgymmy.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import jimmy.mcgymmy.commons.core.Messages;
import jimmy.mcgymmy.commons.core.index.Index;
import jimmy.mcgymmy.logic.commands.exceptions.CommandException;
import jimmy.mcgymmy.logic.parser.ParserUtil;
import jimmy.mcgymmy.logic.parser.parameter.Parameter;
import jimmy.mcgymmy.model.Model;
import jimmy.mcgymmy.model.food.Food;

/**
 * Deletes a food identified using it's displayed index from mcgymmy.
 */
public class DeleteCommand extends Command {
    public static final String COMMAND_WORD = "delete";
    public static final String SHORT_DESCRIPTION = "Delete the selected food item.";

    public static final String MESSAGE_DELETE_FOOD_SUCCESS = "Deleted Food: %1$s";

    private Parameter<Index> indexParameter = this.addParameter(
            "index",
            "",
            "index number used in the displayed food list.",
            "2",
            ParserUtil::parseIndex
    );

    void setParameters(Parameter<Index> indexParameter) {
        this.indexParameter = indexParameter;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Food> lastShownList = model.getFilteredFoodList();
        Index targetIndex = indexParameter.consume();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_FOOD_DISPLAYED_INDEX);
        }

        Food foodToDelete = lastShownList.get(targetIndex.getZeroBased());
        model.deleteFood(targetIndex);
        return new CommandResult(String.format(MESSAGE_DELETE_FOOD_SUCCESS, foodToDelete));
    }
}
