package jimmy.mcgymmy.ui;

import static jimmy.mcgymmy.commons.util.CollectionUtil.requireAllNonNull;

import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.AnchorPane;

public class SummaryDisplay extends UiPart<AnchorPane> {
    private static final String FXML = "SummaryDisplay.fxml";
    private static final String TOTAL_CALORIES_TEXT = "Total Calories %d";
    private static final String TOTAL_PROTEIN_TEXT = "Protein: %s";
    private static final String TOTAL_CARBOHYDRATE_TEXT = "Carbs: %s";
    private static final String TOTAL_FAT_TEXT = "Fats: %s";

    @FXML
    private PieChart pieChart;

    /**
     * Creates a summary display.
     * Contains summary of the items shown.
     */
    public SummaryDisplay() {
        super(FXML);
        pieChart.setClockwise(true);
        pieChart.setLabelLineLength(25);
        pieChart.setLabelsVisible(true);
    }

    public void setTotalMacronutrients(int totalCalories, int totalProtein, int totalCarbs, int totalFats) {
        requireAllNonNull(totalCalories, totalProtein, totalCarbs, totalFats);
        pieChart.setTitle(String.format(TOTAL_CALORIES_TEXT, totalCalories));

        //Reset the data
        pieChart.getData().clear();

        //Update the data to the correct value
        addData(TOTAL_CARBOHYDRATE_TEXT, totalCarbs);
        addData(TOTAL_PROTEIN_TEXT, totalProtein);
        addData(TOTAL_FAT_TEXT, totalFats);
    }

    private void addData(String formatString, int count) {

        //If the count for the data is <= 0, do not show the data value
        if (count <= 0) {
            return;
        }

        //Add the data to the chart
        pieChart.getData().add(new PieChart.Data(String.format(formatString, count), count));
    }
}
