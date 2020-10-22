package jimmy.mcgymmy.storage;

import static jimmy.mcgymmy.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import jimmy.mcgymmy.commons.exceptions.IllegalValueException;
import jimmy.mcgymmy.commons.util.JsonUtil;
import jimmy.mcgymmy.model.McGymmy;
import jimmy.mcgymmy.testutil.TypicalFoods;

public class JsonSerializableMcGymmyTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonSerializableMcGymmyTest");
    private static final Path TYPICAL_FOOD_FILE = TEST_DATA_FOLDER.resolve("typicalFoodMcGymmy.json");
    private static final Path INVALID_FOOD_FILE = TEST_DATA_FOLDER.resolve("invalidFoodMcGymmy.json");
    private static final Path DUPLICATE_FOOD_FILE = TEST_DATA_FOLDER.resolve("duplicateFoodMcGymmy.json");

    @Test
    public void toModelType_typicalFoodFile_success() throws Exception {
        JsonSerializableMcGymmy dataFromFile = JsonUtil.readJsonFile(TYPICAL_FOOD_FILE,
                JsonSerializableMcGymmy.class).get();
        McGymmy mcGymmyFromFile = dataFromFile.toModelType();
        McGymmy typicalFoodMcGymmy = TypicalFoods.getTypicalMcGymmy();
        assertEquals(mcGymmyFromFile, typicalFoodMcGymmy);
    }

    @Test
    public void toModelType_invalidFoodFile_throwsIllegalValueException() throws Exception {
        JsonSerializableMcGymmy dataFromFile = JsonUtil.readJsonFile(INVALID_FOOD_FILE,
                JsonSerializableMcGymmy.class).get();
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }
}
