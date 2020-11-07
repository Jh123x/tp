package jimmy.mcgymmy.model;

import static jimmy.mcgymmy.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
        // identical -> returns true
        assertEquals(userPref, userPref);
        // not same type -> returns false
        assertFalse(userPref.equals("123"));
        // same value -> return true
        assertEquals(new UserPrefs(userPref), new UserPrefs(userPref));
    }

}
