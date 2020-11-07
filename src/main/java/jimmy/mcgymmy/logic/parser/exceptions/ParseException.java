package jimmy.mcgymmy.logic.parser.exceptions;

import jimmy.mcgymmy.commons.exceptions.IllegalValueException;

/**
 * Represents a parse error encountered by a parser.
 */
public class ParseException extends IllegalValueException {

    public ParseException(String message) {
        super(message);
    }

}
