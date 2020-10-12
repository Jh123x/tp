package jimmy.mcgymmy.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import jimmy.mcgymmy.commons.core.GuiSettings;
import jimmy.mcgymmy.commons.core.index.Index;
import jimmy.mcgymmy.logic.parser.CommandParserTestUtil;
import jimmy.mcgymmy.model.McGymmy;
import jimmy.mcgymmy.model.Model;
import jimmy.mcgymmy.model.ReadOnlyMcGymmy;
import jimmy.mcgymmy.model.ReadOnlyUserPrefs;
import jimmy.mcgymmy.model.food.Food;
import jimmy.mcgymmy.testutil.FoodBuilder;

public class AddCommandTest {
    @Test
    public void execute_foodAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingFoodAdded modelStub = new ModelStubAcceptingFoodAdded();
        Food validFood = new FoodBuilder().withCarb("12345").build();
        AddCommand command = new AddCommand();
        command.setParameters(
                new CommandParserTestUtil.ParameterStub<>("n", validFood.getName()),
                new CommandParserTestUtil.OptionalParameterStub<>("p", validFood.getProtein()),
                new CommandParserTestUtil.OptionalParameterStub<>("f", validFood.getFat()),
                new CommandParserTestUtil.OptionalParameterStub<>("c", validFood.getCarbs()),
                new CommandParserTestUtil.OptionalParameterStub<>("t")
        );

        CommandResult commandResult = command.execute(modelStub);

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, validFood), commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validFood), modelStub.foodAdded);
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
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
