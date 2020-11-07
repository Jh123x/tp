package jimmy.mcgymmy.commons.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.awt.Point;

import org.junit.jupiter.api.Test;

class GuiSettingsTest {
    private static final GuiSettings DEFAULT_SETTINGS = new GuiSettings();
    private static final GuiSettings DEFAULT_SETTINGS_2 = new GuiSettings();
    private static final GuiSettings DIFFERENT_SETTINGS_1 = new GuiSettings(100, 100, 10, 10);
    private static final GuiSettings DIFFERENT_SETTINGS_2 = new GuiSettings(100, 100, 10, 10);
    private static final double DEFAULT_HEIGHT = 600;
    private static final double DEFAULT_WIDTH = 740;
    private static final double OTHER_HEIGHT = 100;
    private static final double OTHER_WIDTH = 100;
    private static final Point DEFAULT_POINT = null;
    private static final Point OTHER_POINT = new Point(10, 10);

    @Test
    public void getWindowsCoordinatesTest() {

        //Check if default value is correct
        assertEquals(DEFAULT_SETTINGS.getWindowCoordinates(), DEFAULT_POINT);

        //Check if they are different
        assertNotEquals(DEFAULT_SETTINGS.getWindowCoordinates(), OTHER_POINT);

        //Check if non-default values are equal
        assertEquals(DIFFERENT_SETTINGS_2.getWindowCoordinates(), OTHER_POINT);
    }

    @Test
    public void getWindowsHeightTest() {

        //Check if default value is correct
        assertEquals(DEFAULT_SETTINGS.getWindowHeight(), DEFAULT_HEIGHT);

        //Check if they are different
        assertNotEquals(DEFAULT_SETTINGS.getWindowHeight(), OTHER_HEIGHT);

        //Check if non-default values are equal
        assertEquals(DIFFERENT_SETTINGS_2.getWindowHeight(), OTHER_HEIGHT);
    }

    @Test
    public void getWindowsWidthTest() {

        //Check if default value is correct
        assertEquals(DEFAULT_SETTINGS.getWindowWidth(), DEFAULT_WIDTH);

        //Check if they are different
        assertNotEquals(DEFAULT_SETTINGS.getWindowWidth(), OTHER_WIDTH);

        //Check if non-default values are equal
        assertEquals(DIFFERENT_SETTINGS_2.getWindowWidth(), OTHER_WIDTH);
    }

    @Test
    public void equalsTest() {
        assertNotNull(DEFAULT_SETTINGS);

        //Check if 2 GUI settings are equal
        assertEquals(DEFAULT_SETTINGS, DEFAULT_SETTINGS_2);

        //Check if they are different
        assertNotEquals(DEFAULT_SETTINGS, DIFFERENT_SETTINGS_1);

        //Check if 2 initialised GUI Settings are the same
        assertEquals(DIFFERENT_SETTINGS_2, DIFFERENT_SETTINGS_1);
    }
}
