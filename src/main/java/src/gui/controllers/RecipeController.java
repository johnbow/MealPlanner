package src.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import src.data.Ingredient;

public class RecipeController extends Controller {

    @FXML private Button returnToCalendarButton;
    @FXML private Button addIngredientButton, addRecipeButton;
    @FXML private TextField searchIngredientsField;
    @FXML private ListView<Ingredient> ingredientsList;
    @FXML private TextArea descriptionArea;

    @FXML
    public void initialize() {
        getGui().getStage().setTitle("Add Recipe");

    }

    @FXML
    public void openIngredientDialog() {
        Controller dialog = openDialog(Screen.INGREDIENT, StageStyle.UTILITY, Modality.WINDOW_MODAL);
        IngredientController ingredientController = (IngredientController) dialog;
        ingredientController.setIngredientName(searchIngredientsField.getText());
    }

    @FXML
    private void onReturnToCalendar(ActionEvent event) {
        getGui().loadScreen(Screen.CALENDAR);
    }

    @FXML
    private void onAddRecipe(ActionEvent event) {

    }

}
