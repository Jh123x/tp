package jimmy.mcgymmy.logic.parser;

import static jimmy.mcgymmy.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jimmy.mcgymmy.commons.core.Messages;
import jimmy.mcgymmy.logic.commands.Command;
import jimmy.mcgymmy.logic.commands.CommandResult;
import jimmy.mcgymmy.logic.commands.exceptions.CommandException;
import jimmy.mcgymmy.logic.parser.exceptions.ParseException;
import jimmy.mcgymmy.logic.parser.parameter.OptionalParameter;
import jimmy.mcgymmy.logic.parser.parameter.Parameter;
import jimmy.mcgymmy.model.Model;

public class PrimitiveCommandParserTest {
    public static final String DUMMY_VALUE_1 = "abc";
    public static final String DUMMY_VALUE_2 = "cde";
    public static final String DUMMY_VALUE_3 = "fgh";
    private final PrimitiveCommandParser parser = new PrimitiveCommandParser();

    @BeforeEach
    public void setUp() {
        parser.addCommand("dummy", "dummy", StubCommand::new);
    }

    @AfterEach
    public void tearDown() {
        parser.removeCommand("dummy");
    }

    @Test
    public void parse_dummyCommand_correctType() throws Exception {
        String commandString = String.format("dummy -t1 %s -t2 %s -o1 %s", DUMMY_VALUE_1, DUMMY_VALUE_2, DUMMY_VALUE_3);
        Command command = (Command) parser.parse(commandString);
        assertTrue(command instanceof StubCommand);
    }

    @Test
    public void parse_dummyCommandAllParameters_success() throws Exception {
        String commandString = String.format("dummy -t1 %s -t2 %s -o1 %s", DUMMY_VALUE_1, DUMMY_VALUE_2, DUMMY_VALUE_3);
        StubCommand stubCommand = (StubCommand) parser.parse(commandString);
        assertEquals(stubCommand.getParameter1(), DUMMY_VALUE_1);
        assertEquals(stubCommand.getParameter2(), DUMMY_VALUE_2);
        assertEquals(stubCommand.getOptionalParameter(), Optional.of(DUMMY_VALUE_3));
    }

    @Test
    public void parse_dummyCommandOmitOptional_success() throws Exception {
        String commandString = String.format("dummy -t1 %s -t2 %s", DUMMY_VALUE_1, DUMMY_VALUE_2);
        StubCommand stubCommand = (StubCommand) parser.parse(commandString);
        assertEquals(stubCommand.getParameter1(), DUMMY_VALUE_1);
        assertEquals(stubCommand.getParameter2(), DUMMY_VALUE_2);
        assertEquals(stubCommand.getOptionalParameter(), Optional.empty());
    }

    @Test
    public void parse_dummyCommandSwapOrder_success() throws Exception {
        String commandString = String.format("dummy -t2 %s -o1 %s -t1 %s", DUMMY_VALUE_2, DUMMY_VALUE_3, DUMMY_VALUE_1);
        StubCommand stubCommand = (StubCommand) parser.parse(commandString);
        assertEquals(stubCommand.getParameter1(), DUMMY_VALUE_1);
        assertEquals(stubCommand.getParameter2(), DUMMY_VALUE_2);
        assertEquals(stubCommand.getOptionalParameter(), Optional.of(DUMMY_VALUE_3));
    }

    @Test
    public void parse_dummyCommandOmitRequired_failure() {
        String commandString = String.format("dummy -t2 %s", DUMMY_VALUE_2);
        assertThrows(ParseException.class, () -> parser.parse(commandString));
    }

    @Test
    public void parse_dummyCommandSpacesInParameters_accepted() throws Exception {
        String spacedValue1 = "abc def";
        String spacedValue2 = "a b c d e f g 1 2 3";
        String commandString = String.format("dummy -t1 %s -t2 %s", spacedValue1, spacedValue2);
        StubCommand stubCommand = (StubCommand) parser.parse(commandString);
        assertEquals(stubCommand.getParameter1(), spacedValue1);
        assertEquals(stubCommand.getParameter2(), spacedValue2);
        assertEquals(stubCommand.getOptionalParameter(), Optional.empty());
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, Messages.MESSAGE_UNKNOWN_COMMAND, () -> parser.parse("unknownCommand"));
    }

    static class StubCommand extends Command {
        private final Parameter<String> testParameter1 = CommandParserTestUtil.makeDummyParameter("test1", "t1");
        private final Parameter<String> testParameter2 = CommandParserTestUtil.makeDummyParameter("test2", "t2");
        private final OptionalParameter<String> testOptionalParameter =
                CommandParserTestUtil.makeDummyOptionalParameter("opt", "o1");

        {
            this.registerParameter(testParameter1);
            this.registerParameter(testParameter2);
            this.registerParameter(testOptionalParameter);
        }

        @Override
        public CommandResult execute(Model model) throws CommandException {
            return new CommandResult("nothing");
        }

        public String getParameter1() {
            return testParameter1.consume();
        }

        public String getParameter2() {
            return testParameter2.consume();
        }

        public Optional<String> getOptionalParameter() {
            return testOptionalParameter.getValue();
        }
    }
}
