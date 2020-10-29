package jimmy.mcgymmy.logic.macro;

import static jimmy.mcgymmy.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jimmy.mcgymmy.logic.commands.exceptions.CommandException;
import jimmy.mcgymmy.model.Model;
import jimmy.mcgymmy.model.ModelManager;
import jimmy.mcgymmy.model.macro.MacroList;

// Integration tests
public class NewMacroCommandTest {
    private final Model model = new ModelManager();

    @BeforeEach
    public void setUp() {
        model.setMacroList(new MacroList());
    }

    @Test
    public void noMacroDeclaration_throwsCommandException() {
        NewMacroCommand newMacroCommand = new NewMacroCommand("macro", new String[] {"list"});
        assertThrows(CommandException.class, () -> newMacroCommand.execute(model));
    }

    @Test
    public void duplicateDeclaration_throwsCommandException() {
        NewMacroCommand newMacroCommand = new NewMacroCommand("macro help", new String[] {"list"});
        assertThrows(CommandException.class, () -> newMacroCommand.execute(model));
    }

    @Test
    public void validDeclaration_addsToList() throws Exception {
        NewMacroCommand newMacroCommand = new NewMacroCommand("macro test", new String[] {"list"});
        newMacroCommand.execute(model);
        assertTrue(model.getMacroList().hasMacro("test"));
    }
}
