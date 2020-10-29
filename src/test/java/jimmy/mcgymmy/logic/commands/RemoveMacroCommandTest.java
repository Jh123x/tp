package jimmy.mcgymmy.logic.commands;

import static jimmy.mcgymmy.testutil.TypicalMacros.TEST_MACRO;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jimmy.mcgymmy.logic.commands.exceptions.CommandException;
import jimmy.mcgymmy.logic.parser.CommandParserTestUtil;
import jimmy.mcgymmy.model.Model;
import jimmy.mcgymmy.model.ModelManager;
import jimmy.mcgymmy.model.UserPrefs;
import jimmy.mcgymmy.testutil.TypicalFoods;

public class RemoveMacroCommandTest {
    private Model model;

    @BeforeEach
    public void setUp() throws Exception {
        this.model = new ModelManager(TypicalFoods.getTypicalMcGymmy(), new UserPrefs());
        this.model.setMacroList(this.model.getMacroList().withNewMacro(TEST_MACRO));
    }

    @Test
    public void invalidName_throwsCommandException() {
        RemoveMacroCommand removeMacroCommand = new RemoveMacroCommand();
        removeMacroCommand.setParameters(new CommandParserTestUtil.ParameterStub<>("", "wqfwef"));
        assertThrows(CommandException.class, () -> removeMacroCommand.execute(model));
    }

    @Test
    public void validName_removesMacro() throws Exception {
        RemoveMacroCommand removeMacroCommand = new RemoveMacroCommand();
        removeMacroCommand.setParameters(new CommandParserTestUtil.ParameterStub<>("", "test"));
        removeMacroCommand.execute(this.model);
        assertFalse(this.model.getMacroList().hasMacro("test"));
    }
}
