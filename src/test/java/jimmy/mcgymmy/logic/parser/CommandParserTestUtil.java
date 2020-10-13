package jimmy.mcgymmy.logic.parser;

import java.util.List;
import java.util.Optional;

import jimmy.mcgymmy.logic.commands.Command;
import jimmy.mcgymmy.logic.parser.parameter.AbstractParameter;
import jimmy.mcgymmy.logic.parser.parameter.OptionalParameter;
import jimmy.mcgymmy.logic.parser.parameter.Parameter;

/**
 * Contains helper methods for testing command parsers.
 */
public class CommandParserTestUtil {

    public static Parameter<String> makeDummyParameter(String name, String flag) {
        return new Parameter<>(name, flag, "test", "test", (s) -> s);
    }

    public static OptionalParameter<String> makeDummyOptionalParameter(String name, String flag) {
        return new OptionalParameter<>(name, flag, "test", "test", (s) -> s);
    }

    /**
     * Retrieves the rawValue of a command's parameter
     *
     * @param command the command to inspect
     * @param flag    the desired flag
     * @return the raw value of the parameter, Optional.empty() otherwise.
     */
    public static Optional<String> commandParameterValue(Command command, String flag) {
        List<AbstractParameter> parameterList = command.getParameterSet().getParameterList();
        for (AbstractParameter parameter : parameterList) {
            if (parameter.getFlag().equals(flag)) {
                return parameter.getRawValue();
            }
        }
        return Optional.empty();
    }

    public static class ParameterStub<T> extends Parameter<T> {
        private final T value;

        /**
         * Creates a parameter with a fixed flag and value.
         *
         * @param flag  flag for parameter
         * @param value fixed value
         */
        public ParameterStub(String flag, T value) {
            super("stub", flag, "stub", "NA", s -> value);
            this.value = value;
        }

        @Override
        public T consume() {
            return this.value;
        }
    }

    public static class OptionalParameterStub<T> extends OptionalParameter<T> {
        private final Optional<T> value;

        /**
         * Creates an optional parameter with a fixed flag and value.
         *
         * @param flag  flag for parameter
         * @param value fixed value
         */
        public OptionalParameterStub(String flag, T value) {
            super("stub", flag, "stub", "NA", s -> value);
            this.value = Optional.of(value);
        }

        /**
         * Creates an optional parameter with a fixed flag but no value.
         *
         * @param flag flag for parameter
         */
        public OptionalParameterStub(String flag) {
            super("stub", flag, "stub", "NA", s -> null);
            this.value = Optional.empty();
        }

        @Override
        public Optional<T> getValue() {
            return this.value;
        }
    }
}
