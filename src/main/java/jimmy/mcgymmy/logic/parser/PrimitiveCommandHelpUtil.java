package jimmy.mcgymmy.logic.parser;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.commons.cli.Options;

import jimmy.mcgymmy.logic.commands.Command;
import jimmy.mcgymmy.logic.commands.CommandExecutable;
import jimmy.mcgymmy.logic.commands.CommandResult;
import jimmy.mcgymmy.logic.commands.exceptions.CommandException;
import jimmy.mcgymmy.logic.parser.parameter.AbstractParameter;
import jimmy.mcgymmy.logic.parser.parameter.ParameterSet;

/**
 * A class to generate various help strings for primitive commands.
 */
public class PrimitiveCommandHelpUtil {
    /* In the future, instead of these constants we could create a method
    that inserts help strings for commands that are not stored in the table,
    but should also be listed using `help`. But for now, YAGNI. */
    private static final String MACRO_HELP_STRING = "Add a macro to run several commands in succession.";
    // not splitting up usage with new lines because that may confuse user.
    private static final String MACRO_USAGE_STRING = "usage: macro"
            + "\nMACRONAME FLAG_1 FLAG_2 ... ; "
            + "COMMAND_1 PARAMETERS_TO_COMMAND_1; "
            + "[COMMAND_2 PARAMETERS_TO_COMMAND_2; ...]"
            + "\n\nEXAMPLE: macro breakfast; add -n toast -c 10; add -n egg -p 10";

    private final Map<String, Supplier<Command>> commandTable;
    private final Map<String, String> commandDescriptionTable;

    PrimitiveCommandHelpUtil(Map<String, Supplier<Command>> commandTable, Map<String, String> commandDescriptionTable) {
        this.commandTable = commandTable;
        this.commandDescriptionTable = commandDescriptionTable;
    }

    /**
     * Creates the usage string using commons-cli's HelpFormatter and the createExampleCommand function.
     * @param commandName name of the command.
     * @param parameterSet parameterSet of the command.
     * @return usage string for the command.
     */
    public String getUsage(String commandName, ParameterSet parameterSet) {
        Options options = parameterSet.asOptions();
        String formattedHelp = ParserUtil.getUsageFromHelpFormatter(commandName,
                getUnnamedParameterUsage(parameterSet), options);
        return formattedHelp + "\nEXAMPLE: " + createExampleCommand(commandName, parameterSet.getParameterList());
    }

    private String getUnnamedParameterUsage(ParameterSet parameterSet) {
        return parameterSet.getUnnamedParameter()
                .map(param -> String.format("<arg> %s: %s", param.getName(), param.getDescription()))
                .orElseGet(() -> "");
    }

    private String createExampleCommand(String commandName, List<AbstractParameter> parameterList) {
        return commandName + " " + parameterList.stream()
                .map(p -> p.getFlag().equals("") ? p.getExample() : "-" + p.getFlag() + " " + p.getExample())
                .collect(Collectors.joining(" "));
    }

    private String formatAllCommandsHelp() {
        StringBuilder result = new StringBuilder("Here are all the available commands."
                + "\n\nType: 'help [COMMAND]' for more info on a specific command.\n");
        for (String commandName : commandDescriptionTable.keySet()) {
            result.append(String.format("\n%s: %s", commandName, commandDescriptionTable.get(commandName)));
        }
        result.append("\nmacro: ").append(MACRO_HELP_STRING);
        return result.toString();
    }

    /**
     * Generates a command that outputs the usage of the given command.
     * @param commandName the command to generate usage of.
     * @return Command that outputs the usage string of the command.
     */
    public CommandExecutable newHelpCommand(String commandName) {
        return model -> {
            if (commandName.equals("macro")) {
                return new CommandResult(MACRO_USAGE_STRING);
            }
            if (!commandTable.containsKey(commandName)) {
                throw new CommandException("Error: That command does not exist.");
            }
            Command usageOf = commandTable.get(commandName).get();
            return new CommandResult(getUsage(commandName, usageOf.getParameterSet()));
        };
    }

    /**
     * Returns a command that lists all available commands.
     * @return Command that outputs all available commands.
     */
    public CommandExecutable newHelpCommand() {
        return model -> new CommandResult(formatAllCommandsHelp());
    }
}
