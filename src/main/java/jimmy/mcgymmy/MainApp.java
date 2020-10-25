package jimmy.mcgymmy;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.stage.Stage;
import jimmy.mcgymmy.commons.core.Config;
import jimmy.mcgymmy.commons.core.LogsCenter;
import jimmy.mcgymmy.commons.core.Version;
import jimmy.mcgymmy.commons.exceptions.DataConversionException;
import jimmy.mcgymmy.commons.util.ConfigUtil;
import jimmy.mcgymmy.commons.util.StringUtil;
import jimmy.mcgymmy.logic.Logic;
import jimmy.mcgymmy.logic.LogicManager;
import jimmy.mcgymmy.model.McGymmy;
import jimmy.mcgymmy.model.Model;
import jimmy.mcgymmy.model.ModelManager;
import jimmy.mcgymmy.model.ReadOnlyMcGymmy;
import jimmy.mcgymmy.model.ReadOnlyUserPrefs;
import jimmy.mcgymmy.model.UserPrefs;
import jimmy.mcgymmy.model.macro.MacroList;
import jimmy.mcgymmy.model.util.SampleDataUtil;
import jimmy.mcgymmy.storage.JsonMacroListStorage;
import jimmy.mcgymmy.storage.JsonMcGymmyStorage;
import jimmy.mcgymmy.storage.JsonUserPrefsStorage;
import jimmy.mcgymmy.storage.MacroListStorage;
import jimmy.mcgymmy.storage.McGymmyStorage;
import jimmy.mcgymmy.storage.Storage;
import jimmy.mcgymmy.storage.StorageManager;
import jimmy.mcgymmy.storage.UserPrefsStorage;
import jimmy.mcgymmy.ui.Ui;
import jimmy.mcgymmy.ui.UiManager;

/**
 * Runs the application.
 */
public class MainApp extends Application {

    public static final Version VERSION = new Version(0, 6, 0, true);

    private static final Logger logger = LogsCenter.getLogger(MainApp.class);

    protected Ui ui;
    protected Logic logic;
    protected Storage storage;
    protected Model model;
    protected Config config;

    @Override
    public void init() throws Exception {
        logger.info("=============================[ Initializing McGymmy ]===========================");
        super.init();

        AppParameters appParameters = AppParameters.parse(getParameters());
        config = initConfig(appParameters.getConfigPath());
        UserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(config.getUserPrefsFilePath());
        UserPrefs userPrefs = initPrefs(userPrefsStorage);
        McGymmyStorage mcGymmyStorage = new JsonMcGymmyStorage(userPrefs.getMcGymmyFilePath());
        MacroListStorage macroListStorage = new JsonMacroListStorage(userPrefs.getMacroListFilePath());
        storage = new StorageManager(mcGymmyStorage, macroListStorage, userPrefsStorage);

        initLogging(config);

        model = initModelManager(storage, userPrefs);

        logic = new LogicManager(model, storage);

        ui = new UiManager(logic);
    }

    /**
     * Returns a {@code ModelManager} with the data from {@code storage}'s McGymmy and {@code userPrefs}. <br>
     * The data from the sample McGymmy will be used instead if {@code storage}'s McGymmy is not found,
     * or an empty McGymmy will be used instead if errors occur when reading {@code storage}'s McGymmy.
     */
    private Model initModelManager(Storage storage, ReadOnlyUserPrefs userPrefs) {
        Optional<ReadOnlyMcGymmy> mcGymmyOptional;
        ReadOnlyMcGymmy initialData;
        MacroList macroList;
        try {
            mcGymmyOptional = storage.readMcGymmy();
            if (!mcGymmyOptional.isPresent()) {
                logger.info("Data file not found. Will be starting with a sample McGymmy");
            }
            initialData = mcGymmyOptional.orElseGet(SampleDataUtil::getSampleMcGymmy);
            macroList = storage.readMacroList().orElseGet(MacroList::new);
        } catch (DataConversionException e) {
            logger.warning("Data file not in the correct format. Will be starting with an empty McGymmy");
            initialData = new McGymmy();
            macroList = new MacroList();
        } catch (IOException e) {
            logger.warning("Problem while reading from the file. Will be starting with an empty McGymmy");
            initialData = new McGymmy();
            macroList = new MacroList();
        }

        return new ModelManager(initialData, userPrefs, macroList);
    }

    private void initLogging(Config config) {
        LogsCenter.init(config);
    }

    /**
     * Returns a {@code Config} using the file at {@code configFilePath}. <br>
     * The default file path {@code Config#DEFAULT_CONFIG_FILE} will be used instead
     * if {@code configFilePath} is null.
     */
    protected Config initConfig(Path configFilePath) {
        Config initializedConfig;
        Path configFilePathUsed;

        configFilePathUsed = Config.DEFAULT_CONFIG_FILE;

        if (configFilePath != null) {
            logger.info("Custom Config file specified " + configFilePath);
            configFilePathUsed = configFilePath;
        }

        logger.info("Using config file : " + configFilePathUsed);

        try {
            Optional<Config> configOptional = ConfigUtil.readConfig(configFilePathUsed);
            initializedConfig = configOptional.orElse(new Config());
        } catch (DataConversionException e) {
            logger.warning("Config file at " + configFilePathUsed + " is not in the correct format. "
                    + "Using default config properties");
            initializedConfig = new Config();
        }

        //Update config file in case it was missing to begin with or there are new/unused fields
        try {
            ConfigUtil.saveConfig(initializedConfig, configFilePathUsed);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }
        return initializedConfig;
    }

    /**
     * Returns a {@code UserPrefs} using the file at {@code storage}'s user prefs file path,
     * or a new {@code UserPrefs} with default configuration if errors occur when
     * reading from the file.
     */
    protected UserPrefs initPrefs(UserPrefsStorage storage) {
        Path prefsFilePath = storage.getUserPrefsFilePath();
        logger.info("Using prefs file : " + prefsFilePath);

        UserPrefs initializedPrefs;
        try {
            Optional<UserPrefs> prefsOptional = storage.readUserPrefs();
            initializedPrefs = prefsOptional.orElse(new UserPrefs());
        } catch (DataConversionException e) {
            logger.warning("UserPrefs file at " + prefsFilePath + " is not in the correct format. "
                    + "Using default user prefs");
            initializedPrefs = new UserPrefs();
        } catch (IOException e) {
            logger.warning("Problem while reading from the file. Will be starting with an empty McGymmy");
            initializedPrefs = new UserPrefs();
        }

        //Update prefs file in case it was missing to begin with or there are new/unused fields
        try {
            storage.saveUserPrefs(initializedPrefs);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }

        return initializedPrefs;
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Starting McGymmy " + MainApp.VERSION);
        ui.start(primaryStage);
    }

    @Override
    public void stop() {
        logger.info("============================ [ Stopping McGymmy ] =============================");
        try {
            storage.saveUserPrefs(model.getUserPrefs());
        } catch (IOException e) {
            logger.severe("Failed to save preferences " + StringUtil.getDetails(e));
        }
    }
}
