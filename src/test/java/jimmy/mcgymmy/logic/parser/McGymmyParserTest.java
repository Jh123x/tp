package jimmy.mcgymmy.logic.parser;

import static jimmy.mcgymmy.testutil.Assert.assertThrows;
import static jimmy.mcgymmy.testutil.TypicalMacros.TEST_MACRO;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import jimmy.mcgymmy.logic.commands.AddCommand;
import jimmy.mcgymmy.logic.commands.CommandExecutable;
import jimmy.mcgymmy.logic.commands.ListCommand;
import jimmy.mcgymmy.logic.macro.NewMacroCommand;
import jimmy.mcgymmy.logic.parser.exceptions.ParseException;
import jimmy.mcgymmy.model.macro.Macro;

// Integration tests for the parser
public class McGymmyParserTest {
    @Test
    public void primitiveCommands_getParsed() throws Exception {
        McGymmyParser mcGymmyParser = new McGymmyParser();
        // Equivalence partition: command without arguments
        CommandExecutable listCommand = mcGymmyParser.parse("list");
        // Equivalence partition: command with arguments
        CommandExecutable addCommand = mcGymmyParser.parse("add -n poop -p 200");
        assertTrue(listCommand instanceof ListCommand);
        assertTrue(addCommand instanceof AddCommand);
    }

    @Test
    public void macroDefinitions_getParsedCorrectly() throws Exception {
        McGymmyParser mcGymmyParser = new McGymmyParser();
        // testing that it filters out the empty commands, i.e. the ';;', ';  ;', etc
        CommandExecutable output = mcGymmyParser.parse("macro test  ;list;; exit  ;;");
        assertTrue(output instanceof NewMacroCommand);
        NewMacroCommand newMacroCommand = (NewMacroCommand) output;
        assertEquals("macro test", newMacroCommand.getArgumentDeclaration());
        assertArrayEquals(new String[] {"list", "exit"}, newMacroCommand.getStatements());
    }

    @Test
    public void registeredMacroCommand_getsParsed() throws Exception {
        McGymmyParser mcGymmyParser = new McGymmyParser();
        Macro dummyMacro = new Macro("test", new String[] {}, new String[] {});
        mcGymmyParser.setMacroList(mcGymmyParser.getMacroList().withNewMacro(dummyMacro));
        // this should not throw any errors
        mcGymmyParser.parse("test");
    }

    @Test
    public void invalidMacro_showsUsageToUser() throws Exception {
        McGymmyParser mcGymmyParser = new McGymmyParser();
        mcGymmyParser.setMacroList(mcGymmyParser.getMacroList().withNewMacro(TEST_MACRO));
        try {
            mcGymmyParser.parse("test");
            fail("ParseException not thrown");
        } catch (ParseException e) {
            assertTrue(e.getMessage().contains("Missing required"));
        }
    }

    @Test
    public void unknownCommand_parseError() {
        McGymmyParser mcGymmyParser = new McGymmyParser();
        assertThrows(ParseException.class, () -> mcGymmyParser.parse("aisudbhaiuo"));
    }
}
