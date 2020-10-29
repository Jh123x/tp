package jimmy.mcgymmy.logic.commands;

import jimmy.mcgymmy.logic.commands.exceptions.CommandException;
import jimmy.mcgymmy.logic.parser.parameter.Parameter;
import jimmy.mcgymmy.model.Model;
import jimmy.mcgymmy.model.macro.MacroList;

public class RemoveMacroCommand extends Command {
    public static final String COMMAND_WORD = "remmacro";
    public static final String SHORT_DESCRIPTION = "Remove a macro in McGymmy.";
    public static final String MESSAGE_SUCCESS = "Macro successfully removed: \n%1$s";

    private Parameter<String> commandNameParameter = this.addParameter(
            "which",
            "",
            "Delete the specific macro",
            "deleteMatching"
    );

    void setParameters(Parameter<String> commandNameParameter) {
        this.commandNameParameter = commandNameParameter;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        MacroList macroList = model.getMacroList();
        String macroName = commandNameParameter.consume();

        if (!macroList.hasMacro(macroName)) {
            throw new CommandException(macroName + " is not an existing macro.");
        }

        model.setMacroList(macroList.withoutMacro(macroName));
        return new CommandResult(String.format(MESSAGE_SUCCESS, macroName));
    }
}
