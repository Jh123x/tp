package jimmy.mcgymmy.model;

import static jimmy.mcgymmy.testutil.Assert.assertThrows;
import static jimmy.mcgymmy.testutil.TypicalFoods.getChickenRice;
import static jimmy.mcgymmy.testutil.TypicalFoods.getDanishCookies;
import static jimmy.mcgymmy.testutil.TypicalFoods.getNasiLemak;
import static jimmy.mcgymmy.testutil.TypicalMacros.TEST_MACRO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.Predicate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jimmy.mcgymmy.commons.core.GuiSettings;
import jimmy.mcgymmy.commons.core.index.Index;
import jimmy.mcgymmy.commons.exceptions.IllegalValueException;
import jimmy.mcgymmy.logic.commands.CommandTestUtil;
import jimmy.mcgymmy.logic.predicate.NameContainsKeywordsPredicate;
import jimmy.mcgymmy.model.food.Food;
import jimmy.mcgymmy.model.macro.MacroList;
import jimmy.mcgymmy.testutil.FoodBuilder;
import jimmy.mcgymmy.testutil.McGymmyBuilder;
import jimmy.mcgymmy.testutil.TypicalFoods;

public class ModelManagerTest {

    private ModelManager modelManager = new ModelManager();
    private Model expectedModel = new ModelManager();

    @BeforeEach
    public void setup() {
        modelManager = new ModelManager();
        expectedModel = new ModelManager();
    }

    @Test
    public void constructor() {
        assertEquals(new UserPrefs(), modelManager.getUserPrefs());
        assertEquals(new GuiSettings(), modelManager.getGuiSettings());
        assertEquals(new McGymmy(), new McGymmy(modelManager.getMcGymmy()));
    }

