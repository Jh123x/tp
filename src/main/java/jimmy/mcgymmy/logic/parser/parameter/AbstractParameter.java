package jimmy.mcgymmy.logic.parser.parameter;

import java.util.Optional;

import org.apache.commons.cli.Option;

import jimmy.mcgymmy.logic.parser.exceptions.ParseException;

/**
 * Abstract class for the parameter classes.
 */
public abstract class AbstractParameter {
    private final Option option;
    private final String flag;
    private final String name;
    private final String description;
    private final String example;
    private Optional<String> rawValue = Optional.empty();

    protected AbstractParameter(String name, String flag, String description, String example, boolean isRequired) {
        this.name = name;
        this.flag = flag;
        this.description = description;
        this.example = example;
        this.option = Option.builder(flag)
                .longOpt(name)
                .desc(description)
                .hasArg()
                .numberOfArgs(Option.UNLIMITED_VALUES)
                .required(isRequired)
                .build();
    }

    public void setValue(String rawValue) throws ParseException {
        this.rawValue = Optional.of(rawValue);
    }

    public Optional<String> getRawValue() {
        return this.rawValue;
    }

    public String getName() {
        return this.name;
    }

    public String getFlag() {
        return this.flag;
    }

    public String getDescription() {
        return this.description;
    }

    public String getExample() {
        return this.example;
    }

    public Option asOption() {
        return this.option;
    }

    public boolean isRequired() {
        return true;
    }
}
