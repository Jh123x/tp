package jimmy.mcgymmy.model.macro.exceptions;

import jimmy.mcgymmy.commons.exceptions.IllegalValueException;

public class DuplicateMacroException extends IllegalValueException {
    private static final String defaultMessage = "This macro's name has already been taken.";

    public DuplicateMacroException() {
        super(defaultMessage);
    }
}
