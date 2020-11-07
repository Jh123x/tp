package jimmy.mcgymmy.storage;

import static jimmy.mcgymmy.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import jimmy.mcgymmy.commons.core.index.Index;
import jimmy.mcgymmy.commons.exceptions.DataConversionException;
import jimmy.mcgymmy.model.McGymmy;
import jimmy.mcgymmy.model.ReadOnlyMcGymmy;
import jimmy.mcgymmy.testutil.TypicalFoods;

public class JsonMcGymmyStorageTest {
    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonMcGymmyStorageTest");

    @TempDir
    public Path testFolder;

    @Test
    public void readMcGymmy_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> readMcGymmy(null));
    }

    private java.util.Optional<ReadOnlyMcGymmy> readMcGymmy(String filePath) throws Exception {
        return new JsonMcGymmyStorage(Paths.get(filePath)).readMcGymmy(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readMcGymmy("NonExistentFile.json").isPresent());
    }

    @Test
    public void read_notJsonFormat_exceptionThrown() {
        assertThrows(DataConversionException.class, () -> readMcGymmy("notJsonFormatMcGymmy.json"));
    }

    @Test
    public void readMcGymmy_invalidFoodMcGymmy_throwDataConversionException() {
        assertThrows(DataConversionException.class, () -> readMcGymmy("invalidFoodMcGymmy.json"));
    }

    @Test
    public void readMcGymmy_invalidAndValidFoodMcGymmy_throwDataConversionException() {
        assertThrows(DataConversionException.class, () -> readMcGymmy("invalidAndValidFoodMcGymmy.json"));
    }

    @Test
    public void readAndSaveMcGymmy_allInOrder_success() throws Exception {
        Path filePath = testFolder.resolve("TempMcGymmy.json");
        McGymmy original = TypicalFoods.getTypicalMcGymmy();
        JsonMcGymmyStorage jsonMcGymmyStorage = new JsonMcGymmyStorage(filePath);

        // Save in new file and read back
        jsonMcGymmyStorage.saveMcGymmy(original, filePath);
        ReadOnlyMcGymmy readBack = jsonMcGymmyStorage.readMcGymmy(filePath).get();
        assertEquals(original, new McGymmy(readBack));

        // Modify data, overwrite exiting file, and read back
        original.addFood(TypicalFoods.getHotPlate());
        original.removeFood(Index.fromZeroBased(0));
        jsonMcGymmyStorage.saveMcGymmy(original, filePath);
        readBack = jsonMcGymmyStorage.readMcGymmy(filePath).get();
        assertEquals(original, new McGymmy(readBack));

        // Save and read without specifying file path
        original.addFood(TypicalFoods.getIndomee());
        jsonMcGymmyStorage.saveMcGymmy(original); // file path not specified
        readBack = jsonMcGymmyStorage.readMcGymmy().get(); // file path not specified
        assertEquals(original, new McGymmy(readBack));

    }

    @Test
    public void saveMcGymmy_nullMcGymmy_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveMcGymmy(null, "SomeFile.json"));
    }

    /**
     * Saves {@code mcGymmy} at the specified {@code filePath}.
     */
    private void saveMcGymmy(ReadOnlyMcGymmy mcGymmy, String filePath) {
        try {
            new JsonMcGymmyStorage(Paths.get(filePath))
                    .saveMcGymmy(mcGymmy, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveMcGymmy_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveMcGymmy(new McGymmy(), null));
    }
}
