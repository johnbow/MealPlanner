package src.gui.controllers;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import src.gui.components.Calendar;
import src.gui.components.WeekdayBox;

public class CalendarController extends Controller {

    private WeekdayBox[] weekdayBoxes;

    @FXML private Calendar calendar;
    @FXML private Button prevWeekButton, nextWeekButton;
    @FXML private Label dateLabel;
    @FXML private Button addRecipeButton;

    @FXML
    public void initialize() {
        getGui().getStage().setTitle("Meal Planner");
        calendar.setDateLabel(dateLabel);
        calendar.setCurrentWeek();
        addRecipeButton.requestFocus();
    }

    @FXML
    private void onPreviousWeek(ActionEvent event) {
        calendar.setPreviousWeek();
    }

    @FXML
    private void onNextWeek(ActionEvent event) {
        calendar.setNextWeek();
    }

    @FXML
    private void onAddRecipe(ActionEvent event) {
        getGui().loadScreen(Screen.RECIPE);
    }
}
