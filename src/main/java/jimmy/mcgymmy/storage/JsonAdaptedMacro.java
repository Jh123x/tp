package jimmy.mcgymmy.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import jimmy.mcgymmy.commons.exceptions.IllegalValueException;
import jimmy.mcgymmy.logic.macro.Macro;
import jimmy.mcgymmy.logic.parser.exceptions.ParseException;

/**
 * Jackson-friendly version of {@link Macro}.
 */
public class JsonAdaptedMacro {
    private final String name;
    private final String[] rawCommands;
    private final String[] macroArguments;

    /**
     * Constructs a {@code JsonAdaptedMacro} with the given macro details.
     */
    @JsonCreator
    public JsonAdaptedMacro(@JsonProperty("name") String name, @JsonProperty("arguments") String[] macroArguments,
                            @JsonProperty("commands") String[] rawCommands) {
        this.name = name;
        this.rawCommands = rawCommands;
        this.macroArguments = macroArguments;
    }

    /**
     * Converts a given {@code Macro} into this class for Jackson use.
     */
    public JsonAdaptedMacro(Macro source) {
        this.name = source.getName();
        this.rawCommands = source.getRawCommands();
        this.macroArguments = source.getMacroArguments();
    }

    /**
     * Converts this Jackson-friendly adapted macro object into a {@code Macro} object.
     *
     * @throws IllegalValueException if the macro is invalid.
     */
    public Macro toMacro() throws IllegalValueException {
        try {
            return new Macro(this.name, this.macroArguments, this.rawCommands);
        } catch (ParseException e) {
            throw new IllegalValueException(e.getMessage());
        }
    }

}
