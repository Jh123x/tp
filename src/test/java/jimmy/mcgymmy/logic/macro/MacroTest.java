package jimmy.mcgymmy.logic.macro;

import static jimmy.mcgymmy.testutil.TypicalFoods.getTypicalMcGymmy;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.junit.jupiter.api.Test;

import jimmy.mcgymmy.model.Model;
import jimmy.mcgymmy.model.ModelManager;
import jimmy.mcgymmy.model.UserPrefs;
import jimmy.mcgymmy.model.food.Food;

public class MacroTest {
    private final Macro testMacro;
    private final Macro unnamedParameterTestMacro;

    public MacroTest() throws Exception {
        String addString = "add -n %s -c \\c -p \\p";
        String[] statements = new String[]{String.format(addString, "first"), String.format(addString, "second")};
        this.testMacro = new Macro("test", new String[]{"p", "c"}, statements);
        this.unnamedParameterTestMacro = new Macro("test2", new String[]{"q"}, new String[]{"add -n \\$ -f \\q"});
    }

    @Test
    void testMacro_createdWithCorrectOptions() {
        assertTrue(testMacro.getOptions().hasOption("c"));
        assertTrue(testMacro.getOptions().hasOption("p"));
    }

    @Test
    void testMacro_noExtraOptions() {
        assertEquals(testMacro.getOptions().getOptions().size(), 2);
    }

    @Test
    void testMacro_addsToModel() throws Exception {
        testMacroAdd(testMacro, "-p 20 -c 200", new String[]{"first", "second"});
    }

    @Test
    void testMacroWithUnnamedParameter_addsToModel() throws Exception {
        testMacroAdd(unnamedParameterTestMacro, "nababa -q 200", new String[]{"nababa"});
        testMacroAdd(unnamedParameterTestMacro, "cockroach -q 9912629", new String[]{"cockroach"});
    }

    private void testMacroAdd(Macro toTest, String arguments, String[] itemsToCheckInModel) throws Exception {
        Model model = new ModelManager(getTypicalMcGymmy(), new UserPrefs());
        CommandLineParser commandLineParser = new DefaultParser();
        Options options = toTest.getOptions();
        CommandLine args = commandLineParser.parse(options, arguments.split(" "));
        toTest.executeWith(model, args);
        boolean[] foundList = new boolean[itemsToCheckInModel.length];
        boolean[] allTrueList = new boolean[itemsToCheckInModel.length];
        for (int i = 0; i < itemsToCheckInModel.length; i++) {
            foundList[i] = false;
            allTrueList[i] = true;
        }
        for (Food food : model.getFilteredFoodList()) {
            // TODO: possibly add more convenience functions to our model.
            for (int i = 0; i < itemsToCheckInModel.length; i++) {
                if (food.getName().fullName.equals(itemsToCheckInModel[i])) {
                    foundList[i] = true;
                }
            }
        }
        assertArrayEquals(allTrueList, foundList);
    }
}
