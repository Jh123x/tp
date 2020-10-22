package jimmy.mcgymmy.logic.macro;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import jimmy.mcgymmy.logic.commands.CommandExecutable;
import jimmy.mcgymmy.logic.commands.CommandResult;
import jimmy.mcgymmy.logic.commands.exceptions.CommandException;
import jimmy.mcgymmy.logic.parser.PrimitiveCommandParser;
import jimmy.mcgymmy.logic.parser.exceptions.ParseException;
import jimmy.mcgymmy.model.Model;

/**
 * Macro class that deals with user-created macros.
 */
public class Macro {
    private final String name;
    private final String[] rawCommands;
    private final String[] macroArguments;
    private final Options options;

    /**
     * Constructor for macros
     * TODO: more elaboration needed?
     * @param name name of the macro.
     * @param macroArguments List of arguments to the macro.
     * @param commands The commands to be run.
     * @throws ParseException if the format of the arguments are wrong.
     */
    public Macro(String name, String[] macroArguments, String[] commands) throws ParseException {
        // TODO: use these strings to serialize macro
        this(name, Macro.parseOptions(macroArguments), macroArguments, commands);
    }

    Macro(String name, Options options, String[] macroArguments, String[] commands) {
        this.name = name;
        this.options = options;
        this.rawCommands = commands;
        this.macroArguments = macroArguments;
    }

    public String getName() {
        return this.name;
    }

    public Options getOptions() {
        return this.options;
    }

    public String[] getRawCommands() {
        return rawCommands;
    }

    public String[] getMacroArguments() {
        return macroArguments;
    }

    public CommandExecutable commandInstance(CommandLine args) {
        return model -> executeWith(model, args);
    }

    /**
     * Executes the macro on the model.
     * Returns upon encountering a help or exit function
     * (i.e. does not attempt to execute any command
     * after encountering help or exit)
     * @param model Model to run the macro's commands on.
     * @param args arguments to the macro.
     * @return CommandResult produced by concatenating the messages from all the executed commands.
     * @throws CommandException if any command encounters an error.
     */
    public CommandResult executeWith(Model model, CommandLine args) throws CommandException {
        String[] rawCommands = this.substituteAll(args);
        List<String> messagesToUser = new ArrayList<>();
        List<CommandExecutable> commandExecutables = this.parseCommands(rawCommands);
        int lastCommandIndex = 0;
        try {
            for (lastCommandIndex = 0; lastCommandIndex < commandExecutables.size(); lastCommandIndex++) {
                CommandResult result = commandExecutables.get(lastCommandIndex).execute(model);
                if (result.isExit() || result.isShowHelp()) {
                    return result;
                }
                messagesToUser.add(result.getFeedbackToUser());
            }
            return new CommandResult(String.join("\n", messagesToUser));
        } catch (CommandException e) {
            /* note: not factoring out code below because its only used here and its
               purpose/what it's doing is obvious, and factoring it out will be very messy. */
            String errorLocation = "\n\nAn error occurred when executing this command:\n"
                + rawCommands[lastCommandIndex];
            String doneCommands = lastCommandIndex == 0 ? ""
                : "\n\nThe following commands executed successfully:\n"
                + String.join("\n", Arrays.copyOfRange(rawCommands, 0, lastCommandIndex));
            String notDoneCommands = lastCommandIndex + 1 == rawCommands.length ? ""
                : "\n\n The following commands were not executed:\n"
                + String.join("\n", Arrays.copyOfRange(rawCommands, lastCommandIndex + 1, rawCommands.length));

            String message = e.getMessage() + errorLocation + doneCommands + notDoneCommands;
            throw new CommandException(message);
        }
    }

    private static Options parseOptions(String[] macroArgs) throws ParseException {
        Options options = new Options();
        try {
            for (String name : macroArgs) {
                String description = "macro argument " + name;
                Option option = new Option(name, true, description);
                option.setRequired(true);
                options.addOption(option);
            }
        } catch (IllegalArgumentException e) {
            // TODO better error message
            throw new ParseException("Wrong format for macros.");
        }
        return options;
    }

    private String[] substituteAll(CommandLine args) {
        String[] output = new String[this.rawCommands.length];
        for (int i = 0; i < this.rawCommands.length; i++) {
            output[i] = substitute(args, this.rawCommands[i]);
        }
        return output;
    }

    private List<CommandExecutable> parseCommands(String[] rawCommands) throws CommandException {
        PrimitiveCommandParser parser = new PrimitiveCommandParser();
        List<CommandExecutable> commandExecutables = new ArrayList<>();
        try {
            for (String rawCommand : rawCommands) {
                commandExecutables.add(parser.parse(rawCommand));
            }
            return commandExecutables;

        } catch (ParseException e) {
            // TODO better error message
            throw new CommandException("Macro failed to parse: " + e.getMessage());
        }
    }

    private String substitute(CommandLine args, String line) {
        // TODO possibly use a StringBuilder here instead for performance.
        for (Iterator<Option> i = args.iterator(); i.hasNext(); ) {
            Option option = i.next();
            String from = option.getOpt();
            String to = args.getOptionValue(from);
            line = line.replaceAll("\\\\" + from, to);
        }

        String unusedArgs = String.join(" ", args.getArgList());
        // regex is matching for "\$".
        return line.replaceAll("\\\\\\$", unusedArgs).trim();
    }
}
