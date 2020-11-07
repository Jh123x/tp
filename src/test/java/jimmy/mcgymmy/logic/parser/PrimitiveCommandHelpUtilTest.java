package jimmy.mcgymmy.logic.parser;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jimmy.mcgymmy.logic.commands.CommandResult;
import jimmy.mcgymmy.model.Model;
import jimmy.mcgymmy.model.ModelManager;

// integration tests for HelpCommand
public class PrimitiveCommandHelpUtilTest {
    private PrimitiveCommandHelpUtil helpUtil;
    private Model model;

    @BeforeEach
    public void setUp() {
        PrimitiveCommandParser parser = new PrimitiveCommandParser();
        helpUtil = new PrimitiveCommandHelpUtil(parser.getCommandTable(), parser.getCommandDescriptionTable());
        model = new ModelManager();
    }

    @Test
    public void noArguments_correctOutput() throws Exception {
        CommandResult commandResult = helpUtil.newHelpCommand().execute(model);
        /* Checking for contained keywords: If we check for the whole string,
           minor format changes might break the test. Checking for keywords
           should suffice. */
        // Check if 'for more info...' string is included
        assertTrue(commandResult.getFeedbackToUser().contains("help [COMMAND]"));
        // Equivalence partition: commands in commandTable
        assertTrue(commandResult.getFeedbackToUser().contains("add: "));
        assertTrue(commandResult.getFeedbackToUser().contains("list: "));
        // Equivalence partition: special case macro command
        assertTrue(commandResult.getFeedbackToUser().contains("macro: "));
    }

    @Test
    public void argumentFromCommandTable_correctOutput() throws Exception {
        // Equivalence partition: checking 1 command from the table should suffice.
        CommandResult commandResult = helpUtil.newHelpCommand("add").execute(model);
        // Check 'usage:' is included
        assertTrue(commandResult.getFeedbackToUser().contains("usage"));
        // Check if parameter string for 1 parameter is included (same partition as other parameters).
        assertTrue(commandResult.getFeedbackToUser().contains("-n"));
        // Check 'EXAMPLE' is included
        assertTrue(commandResult.getFeedbackToUser().contains("EXAMPLE"));
    }

    @Test
    public void argumentIsMacro_correctOutput() throws Exception {
        // Equivalence partition: checking special case of macro command.
        CommandResult commandResult = helpUtil.newHelpCommand("macro").execute(model);
        // Check 'usage:' is included
        assertTrue(commandResult.getFeedbackToUser().contains("usage"));
        // Check if format string is included.
        assertTrue(commandResult.getFeedbackToUser().contains("MACRONAME"));
        // Check 'EXAMPLE' is included
        assertTrue(commandResult.getFeedbackToUser().contains("EXAMPLE"));
    }
}
