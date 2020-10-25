package jimmy.mcgymmy.logic.macro;

import java.util.Arrays;

import jimmy.mcgymmy.commons.exceptions.IllegalValueException;
import jimmy.mcgymmy.logic.commands.CommandExecutable;
import jimmy.mcgymmy.logic.commands.CommandResult;
import jimmy.mcgymmy.logic.commands.exceptions.CommandException;
import jimmy.mcgymmy.model.Model;
import jimmy.mcgymmy.model.macro.Macro;
import jimmy.mcgymmy.model.macro.MacroList;

public class NewMacroCommand implements CommandExecutable {
    private final String argumentDeclaration;
    private final String[] statements;

    /**
     * Constructor for this unique command.
     * @param argumentDeclaration The macro declaration containing the name and arguments to the macro.
     *                            e.g. "macro poop n m".
     * @param statements The commands to be executed in the macro.
     */
    public NewMacroCommand(String argumentDeclaration, String[] statements) {
        this.argumentDeclaration = argumentDeclaration;
        this.statements = statements;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        String[] splitDeclaration = argumentDeclaration.split("\\s+");
        try {
            Macro newMacro = new Macro(
                    splitDeclaration[1],
                    Arrays.copyOfRange(splitDeclaration, 2, splitDeclaration.length),
                    this.statements);
            MacroList macroList = model.getMacroList();
            model.setMacroList(macroList.withNewMacro(newMacro));
            return new CommandResult(newMacro.getName() + " successfully added.");
        } catch (IllegalValueException e) {
            throw new CommandException(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            // TODO better errors?
            throw new CommandException("Error: missing macro name.");
        }
    }

    public String getArgumentDeclaration() {
        return argumentDeclaration;
    }

    public String[] getStatements() {
        return statements;
    }
}
