package src.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class RecipeController extends Controller {

    private Stage ingredientDialog;
    @FXML private Button returnToCalendarButton;
    @FXML private Button addIngredientButton, addRecipeButton;

    @FXML
    public void initialize() {
        getGui().getStage().setTitle("Add Recipe");

    }

    @FXML
    public void openIngredientDialog() {
        ingredientDialog = getGui().loadDialog(Screen.INGREDIENT, StageStyle.UTILITY, Modality.WINDOW_MODAL);
        ingredientDialog.show();
    }

    @FXML
    private void onReturnToCalendar(ActionEvent event) {
        getGui().loadScreen(Screen.CALENDAR);
    }

    @FXML
    private void onAddRecipe(ActionEvent event) {

    }

}
