package jimmy.mcgymmy.model;

import static java.util.Objects.requireNonNull;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import jimmy.mcgymmy.commons.core.GuiSettings;

/**
 * Represents User's preferences.
 */
public class UserPrefs implements ReadOnlyUserPrefs {

    private GuiSettings guiSettings = new GuiSettings();
    private Path mcGymmyFilePath = Paths.get("data", "mcgymmy.json");
    private Path macroListFilePath = Paths.get("data", "macrolist.json");

    /**
     * Creates a {@code UserPrefs} with default values.
     */
    public UserPrefs() {
    }

    /**
     * Creates a {@code UserPrefs} with the prefs in {@code userPrefs}.
     */
    public UserPrefs(ReadOnlyUserPrefs userPrefs) {
        this();
        resetData(userPrefs);
    }

    /**
     * Resets the existing data of this {@code UserPrefs} with {@code newUserPrefs}.
     */
    public void resetData(ReadOnlyUserPrefs newUserPrefs) {
        requireNonNull(newUserPrefs);
        setGuiSettings(newUserPrefs.getGuiSettings());
        setMcGymmyFilePath(newUserPrefs.getMcGymmyFilePath());
        setMacroListFilePath(newUserPrefs.getMacroListFilePath());
    }

    public GuiSettings getGuiSettings() {
        return guiSettings;
    }

    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        this.guiSettings = guiSettings;
    }

    public Path getMcGymmyFilePath() {
        return mcGymmyFilePath;
    }

    public void setMcGymmyFilePath(Path mcGymmyFilePath) {
        requireNonNull(mcGymmyFilePath);
        this.mcGymmyFilePath = mcGymmyFilePath;
    }

    public Path getMacroListFilePath() {
        return macroListFilePath;
    }

    public void setMacroListFilePath(Path macroListFilePath) {
        requireNonNull(macroListFilePath);
        this.macroListFilePath = macroListFilePath;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof UserPrefs)) { //this handles null as well.
            return false;
        }

        UserPrefs o = (UserPrefs) other;

        return guiSettings.equals(o.guiSettings)
                && mcGymmyFilePath.equals(o.mcGymmyFilePath)
                && macroListFilePath.equals(o.macroListFilePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guiSettings, mcGymmyFilePath, macroListFilePath);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Gui Settings : " + guiSettings);
        sb.append("\nLocal data file location : " + mcGymmyFilePath);
        sb.append("\nLocal macro file location : " + macroListFilePath);
        return sb.toString();
    }

}
