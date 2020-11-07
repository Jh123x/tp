package jimmy.mcgymmy.logic.parser.parameter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import jimmy.mcgymmy.logic.parser.CommandParserTestUtil;

public class ParameterTest { // TODO: options test, here + optional param

    @Test
    void parameter_storesCorrectValue() throws Exception {
        //Test values
        Parameter<Integer> testParameter = new Parameter<>("intparam", "j", "test", "test", String::length);
        testParameter.setValue("abcdef");
        assertEquals(testParameter.consume(), 6);

        //Test another value
        Parameter<Integer> testParameter2 = new Parameter<>("intparam2", "j", "test2", "test2", String::length);
        testParameter2.setValue("Hello world   ");
        assertEquals(testParameter2.consume(), 14);
    }

    @Test
    void parameter_keepsParentRawValue() throws Exception {
        Parameter<String> parameter = CommandParserTestUtil.makeDummyParameter("test", "t");
        parameter.setValue("poop");
        assertEquals(parameter.getRawValue(), Optional.of("poop"));
    }
}
