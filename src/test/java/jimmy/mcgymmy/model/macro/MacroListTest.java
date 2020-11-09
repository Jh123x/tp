package jimmy.mcgymmy.model.macro;

import static jimmy.mcgymmy.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import jimmy.mcgymmy.model.macro.exceptions.DuplicateMacroException;

public class MacroListTest {

    @Test
    public void addDuplicate_throwsError() throws Exception {
        Macro dummyMacro = new Macro("test2", new String[]{}, new String[]{});
        MacroList macroList = (new MacroList()).withNewMacro(dummyMacro);
        Macro duplicateMacro1 = new Macro("help", new String[]{}, new String[]{});
        Macro duplicateMacro2 = new Macro("test2", new String[]{}, new String[]{});
        assertThrows(DuplicateMacroException.class, ()->macroList.withNewMacro(duplicateMacro1));
        assertThrows(DuplicateMacroException.class, ()->macroList.withNewMacro(duplicateMacro2));
    }

}
