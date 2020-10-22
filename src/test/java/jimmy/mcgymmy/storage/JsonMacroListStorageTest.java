package jimmy.mcgymmy.storage;

import static jimmy.mcgymmy.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import jimmy.mcgymmy.commons.exceptions.DataConversionException;
import jimmy.mcgymmy.logic.macro.Macro;
import jimmy.mcgymmy.logic.macro.MacroList;

public class JsonMacroListStorageTest {
    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonMacroListStorageTest");

    private Optional<MacroList> readMacroList(String filePath) throws Exception {
        return new JsonMacroListStorage(TEST_DATA_FOLDER.resolve(filePath)).readMacroList();
    }

    @Test
    public void readMcGymmy_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> readMacroList(null));
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readMacroList("NonExistentFile.json").isPresent());
    }

    @Test
    public void validMacroList_success() throws Exception {
        Optional<MacroList> result = readMacroList("validMacroList.json");
        assertTrue(result.isPresent());
        MacroList macroList = result.get();
        Macro first = macroList.getMacro("first");
        Macro second = macroList.getMacro("second");
        assertArrayEquals(new String[]{"a", "b"}, first.getMacroArguments());
        assertArrayEquals(new String[]{"list", "help"}, first.getRawCommands());
        assertArrayEquals(new String[]{"c", "d"}, second.getMacroArguments());
        assertArrayEquals(new String[]{"clear"}, second.getRawCommands());
    }

    @Test
    public void toModelType_invalidFoodFile_throwsIllegalValueException() throws Exception {
        assertThrows(DataConversionException.class, () -> readMacroList("invalidMacroList.json"));
    }
}
