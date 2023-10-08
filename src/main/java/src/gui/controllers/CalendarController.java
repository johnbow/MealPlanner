package src.gui.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import src.data.RecipeInfo;
import src.gui.components.Calendar;
import src.gui.components.RecipeDisplay;
import src.gui.components.SearchBar;
import src.util.QueryService;

public class CalendarController extends Controller {

    @FXML private Calendar calendar;
    @FXML private RecipeDisplay recipeDisplay;
    @FXML private SearchBar<RecipeInfo> recipeSearchBar;
    @FXML private Button prevWeekButton, nextWeekButton;
    @FXML private Label dateLabel;
    @FXML private Button addRecipeButton;

    @FXML
    public void initialize() {
        getGui().getMainStage().setTitle("Meal Planner");
        recipeDisplay.setGUI(getGui());
        calendar.setGui(getGui());
        calendar.setDateLabel(dateLabel);
        calendar.setCurrentWeek();
        Platform.runLater(addRecipeButton::requestFocus);
        recipeSearchBar.setSearchQuery(new QueryService<>(getGui().getConfig().getQueryResultsLimit(),
                ((resultSet, searchText, capacity) ->
                        getGui().getDatabase().addRecipeInfosTo(resultSet, searchText, capacity))));
        recipeSearchBar.setDisplay(recipeDisplay.getItems());
        recipeSearchBar.search();
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

    @Override
    public void onClose() {
        super.onClose();
        calendar.saveDisplayedWeek();
    }
}
