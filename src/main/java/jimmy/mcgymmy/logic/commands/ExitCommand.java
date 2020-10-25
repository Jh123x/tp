package jimmy.mcgymmy.logic.commands;

import jimmy.mcgymmy.model.Model;

/**
 * Terminates the program.
 */
public class ExitCommand extends Command {

    public static final String COMMAND_WORD = "exit";
    public static final String SHORT_DESCRIPTION = "Exit McGymmy.";

    public static final String MESSAGE_EXIT_ACKNOWLEDGEMENT = "Exiting McGymmy as requested ...";

    @Override
    public CommandResult execute(Model model) {
        return new CommandResult(MESSAGE_EXIT_ACKNOWLEDGEMENT, true);
    }

}
