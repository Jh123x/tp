package jimmy.mcgymmy.storage;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import jimmy.mcgymmy.commons.core.LogsCenter;
import jimmy.mcgymmy.commons.exceptions.DataConversionException;
import jimmy.mcgymmy.commons.exceptions.IllegalValueException;
import jimmy.mcgymmy.commons.util.FileUtil;
import jimmy.mcgymmy.commons.util.JsonUtil;
import jimmy.mcgymmy.model.macro.MacroList;

public class JsonMacroListStorage implements MacroListStorage {

    private static final Logger logger = LogsCenter.getLogger(JsonMacroListStorage.class);

    private Path filePath;

    public JsonMacroListStorage(Path filePath) {
        this.filePath = filePath;
    }


    @Override
    public Path getMacroListFilePath() {
        return filePath;
    }

    @Override
    public Optional<MacroList> readMacroList() throws DataConversionException {
        requireNonNull(filePath);

        Optional<JsonSerializableMacroList> jsonMacroList = JsonUtil.readJsonFile(
                filePath, JsonSerializableMacroList.class);
        if (!jsonMacroList.isPresent()) {
            return Optional.empty();
        }

        try {
            return Optional.of(jsonMacroList.get().toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + filePath + ": " + ive.getMessage());
            throw new DataConversionException(ive);
        }
    }

    @Override
    public void saveMacroList(MacroList macroList) throws IOException {
        requireNonNull(macroList);
        FileUtil.createIfMissing(filePath);
        JsonUtil.saveJsonFile(new JsonSerializableMacroList(macroList), filePath);
    }

}
