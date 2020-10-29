package jimmy.mcgymmy.logic.parser;

import java.util.Arrays;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;

import jimmy.mcgymmy.commons.core.Messages;
import jimmy.mcgymmy.logic.commands.CommandExecutable;
import jimmy.mcgymmy.logic.macro.MacroRunner;
import jimmy.mcgymmy.logic.macro.NewMacroCommand;
import jimmy.mcgymmy.logic.parser.exceptions.ParseException;
import jimmy.mcgymmy.model.macro.Macro;
import jimmy.mcgymmy.model.macro.MacroList;

/**
 * Parser for all McGymmy commands.
 */
public class McGymmyParser {
    private MacroList macroList;
    private final PrimitiveCommandParser primitiveCommandParser;

    /**
     * Constructor for McGymmyParser, but create a new macroList.
     */
    public McGymmyParser() {
        this(new MacroList());
    }

    /**
     * Constructor for McGymmyParser
     * @param macroList the macroList to supply
     */
    public McGymmyParser(MacroList macroList) {
        this.primitiveCommandParser = new PrimitiveCommandParser();
        this.macroList = macroList;
    }

    /**
     * Parses a raw input string from the user into an executable Command or macro.
     *
     * @param text raw input from the user
     * @return Command if parsing is successful
     * @throws ParseException if command is not found
     * @throws ParseException if a required argument to the command is not supplied
     * @throws ParseException if an argument to the command is not in the correct format
     */
    public CommandExecutable parse(String text) throws ParseException {
        ParserUtil.HeadTailString headTail = ParserUtil.HeadTailString.splitString(text);
        String commandName = headTail.getHead();
        try {
            if (commandName.equals("macro")) {
                return parseCreateMacro(text);
            } else if (this.macroList.hasMacro(commandName)) {
                return this.parseRunMacro(commandName, headTail.getTail());
            } else if (this.primitiveCommandParser.hasCommand(commandName)) {
                return this.primitiveCommandParser.parsePrimitiveCommand(commandName, headTail.getTail());
            } else {
                throw new ParseException(Messages.MESSAGE_UNKNOWN_COMMAND);
            }
        } catch (IllegalArgumentException e) {
            //Catch the error when the value of protein is > 1000 or < 0
            throw new ParseException(e.getMessage());
        }
    }

    public void setMacroList(MacroList macroList) {
        this.macroList = macroList;
    }

    public MacroList getMacroList() {
        return macroList;
    }

    /**
     * Creates a new macro using the String declaration.
     *
     * @param declaration Macro declaration string. Format in the user guide.
     * @return Macro that was created
     * @throws ParseException if declaration has the wrong format.
     */
    private CommandExecutable parseCreateMacro(String declaration) throws ParseException {
        // note: following line also trims whitespace between semicolons.
        ParserUtil.HeadTailString headTail = ParserUtil.HeadTailString.splitString(declaration, " *; *");
        String[] tailWithoutBlanks = Arrays.stream(headTail.getTail())
                .filter(s->!s.isBlank())
                .toArray(String[]::new);
        return new NewMacroCommand(headTail.getHead(), tailWithoutBlanks);
    }

    private CommandExecutable parseRunMacro(String commandName, String[] arguments) throws ParseException {
        CommandLineParser commandLineParser = new DefaultParser();
        Macro macro = this.macroList.getMacro(commandName);
        Options options = macro.getOptions();
        try {
            CommandLine args = commandLineParser.parse(options, arguments);
            return MacroRunner.asCommandInstance(macro, args);
        } catch (org.apache.commons.cli.ParseException e) {
            String formattedHelp = ParserUtil.getUsageFromHelpFormatter(commandName, "", options);
            throw new ParseException(formattedHelp);
        }
    }
}
