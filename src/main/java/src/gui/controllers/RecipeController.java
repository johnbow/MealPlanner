package src.gui.controllers;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.util.converter.IntegerStringConverter;

import src.data.*;
import src.gui.components.IngredientListViewCell;
import src.gui.components.QueryService;
import src.gui.components.SearchBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RecipeController extends Controller {

    private final Task<Boolean> addRecipeTask = new Task<>() {
        @Override
        protected Boolean call() {
            return addRecipe();
        }
    };
    private final Thread addRecipeThread = new Thread(addRecipeTask);

    @FXML private Button returnToCalendarButton;
    @FXML private Button addIngredientButton, addRecipeButton;
    @FXML private SearchBar<Ingredient> ingredientSearchBar;
    @FXML private TextField nameField, servingsField;
    @FXML private ListView<Ingredient> ingredientsList;
    @FXML private TextArea descriptionArea;

    @FXML
    public void initialize() {
        getGui().getStage().setTitle("Add Recipe");
        servingsField.setTextFormatter(new TextFormatter<Integer>(
                new IntegerStringConverter(), 1, Config.INT_FILTER_2_PLACES));
        addRecipeTask.setOnSucceeded(t -> {
            boolean added = addRecipeTask.getValue();
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
        addRecipeThread.start();
    }

    private void configureSearchBar() {
        ingredientSearchBar.setDisplay(ingredientsList.getItems());
        int capacity = getGui().getConfig().getListViewResultsLimit();
        ingredientSearchBar.setSearchQuery(new QueryService<>(capacity) {
            @Override
            protected Boolean query(Set<Ingredient> resultSet, String searchText) {
                return getGui().getDatabase().addIngredientsTo(resultSet, searchText, capacity);
            }
        });
        ingredientsList.setCellFactory(ingredientListView -> new IngredientListViewCell());
    }

    private boolean addRecipe() {
        String name = nameField.getText();
        String servingsStr = servingsField.getText();
        List<QuantityIngredient> pairs = getIngredientsWithQuantities();
        // TODO: Uncomment if adding ingredients is implemented
        if (name.isBlank() || servingsStr.isBlank()/* || ingredients.isEmpty()*/)
            return false;
        // TODO: Calculate total_calories
        RecipeInfo info = new RecipeInfo(name, Integer.parseInt(servingsStr), calculateTotalCalories(pairs));
        Recipe recipe = new Recipe(info, descriptionArea.getText(), pairs);
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

    private List<QuantityIngredient> getIngredientsWithQuantities() {
        // TODO
        return new ArrayList<>();
    }

    private double calculateTotalCalories(List<QuantityIngredient> pairs) {
        // TODO
        return 0.d;
    }
}
