package src.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class RecipeController extends Controller {

    @FXML private Button returnToCalendarButton;

    @FXML
    public void initialize() {
        getGui().getStage().setTitle("Add Recipe");

    }

    @FXML
    private void onReturnToCalendar() {
        getGui().loadScreen(Screen.CALENDAR);
    }

}
