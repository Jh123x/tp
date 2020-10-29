package jimmy.mcgymmy.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;

import jimmy.mcgymmy.logic.commands.AddCommand;
import jimmy.mcgymmy.logic.commands.ClearCommand;
import jimmy.mcgymmy.logic.commands.Command;
import jimmy.mcgymmy.logic.commands.DeleteCommand;
import jimmy.mcgymmy.logic.commands.EditCommand;
import jimmy.mcgymmy.logic.commands.ExitCommand;
import jimmy.mcgymmy.logic.commands.FindCommand;
import jimmy.mcgymmy.logic.commands.ListCommand;
import jimmy.mcgymmy.logic.parser.exceptions.ParseException;
import jimmy.mcgymmy.model.Model;
import jimmy.mcgymmy.model.ModelManager;

public class PrimitiveCommandParserCommandsTest {
    private final PrimitiveCommandParser parser = new PrimitiveCommandParser();

    @Test
    public void defaultCommands_added() {
        // if this breaks, you need to add the command in McGymmyParser.addDefaultCommands
        String[] commands = {"add", "edit", "delete", "clear", "find", "delete", "list"};
        Set<String> registeredCommands = parser.getRegisteredCommands();
        for (String command : commands) {
            assertTrue(registeredCommands.contains(command));
        }
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parse(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parse(ListCommand.COMMAND_WORD) instanceof ListCommand);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parse(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        String searchString = "haiufas iuaohbfiasduo";
        Command command = (Command) parser.parse(FindCommand.COMMAND_WORD + " " + searchString);
        assertTrue(command instanceof FindCommand);
        assertEquals(CommandParserTestUtil.commandParameterValue(command, ""), Optional.of(searchString));
    }

    @Test
    public void parseCommand_delete() throws Exception {
        String indexString = "68";
        Command command = (Command) parser.parse(DeleteCommand.COMMAND_WORD + " " + indexString);
        assertTrue(command instanceof DeleteCommand);
        assertEquals(CommandParserTestUtil.commandParameterValue(command, ""), Optional.of(indexString));
    }

    @Test
    public void parseCommand_deleteEmptyParameter_failure() {
        assertThrows(ParseException.class, () -> parser.parse(DeleteCommand.COMMAND_WORD));
    }

    @Test
    public void parseCommand_add() throws Exception {
        String nameString = "Chicken Rice";
        String proteinString = "991";
        String fatString = "99";
        String carbString = "555";
        String commandString = String.format("%s -n %s -p %s -f %s -c %s",
                AddCommand.COMMAND_WORD,
                nameString,
                proteinString,
                fatString,
                carbString);
        Command command = (Command) parser.parse(commandString);
        assertTrue(command instanceof AddCommand);
        assertEquals(CommandParserTestUtil.commandParameterValue(command, "n"), Optional.of(nameString));
        assertEquals(CommandParserTestUtil.commandParameterValue(command, "p"), Optional.of(proteinString));
        assertEquals(CommandParserTestUtil.commandParameterValue(command, "f"), Optional.of(fatString));
        assertEquals(CommandParserTestUtil.commandParameterValue(command, "c"), Optional.of(carbString));
    }

    @Test
    public void parseCommand_addMissingParameters_failure() {
        String commandString = String.format("%s -p 123", AddCommand.COMMAND_WORD);
        assertThrows(ParseException.class, () -> parser.parse(commandString));
    }

    @Test
    public void parseCommand_edit() throws Exception {
        String indexString = "68";
        String proteinString = "123";
        String fatString = "456";
        String carbString = "789";
        String commandString = String.format("%s %s -p %s -f %s -c %s",
                EditCommand.COMMAND_WORD,
                indexString,
                proteinString,
                fatString,
                carbString);
        Command command = (Command) parser.parse(commandString);
        assertTrue(command instanceof EditCommand);
        assertEquals(CommandParserTestUtil.commandParameterValue(command, ""), Optional.of(indexString));
        assertEquals(CommandParserTestUtil.commandParameterValue(command, "n"), Optional.empty());
        assertEquals(CommandParserTestUtil.commandParameterValue(command, "p"), Optional.of(proteinString));
        assertEquals(CommandParserTestUtil.commandParameterValue(command, "f"), Optional.of(fatString));
        assertEquals(CommandParserTestUtil.commandParameterValue(command, "c"), Optional.of(carbString));
    }

    @Test
    public void parseCommand_editMissingIndex_failure() {
        String commandString = String.format("%s -n poop", EditCommand.COMMAND_WORD);
        assertThrows(ParseException.class, () -> parser.parse(commandString));
    }

    @Test
    public void parseCommand_helpNoArguments() throws Exception {
        Model model = new ModelManager();
        PrimitiveCommandHelpUtil helpUtil = new PrimitiveCommandHelpUtil(
                parser.getCommandTable(),
                parser.getCommandDescriptionTable());
        assertEquals(parser.parse("help").execute(model), helpUtil.newHelpCommand().execute(model));
    }

    @Test
    public void parseCommand_helpCommands() throws Exception {
        // Should suffice to check "help [COMMAND]" works.
        Model model = new ModelManager();
        PrimitiveCommandHelpUtil helpUtil = new PrimitiveCommandHelpUtil(
                parser.getCommandTable(),
                parser.getCommandDescriptionTable());
        for (String commandName : parser.getRegisteredCommands()) {
            if (commandName.equals("help")) {
                continue;
            }
            assertEquals(parser.parse("help " + commandName).execute(model),
                    helpUtil.newHelpCommand(commandName).execute(model));
        }
    }
}
