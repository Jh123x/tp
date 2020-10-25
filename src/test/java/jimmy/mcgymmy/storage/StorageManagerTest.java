package jimmy.mcgymmy.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import jimmy.mcgymmy.commons.core.GuiSettings;
import jimmy.mcgymmy.model.McGymmy;
import jimmy.mcgymmy.model.ReadOnlyMcGymmy;
import jimmy.mcgymmy.model.UserPrefs;
import jimmy.mcgymmy.testutil.TypicalFoods;

public class StorageManagerTest {

    @TempDir
    public Path testFolder;

    private StorageManager storageManager;

    @BeforeEach
    public void setUp() {
        JsonMcGymmyStorage mcGymmyStorage = new JsonMcGymmyStorage(getTempFilePath("ab"));
        JsonMacroListStorage macroListStorage = new JsonMacroListStorage(getTempFilePath("ab"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(getTempFilePath("prefs"));
        storageManager = new StorageManager(mcGymmyStorage, macroListStorage, userPrefsStorage);
    }

    private Path getTempFilePath(String fileName) {
        return testFolder.resolve(fileName);
    }

    @Test
    public void prefsReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonUserPrefsStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonUserPrefsStorageTest} class.
         */
        UserPrefs original = new UserPrefs();
        original.setGuiSettings(new GuiSettings(300, 600, 4, 6));
        storageManager.saveUserPrefs(original);
        UserPrefs retrieved = storageManager.readUserPrefs().get();
        assertEquals(original, retrieved);
    }

    @Test
    public void mcGymmyReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonMcGymmyStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonMcGymmyStorageTest} class.
         */
        McGymmy original = TypicalFoods.getTypicalMcGymmy();
        storageManager.saveMcGymmy(original);
        ReadOnlyMcGymmy retrieved = storageManager.readMcGymmy().get();
        assertEquals(original, new McGymmy(retrieved));
    }

    @Test
    public void getMcGymmyFilePath() {
        assertNotNull(storageManager.getMcGymmyFilePath());
    }

}
