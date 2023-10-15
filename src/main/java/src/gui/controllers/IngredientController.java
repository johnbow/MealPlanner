package src.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.converter.DoubleStringConverter;
import src.data.Config;
import src.data.Database;
import src.data.Ingredient;
import src.data.Measure;
import src.util.Converters;

public class IngredientController extends Controller {

    private final String calorieFormatString = "kcal per %s %s";
    private final String carbsString = "%s carbohydrates";
    private final String fatString = "%s fat";
    private final String proteinString = "%s protein";

    @FXML private TextField nameField;
    @FXML private TextField measureSizeField;
    @FXML private TextField calorieField;
    @FXML private TextField carbsField, proteinField, fatField;
    @FXML private ChoiceBox<Measure> measureBox;
    @FXML private CheckBox macrosPercentBox;
    @FXML private Button addButton, toggleMacros;
    @FXML private Label calorieLabel, errorLabel;
    @FXML private Label carbsLabel, proteinLabel, fatLabel;
    @FXML private VBox macrosBox;

    private Measure.Number measureNumber = Measure.Number.NOT_DEFINED;
    private boolean macrosVisible = false;

    @FXML
    public void initialize() {
        showErrorLabel(false);
        configureMacros();
        configureCalorieField();
        setMeasureBoxListeners();
        setMeasureNumber(measureNumber);
        setMeasureSizeFieldListeners();
        configureMeasureBox();
    }

    @FXML
    private void onAddIngredient(ActionEvent event) {
        if (calorieField.getText().isBlank() || measureSizeField.getText().isBlank() || nameField.getText().isBlank())
            return;
        // TODO: check if ingredient name is not already present
        Ingredient ingredient = new Ingredient(
                nameField.getText(),
                measureBox.getValue().singularName(),
                Double.parseDouble(measureSizeField.getText()),
                Double.parseDouble(calorieField.getText()),
                getMacroTypeInfo(),
                Double.parseDouble(carbsField.getText()),
                Double.parseDouble(fatField.getText()),
                Double.parseDouble(proteinField.getText())
        );
        boolean isValid = getGui().getDatabase().insertIngredient(ingredient);
        if (isValid) {
            if (getGui().getController() instanceof RecipeController recipeController)
                recipeController.getIngredientSearchBar().search();
            closeThisDialog();
        }
    }

    @FXML
    private void onToggleMacros(ActionEvent event) {
        macrosVisible = !macrosVisible;
        showMacros(macrosVisible);
        if (!macrosVisible)
            clearMacroFields();
        getStage().sizeToScene();
    }

    @FXML
    private void onMacrosPercentToggled(ActionEvent event) {
        updateMacroLabels();
    }

    private void clearMacroFields() {
        // TODO: set to default values
        carbsField.setText("0.0");
        fatField.setText("0.0");
        proteinField.setText("0.0");
    }

    private void updateMacroLabels() {
        updateMacroLabels(measureBox.getValue().abbreviation());
    }

    private void updateMacroLabels(String newUnit) {
        String unit = macrosPercentBox.isSelected() ? "%" : newUnit;
        carbsLabel.setText(String.format(carbsString, unit));
        fatLabel.setText(String.format(fatString, unit));
        proteinLabel.setText(String.format(proteinString, unit));
    }

    private int getMacroTypeInfo() {
        if (!macrosVisible) return Ingredient.MACROS_NOT_SPECIFIED;
        return macrosPercentBox.isSelected() ? Ingredient.MACROS_IN_PERCENT : Ingredient.MACROS_IN_ABSOLUTE_UNITS;
    }

    /**
     * Sets if measures should have their singular or plural names.
     */
    private void setMeasureNumber(Measure.Number number) {
        if (number == this.measureNumber) return;
        measureNumber = number;
        measureBox.setConverter(new Converters.MeasureStringConverter(measureNumber));
    }

    private void configureCalorieField() {
        calorieField.setTextFormatter(new TextFormatter<>(
                new DoubleStringConverter(), 0.0, Config.DOUBLE_FILTER));
    }

    private void updateCalorieLabel(String quantity, String measure) {
        calorieLabel.setText(String.format(calorieFormatString, quantity, measure));
    }

    public void setIngredientName(String text) {
        nameField.setText(text);
    }

    private void configureMacros() {
        showMacros(macrosVisible);
        carbsField.setTextFormatter(new TextFormatter<>(
                new DoubleStringConverter(), 0.0, Config.DOUBLE_FILTER));
        proteinField.setTextFormatter(new TextFormatter<>(
                new DoubleStringConverter(), 0.0, Config.DOUBLE_FILTER));
        fatField.setTextFormatter(new TextFormatter<>(
                new DoubleStringConverter(), 0.0, Config.DOUBLE_FILTER));
    }

    /**
     * Sets the selectionListener property of the measureBox.
     */
    private void setMeasureBoxListeners() {
        measureBox.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, number2) -> {
            Measure selected = measureBox.getItems().get((Integer) number2);
            String defaultQuantity = String.valueOf(selected.defaultQuantity());
            measureSizeField.setText(defaultQuantity);
            updateMacroLabels(selected.abbreviation());
            updateCalorieLabel(defaultQuantity, selected.getNameByQuantity(selected.defaultQuantity()));
        });
    }

    /**
     * Sets the TextFormatter and textProperty listener of the quantityField.
     */
    private void setMeasureSizeFieldListeners() {
        measureSizeField.setTextFormatter(new TextFormatter<Double>(
                new DoubleStringConverter(), 0.0, Config.DOUBLE_FILTER));
        measureSizeField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isBlank()) return;
            if (Math.abs(Double.parseDouble(newValue) - 1.0) < 0.0001)
                setMeasureNumber(Measure.Number.SINGULAR);
            else
                setMeasureNumber(Measure.Number.PLURAL);
            if (measureBox.getValue() != null)
                updateCalorieLabel(newValue, measureBox.getValue().getName(measureNumber));
        });
    }

    private void configureMeasureBox() {
        measureBox.getItems().addAll(Database.getMeasures());
        measureBox.getSelectionModel().selectFirst();
        updateCalorieLabel(measureSizeField.getText(), measureBox.getValue().getName(measureNumber));
    }

    private void showErrorLabel(boolean show) {
        errorLabel.setManaged(show);
        errorLabel.setVisible(show);
    }

    private void showMacros(boolean show) {
        macrosBox.setManaged(show);
        macrosBox.setVisible(show);
    }

}
