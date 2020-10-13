package jimmy.mcgymmy.logic.commands;

import static java.util.Objects.requireNonNull;

import jimmy.mcgymmy.model.McGymmy;
import jimmy.mcgymmy.model.Model;

/**
 * Clears mcgymmy.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String SHORT_DESCRIPTION = "Deletes all food items in McGymmy.";
    public static final String MESSAGE_SUCCESS = "McGymmy has been cleared!";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.setMcGymmy(new McGymmy());
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
