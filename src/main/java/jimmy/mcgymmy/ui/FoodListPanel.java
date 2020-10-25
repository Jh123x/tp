package jimmy.mcgymmy.ui;

import java.util.logging.Logger;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import jimmy.mcgymmy.commons.core.LogsCenter;
import jimmy.mcgymmy.model.food.Food;

/**
 * Panel containing the list of persons.
 */
public class FoodListPanel extends UiPart<Region> {
    private static final String FXML = "FoodListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(FoodListPanel.class);

    @FXML
    private ListView<Food> foodListView;

    private int currentCalories = 0;
    private int currentFats = 0;
    private int currentProteins = 0;
    private int currentCarbs = 0;

    /**
     * Creates a {@code FoodListPanel} with the given {@code ObservableList}.
     */
    public FoodListPanel(ObservableList<Food> foodList) {
        super(FXML);

        //Add listener to update the current calories whenever there is a change.
        foodList.addListener((ListChangeListener<Food>) c -> {
            if (c.next()) {
                updateCurrentCalories();
            }
        });
        foodListView.setItems(foodList);
        foodListView.setCellFactory(listView -> new FoodListViewCell());

        //Update calories at the start
        updateCurrentCalories();
    }

    private void updateCurrentCalories() {
        //Calculate the current calories when the list is changed.
        currentCalories = 0;
        currentFats = 0;
        currentCarbs = 0;
        currentProteins = 0;
        for (Food food : foodListView.getItems()) {
            currentCalories += food.getCalories();
            currentFats += food.getFat().getAmount();
            currentCarbs += food.getCarbs().getAmount();
            currentProteins += food.getProtein().getAmount();
        }
    }

    /**
     * Gets sum of calories in current list.
     * @return sum of calories of current list.
     */
    public int getCurrentCalories() {
        return currentCalories;
    }

    /**
     * Gets sum of fats in current list.
     * @return sum of fats of current list.
     */
    public int getCurrentFats() {
        return currentFats;
    }

    /**
     * Gets sum of proteins in current list.
     * @return sum of proteins of current list.
     */
    public int getCurrentProteins() {
        return currentProteins;
    }

    /**
     * Gets sum of carbs in current list.
     * @return sum of carbs of current list.
     */
    public int getCurrentCarbs() {
        return currentCarbs;
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Food} using a {@code FoodCard}.
     */
    class FoodListViewCell extends ListCell<Food> {

        @Override
        protected void updateItem(Food food, boolean empty) {
            super.updateItem(food, empty);

            if (empty || food == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new FoodCard(food, getIndex() + 1).getRoot());
            }
        }
    }

}
