package jimmy.mcgymmy.model;

import javafx.collections.ObservableList;
import jimmy.mcgymmy.model.food.Food;

/**
 * Unmodifiable view of McGymmy.
 */
public interface ReadOnlyMcGymmy {

    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Food> getFoodList();

}
