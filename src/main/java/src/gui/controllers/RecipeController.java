package src.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.util.converter.IntegerStringConverter;
import src.data.Config;
import src.data.Ingredient;
import src.data.Recipe;
import src.data.RecipeInfo;

import java.util.ArrayList;
import java.util.List;

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
        List<Ingredient> ingredients = getIngredients();
        if (name.isBlank() || servingsStr.isBlank()/* || ingredients.isEmpty()*/)
            return;
        // TODO: Calculate total_calories
        RecipeInfo info = new RecipeInfo(name, Integer.parseInt(servingsStr), calculateTotalCalories(ingredients));
        Recipe recipe = new Recipe(info, descriptionArea.getText(), ingredients);
        boolean valid = getGui().getDatabase().insertRecipeInfo(info);
        if (!valid) return;
        // create file to store full recipe
        valid = getGui().getConfig().getJsonLoader().write(recipe);
        if (!valid) {
            System.err.println("Recipe already exists!");
            getGui().getDatabase().removeRecipeInfo(info);
            return;
        }
        getGui().loadScreen(Screen.CALENDAR);
    }

    private List<Ingredient> getIngredients() {
        // TODO
        return new ArrayList<>();
    }

    private double calculateTotalCalories(List<Ingredient> ingredients) {
        // TODO
        return 0.d;
    }

}
