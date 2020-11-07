package jimmy.mcgymmy.logic.parser.parameter;

import java.util.Optional;

import jimmy.mcgymmy.logic.parser.exceptions.ParseException;

/**
 * Class used to declare and contain required parameters for McGymmy commands.
 *
 * @param <T> The type of the parameter.
 */
public class Parameter<T> extends AbstractParameter {
    private final ParameterConverter<T> converter;
    private Optional<T> value = Optional.empty();

    /**
     * Creates a required parameter.
     *
     * @param name        Name of the parameter.
     * @param flag        flag used in the command, e.g. '-c' in 'add banana -c 100'
     * @param description short description of what the parameter represents, e.g. 'carbs as an integer'
     * @param example     example value, e.g. '100'
     * @param converter   Function to parse the value from a String to T. May throw a ParseError.
     */
    public Parameter(String name, String flag, String description, String example, ParameterConverter<T> converter) {
        super(name, flag, description, example, true);
        this.converter = converter;
    }

    @Override
    public void setValue(String rawValue) throws ParseException {
        super.setValue(rawValue);
        this.value = Optional.of(converter.apply(rawValue));
    }

    /**
     * Returns the value entered by the user. Value is guaranteed to be present.
     *
     * @return T value that was parsed.
     */
    public T consume() {
        assert this.value.isPresent() : "Command being run before being initialized correctly.";
        return this.value.get();
    }
}
