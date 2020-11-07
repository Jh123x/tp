package jimmy.mcgymmy.logic.parser.parameter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import jimmy.mcgymmy.logic.parser.CommandParserTestUtil;

public class OptionalParameterTest {

    private static final OptionalParameter<String> OPTIONAL_PARAMETER =
            CommandParserTestUtil.makeDummyOptionalParameter("test", "t");
    @Test
    void optionalParameter_storesCorrectValue() throws Exception {
        OptionalParameter<Integer> testParameter = new OptionalParameter<>(
                "intparam",
                "i",
                "test",
                "test",
                String::length);
        testParameter.setValue("abcdef");
        assertEquals(testParameter.getValue().map(i -> i + 1), Optional.of(7));
    }

    @Test
    void optionalParameter_keepsParentRawValue() throws Exception {
        OptionalParameter<String> parameter = CommandParserTestUtil.makeDummyOptionalParameter("test", "t");
        assertEquals(parameter.getRawValue(), OPTIONAL_PARAMETER.getRawValue());
        parameter.setValue("poop");
        assertEquals(parameter.getRawValue(), Optional.of("poop"));
        assertNotEquals(parameter.getRawValue(), OPTIONAL_PARAMETER.getRawValue());
    }

    @Test
    public void optionalParameter_notRequired() {
        assertFalse(OPTIONAL_PARAMETER.isRequired());
    }

}
