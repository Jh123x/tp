package jimmy.mcgymmy.testutil;

import jimmy.mcgymmy.model.macro.Macro;

public class TypicalMacros {
    public static final Macro TEST_MACRO;
    public static final Macro UNNAMED_PARAMETER_TEST_MACRO;
    private static final String addString = "add -n %s -c \\c -p \\p";
    private static final String[] statements = new String[] {
            String.format(addString, "first"),
            String.format(addString, "second")};

    static {
        try {
            TEST_MACRO = new Macro("test", new String[] {"p", "c"}, statements);
            UNNAMED_PARAMETER_TEST_MACRO = new Macro("test2", new String[] {"q"}, new String[] {"add -n \\$ -f \\q"});
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
