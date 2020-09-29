package seedu.address.logic.parser.parameter;

import java.util.Optional;

import org.apache.commons.cli.Option;

import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Abstract class for the parameter classes.
 */
public abstract class AbstractParameter {
    private final Option option;
    private final String flag;
    private final String example;
    private Optional<String> rawValue = Optional.empty();

    protected AbstractParameter(String name, String flag, String description, String example, boolean isRequired) {
        this.flag = flag;
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

    public String getFlag() {
        return this.flag;
    }

    public String getExample() {
        return this.example;
    }

    public Option asOption() {
        return this.option;
    }
}
