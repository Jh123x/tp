package jimmy.mcgymmy.commons.util;

import static java.util.Objects.requireNonNull;

import javafx.scene.image.Image;
import jimmy.mcgymmy.MainApp;
import jimmy.mcgymmy.commons.exceptions.IllegalValueException;

/**
 * A container for App specific utility functions
 */
public class AppUtil {

    /**
     * Gets an {@code Image} from the specified path.
     */
    public static Image getImage(String imagePath) {
        requireNonNull(imagePath);
        return new Image(MainApp.class.getResourceAsStream(imagePath));
    }

    /**
     * Checks that {@code condition} is true. Used for validating arguments to methods.
     *
     * @throws IllegalValueException if {@code condition} is false.
     */
    public static void checkArgument(Boolean condition) throws IllegalValueException {
        if (!condition) {
            throw new IllegalValueException("Error");
        }
    }

    /**
     * Checks that {@code condition} is true. Used for validating arguments to methods.
     *
     * @throws IllegalValueException with {@code errorMessage} if {@code condition} is false.
     */
    public static void checkArgument(Boolean condition, String errorMessage) throws IllegalValueException {
        if (!condition) {
            throw new IllegalValueException(errorMessage);
        }
    }
}
