package jimmy.mcgymmy.logic.commands;

import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import jimmy.mcgymmy.commons.core.LogsCenter;
import jimmy.mcgymmy.commons.exceptions.DataConversionException;
import jimmy.mcgymmy.logic.commands.exceptions.CommandException;
import jimmy.mcgymmy.logic.parser.ParserUtil;
import jimmy.mcgymmy.logic.parser.parameter.Parameter;
import jimmy.mcgymmy.model.Model;
import jimmy.mcgymmy.model.ReadOnlyMcGymmy;
import jimmy.mcgymmy.storage.JsonMcGymmyStorage;

/**
 * Imports a McGymmy datafile into McGymmy.
 */
public class ImportCommand extends Command {

    public static final String COMMAND_WORD = "import";
    public static final String SHORT_DESCRIPTION = "Import a McGymmy Save file";

    public static final String MESSAGE_IMPORT_FOOD_SUCCESS = "Imported %s";
    public static final String MESSAGE_IMPORT_FOOD_FAILURE = "Please select a valid .json file: %s";

    private static final Logger importLogger = LogsCenter.getLogger(ExportCommand.class);

    private Parameter<Path> fileParameter = this.addParameter(
            "filepath",
            "",
            "File path to the McGymmy save file",
            "C:/McGymmy/data",
            ParserUtil::parseFile
    );

    void setParameters(Parameter<Path> fileParameter) {
        this.fileParameter = fileParameter;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        Path filepath = fileParameter.consume();
        JsonMcGymmyStorage importedMcgymmy = new JsonMcGymmyStorage(filepath);
        String localFailureMessage = String.format(MESSAGE_IMPORT_FOOD_FAILURE, filepath);
        String localSuccessMessage = String.format(MESSAGE_IMPORT_FOOD_SUCCESS, filepath.getFileName());

        try {
            Optional<ReadOnlyMcGymmy> readOnlyMcGymmyOptional = importedMcgymmy.readMcGymmy();

            if (readOnlyMcGymmyOptional.isEmpty()) {
                throw new CommandException(localFailureMessage);
            }

            model.setMcGymmy(readOnlyMcGymmyOptional.get());
            importLogger.fine(localSuccessMessage);
            return new CommandResult(localSuccessMessage);

        } catch (DataConversionException e) {
            importLogger.warning(localFailureMessage);
            throw new CommandException(localFailureMessage);
        }
    }
}
