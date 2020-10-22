package jimmy.mcgymmy.logic.commands;

import static java.util.Objects.requireNonNull;
import static jimmy.mcgymmy.testutil.TypicalFoods.getTypicalMcGymmy;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import jimmy.mcgymmy.commons.core.GuiSettings;
import jimmy.mcgymmy.commons.core.index.Index;
import jimmy.mcgymmy.logic.parser.CommandParserTestUtil;
import jimmy.mcgymmy.model.McGymmy;
import jimmy.mcgymmy.model.Model;
import jimmy.mcgymmy.model.ModelManager;
import jimmy.mcgymmy.model.ReadOnlyMcGymmy;
import jimmy.mcgymmy.model.ReadOnlyUserPrefs;
import jimmy.mcgymmy.model.UserPrefs;
import jimmy.mcgymmy.model.food.Food;
import jimmy.mcgymmy.model.tag.Tag;
import jimmy.mcgymmy.testutil.FoodBuilder;



public class AddCommandTest {
    private Model model = new ModelManager(getTypicalMcGymmy(), new UserPrefs());

    @BeforeEach
    private void initEach() {
        model = new ModelManager(getTypicalMcGymmy(), new UserPrefs());
    }

    @Test
    public void execute_validFoodWithoutDate_addSuccessfulAndSetCurrentDayAsDate() throws Exception {
        ModelStubAcceptingFoodAdded modelStub = new ModelStubAcceptingFoodAdded();
        Food validFood = new FoodBuilder().withCarb("12345").build();
        AddCommand command = new AddCommand();
        command.setParameters(
                new CommandParserTestUtil.ParameterStub<>("n", validFood.getName()),
                new CommandParserTestUtil.OptionalParameterStub<>("p", validFood.getProtein()),
                new CommandParserTestUtil.OptionalParameterStub<>("f", validFood.getFat()),
                new CommandParserTestUtil.OptionalParameterStub<>("c", validFood.getCarbs()),
                new CommandParserTestUtil.OptionalParameterStub<>("t"),
                new CommandParserTestUtil.OptionalParameterStub<>("d")
        );

        CommandResult commandResult = command.execute(modelStub);

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, validFood), commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validFood), modelStub.foodAdded);
    }

    @Test
    public void execute_validFoodWithDate_addSuccessfulAndSetInputDateAsDate() throws Exception {
        Food validFoodWithDate = new FoodBuilder().withCarb("12345").withDate("20/4/2020").build();
        AddCommand command = new AddCommand();
        command.setParameters(
            new CommandParserTestUtil.ParameterStub<>("n", validFoodWithDate.getName()),
            new CommandParserTestUtil.OptionalParameterStub<>("p", validFoodWithDate.getProtein()),
            new CommandParserTestUtil.OptionalParameterStub<>("f", validFoodWithDate.getFat()),
            new CommandParserTestUtil.OptionalParameterStub<>("c", validFoodWithDate.getCarbs()),
            new CommandParserTestUtil.OptionalParameterStub<>("t"),
            new CommandParserTestUtil.OptionalParameterStub<>("d", validFoodWithDate.getDate())
        );

        String expectedMessage = String.format(AddCommand.MESSAGE_SUCCESS, validFoodWithDate);
        Model expectedModel = new ModelManager(new McGymmy(model.getMcGymmy()), new UserPrefs());
        expectedModel.addFood(validFoodWithDate);

        CommandTestUtil.assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validFoodWithTag_addSuccessful() {
        Food validFoodWithTag = new FoodBuilder().withCarb("12345").withTags("hello").build();
        AddCommand command = new AddCommand();
        command.setParameters(
            new CommandParserTestUtil.ParameterStub<>("n", validFoodWithTag.getName()),
            new CommandParserTestUtil.OptionalParameterStub<>("p", validFoodWithTag.getProtein()),
            new CommandParserTestUtil.OptionalParameterStub<>("f", validFoodWithTag.getFat()),
            new CommandParserTestUtil.OptionalParameterStub<>("c", validFoodWithTag.getCarbs()),
            new CommandParserTestUtil.OptionalParameterStub<>("t", new Tag("hello")),
            new CommandParserTestUtil.OptionalParameterStub<>("d")
        );

        String expectedMessage = String.format(AddCommand.MESSAGE_SUCCESS, validFoodWithTag);
        Model expectedModel = new ModelManager(new McGymmy(model.getMcGymmy()), new UserPrefs());
        expectedModel.addFood(validFoodWithTag);

        CommandTestUtil.assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateFood_success() {
        Food validFood = new FoodBuilder().withCarb("12345").build();
        AddCommand command = new AddCommand();
        command.setParameters(
                new CommandParserTestUtil.ParameterStub<>("n", validFood.getName()),
                new CommandParserTestUtil.OptionalParameterStub<>("p", validFood.getProtein()),
                new CommandParserTestUtil.OptionalParameterStub<>("f", validFood.getFat()),
                new CommandParserTestUtil.OptionalParameterStub<>("c", validFood.getCarbs()),
                new CommandParserTestUtil.OptionalParameterStub<>("t"),
                new CommandParserTestUtil.OptionalParameterStub<>("d")
        );

        String expectedMessage = String.format(AddCommand.MESSAGE_SUCCESS, validFood);
        Model expectedModel = new ModelManager(new McGymmy(model.getMcGymmy()), new UserPrefs());
        expectedModel.addFood(validFood);
        expectedModel.addFood(validFood);

        command.execute(model);

        CommandTestUtil.assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {

        @Override
        public void clearFilteredFood() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getMcGymmyFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setMcGymmyFilePath(Path mcGymmyFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addFood(Food food) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyMcGymmy getMcGymmy() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setMcGymmy(ReadOnlyMcGymmy mcGymmy) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasFood(Food food) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteFood(Index target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setFood(Index index, Food editedFood) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean canUndo() {
            throw new AssertionError("This method should not be called.");
        }


        @Override
        public void undo() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Food> getFilteredFoodList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredFoodList(Predicate<Food> predicate) {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that always accept the food being added.
     */
    private class ModelStubAcceptingFoodAdded extends ModelStub {
        final ArrayList<Food> foodAdded = new ArrayList<>();

        @Override
        public boolean hasFood(Food food) {
            requireNonNull(food);
            return foodAdded.stream().anyMatch(food::equals);
        }

        @Override
        public void addFood(Food food) {
            requireNonNull(food);
            foodAdded.add(food);
        }

        @Override
        public ReadOnlyMcGymmy getMcGymmy() {
            return new McGymmy();
        }
    }

}
