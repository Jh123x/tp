package jimmy.mcgymmy.logic.commands;

import static jimmy.mcgymmy.logic.commands.CommandTestUtil.assertCommandFailure;
import static jimmy.mcgymmy.logic.commands.CommandTestUtil.assertCommandSuccess;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import jimmy.mcgymmy.logic.parser.CommandParserTestUtil.ParameterStub;
import jimmy.mcgymmy.model.McGymmy;
import jimmy.mcgymmy.model.ModelManager;
import jimmy.mcgymmy.testutil.TypicalFoods;

/**
 * Invalid Commands are not tested as they are checked in the parser.
 */
class ImportCommandTest {

    private static final String VALID_FILE = Paths
            .get("src", "test", "data", "JsonSerializableMcGymmyTest", "typicalFoodMcGymmy.json").toString();
    private static final String INVALID_FILE = Paths
            .get("src", "test", "data", "JsonMcGymmyStorageTest", "notJsonFormatMcGymmy.json").toString();
    private static final String NOT_FOUND_FILE = Paths
            .get("src", "test", "data", "JsonSerializableMcGymmyTest", "not found.json").toString();
    private static final ParameterStub<Path> VALID_FILE_STUB = new ParameterStub<Path>("", Path.of(VALID_FILE));
    private static final ParameterStub<Path> NOT_FOUND_FILE_STUB =
            new ParameterStub<Path>("", Path.of(NOT_FOUND_FILE));
    private static final ParameterStub<Path> INVALID_FILE_STUB =
            new ParameterStub<Path>("", Path.of(INVALID_FILE));

    @Test
    public void validImport_success() {
        ModelManager model1 = new ModelManager();

        ImportCommand importCommand = new ImportCommand();
        importCommand.setParameters(VALID_FILE_STUB);

        //Create Expected model
        ModelManager model = new ModelManager();
        McGymmy mcGymmy = new McGymmy();
        mcGymmy.setFoodItems(TypicalFoods.getTypicalFoodItems());
        model.setMcGymmy(mcGymmy);

        assertCommandSuccess(importCommand, model1,
                String.format(ImportCommand.MESSAGE_IMPORT_FOOD_SUCCESS, "typicalFoodMcGymmy.json"), model);
    }

    @Test
    public void invalidFileNotFoundImport_failure() {
        ModelManager model1 = new ModelManager();
        ImportCommand importCommand = new ImportCommand();
        importCommand.setParameters(NOT_FOUND_FILE_STUB);
        assertCommandFailure(importCommand, model1,
                String.format(ImportCommand.MESSAGE_IMPORT_FOOD_FAILURE, NOT_FOUND_FILE_STUB.consume()));
    }

    @Test
    public void invalidFileImport_failure() {
        ModelManager model1 = new ModelManager();
        ImportCommand importCommand = new ImportCommand();
        importCommand.setParameters(INVALID_FILE_STUB);
        assertCommandFailure(importCommand, model1,
                String.format(ImportCommand.MESSAGE_IMPORT_FOOD_FAILURE, INVALID_FILE_STUB.consume()));
    }
}
