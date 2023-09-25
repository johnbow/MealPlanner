package src.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.util.converter.IntegerStringConverter;
import src.Config;
import src.data.Ingredient;
import src.data.RecipeInfo;

public class RecipeController extends Controller {

    @FXML private Button returnToCalendarButton;
    @FXML private Button addIngredientButton, addRecipeButton;
    @FXML private TextField searchIngredientsField;
    @FXML private TextField nameField, servingsField;
    @FXML private ListView<Ingredient> ingredientsList;
    @FXML private TextArea descriptionArea;

    @FXML
    public void initialize() {
        getGui().getStage().setTitle("Add Recipe");
        servingsField.setTextFormatter(new TextFormatter<Integer>(
                new IntegerStringConverter(), 1, Config.INT_FILTER_2_PLACES));
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
        String name = nameField.getText();
        String servingsStr = servingsField.getText();
        if (name.isBlank() || servingsStr.isBlank())
            return;
        // TODO: Calculate total_calories
        RecipeInfo info = new RecipeInfo(name, Integer.parseInt(servingsStr), 0.0);
        boolean valid = getGui().getDatabase().insertRecipeInfo(info);
        if (valid)
            getGui().loadScreen(Screen.CALENDAR);
    }

}
