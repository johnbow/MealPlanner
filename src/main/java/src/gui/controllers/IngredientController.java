package src.gui.controllers;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;
import src.data.Config;
import src.data.Ingredient;
import src.data.Measure;

import java.util.List;

public class IngredientController extends Controller {
    private Measure.Number measureNumber = Measure.Number.PLURAL;
    private final String calorieFormatString = "kcal per %s %s";

    private final Task<List<Measure>> loadMeasuresTask = new Task<>() {
        @Override protected List<Measure> call() {
            return getGui().getDatabase().getMeasures();
        }
    };
    private final Thread loadMeasuresThread = new Thread(loadMeasuresTask);

    @FXML private TextField nameField;
    @FXML private TextField measureSizeField;
    @FXML private TextField calorieField;
    @FXML private ChoiceBox<Measure> measureBox;
    @FXML private Button addButton;
    @FXML private Label calorieLabel, errorLabel;

    @FXML
    public void initialize() {
        errorLabel.setManaged(false);
        setMeasureBoxListeners();
        setMeasureNumber(measureNumber);
        setMeasureSizeFieldListeners();
        setLoadMeasuresTaskListeners();
        loadMeasures();
    }

    @FXML
    private void onAddIngredient() {
        if (calorieField.getText().isBlank() || measureSizeField.getText().isBlank() || nameField.getText().isBlank())
            return;
        // TODO: check if ingredient name is not already present
        boolean isValid = getGui().getDatabase().insertIngredient(new Ingredient(
                nameField.getText(),
                measureBox.getValue(),
                Double.parseDouble(measureSizeField.getText()),
                Double.parseDouble(calorieField.getText())
        ));
        if (isValid)
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

    private void loadMeasures() {
        if (loadMeasuresThread.isAlive())
            return;
        loadMeasuresThread.start();
    }

    private void updateCalorieLabel(String quantity, String measure) {
        calorieLabel.setText(String.format(calorieFormatString, quantity, measure));
    }

    public void setIngredientName(String text) {
        nameField.setText(text);
    }

    /**
     * Sets the selectionListener property of the measureBox.
     */
    private void setMeasureBoxListeners() {
        measureBox.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, number2) -> {
            Measure selected = measureBox.getItems().get((Integer) number2);
            String defaultQuantity = String.valueOf(selected.defaultQuantity());
            measureSizeField.setText(defaultQuantity);
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
    }

    private void setLoadMeasuresTaskListeners() {
        loadMeasuresTask.setOnSucceeded(t -> {
            final List<Measure> measures = loadMeasuresTask.getValue();
            measureBox.getItems().addAll(measures);
            measureBox.getSelectionModel().selectFirst();
            updateCalorieLabel(measureSizeField.getText(), measureBox.getValue().getName(measureNumber));
        });
    }


}
