package jimmy.mcgymmy.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import jimmy.mcgymmy.commons.core.LogsCenter;
import jimmy.mcgymmy.commons.exceptions.DataConversionException;
import jimmy.mcgymmy.model.ReadOnlyMcGymmy;
import jimmy.mcgymmy.model.ReadOnlyUserPrefs;
import jimmy.mcgymmy.model.UserPrefs;
import jimmy.mcgymmy.model.macro.MacroList;

/**
 * Manages storage of McGymmy data in local storage.
 */
public class StorageManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private final McGymmyStorage mcGymmyStorage;
    private final MacroListStorage macroListStorage;
    private final UserPrefsStorage userPrefsStorage;

    /**
     * Creates a {@code StorageManager} with the given {@code McGymmyStorage} and {@code UserPrefStorage}.
     */
    public StorageManager(McGymmyStorage mcGymmyStorage, MacroListStorage macroListStorage,
                          UserPrefsStorage userPrefsStorage) {
        super();
        this.mcGymmyStorage = mcGymmyStorage;
        this.macroListStorage = macroListStorage;
        this.userPrefsStorage = userPrefsStorage;
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Path getUserPrefsFilePath() {
        return userPrefsStorage.getUserPrefsFilePath();
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }

    // ================ MacroList methods ==============================


    @Override
    public Path getMacroListFilePath() {
        return macroListStorage.getMacroListFilePath();
    }

    @Override
    public Optional<MacroList> readMacroList() throws DataConversionException, IOException {
        return macroListStorage.readMacroList();
    }

    @Override
    public void saveMacroList(MacroList macroList) throws IOException {
        macroListStorage.saveMacroList(macroList);
    }

    // ================ McGymmy methods ==============================

    @Override
    public Path getMcGymmyFilePath() {
        return mcGymmyStorage.getMcGymmyFilePath();
    }

    @Override
    public Optional<ReadOnlyMcGymmy> readMcGymmy() throws DataConversionException, IOException {
        return readMcGymmy(mcGymmyStorage.getMcGymmyFilePath());
    }

    @Override
    public Optional<ReadOnlyMcGymmy> readMcGymmy(Path filePath) throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return mcGymmyStorage.readMcGymmy(filePath);
    }

    @Override
    public void saveMcGymmy(ReadOnlyMcGymmy mcGymmy) throws IOException {
        saveMcGymmy(mcGymmy, mcGymmyStorage.getMcGymmyFilePath());
    }

    @Override
    public void saveMcGymmy(ReadOnlyMcGymmy mcGymmy, Path filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        mcGymmyStorage.saveMcGymmy(mcGymmy, filePath);
    }

}
