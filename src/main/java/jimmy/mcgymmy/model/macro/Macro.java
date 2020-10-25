package jimmy.mcgymmy.model.macro;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import jimmy.mcgymmy.commons.exceptions.IllegalValueException;

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
     * @throws IllegalValueException if the format of the arguments are wrong.
     */
    public Macro(String name, String[] macroArguments, String[] commands) throws IllegalValueException {
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

    private static Options parseOptions(String[] macroArgs) throws IllegalValueException {
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
            throw new IllegalValueException("Wrong format for macros.");
        }
        return options;
    }
}
