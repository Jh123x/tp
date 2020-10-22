package jimmy.mcgymmy.model;

import java.nio.file.Path;

import jimmy.mcgymmy.commons.core.GuiSettings;

/**
 * Unmodifiable view of user prefs.
 */
public interface ReadOnlyUserPrefs {

    GuiSettings getGuiSettings();

    Path getMcGymmyFilePath();

    Path getMacroListFilePath();
}
