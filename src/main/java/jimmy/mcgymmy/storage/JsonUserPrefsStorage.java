package jimmy.mcgymmy.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import jimmy.mcgymmy.commons.exceptions.DataConversionException;
import jimmy.mcgymmy.commons.util.JsonUtil;
import jimmy.mcgymmy.model.ReadOnlyUserPrefs;
import jimmy.mcgymmy.model.UserPrefs;

/**
 * A class to access UserPrefs stored in the hard disk as a json file.
 */
public class JsonUserPrefsStorage implements UserPrefsStorage {

    private final Path filePath;

    public JsonUserPrefsStorage(Path filePath) {
        this.filePath = filePath;
    }

    @Override
    public Path getUserPrefsFilePath() {
        return filePath;
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException {
        return readUserPrefs(filePath);
    }

    /**
     * Similar to {@link #readUserPrefs()}
     *
     * @param prefsFilePath location of the data. Cannot be null.
     * @throws DataConversionException if the file format is not as expected.
     */
    public Optional<UserPrefs> readUserPrefs(Path prefsFilePath) throws DataConversionException {
        return JsonUtil.readJsonFile(prefsFilePath, UserPrefs.class);
    }

    @Override
    public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {
        JsonUtil.saveJsonFile(userPrefs, filePath);
    }

}
