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

class TagCommandTest {

    private final Model model = new ModelManager(TypicalFoods.getTypicalMcGymmy(), new UserPrefs());
    private final String tag1 = "hello";
    private final String tag2 = "goodbye";
    private final String tag3 = "seeYouAgain";

    @Test
    public void execute_validIndexUnfilteredList_success() throws IllegalValueException {
        Index index = TypicalIndexes.INDEX_FIRST_FOOD;
        Food foodToTag = model.getFilteredFoodList().get(index.getZeroBased());
        TagCommand tagCommand = new TagCommand();
        tagCommand.setParameters(
                new CommandParserTestUtil.ParameterStub<>("", TypicalIndexes.INDEX_FIRST_FOOD),
                new CommandParserTestUtil.ParameterStub<>("t", new Tag(tag1))
        );

        String expectedMessage = String.format(TagCommand.MESSAGE_SUCCESS, tag1);

        ModelManager expectedModel = new ModelManager(model.getMcGymmy(), new UserPrefs());
        expectedModel.setFood(index, new FoodBuilder(foodToTag).withTags(tag1).build());

        CommandTestUtil.assertCommandSuccess(tagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() throws IllegalValueException {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredFoodList().size() + 1);
        TagCommand tagCommand = new TagCommand();
        tagCommand.setParameters(
                new CommandParserTestUtil.ParameterStub<>("", outOfBoundIndex),
                new CommandParserTestUtil.ParameterStub<>("t", new Tag(tag1))
        );
        CommandTestUtil.assertCommandFailure(tagCommand, model, Messages.MESSAGE_INVALID_FOOD_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() throws IllegalValueException {

        CommandTestUtil.showFoodAtIndex(model, TypicalIndexes.INDEX_FIRST_FOOD);

        Food foodToTag = model.getFilteredFoodList().get(TypicalIndexes.INDEX_FIRST_FOOD.getZeroBased());
        TagCommand tagCommand = new TagCommand();
        tagCommand.setParameters(
                new CommandParserTestUtil.ParameterStub<>("", TypicalIndexes.INDEX_FIRST_FOOD),
                new CommandParserTestUtil.ParameterStub<>("t", new Tag(tag3))
        );

        String expectedMessage = String.format(TagCommand.MESSAGE_SUCCESS, tag3);
        ModelManager expectedModel = new ModelManager(model.getMcGymmy(), new UserPrefs());
        expectedModel.setFood(Index.fromZeroBased(0),
                new FoodBuilder(foodToTag).withTags(tag3).build());

        CommandTestUtil.assertCommandSuccess(tagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateTag_throwsCommandException() throws IllegalValueException {
        Index index = TypicalIndexes.INDEX_FIRST_FOOD;
        Food foodToTag = model.getFilteredFoodList().get(index.getZeroBased());
        TagCommand tagCommand = new TagCommand();
        tagCommand.setParameters(
            new CommandParserTestUtil.ParameterStub<>("", TypicalIndexes.INDEX_FIRST_FOOD),
            new CommandParserTestUtil.ParameterStub<>("t", new Tag(tag2))
        );

        String expectedMessage = String.format(TagCommand.MESSAGE_SUCCESS, tag2);

        ModelManager expectedModel = new ModelManager(model.getMcGymmy(), new UserPrefs());
        expectedModel.setFood(index, new FoodBuilder(foodToTag).withTags(tag2).build());

        // add tag for the first time
        CommandTestUtil.assertCommandSuccess(tagCommand, model, expectedMessage, expectedModel);
        CommandTestUtil.assertCommandFailure(tagCommand, model,
            String.format(TagCommand.MESSAGE_DUPLICATE_TAG, tag2, foodToTag.getName().fullName));
    }
}
