package src.gui.controllers;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.util.converter.IntegerStringConverter;

import src.data.*;
import src.gui.components.ImageSelector;
import src.gui.components.IngredientListViewCell;
import src.gui.components.IngredientTable;
import src.util.QueryService;
import src.gui.components.SearchBar;

import java.util.List;

public class RecipeController extends Controller {

    private final Service<Boolean> addRecipeService = new Service<>() {
        @Override
        protected Task<Boolean> createTask() {
            return new Task<>() {
                @Override
                protected Boolean call() {
                    return addRecipe();
                }
            };
        }
    };

    @FXML private Button returnToCalendarButton;
    @FXML private Button addIngredientButton, addRecipeButton;
    @FXML private SearchBar<Ingredient> ingredientSearchBar;
    @FXML private TextField nameField, servingsField;
    @FXML private ListView<Ingredient> ingredientsList;
    @FXML private TextArea descriptionArea;
    @FXML private IngredientTable ingredientsTable;
    @FXML private ImageSelector imageSelector;

    @FXML
    public void initialize() {
        getGui().getMainStage().setTitle("Add Recipe");
        servingsField.setTextFormatter(new TextFormatter<>(
                new IntegerStringConverter(), 1, Config.INT_FILTER_2_PLACES));
        addRecipeService.setOnSucceeded(t -> {
            boolean added = addRecipeService.getValue();
            if (added)
                getGui().loadScreen(Screen.CALENDAR);
        });
        configureSearchBar();
        ingredientSearchBar.search();
    }

    @FXML
    public void openIngredientDialog() {
        Controller dialog = openDialog(Screen.INGREDIENT, StageStyle.UTILITY, Modality.WINDOW_MODAL);
        IngredientController ingredientController = (IngredientController) dialog;
        ingredientController.setIngredientName(ingredientSearchBar.getText());
        ingredientController.getStage().setResizable(false);
    }

    @FXML
    private void onReturnToCalendar(ActionEvent event) {
        getGui().loadScreen(Screen.CALENDAR);
    }

    @FXML
    private void onAddRecipe(ActionEvent event) {
        addRecipeService.restart();
    }

    private void configureSearchBar() {
        ingredientSearchBar.setDisplay(ingredientsList.getItems());
        ingredientSearchBar.setSearchQuery(new QueryService<>(
                getGui().getConfig().getQueryResultsLimit(), (resultSet, searchText, capacity) ->
                getGui().getDatabase().addIngredientsTo(resultSet, searchText, capacity)));
        ingredientsList.setCellFactory(ingredientListView -> new IngredientListViewCell());
        ingredientsList.setOnMouseClicked(click -> {
            if (click.getClickCount() == 2)
                addSelectedIngredientToRecipe();
        });
    }

    private boolean addRecipe() {
        String name = nameField.getText();
        String servingsStr = servingsField.getText();
        List<QuantityIngredient> qIngredients = ingredientsTable.getAllIngredients();
        if (name.isBlank() || servingsStr.isBlank() || qIngredients.isEmpty())
            return false;

        RecipeInfo info = new RecipeInfo(name, Integer.parseInt(servingsStr), calculateTotalCalories(qIngredients));
        Recipe recipe = new Recipe(info, descriptionArea.getText(), qIngredients);
        boolean valid = getGui().getDatabase().insertRecipeInfo(info);
        if (!valid) return false;

        // create file to store full recipe
        valid = getGui().getConfig().getJsonLoader().write(recipe);
        if (!valid) {
            System.err.println("Recipe already exists!");
            getGui().getDatabase().removeRecipeInfo(info);
            return false;
        }
        return true;
    }

    public SearchBar<Ingredient> getIngredientSearchBar() {
        return ingredientSearchBar;
    }

    private void addSelectedIngredientToRecipe() {
        Ingredient selected = ingredientsList.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        if (!ingredientsTable.containsIngredient(selected))
            ingredientsTable.addIngredient(selected);
    }

    private double calculateTotalCalories(List<QuantityIngredient> qIngredients) {
        return qIngredients.stream().mapToDouble(QuantityIngredient::calories).sum();
    }
}
