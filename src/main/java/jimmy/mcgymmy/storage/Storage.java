package jimmy.mcgymmy.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import jimmy.mcgymmy.commons.exceptions.DataConversionException;
import jimmy.mcgymmy.model.ReadOnlyMcGymmy;
import jimmy.mcgymmy.model.ReadOnlyUserPrefs;
import jimmy.mcgymmy.model.UserPrefs;

/**
 * API of the Storage component
 */
public interface Storage extends McGymmyStorage, UserPrefsStorage, MacroListStorage {

    @Override
    Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException;

    @Override
    void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException;

    @Override
    Path getMcGymmyFilePath();

    @Override
    Optional<ReadOnlyMcGymmy> readMcGymmy() throws DataConversionException, IOException;

    @Override
    void saveMcGymmy(ReadOnlyMcGymmy mcGymmy) throws IOException;

}
