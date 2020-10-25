package jimmy.mcgymmy.logic.parser;

import static jimmy.mcgymmy.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        // only test these 3 but it should suffice
        McGymmyParser mcGymmyParser = new McGymmyParser();
        CommandExecutable listCommand = mcGymmyParser.parse("list");
        CommandExecutable addCommand = mcGymmyParser.parse("add -n poop -p 200");
        assertTrue(listCommand instanceof ListCommand);
        assertTrue(addCommand instanceof AddCommand);
    }

    @Test
    public void macroDefinitions_getParsedCorrectly() throws Exception {
        McGymmyParser mcGymmyParser = new McGymmyParser();
        // should also filter out the empty commands, i.e. the ';;', ';  ;', etc
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
    public void unknownCommand_parseError() {
        McGymmyParser mcGymmyParser = new McGymmyParser();
        assertThrows(ParseException.class, () -> mcGymmyParser.parse("aisudbhaiuo"));
    }
}
