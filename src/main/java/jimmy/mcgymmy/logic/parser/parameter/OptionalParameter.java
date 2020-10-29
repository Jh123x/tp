package jimmy.mcgymmy.logic.parser.parameter;

import java.util.Optional;

import jimmy.mcgymmy.logic.parser.exceptions.ParseException;

/**
 * Class used to declare and contain optional parameters for McGymmy commands.
 *
 * @param <T> The type of the parameter.
 */
public class OptionalParameter<T> extends AbstractParameter {
    private final ParameterConverter<T> converter;
    private Optional<T> value = Optional.empty();

    /**
     * Create an optional parameter
     *
     * @param name        Name of the parameter.
     * @param flag        flag used in the command, e.g. '-c' in 'add banana -c 100'
     * @param description short description of what the parameter represents, e.g. 'carbs as an integer'
     * @param example     example value, e.g. '100'
     * @param converter   Function to parse the value from a String to T. May throw a ParseError.
     */
    public OptionalParameter(String name, String flag, String description,
                             String example, ParameterConverter<T> converter) {
        super(name, flag, description, example, false);
        this.converter = converter;
    }

    public Optional<T> getValue() {
        return value;
    }

    @Override
    public void setValue(String rawValue) throws ParseException {
        super.setValue(rawValue);
        this.value = Optional.of(converter.apply(rawValue));
    }

    @Override
    public boolean isRequired() {
        return false;
    }
}

