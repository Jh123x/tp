package jimmy.mcgymmy.logic.macro;

import static jimmy.mcgymmy.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jimmy.mcgymmy.logic.commands.exceptions.CommandException;
import jimmy.mcgymmy.model.Model;
import jimmy.mcgymmy.model.ModelManager;
import jimmy.mcgymmy.model.macro.MacroList;

// Integration tests
public class NewMacroCommandTest {
    private static final String[] ARGUMENTS = new String[] {"list"};
    private static final String[] ARGUMENTS_2 = new String[] {"list2"};
    private static final String MACRO_STRING_DEFAULT = "macro";
    private static final String MACRO_STRING_HELP = "macro help";
    private static final String MACRO_STRING_TEST = "macro test";
    private static final NewMacroCommand MACRO_COMMAND_DEFAULT = new NewMacroCommand(MACRO_STRING_DEFAULT, ARGUMENTS);
    private static final NewMacroCommand MACRO_COMMAND_HELP = new NewMacroCommand(MACRO_STRING_HELP, ARGUMENTS);
    private static final NewMacroCommand MACRO_COMMAND_TEST = new NewMacroCommand(MACRO_STRING_TEST, ARGUMENTS);
    private static final NewMacroCommand MACRO_COMMAND_DIFFERENT =
            new NewMacroCommand(MACRO_STRING_DEFAULT, ARGUMENTS_2);
    private final Model model = new ModelManager();

    @BeforeEach
    public void setUp() {
        model.setMacroList(new MacroList());
    }

    @Test
    public void noMacroDeclaration_throwsCommandException() {
        assertThrows(CommandException.class, () -> MACRO_COMMAND_DEFAULT.execute(model));
    }

    @Test
    public void duplicateDeclaration_throwsCommandException() {
        assertThrows(CommandException.class, () -> MACRO_COMMAND_HELP.execute(model));
    }

    @Test
    public void validDeclaration_addsToList() throws Exception {
        MACRO_COMMAND_TEST.execute(model);
        assertTrue(model.getMacroList().hasMacro("test"));
    }

    @Test
    public void checkStatementEquality() {
        //Same Commands should be equal
        assertEquals(MACRO_COMMAND_HELP.getStatements(), ARGUMENTS);
        assertEquals(MACRO_COMMAND_HELP.getStatements(), MACRO_COMMAND_TEST.getStatements());

        //Different Commands should be different
        assertNotEquals(MACRO_COMMAND_HELP.getStatements(), MACRO_COMMAND_DIFFERENT.getStatements());
    }
}
