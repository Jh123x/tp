package jimmy.mcgymmy.commons.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

public class ConfigTest {

    private static final Path DEFAULT_PREF_PATH = Paths.get("preferences.json");
    private static final Path TEST_PREF_PATH = Paths.get("dummy.json");

    @Test
    public void toString_defaultObject_stringReturned() {
        String defaultConfigAsString = "Current log level : INFO\n"
                + "Preference file Location : preferences.json";

        assertEquals(defaultConfigAsString, new Config().toString());
    }

    @Test
    public void getPathTest() {

        //Check if getPath is correct
        Config defaultConfig = new Config();
        assertEquals(defaultConfig.getUserPrefsFilePath(), DEFAULT_PREF_PATH);

        //Check set path
        defaultConfig.setUserPrefsFilePath(TEST_PREF_PATH);
        assertEquals(TEST_PREF_PATH, defaultConfig.getUserPrefsFilePath());

        //Check for inequality
        assertNotSame(DEFAULT_PREF_PATH, defaultConfig.getUserPrefsFilePath());
    }

    @Test
    public void equalsMethod() {
        Config defaultConfig = new Config();
        assertNotNull(defaultConfig);

        //Test same object
        assertEquals(defaultConfig, defaultConfig);

        //2 Object with Same type
        Config defaultConfig2 = new Config();
        assertEquals(defaultConfig2, defaultConfig);

        //Same type of object with different types
        Config config3 = new Config();
        config3.setUserPrefsFilePath(TEST_PREF_PATH);
        assertNotEquals(config3, defaultConfig);

        //2 Modified Configs
        Config config4 = new Config();
        config4.setUserPrefsFilePath(TEST_PREF_PATH);
        assertEquals(config3, config4);
    }
}
