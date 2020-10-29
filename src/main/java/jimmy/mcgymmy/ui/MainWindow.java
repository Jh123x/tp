package jimmy.mcgymmy.ui;

import java.io.File;
import java.time.LocalDate;
import java.util.Optional;
import java.util.logging.Logger;

import com.jfoenix.controls.JFXDatePicker;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jimmy.mcgymmy.commons.core.GuiSettings;
import jimmy.mcgymmy.commons.core.LogsCenter;
import jimmy.mcgymmy.logic.Logic;
import jimmy.mcgymmy.logic.commands.CommandResult;
import jimmy.mcgymmy.logic.commands.exceptions.CommandException;
import jimmy.mcgymmy.logic.parser.exceptions.ParseException;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart<Stage> {

    private static final String FXML = "MainWindow.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    private final Stage primaryStage;
    private final Logic logic;

    // Independent Ui parts residing in this Ui container
    private FoodListPanel foodListPanel;
    private ResultDisplay resultDisplay;
    private File file;
    private File directory;

    @FXML
    private StackPane commandBoxPlaceholder;

    @FXML
    private CommandBox commandBox;

    @FXML
    private JFXDatePicker datePicker;

    @FXML
    private StackPane foodListPanelPlaceholder;

    @FXML
    private MenuItem helpMenuItem;

    @FXML
    private StackPane resultDisplayPlaceholder;

    @FXML
    private StackPane statusbarPlaceholder;

    @FXML
    private StackPane summaryPanelPlaceholder;

    @FXML
    private SummaryDisplay summaryPanel;

    /**
     * Creates a {@code MainWindow} with the given {@code Stage} and {@code Logic}.
     */
    public MainWindow(Stage primaryStage, Logic logic) {
        super(FXML, primaryStage);

        //Initialise values
        file = null;
        directory = null;

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;

        // Configure the UI
        setWindowDefaultSize(logic.getGuiSettings());
        setAccelerators();

    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    private void setAccelerators() {
        setAccelerator(helpMenuItem, KeyCombination.valueOf("F1"));
    }

    /**
     * Sets the accelerator of a MenuItem.
     *
     * @param keyCombination the KeyCombination value of the accelerator
     */
    private void setAccelerator(MenuItem menuItem, KeyCombination keyCombination) {
        menuItem.setAccelerator(keyCombination);

        /*
         * TODO: the code below can be removed once the bug reported here
         * https://bugs.openjdk.java.net/browse/JDK-8131666
         * is fixed in later version of SDK.
         *
         * According to the bug report, TextInputControl (TextField, TextArea) will
         * consume function-key events. Because CommandBox contains a TextField, and
         * ResultDisplay contains a TextArea, thus some accelerators (e.g F1) will
         * not work when the focus is in them because the key event is consumed by
         * the TextInputControl(s).
         *
         * For now, we add following event filter to capture such key events and open
         * help window purposely so to support accelerators even when focus is
         * in CommandBox or ResultDisplay.
         */
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getTarget() instanceof TextInputControl && keyCombination.match(event)) {
                menuItem.getOnAction().handle(new ActionEvent());
                event.consume();
            }
        });
    }

    /**
     * Fills up all the placeholders of this window.
     */
    void fillInnerParts() {
        foodListPanel = new FoodListPanel(logic.getFilteredFoodList());
        foodListPanelPlaceholder.getChildren().add(foodListPanel.getRoot());

        resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());

        StatusBarFooter statusBarFooter = new StatusBarFooter(logic.getMcGymmyFilePath());
        statusbarPlaceholder.getChildren().add(statusBarFooter.getRoot());

        commandBox = new CommandBox(this::executeCommand);
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());

        summaryPanel = new SummaryDisplay();
        summaryPanelPlaceholder.getChildren().add(summaryPanel.getRoot());

        //Update current value to total calories and macronutrient values.
        updateGraph();

        //Add listener to execute after date is changed
        datePicker.valueProperty()
                .addListener((observable, oldDate, newDate) -> {
                    setDate();
                });
    }

    /**
     * Sets the default size based on {@code guiSettings}.
     */
    private void setWindowDefaultSize(GuiSettings guiSettings) {
        primaryStage.setHeight(guiSettings.getWindowHeight());
        primaryStage.setWidth(guiSettings.getWindowWidth());
        if (guiSettings.getWindowCoordinates() != null) {
            primaryStage.setX(guiSettings.getWindowCoordinates().getX());
            primaryStage.setY(guiSettings.getWindowCoordinates().getY());
        }
    }

    /**
     * Shows help message in the result box.
     */
    @FXML
    public void handleHelp() {
        try {
            executeCommand("help");
        } catch (CommandException | ParseException e) {
            assert false : "Help button on menu error";
        }
    }

    /**
     * Handles the importing of data file.
     */
    @FXML
    public void handleImport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose file to import");
        try {
            file = fileChooser.showOpenDialog(primaryStage);
            logger.info(String.format("User selected '%s'", file.toString()));
            executeCommand(String.format("import %s", file.toString()));
        } catch (RuntimeException | CommandException | ParseException e) {
            file = null;
            logger.info("User did not select a valid file");
            resultDisplay.setFeedbackToUser("Please select a valid import file");
        }
    }

    /**
     * Get the import file directory chosen by the user.
     */
    public Optional<File> getImportFileDirectory() {
        if (file == null) {
            return Optional.empty();
        } else {
            return Optional.of(file);
        }
    }

    /**
     * Handles the exporting of data file.
     */
    @FXML
    public void handleExport() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose location to export");
        try {
            directory = directoryChooser.showDialog(primaryStage);
            logger.info(String.format("User selected '%s'", directory.toString()));
            executeCommand(String.format("export %s", directory.toString()));
        } catch (RuntimeException | CommandException | ParseException e) {
            directory = null;
            logger.info("User did not select a valid directory");
            resultDisplay.setFeedbackToUser("Please select a valid directory");
        }
    }

    /**
     * Get the export directory chosen by the user.
     */
    public Optional<File> getExportDirectory() {
        if (directory == null) {
            return Optional.empty();
        } else {
            return Optional.of(directory);
        }
    }


    void show() {
        primaryStage.show();
    }

    /**
     * Add the selected Date to the commandLine.
     */
    public void setDate() {
        if (getDate().isEmpty()) {
            return;
        }
        logger.info(String.format("Selected %s", getDate().get().toString()));
        try {
            executeCommand(String.format("find -d %s", getDate().get().toString()));
            datePicker.setValue(null);
        } catch (CommandException | ParseException e) {
            assert false : "Help button on menu error";
        }
    }

    /**
     * Get the date which the user selected.
     *
     * @return Optional containing the date selected. Null if no date is selected.
     */
    public Optional<LocalDate> getDate() {
        LocalDate date = datePicker.getValue();
        if (date == null) {
            return Optional.empty();
        } else {
            return Optional.of(date);
        }
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        GuiSettings guiSettings = new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
        logic.setGuiSettings(guiSettings);
        primaryStage.hide();
    }

    public FoodListPanel getFoodListPanel() {
        return foodListPanel;
    }

    /**
     * Executes the command and returns the result.
     *
     * @see Logic#execute(String)
     */
    private CommandResult executeCommand(String commandText) throws CommandException, ParseException {
        try {
            CommandResult commandResult = logic.execute(commandText);
            logger.info("Result: " + commandResult.getFeedbackToUser());
            resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());
            if (commandResult.isExit()) {
                handleExit();
            }
            updateGraph();
            return commandResult;
        } catch (CommandException | ParseException e) {
            logger.info("Invalid command: " + commandText);
            resultDisplay.setFeedbackToUser(e.getMessage());
            updateGraph();
            throw e;
        }
    }

    private void updateGraph() {
        //Update the graphs
        summaryPanel.setTotalMacronutrients(
                getFoodListPanel().getCurrentCalories(),
                getFoodListPanel().getCurrentProteins(),
                getFoodListPanel().getCurrentCarbs(),
                getFoodListPanel().getCurrentFats()
        );
    }
}
