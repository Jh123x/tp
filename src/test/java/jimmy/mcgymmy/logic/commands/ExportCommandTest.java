package jimmy.mcgymmy.logic.commands;

import static jimmy.mcgymmy.logic.commands.CommandTestUtil.assertCommandFailure;
import static jimmy.mcgymmy.logic.commands.CommandTestUtil.assertCommandSuccess;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import jimmy.mcgymmy.logic.parser.CommandParserTestUtil.OptionalParameterStub;
import jimmy.mcgymmy.logic.parser.CommandParserTestUtil.ParameterStub;
import jimmy.mcgymmy.model.Model;
import jimmy.mcgymmy.model.ModelManager;

class ExportCommandTest {

    private static final String INVALID_DIR_STRING = Paths.get("c:", "Invalid String").toString();
    private static final String VALID_DIR_STRING = Paths.get("src", "test", "data", "ExportData").toString();
    private static final Path INVALID_DIR_PATH = Path.of(INVALID_DIR_STRING);
    private static final Path VALID_DIR_PATH = Path.of(VALID_DIR_STRING);
    private static final Path VALID_DIR_PATH_DEFAULT = Path.of(VALID_DIR_STRING, "mcgymmy.json");
    private static final Path VALID_DIR_PATH_OPTIONAL = Path.of(VALID_DIR_STRING, "mc.json");
    private static final Model DEFAULT_MODEL = new ModelManager();
    private static final ParameterStub<Path> VALID_PARAMETER_STUB = new ParameterStub<Path>("", VALID_DIR_PATH);
    private static final OptionalParameterStub<String> EMPTY_OPTIONAL_PARAMETER_STUB =
            new OptionalParameterStub<String>("o");
    private static final OptionalParameterStub<String> STRING_OPTIONAL_PARAMETER_STUB =
            new OptionalParameterStub<String>("o", "mc.json");
    private static final ParameterStub<Path> INVALID_PARAMETER_STUB =
            new ParameterStub<Path>("", INVALID_DIR_PATH);

    @Test
    public void directoryNotFoundError() {
        ExportCommand exportCommand = new ExportCommand();
        exportCommand.setPathParameter(INVALID_PARAMETER_STUB, STRING_OPTIONAL_PARAMETER_STUB);
        assertCommandFailure(exportCommand,
                DEFAULT_MODEL, String.format(ExportCommand.MESSAGE_FAILURE, INVALID_DIR_STRING));
    }

    @Test
    public void invalidOutputName() {
        ExportCommand exportCommand = new ExportCommand();

        exportCommand.setPathParameter(INVALID_PARAMETER_STUB, EMPTY_OPTIONAL_PARAMETER_STUB);
        assertCommandFailure(exportCommand, DEFAULT_MODEL,
                String.format(ExportCommand.MESSAGE_FAILURE, INVALID_DIR_STRING));
    }

    @Test
    public void exportWithOptionalSuccess() {
        File exportFile = new File(VALID_DIR_PATH_OPTIONAL.toString());
        if (exportFile.exists()) {
            exportFile.delete();
        }
        ExportCommand exportCommand = new ExportCommand();

        exportCommand.setPathParameter(VALID_PARAMETER_STUB, STRING_OPTIONAL_PARAMETER_STUB);
        assertCommandSuccess(exportCommand, DEFAULT_MODEL,
                String.format(ExportCommand.MESSAGE_SUCCESS, VALID_DIR_PATH_OPTIONAL),
                DEFAULT_MODEL);
        //Remove the file after command executes
        exportFile = new File(VALID_DIR_PATH_OPTIONAL.toString());
        exportFile.delete();
    }

    @Test
    public void exportWithoutOptionalSuccess() {
        File exportFile = new File(VALID_DIR_PATH_DEFAULT.toString());
        if (exportFile.exists()) {
            exportFile.delete();
        }
        ExportCommand exportCommand = new ExportCommand();
        exportCommand.setPathParameter(VALID_PARAMETER_STUB, EMPTY_OPTIONAL_PARAMETER_STUB);
        assertCommandSuccess(exportCommand, DEFAULT_MODEL,
                String.format(ExportCommand.MESSAGE_SUCCESS, VALID_DIR_PATH_DEFAULT),
                DEFAULT_MODEL);
        //Remove the file after command executes
        exportFile = new File(VALID_DIR_PATH_DEFAULT.toString());
        exportFile.delete();
    }

}
