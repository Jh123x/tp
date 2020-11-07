package jimmy.mcgymmy.logic.commands;

import static jimmy.mcgymmy.logic.commands.CommandTestUtil.assertCommandFailure;
import static jimmy.mcgymmy.logic.commands.CommandTestUtil.assertCommandSuccess;
import static jimmy.mcgymmy.logic.commands.CommandTestUtil.showFoodAtIndex;
import static jimmy.mcgymmy.testutil.TypicalFoods.getTypicalMcGymmy;
import static jimmy.mcgymmy.testutil.TypicalIndexes.INDEX_FIRST_FOOD;
import static jimmy.mcgymmy.testutil.TypicalIndexes.INDEX_SECOND_FOOD;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import jimmy.mcgymmy.commons.core.Messages;
import jimmy.mcgymmy.commons.core.index.Index;
import jimmy.mcgymmy.commons.exceptions.IllegalValueException;
import jimmy.mcgymmy.logic.parser.CommandParserTestUtil;
import jimmy.mcgymmy.model.McGymmy;
import jimmy.mcgymmy.model.Model;
import jimmy.mcgymmy.model.ModelManager;
import jimmy.mcgymmy.model.UserPrefs;
import jimmy.mcgymmy.model.food.Food;
import jimmy.mcgymmy.model.food.Name;
import jimmy.mcgymmy.testutil.FoodBuilder;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for EditCommand.
 */
