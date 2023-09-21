package src.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;
import src.data.Ingredient;
import src.data.Measure;

public class IngredientController extends Controller {
    private Measure.Number measureNumber = Measure.Number.PLURAL;
    private final String calorieFormatString = "kcal per %s %s";

    @FXML private TextField nameField;
    @FXML private TextField quantityField;
    @FXML private TextField calorieField;
    @FXML private ChoiceBox<Measure> measureBox;
    @FXML private Button addButton;
    @FXML private Label calorieLabel;

    @FXML
    public void initialize() {
        measureBox.getItems().addAll(getGui().getDatabase().getMeasures());
        measureBox.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, number2) -> {
            Measure selected = measureBox.getItems().get((Integer) number2);
            String defaultQuantity = String.valueOf(selected.getDefaultQuantity());
            quantityField.setText(defaultQuantity);
            updateCalorieLabel(defaultQuantity, selected.getNameByQuantity(selected.getDefaultQuantity()));
        });
        quantityField.setTextFormatter(new TextFormatter<Double>(
                new DoubleStringConverter(), 0.0, getGui().getConfig().getDoubleFilter()));
        setMeasureNumber(measureNumber);
        quantityField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isBlank()) return;
            Measure.Number oldMeasureNumber = measureNumber;
            if (Math.abs(Double.parseDouble(newValue) - 1.0) < 0.0001)
                measureNumber = Measure.Number.SINGULAR;
            else
                measureNumber = Measure.Number.PLURAL;
            if (measureNumber != oldMeasureNumber)
                setMeasureNumber(measureNumber);
            if (measureBox.getValue() != null)
                updateCalorieLabel(newValue, measureBox.getValue().getName(measureNumber));
        });
        measureBox.getSelectionModel().selectFirst();
        updateCalorieLabel(quantityField.getText(), measureBox.getValue().getName(measureNumber));
    }

    @FXML
    private void onAddIngredient() {
        if (calorieField.getText().isBlank() || quantityField.getText().isBlank() || nameField.getText().isBlank())
            return;
        // TODO: check if ingredient name is not already present
        getGui().getDatabase().addIngredient(new Ingredient(
                nameField.getText(),
                measureBox.getValue(),
                Double.parseDouble(quantityField.getText()),
                Double.parseDouble(calorieField.getText())
        ));
        closeThisDialog();
    }

    /**
     * Sets if measures should have their singular or plural names.
     */
    private void setMeasureNumber(Measure.Number number) {
        measureBox.setConverter(new StringConverter<Measure>() {
            @Override
            public String toString(Measure object) {
                if (object == null) return null;
                return object.getName(number);
            }

            @Override
            public Measure fromString(String string) {
                for (Measure measure : measureBox.getItems()) {
                    if (measure.getName(number).equals(string))
                        return measure;
                }
                return null;
            }
        });
    }

    private void updateCalorieLabel(String quantity, String measure) {
        calorieLabel.setText(String.format(calorieFormatString, quantity, measure));
    }

    public void setIngredientName(String text) {
        nameField.setText(text);
    }


}
