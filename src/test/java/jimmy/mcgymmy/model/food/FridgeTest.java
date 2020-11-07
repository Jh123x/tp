package jimmy.mcgymmy.model.food;

import static jimmy.mcgymmy.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jimmy.mcgymmy.commons.core.index.Index;
import jimmy.mcgymmy.commons.exceptions.IllegalValueException;

class FridgeTest {
    private static Food chimken;
    private static Food ramen;
    private Fridge fridge;

    static {
        try {
            chimken = new Food("Chimken", 1, 2, 3);
            ramen = new Food("Ramen", 2, 3, 4);
        } catch (IllegalValueException e) {
            assert false : "Error in food";
        }
    }

    @BeforeEach
    public void beforeEachTestMethod() {
        fridge = new Fridge();
    }

    @Test
    public void contains_nullFood_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> fridge.contains(null));
    }

    @Test
    public void contains_foodNotInList_returnsFalse() {
        assertFalse(fridge.contains(chimken));
    }

    @Test
    public void contains_foodInList_returnsTrue() {
        fridge.add(chimken);
        assertTrue(fridge.contains(chimken));
    }

    @Test
    public void add_nullFood_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> fridge.add(null));
    }

    @Test
    public void setFood_indexLessThanZero_throwsIndexOutOfBoundException() {
        assertThrows(IndexOutOfBoundsException.class, () -> fridge.setFood(Index.fromZeroBased(-1), chimken));
    }

    @Test
    public void setFood_indexLargerThanOrEqualToSize_throwsIndexOutOfBoundException() {
        assertThrows(IndexOutOfBoundsException.class, () -> fridge.setFood(Index.fromZeroBased(1), chimken));
    }

    @Test
    public void setFood_nullEditedFood_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> fridge.setFood(Index.fromZeroBased(1), null));
    }

    @Test
    public void setFood_validFood_success() {
        fridge.add(chimken);
        fridge.setFood(Index.fromZeroBased(0), ramen);
        Fridge expectedFridge = new Fridge();
        expectedFridge.add(ramen);
        assertEquals(expectedFridge, this.fridge);
    }

    @Test
    public void remove_removeIndexLesserThanZero_throwsIndexOutOfBoundException() {
        assertThrows(IndexOutOfBoundsException.class, () -> fridge.remove(Index.fromZeroBased(-1)));
    }

    @Test
    public void remove_removeIndexLargerThanOrEqualToSize_throwsIndexOutOfBoundException() {
        assertThrows(IndexOutOfBoundsException.class, () -> fridge.remove(Index.fromZeroBased(-1)));
    }

    @Test
    public void remove_validIndex_removesFood() {
        fridge.add(chimken);
        fridge.remove(Index.fromZeroBased(0));
        Fridge expectedFridge = new Fridge();
        assertEquals(expectedFridge, fridge);
    }

    @Test
    public void setFoods_nullFridge_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> fridge.setFoods((Fridge) null));
    }

    @Test
    public void setFoods_fridge_replacesOwnListWithProvidedFridge() {
        fridge.add(chimken);
        Fridge expectedFridge = new Fridge();
        expectedFridge.add(ramen);
        fridge.setFoods(expectedFridge);
        assertEquals(expectedFridge, fridge);
    }

    @Test
    public void setFoods_nullList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> fridge.setFoods((List<Food>) null));
    }

    @Test
    public void setFoods_list_replacesOwnListWithProvidedList() {
        fridge.add(chimken);
        List<Food> foodList = Collections.singletonList(ramen);
        fridge.setFoods(foodList);
        Fridge expectedFridge = new Fridge();
        expectedFridge.add(ramen);
        assertEquals(expectedFridge, fridge);
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(
                UnsupportedOperationException.class, () -> fridge.asUnmodifiableObservableList().remove(0));
    }
}