public class EditCommandTest {
    private static final String VALID_NAME_BOB = "McSpicy";
    private static final String VALID_PROTEIN_BOB = "999";
    private static final String VALID_DATE = "12-04-2020";
    private final Model model = new ModelManager(getTypicalMcGymmy(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() throws IllegalValueException {
        Food editedFood = new FoodBuilder().withTags("lunch").build();

        EditCommand editCommand = new EditCommand();
        editCommand.setParameters(
                new CommandParserTestUtil.ParameterStub<>("", INDEX_FIRST_FOOD),
                new CommandParserTestUtil.OptionalParameterStub<>("n", editedFood.getName()),
                new CommandParserTestUtil.OptionalParameterStub<>("p", editedFood.getProtein()),
                new CommandParserTestUtil.OptionalParameterStub<>("f", editedFood.getFat()),
                new CommandParserTestUtil.OptionalParameterStub<>("c", editedFood.getCarbs()),
                new CommandParserTestUtil.OptionalParameterStub<>("d", editedFood.getDate())
        );

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_FOOD_SUCCESS, editedFood);

        Model expectedModel = new ModelManager(new McGymmy(model.getMcGymmy()), new UserPrefs());
        expectedModel.setFood(Index.fromZeroBased(0), editedFood);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() throws IllegalValueException {
        Index indexLastFood = Index.fromOneBased(model.getFilteredFoodList().size());
        Food lastFood = model.getFilteredFoodList().get(indexLastFood.getZeroBased());

        FoodBuilder foodInList = new FoodBuilder(lastFood);
        Food editedFood = foodInList.withName(new Name(VALID_NAME_BOB)).withProtein(VALID_PROTEIN_BOB).build();

        EditCommand editCommand = new EditCommand();
        editCommand.setParameters(
                new CommandParserTestUtil.ParameterStub<>("", indexLastFood),
                new CommandParserTestUtil.OptionalParameterStub<>("n", editedFood.getName()),
                new CommandParserTestUtil.OptionalParameterStub<>("p", editedFood.getProtein()),
                new CommandParserTestUtil.OptionalParameterStub<>("f"),
                new CommandParserTestUtil.OptionalParameterStub<>("c"),
                new CommandParserTestUtil.OptionalParameterStub<>("d")
        );

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_FOOD_SUCCESS, editedFood);

        Model expectedModel = new ModelManager(new McGymmy(model.getMcGymmy()), new UserPrefs());
        expectedModel.setFood(indexLastFood, editedFood);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_dateSpecifiedUnfilteredList_success() throws IllegalValueException {
        Index indexLastFood = Index.fromOneBased(model.getFilteredFoodList().size());
        Food lastFood = model.getFilteredFoodList().get(indexLastFood.getZeroBased());

        FoodBuilder foodInList = new FoodBuilder(lastFood);
        Food editedFood = foodInList.withDate(VALID_DATE).build();

        EditCommand editCommand = new EditCommand();
        editCommand.setParameters(
                new CommandParserTestUtil.ParameterStub<>("", indexLastFood),
                new CommandParserTestUtil.OptionalParameterStub<>("n"),
                new CommandParserTestUtil.OptionalParameterStub<>("p"),
                new CommandParserTestUtil.OptionalParameterStub<>("f"),
                new CommandParserTestUtil.OptionalParameterStub<>("c"),
                new CommandParserTestUtil.OptionalParameterStub<>("d", editedFood.getDate())
        );

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_FOOD_SUCCESS, editedFood);

        Model expectedModel = new ModelManager(new McGymmy(model.getMcGymmy()), new UserPrefs());
        expectedModel.setFood(indexLastFood, editedFood);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_failure() {
        EditCommand editCommand = new EditCommand();
        editCommand.setParameters(
                new CommandParserTestUtil.ParameterStub<>("", INDEX_FIRST_FOOD),
                new CommandParserTestUtil.OptionalParameterStub<>("n"),
                new CommandParserTestUtil.OptionalParameterStub<>("p"),
                new CommandParserTestUtil.OptionalParameterStub<>("f"),
                new CommandParserTestUtil.OptionalParameterStub<>("c"),
                new CommandParserTestUtil.OptionalParameterStub<>("d")
        );

        String expectedMessage = EditCommand.MESSAGE_NOT_EDITED;

        assertCommandFailure(editCommand, model, expectedMessage);
    }

    @Test
    public void execute_filteredList_success() throws IllegalValueException {
        showFoodAtIndex(model, INDEX_FIRST_FOOD);

        Food foodInFilteredList = model.getFilteredFoodList().get(INDEX_FIRST_FOOD.getZeroBased());
        Food editedFood = new FoodBuilder(foodInFilteredList).withName(new Name(VALID_NAME_BOB)).build();

        EditCommand editCommand = new EditCommand();
        editCommand.setParameters(
                new CommandParserTestUtil.ParameterStub<>("", INDEX_FIRST_FOOD),
                new CommandParserTestUtil.OptionalParameterStub<>("n", editedFood.getName()),
                new CommandParserTestUtil.OptionalParameterStub<>("p"),
                new CommandParserTestUtil.OptionalParameterStub<>("f"),
                new CommandParserTestUtil.OptionalParameterStub<>("c"),
                new CommandParserTestUtil.OptionalParameterStub<>("d")
        );

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_FOOD_SUCCESS, editedFood);

        Model expectedModel = new ModelManager(new McGymmy(model.getMcGymmy()), new UserPrefs());
        expectedModel.setFood(Index.fromZeroBased(0), editedFood);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateFoodUnfilteredList_success() {
        Food firstFood = model.getFilteredFoodList().get(INDEX_FIRST_FOOD.getZeroBased());
        EditCommand editCommand = new EditCommand();
        editCommand.setParameters(
                new CommandParserTestUtil.ParameterStub<>("", INDEX_SECOND_FOOD),
                new CommandParserTestUtil.OptionalParameterStub<>("n", firstFood.getName()),
                new CommandParserTestUtil.OptionalParameterStub<>("p", firstFood.getProtein()),
                new CommandParserTestUtil.OptionalParameterStub<>("f", firstFood.getFat()),
                new CommandParserTestUtil.OptionalParameterStub<>("c", firstFood.getCarbs()),
                new CommandParserTestUtil.OptionalParameterStub<>("d", firstFood.getDate())
        );

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_FOOD_SUCCESS, firstFood);
        Model expectedModel = new ModelManager(new McGymmy(model.getMcGymmy()), new UserPrefs());
        expectedModel.setFood(Index.fromZeroBased(0), firstFood);
        expectedModel.setFood(Index.fromZeroBased(1), firstFood);
        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_editedFoodSameAsInitialFood_returnsFoodNoChangeMessage() {
        Food foodInList =
                model.getMcGymmy().getFoodList().get(INDEX_FIRST_FOOD.getZeroBased());
        EditCommand editCommand = new EditCommand();
        editCommand.setParameters(
                new CommandParserTestUtil.ParameterStub<>("", INDEX_FIRST_FOOD),
                new CommandParserTestUtil.OptionalParameterStub<>("n"),
                new CommandParserTestUtil.OptionalParameterStub<>("p", foodInList.getProtein()),
                new CommandParserTestUtil.OptionalParameterStub<>("f"),
                new CommandParserTestUtil.OptionalParameterStub<>("c"),
                new CommandParserTestUtil.OptionalParameterStub<>("d")
        );

        String expectedMessage = String.format(EditCommand.MESSAGE_FOOD_NO_CHANGE, foodInList);
        Model expectedModel = new ModelManager(new McGymmy(model.getMcGymmy()), new UserPrefs());
        expectedModel.setFood(INDEX_FIRST_FOOD, foodInList);
        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateFoodFilteredList_success() {
        showFoodAtIndex(model, INDEX_FIRST_FOOD);

        // edit food in filtered list into a duplicate in fridge
        Food foodInList =
                model.getMcGymmy().getFoodList().get(INDEX_SECOND_FOOD.getZeroBased());
        EditCommand editCommand = new EditCommand();
        editCommand.setParameters(
                new CommandParserTestUtil.ParameterStub<>("", INDEX_FIRST_FOOD),
                new CommandParserTestUtil.OptionalParameterStub<>("n", foodInList.getName()),
                new CommandParserTestUtil.OptionalParameterStub<>("p", foodInList.getProtein()),
                new CommandParserTestUtil.OptionalParameterStub<>("f", foodInList.getFat()),
                new CommandParserTestUtil.OptionalParameterStub<>("c", foodInList.getCarbs()),
                new CommandParserTestUtil.OptionalParameterStub<>("d", foodInList.getDate())
        );

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_FOOD_SUCCESS, foodInList);
        Model expectedModel = new ModelManager(new McGymmy(model.getMcGymmy()), new UserPrefs());
        expectedModel.setFood(Index.fromZeroBased(0), foodInList);
        expectedModel.setFood(Index.fromZeroBased(1), foodInList);
        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidFoodIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredFoodList().size() + 1);
        EditCommand editCommand = new EditCommand();
        editCommand.setParameters(
                new CommandParserTestUtil.ParameterStub<>("", outOfBoundIndex),
                new CommandParserTestUtil.OptionalParameterStub<>("n"),
                new CommandParserTestUtil.OptionalParameterStub<>("p"),
                new CommandParserTestUtil.OptionalParameterStub<>("f"),
                new CommandParserTestUtil.OptionalParameterStub<>("c"),
                new CommandParserTestUtil.OptionalParameterStub<>("d")
        );

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_FOOD_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidFoodIndexFilteredList_failure() {
        showFoodAtIndex(model, INDEX_FIRST_FOOD);
        Index outOfBoundIndex = INDEX_SECOND_FOOD;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getMcGymmy().getFoodList().size());

        EditCommand editCommand = new EditCommand();
        editCommand.setParameters(
                new CommandParserTestUtil.ParameterStub<>("", outOfBoundIndex),
                new CommandParserTestUtil.OptionalParameterStub<>("n"),
                new CommandParserTestUtil.OptionalParameterStub<>("p"),
                new CommandParserTestUtil.OptionalParameterStub<>("f"),
                new CommandParserTestUtil.OptionalParameterStub<>("c"),
                new CommandParserTestUtil.OptionalParameterStub<>("d")
        );

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_FOOD_DISPLAYED_INDEX);
    }
}
