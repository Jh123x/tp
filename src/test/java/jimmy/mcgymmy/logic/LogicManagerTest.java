package jimmy.mcgymmy.logic;

import static jimmy.mcgymmy.commons.core.Messages.MESSAGE_INVALID_FOOD_DISPLAYED_INDEX;
import static jimmy.mcgymmy.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static jimmy.mcgymmy.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import jimmy.mcgymmy.commons.exceptions.IllegalValueException;
import jimmy.mcgymmy.logic.commands.AddCommand;
import jimmy.mcgymmy.logic.commands.CommandResult;
import jimmy.mcgymmy.logic.commands.ListCommand;
import jimmy.mcgymmy.logic.commands.exceptions.CommandException;
import jimmy.mcgymmy.logic.parser.exceptions.ParseException;
import jimmy.mcgymmy.model.Model;
import jimmy.mcgymmy.model.ModelManager;
import jimmy.mcgymmy.model.ReadOnlyMcGymmy;
import jimmy.mcgymmy.model.UserPrefs;
import jimmy.mcgymmy.model.food.Food;
import jimmy.mcgymmy.model.food.Name;
import jimmy.mcgymmy.storage.JsonMacroListStorage;
import jimmy.mcgymmy.storage.JsonMcGymmyStorage;
import jimmy.mcgymmy.storage.JsonUserPrefsStorage;
import jimmy.mcgymmy.storage.StorageManager;
import jimmy.mcgymmy.testutil.FoodBuilder;

public class LogicManagerTest {
    private static final IOException DUMMY_IO_EXCEPTION = new IOException("dummy exception");

    @TempDir
    public Path temporaryFolder;

    private final Model model = new ModelManager();
    private Logic logic;

    @BeforeEach
    public void setUp() {
        JsonMcGymmyStorage mcGymmyStorage =
                new JsonMcGymmyStorage(temporaryFolder.resolve("mcGymmy.json"));
        JsonMacroListStorage macroListStorage = new JsonMacroListStorage(temporaryFolder.resolve("macroList.json"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json"));
        StorageManager storage = new StorageManager(mcGymmyStorage, macroListStorage, userPrefsStorage);
        logic = new LogicManager(model, storage);
    }

    @Test
    public void execute_invalidCommandFormat_throwsParseException() {
        String invalidCommand = "uicfhmowqewca";
        assertParseException(invalidCommand, MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void execute_commandExecutionError_throwsCommandException() {
        String deleteCommand = "delete 9";
        assertCommandException(deleteCommand, MESSAGE_INVALID_FOOD_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validCommand_success() throws Exception {
        String listCommand = ListCommand.COMMAND_WORD;
        assertCommandSuccess(listCommand, ListCommand.MESSAGE_SUCCESS, model);
    }

    @Test
    public void execute_storageThrowsIoException_throwsCommandException() throws IllegalValueException {
        // Setup LogicManager with JsonMcGymmyIoExceptionThrowingStub
        JsonMcGymmyStorage mcGymmyStorage =
                new JsonMcGymmyIoExceptionThrowingStub(temporaryFolder.resolve("ioExceptionMcGymmy.json"));
        JsonMacroListStorage macroListStorage =
                new JsonMacroListStorage(temporaryFolder.resolve("ioExceptionMacroList.json"));
        JsonUserPrefsStorage userPrefsStorage =
                new JsonUserPrefsStorage(temporaryFolder.resolve("ioExceptionUserPrefs.json"));
        StorageManager storage = new StorageManager(mcGymmyStorage, macroListStorage, userPrefsStorage);
        logic = new LogicManager(model, storage);

        // Execute add command
        // String addCommand = AddCommand.COMMAND_WORD + " -n amy -p 99999999 -e amy@amy.com";
        String addCommand = AddCommand.COMMAND_WORD + " -n amy -p 999 -f 123 -c 543";
        Food expectedFood = new FoodBuilder().withName(new Name("amy")).withProtein("999")
                .withFat("123").withCarb("543").build();
        ModelManager expectedModel = new ModelManager();
        expectedModel.addFood(expectedFood);
        String expectedMessage = LogicManager.FILE_OPS_ERROR_MESSAGE + DUMMY_IO_EXCEPTION;
        assertCommandFailure(addCommand, CommandException.class, expectedMessage, expectedModel);
    }

    @Test
    public void getFilteredFoodList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> logic.getFilteredFoodList().remove(0));
    }

    /**
     * Executes the command and confirms that
     * - no exceptions are thrown <br>
     * - the feedback message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     *
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandSuccess(String inputCommand, String expectedMessage,
                                      Model expectedModel) throws CommandException, ParseException {
        CommandResult result = logic.execute(inputCommand);
        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(expectedModel, model);
    }

    /**
     * Executes the command, confirms that a ParseException is thrown and that the result message is correct.
     *
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertParseException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, ParseException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that a CommandException is thrown and that the result message is correct.
     *
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, CommandException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that the exception is thrown and that the result message is correct.
     *
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
                                      String expectedMessage) {
        Model expectedModel = new ModelManager(model.getMcGymmy(), new UserPrefs());
        assertCommandFailure(inputCommand, expectedException, expectedMessage, expectedModel);
    }

    /**
     * Executes the command and confirms that
     * - the {@code expectedException} is thrown <br>
     * - the resulting error message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     *
     * @see #assertCommandSuccess(String, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
                                      String expectedMessage, Model expectedModel) {
        assertThrows(expectedException, expectedMessage, () -> logic.execute(inputCommand));
        assertEquals(expectedModel, model);
    }

    /**
     * A stub class to throw an {@code IOException} when the save method is called.
     */
    private static class JsonMcGymmyIoExceptionThrowingStub extends JsonMcGymmyStorage {
        private JsonMcGymmyIoExceptionThrowingStub(Path filePath) {
            super(filePath);
        }

        @Override
        public void saveMcGymmy(ReadOnlyMcGymmy mcGymmy, Path filePath) throws IOException {
            throw DUMMY_IO_EXCEPTION;
        }
    }
}
