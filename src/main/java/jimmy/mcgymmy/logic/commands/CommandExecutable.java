package jimmy.mcgymmy.logic.commands;

import jimmy.mcgymmy.logic.commands.exceptions.CommandException;
import jimmy.mcgymmy.model.Model;

/**
 * Interface for generalized Commands run by the user.
 */
public interface CommandExecutable {

    /**
     * Executes the command and returns the result message.
     *
     * @param model {@code Model} which the command should operate on.
     * @return feedback message of the operation result for display
     * @throws CommandException If an error occurs during command execution.
     */
    CommandResult execute(Model model) throws CommandException;
}
