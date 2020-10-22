package jimmy.mcgymmy.logic.macro;

import static jimmy.mcgymmy.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import jimmy.mcgymmy.logic.commands.exceptions.CommandException;
import jimmy.mcgymmy.model.Model;
import jimmy.mcgymmy.model.ModelManager;

// Integration tests
public class NewMacroCommandTest {
    private Model model = new ModelManager();

    @Test
    public void noMacroDeclaration_throwsCommandException() {
        MacroList macroList = new MacroList();
        NewMacroCommand newMacroCommand = new NewMacroCommand(macroList, "macro", new String[]{"list"});
        assertThrows(CommandException.class, () -> newMacroCommand.execute(model));
    }

    @Test
    public void duplicateDeclaration_throwsCommandException() {
        MacroList macroList = new MacroList();
        NewMacroCommand newMacroCommand = new NewMacroCommand(macroList, "macro help", new String[]{"list"});
        assertThrows(CommandException.class, () -> newMacroCommand.execute(model));
    }

    @Test
    public void validDeclaration_addsToList() throws Exception {
        MacroList macroList = new MacroList();
        NewMacroCommand newMacroCommand = new NewMacroCommand(macroList, "macro test", new String[]{"list"});
        newMacroCommand.execute(model);
        assertTrue(macroList.hasMacro("test"));
    }
}
