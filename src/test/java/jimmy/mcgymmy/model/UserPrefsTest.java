package jimmy.mcgymmy.model;

import static jimmy.mcgymmy.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

public class UserPrefsTest {

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        UserPrefs userPref = new UserPrefs();
        assertThrows(NullPointerException.class, () -> userPref.setGuiSettings(null));
    }

    @Test
    public void setMcGymmyFilePath_nullPath_throwsNullPointerException() {
        UserPrefs userPrefs = new UserPrefs();
        assertThrows(NullPointerException.class, () -> userPrefs.setMcGymmyFilePath(null));
        assertThrows(NullPointerException.class, () -> userPrefs.setMcGymmyFilePath(null));
    }

    @Test
    public void equals_returnsCorrectResult() {
        UserPrefs userPref = new UserPrefs();
        // identical -> equal
        assertEquals(userPref, userPref);

        // same values different objects -> Equal
        UserPrefs userPrefsCopy = new UserPrefs();
        assertEquals(userPref, userPrefsCopy);

        // not same type -> not equal
        assertNotEquals(userPref, "123");

        // different value -> not equal
        UserPrefs userPrefs2 = new UserPrefs();
        userPrefs2.setMcGymmyFilePath(Paths.get("testpath"));
        assertNotEquals(userPrefs2, userPref);

    }

}
