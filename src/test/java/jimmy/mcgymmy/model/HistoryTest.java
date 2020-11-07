package jimmy.mcgymmy.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Stack;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.util.Pair;
import jimmy.mcgymmy.model.food.Food;
import jimmy.mcgymmy.model.macro.MacroList;
import jimmy.mcgymmy.testutil.McGymmyBuilder;
import jimmy.mcgymmy.testutil.TypicalFoods;

class HistoryTest {
    private static class ModelManagerStub extends ModelManager {
        ModelManagerStub(McGymmy mcGymmy, MacroList macroList, Predicate<Food> filterPredicate) {
            super(mcGymmy, new UserPrefs(), macroList);
            this.updateFilteredFoodList(filterPredicate);
        }
    }

    private class HistoryStub extends History {
        HistoryStub() {
            super();
        }

        Stack<Pair<McGymmy, Pair<Predicate<Food>, MacroList>>> getStack() {
            return super.stack;
        }
    }

    private static final McGymmy MC_GYMMY = new McGymmyBuilder().withFood(TypicalFoods.getChickenRice()).build();
    private static final Predicate<Food> FOOD_PREDICATE = food -> false;
    private static final MacroList MACRO_LIST = new MacroList();
    private static final ModelManager MODEL_MANAGER = new ModelManagerStub(MC_GYMMY, MACRO_LIST, FOOD_PREDICATE);

    @Test
    public void save_successfullySaveData() {
        HistoryStub historyStub = new HistoryStub();
        historyStub.save(MODEL_MANAGER);
        Stack<Pair<McGymmy, Pair<Predicate<Food>, MacroList>>> stack = historyStub.getStack();
        assertEquals(MC_GYMMY, stack.peek().getKey());
        assertEquals(MACRO_LIST, stack.peek().getValue().getValue());
        assertEquals(FOOD_PREDICATE, stack.peek().getValue().getKey());
    }

    @Test
    public void empty_emptyHistory_returnsTrue() {
        assertTrue(new HistoryStub().empty());
    }

    @Test
    public void empty_notEmptyHistory_returnsFalse() {
        HistoryStub historyStub = new HistoryStub();
        historyStub.save(MODEL_MANAGER);
        assertFalse(historyStub.empty());
    }

    @Test
    public void pop_nonEmptyHistory_success() {
        HistoryStub historyStub = new HistoryStub();
        historyStub.save(MODEL_MANAGER);
        historyStub.pop();
        assertTrue(historyStub.empty());
    }

    @Test
    public void pop_emptyHistory_throwsAssertionError() {
        assertThrows(AssertionError.class, () -> new HistoryStub().pop());
    }

    @Test
    public void peekMcGymmy_returnsCorrectMcGymmy_doesNotPop() {
        HistoryStub historyStub = new HistoryStub();
        historyStub.save(MODEL_MANAGER);
        assertEquals(MC_GYMMY, historyStub.peekMcGymmy());
        assertFalse(historyStub.empty());
    }

    @Test
    public void peekMacroList_returnsCorrectMacroList_doesNotPop() {
        HistoryStub historyStub = new HistoryStub();
        historyStub.save(MODEL_MANAGER);
        assertEquals(MACRO_LIST, historyStub.peekMacroList());
        assertFalse(historyStub.empty());
    }

    @Test
    public void peekPredicate_returnsCorrectPredicate_doesNotPop() {
        HistoryStub historyStub = new HistoryStub();
        historyStub.save(MODEL_MANAGER);
        assertEquals(FOOD_PREDICATE, historyStub.peekPredicate());
        assertFalse(historyStub.empty());
    }
}
