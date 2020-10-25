package jimmy.mcgymmy.logic;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import jimmy.mcgymmy.commons.core.GuiSettings;
import jimmy.mcgymmy.commons.core.LogsCenter;
import jimmy.mcgymmy.commons.exceptions.DataConversionException;
import jimmy.mcgymmy.logic.commands.CommandExecutable;
import jimmy.mcgymmy.logic.commands.CommandResult;
import jimmy.mcgymmy.logic.commands.exceptions.CommandException;
import jimmy.mcgymmy.logic.parser.McGymmyParser;
import jimmy.mcgymmy.logic.parser.exceptions.ParseException;
import jimmy.mcgymmy.model.Model;
import jimmy.mcgymmy.model.ReadOnlyMcGymmy;
import jimmy.mcgymmy.model.food.Food;
import jimmy.mcgymmy.model.macro.MacroList;
import jimmy.mcgymmy.storage.Storage;

/**
 * The main LogicManager of the app.
 */
public class LogicManager implements Logic {
    public static final String FILE_OPS_ERROR_MESSAGE = "Could not save data to file: ";
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Storage storage;
    private McGymmyParser mcGymmyParser;

    /**
     * Constructs a {@code LogicManager} with the given {@code Model} and {@code Storage}.
     */
    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.storage = storage;
        try {
            Optional<MacroList> optionalMacroList = storage.readMacroList();
            this.mcGymmyParser = optionalMacroList
                    .map(macroList -> new McGymmyParser(macroList))
                    .orElseGet(McGymmyParser::new);
        } catch (IOException | DataConversionException e) {
            this.mcGymmyParser = new McGymmyParser();
        }
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");

        CommandResult commandResult;
        CommandExecutable executable = mcGymmyParser.parse(commandText);
        commandResult = executable.execute(model);
        mcGymmyParser.setMacroList(model.getMacroList());

        try {
            storage.saveMacroList(mcGymmyParser.getMacroList());
            storage.saveMcGymmy(model.getMcGymmy());
        } catch (IOException ioe) {
            throw new CommandException(FILE_OPS_ERROR_MESSAGE + ioe, ioe);
        }

        return commandResult;
    }

    @Override
    public ReadOnlyMcGymmy getMcGymmy() {
        return model.getMcGymmy();
    }

    @Override
    public ObservableList<Food> getFilteredFoodList() {
        return model.getFilteredFoodList();
    }

    @Override
    public Path getMcGymmyFilePath() {
        return model.getMcGymmyFilePath();
    }

    @Override
    public GuiSettings getGuiSettings() {
        return model.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        model.setGuiSettings(guiSettings);
    }
}