    @Test
    public void setUserPrefs_nullUserPrefs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setUserPrefs(null));
    }

    @Test
    public void setUserPrefs_validUserPrefs_copiesUserPrefs() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setMcGymmyFilePath(Paths.get("jimmy", "mcgymmy", "file", "path"));
        userPrefs.setGuiSettings(new GuiSettings(1, 2, 3, 4));
        modelManager.setUserPrefs(userPrefs);
        assertEquals(userPrefs, modelManager.getUserPrefs());

        // Modifying userPrefs should not modify modelManager's userPrefs
        UserPrefs oldUserPrefs = new UserPrefs(userPrefs);
        userPrefs.setMcGymmyFilePath(Paths.get("new" , "jimmy", "mcgymmy", "file", "path"));
        assertEquals(oldUserPrefs, modelManager.getUserPrefs());
    }

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setGuiSettings(null));
    }

    @Test
    public void setGuiSettings_validGuiSettings_setsGuiSettings() {
        GuiSettings guiSettings = new GuiSettings(1, 2, 3, 4);
        modelManager.setGuiSettings(guiSettings);
        assertEquals(guiSettings, modelManager.getGuiSettings());
    }

    @Test
    public void setMcGymmyFilePath_nullPath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setMcGymmyFilePath(null));
    }

    @Test
    public void setMcGymmyFilePath_validPath_setsMcGymmyFilePath() {
        Path path = Paths.get("jimmy", "mcgymmy", "file", "path");
        modelManager.setMcGymmyFilePath(path);
        assertEquals(path, modelManager.getMcGymmyFilePath());
    }

    @Test
    public void hasFood_nullFood_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasFood(null));
    }

    @Test
    public void hasFood_foodNotInMcGymmy_returnsFalse() {
        assertFalse(modelManager.hasFood(getChickenRice()));
    }

    @Test
    public void hasFood_foodInMcGymmy_returnsTrue() {
        modelManager.addFood(getChickenRice());
        assertTrue(modelManager.hasFood(getChickenRice()));
    }

    @Test
    public void getFilteredFoodList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredFoodList().remove(0));
    }

    @Test
    public void canUndo_newModelManager_returnsFalse() {
        assertFalse(modelManager.canUndo());
    }

    @Test
    public void canUndo_modelGotChangedPreviously_returnsTrue() {
        modelManager.addFood(getChickenRice());
        assertTrue(modelManager.canUndo());
    }

    @Test
    public void canUndo_modelGotChangedButUndoAlready_returnsFalse() {
        modelManager.addFood(getChickenRice());
        modelManager.undo();
        assertFalse(modelManager.canUndo());
    }

    @Test
    public void clear_empty_mcGymmyHasCorrectContent() {
        McGymmy expected = new McGymmyBuilder().build();
        modelManager.clearFilteredFood();
        assertEquals(expected, modelManager.getMcGymmy());
    }

    @Test
    public void clear_nonEmpty_mcGymmyHasCorrectContent() {
        McGymmy expected = new McGymmyBuilder().build();
        modelManager.addFood(getChickenRice());
        modelManager.addFood(getNasiLemak());
        modelManager.clearFilteredFood();
        assertEquals(expected, modelManager.getMcGymmy());
    }

    @Test
    public void find_empty_mcGymmyHasCorrectContent() {
        modelManager.updateFilteredFoodList((food) -> food.getName()
                .fullName.contains(getChickenRice().getName().fullName));

        assertEquals(expectedModel.getFilteredFoodList(), modelManager.getFilteredFoodList());
    }

    @Test
    public void find_nonEmpty_mcGymmyHasCorrectContent() {
        expectedModel.addFood(getChickenRice());
        modelManager.addFood(getChickenRice());
        modelManager.addFood(getNasiLemak());
        modelManager.updateFilteredFoodList((food) -> food.getName()
                        .fullName.contains(getChickenRice().getName().fullName));
        assertEquals(expectedModel.getFilteredFoodList(), modelManager.getFilteredFoodList());
    }

    @Test
    public void findThenClear_nonEmpty_mcGymmyHasCorrectContent() {
        expectedModel.addFood(getChickenRice());
        modelManager.addFood(getChickenRice());
        modelManager.addFood(getNasiLemak());
        modelManager.updateFilteredFoodList((food) -> food.getName()
                .fullName.contains(getNasiLemak().getName().fullName));
        modelManager.clearFilteredFood();
        modelManager.updateFilteredFoodList((food) -> true);
        assertEquals(expectedModel.getFilteredFoodList(), modelManager.getFilteredFoodList());
    }



    @Test
    public void undo_undoAfterAddFood_mcGymmyHasCorrectContent() {
        modelManager.addFood(getChickenRice());
        McGymmy expected = new McGymmyBuilder().withFood(TypicalFoods.getChickenRice()).build();
        modelManager.addFood(getNasiLemak());
        modelManager.undo();
        assertEquals(expected, modelManager.getMcGymmy());
    }

    @Test
    public void undo_undoAfterDeleteFood_mcGymmyHasCorrectContent() {
        modelManager.addFood(getChickenRice());
        McGymmy expected = new McGymmyBuilder().withFood(TypicalFoods.getChickenRice()).build();
        modelManager.deleteFood(Index.fromOneBased(1));
        modelManager.undo();
        assertEquals(expected, modelManager.getMcGymmy());
    }

    @Test
    public void undo_undoAfterSetMcGymmy_mcGymmyHasCorrectContent() {
        modelManager.addFood(getChickenRice());
        McGymmy expected = new McGymmyBuilder().withFood(TypicalFoods.getChickenRice()).build();
        McGymmy mcGymmy = new McGymmyBuilder().withFood(TypicalFoods.getChickenRice()).withFood(
                TypicalFoods.getNasiLemak()).build();
        modelManager.setMcGymmy(mcGymmy);
        modelManager.undo();
        assertEquals(expected, modelManager.getMcGymmy());
    }

    @Test
    public void undo_undoAfterSetFood_mcGymmyHasCorrectContent() throws IllegalValueException {
        McGymmy expected = new McGymmyBuilder().withFood(getChickenRice()).build();
        modelManager.addFood(getChickenRice());
        Food editedFood = new FoodBuilder(getChickenRice()).withCarb("50").build();
        modelManager.setFood(Index.fromOneBased(1), editedFood);
        modelManager.undo();
        assertEquals(expected, modelManager.getMcGymmy());
    }

    @Test
    public void undo_undoAfterUpdateFilteredFoodList_modelManagerHasCorrectContent() {
        McGymmy expectedMcGymmy = new McGymmyBuilder().withFood(getChickenRice()).withFood(getDanishCookies()).build();
        ModelManager expectedModelManager = new ModelManager(expectedMcGymmy, new UserPrefs());
        modelManager.addFood(getChickenRice());
        modelManager.addFood(getDanishCookies());
        CommandTestUtil.showFoodAtIndex(modelManager, Index.fromOneBased(2)); // updateFilterdFoodList
        modelManager.undo();
        assertEquals(modelManager, expectedModelManager);
    }

    @Test
    public void undo_undoAfterClearFilteredFood_modelManagerHasCorrectContent() {
        McGymmy expectedMcGymmy = new McGymmyBuilder().withFood(getChickenRice()).withFood(getDanishCookies()).build();
        ModelManager expectedModelManager = new ModelManager(expectedMcGymmy, new UserPrefs());
        modelManager.addFood(getChickenRice());
        modelManager.addFood(getDanishCookies());
        modelManager.updateFilteredFoodList(food -> food.equals(getChickenRice()));
        modelManager.clearFilteredFood();
        modelManager.undo();
        assertEquals(modelManager.getMcGymmy(), expectedMcGymmy);
    }

    @Test
    public void undo_undoAfterSetMacroList_modelManagerHasCorrectContent() throws Exception {
        ModelManager expectedModelManager = new ModelManager(modelManager.getMcGymmy(), new UserPrefs());
        MacroList newMacroList = modelManager.getMacroList().withNewMacro(TEST_MACRO);
        modelManager.setMacroList(newMacroList);
        modelManager.undo();
        assertEquals(modelManager, expectedModelManager);
    }

    @Test
    public void undo_undoMultipleTimes_mcGymmyHasCorrectContent() throws IllegalValueException {
        Food newChickenRice = new FoodBuilder(getChickenRice()).withDate("2020-04-12").build();
        Food newNasiLemak = new FoodBuilder().withTags("Lunch").build();

        McGymmy expected1 = new McGymmyBuilder().build();
        McGymmy expected2 = new McGymmyBuilder().withFood(getChickenRice()).build();
        McGymmy expected3 = new McGymmyBuilder().withFood(getChickenRice()).withFood(getNasiLemak()).build();
        McGymmy expected4 = new McGymmyBuilder().withFood(newChickenRice).withFood(getNasiLemak()).build();
        McGymmy expected5 = new McGymmyBuilder().withFood(newChickenRice).withFood(newNasiLemak).build();

        modelManager.addFood(getChickenRice());
        modelManager.addFood(getNasiLemak());
        modelManager.setFood(Index.fromOneBased(1), newChickenRice);
        modelManager.setFood(Index.fromOneBased(2), newNasiLemak);
        modelManager.deleteFood(Index.fromOneBased(1));

        modelManager.undo();
        assertEquals(expected5, modelManager.getMcGymmy());

        modelManager.undo();
        assertEquals(expected4, modelManager.getMcGymmy());

        modelManager.undo();
        assertEquals(expected3, modelManager.getMcGymmy());

        modelManager.undo();
        assertEquals(expected2, modelManager.getMcGymmy());

        modelManager.undo();
        assertEquals(expected1, modelManager.getMcGymmy());

    }

    @Test
    public void undo_undoMultipleTimes_modelHasCorrectContent() throws Exception {
        Food newChickenRice = new FoodBuilder(getChickenRice()).withDate("2020-04-12").build();
        Food newNasiLemak = new FoodBuilder().withTags("Lunch").build();

        McGymmy expectedMcGymmy1 = new McGymmyBuilder().build();
        McGymmy expectedMcGymmy2 = new McGymmyBuilder().withFood(getChickenRice()).build();
        McGymmy expectedMcGymmy3 = new McGymmyBuilder().withFood(getChickenRice()).withFood(getNasiLemak()).build();

        MacroList macroList1 = modelManager.getMacroList();
        MacroList macroList2 = modelManager.getMacroList().withNewMacro(TEST_MACRO);
        MacroList macroList3 = modelManager.getMacroList().withoutMacro("test");

        modelManager.addFood(getChickenRice());
        modelManager.setMacroList(macroList2);
        modelManager.addFood(getNasiLemak());
        modelManager.setMacroList(macroList3);
        modelManager.setFood(Index.fromOneBased(1), newChickenRice);

        modelManager.undo();
        assertEquals(expectedMcGymmy3, modelManager.getMcGymmy());
        assertEquals(modelManager.getMacroList(), macroList3);

        modelManager.undo();
        assertEquals(expectedMcGymmy3, modelManager.getMcGymmy());
        assertEquals(modelManager.getMacroList(), macroList2);

        modelManager.undo();
        assertEquals(expectedMcGymmy2, modelManager.getMcGymmy());
        assertEquals(modelManager.getMacroList(), macroList2);

        modelManager.undo();
        assertEquals(expectedMcGymmy2, modelManager.getMcGymmy());
        assertEquals(modelManager.getMacroList(), macroList1);

        modelManager.undo();
        assertEquals(expectedMcGymmy1, modelManager.getMcGymmy());
        assertEquals(modelManager.getMacroList(), macroList1);
    }

    @Test
    public void undo_updateFilteredFoodListWithSamePredicateMultipleTime_undoMultipleTimes() {
        McGymmy expectedMcGymmy = new McGymmyBuilder().withFood(getChickenRice()).withFood(getDanishCookies()).build();
        ModelManager expectedModelManager = new ModelManager(expectedMcGymmy, new UserPrefs());
        modelManager = new ModelManager(expectedMcGymmy, new UserPrefs());
        Predicate<Food> predicate = food -> false;
        modelManager.updateFilteredFoodList(predicate);
        modelManager.updateFilteredFoodList(predicate);
        modelManager.undo();
        assertTrue(modelManager.canUndo());
        modelManager.undo();
        assertEquals(modelManager, expectedModelManager);
    }

    @Test
    public void equals() {
        McGymmy mcGymmy = new McGymmyBuilder().withFood(TypicalFoods.getChickenRice()).withFood(
                TypicalFoods.getNasiLemak()).build();
        McGymmy differentMcGymmy = new McGymmy();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(mcGymmy, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(mcGymmy, userPrefs);
        assertEquals(modelManagerCopy, modelManager);

        // same object -> returns true
        assertEquals(modelManager, modelManager);

        // null -> returns false
        assertNotEquals(modelManager, null);

        // different types -> returns false
        assertNotEquals(modelManager, 5);

        // different mcGymmy -> returns false
        assertNotEquals(new ModelManager(differentMcGymmy, userPrefs), modelManager);

        // different filteredList -> returns false
        String[] keywords = getChickenRice().getName().fullName.split("\\s+");
        modelManager.updateFilteredFoodList(new NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertNotEquals(new ModelManager(mcGymmy, userPrefs), modelManager);

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredFoodList(Model.PREDICATE_SHOW_ALL_FOODS);

        // different userPrefs -> returns false
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setMcGymmyFilePath(Paths.get("differentFilePath"));
        assertNotEquals(new ModelManager(mcGymmy, differentUserPrefs), modelManager);
    }
}
