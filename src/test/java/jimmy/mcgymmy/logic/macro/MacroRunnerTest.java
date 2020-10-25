package jimmy.mcgymmy.logic.macro;

import static jimmy.mcgymmy.testutil.TypicalFoods.getTypicalMcGymmy;
import static jimmy.mcgymmy.testutil.TypicalMacros.TEST_MACRO;
import static jimmy.mcgymmy.testutil.TypicalMacros.UNNAMED_PARAMETER_TEST_MACRO;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.junit.jupiter.api.Test;

import jimmy.mcgymmy.model.Model;
import jimmy.mcgymmy.model.ModelManager;
import jimmy.mcgymmy.model.UserPrefs;
import jimmy.mcgymmy.model.food.Food;
import jimmy.mcgymmy.model.macro.Macro;

public class MacroRunnerTest {

    @Test
    void testMacro_addsToModel() throws Exception {
        testMacroAdd(TEST_MACRO, "-p 20 -c 200", new String[]{"first", "second"});
    }

    @Test
    void testMacroWithUnnamedParameter_addsToModel() throws Exception {
        testMacroAdd(UNNAMED_PARAMETER_TEST_MACRO, "nababa -q 200", new String[]{"nababa"});
        testMacroAdd(UNNAMED_PARAMETER_TEST_MACRO, "cockroach -q 9912629", new String[]{"cockroach"});
    }

    private void testMacroAdd(Macro toTest, String arguments, String[] itemsToCheckInModel) throws Exception {
        Model model = new ModelManager(getTypicalMcGymmy(), new UserPrefs());
        CommandLineParser commandLineParser = new DefaultParser();
        Options options = toTest.getOptions();
        CommandLine args = commandLineParser.parse(options, arguments.split(" "));
        MacroRunner.asCommandInstance(toTest, args).execute(model);
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
