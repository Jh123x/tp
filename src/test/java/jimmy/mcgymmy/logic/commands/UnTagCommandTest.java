package jimmy.mcgymmy.logic.commands;

import org.junit.jupiter.api.Test;

import jimmy.mcgymmy.commons.core.Messages;
import jimmy.mcgymmy.commons.core.index.Index;
import jimmy.mcgymmy.commons.exceptions.IllegalValueException;
import jimmy.mcgymmy.logic.parser.CommandParserTestUtil;
import jimmy.mcgymmy.model.Model;
import jimmy.mcgymmy.model.ModelManager;
import jimmy.mcgymmy.model.UserPrefs;
import jimmy.mcgymmy.model.food.Food;
import jimmy.mcgymmy.model.tag.Tag;
import jimmy.mcgymmy.testutil.FoodBuilder;
import jimmy.mcgymmy.testutil.TypicalFoods;
import jimmy.mcgymmy.testutil.TypicalIndexes;

class UnTagCommandTest {

    private final Model model = new ModelManager(TypicalFoods.getTypicalMcGymmy(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() throws IllegalValueException {
        Index index = TypicalIndexes.INDEX_FIRST_FOOD;
        Food foodToRmTag = model.getFilteredFoodList().get(index.getZeroBased());
        UnTagCommand unTagCommand = new UnTagCommand();
        unTagCommand.setParameters(
                new CommandParserTestUtil.ParameterStub<>("", TypicalIndexes.INDEX_FIRST_FOOD),
                new CommandParserTestUtil.ParameterStub<>("t", new Tag("lunch"))
        );

        String expectedMessage = String.format(UnTagCommand.MESSAGE_SUCCESS, "lunch");

        ModelManager expectedModel = new ModelManager(model.getMcGymmy(), new UserPrefs());
        Food expFood = new FoodBuilder(foodToRmTag).build();
        expFood.removeTag(new Tag("lunch"));
        expectedModel.setFood(index, expFood);

        CommandTestUtil.assertCommandSuccess(unTagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() throws IllegalValueException {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredFoodList().size() + 1);
        UnTagCommand unTagCommand = new UnTagCommand();
        unTagCommand.setParameters(
                new CommandParserTestUtil.ParameterStub<>("", outOfBoundIndex),
                new CommandParserTestUtil.ParameterStub<>("t", new Tag("lunch"))
        );
        CommandTestUtil.assertCommandFailure(unTagCommand, model, Messages.MESSAGE_INVALID_FOOD_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexUnfilteredList_throwsCommandException() throws IllegalValueException {
        UnTagCommand unTagCommand = new UnTagCommand();
        unTagCommand.setParameters(
                new CommandParserTestUtil.ParameterStub<>("", TypicalIndexes.INDEX_FIRST_FOOD),
                new CommandParserTestUtil.ParameterStub<>("t", new Tag("DoesNotExist"))
        );
        Food food = model.getFilteredFoodList().get(TypicalIndexes.INDEX_FIRST_FOOD.getZeroBased());

        CommandTestUtil.assertCommandFailure(unTagCommand, model, String.format(UnTagCommand.MESSAGE_NOT_FOUND_TAG,
                "DoesNotExist", food.getName().fullName));
    }

    @Test
    public void execute_validIndexFilteredList_success() throws IllegalValueException {

        CommandTestUtil.showFoodAtIndex(model, TypicalIndexes.INDEX_SECOND_FOOD);

        Food foodToTag = model.getFilteredFoodList().get(TypicalIndexes.INDEX_FIRST_FOOD.getZeroBased());
        UnTagCommand unTagCommand = new UnTagCommand();
        unTagCommand.setParameters(
                new CommandParserTestUtil.ParameterStub<>("", TypicalIndexes.INDEX_FIRST_FOOD),
                new CommandParserTestUtil.ParameterStub<>("t", new Tag("lunch"))
        );

        String expectedMessage = String.format(UnTagCommand.MESSAGE_SUCCESS, "lunch");
        ModelManager expectedModel = new ModelManager(model.getMcGymmy(), new UserPrefs());
        Food expFood = new FoodBuilder(foodToTag).build();
        expFood.removeTag(new Tag("lunch"));
        expectedModel.setFood(TypicalIndexes.INDEX_SECOND_FOOD, expFood);

        CommandTestUtil.assertCommandSuccess(unTagCommand, model, expectedMessage, expectedModel);
    }

}
