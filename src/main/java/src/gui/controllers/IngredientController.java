package src.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;
import src.data.Ingredient;
import src.data.Measure;

public class IngredientController extends Controller {

    private final double DEFAULT_PORTION_SIZE = 100.0;
    private Measure.Number measureNumber = Measure.Number.SINGULAR;

    @FXML private TextField nameField;
    @FXML private TextField quantityField;
    @FXML private ChoiceBox<Measure> measureBox;
    @FXML private Button addButton;

    @FXML
    public void initialize() {
        measureBox.getItems().addAll(getGui().getDatabase().getMeasures());
        measureBox.getSelectionModel().selectFirst();
        quantityField.setTextFormatter(new TextFormatter<Double>(
                new DoubleStringConverter(), DEFAULT_PORTION_SIZE, getGui().getConfig().getDoubleFilter()));
        setMeasureNumber(measureNumber);
        quantityField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) return;
            Measure.Number oldMeasureNumber = measureNumber;
            if (Math.abs(Double.parseDouble(newValue) - 1.0) < 0.0001)
                measureNumber = Measure.Number.SINGULAR;
            else
                measureNumber = Measure.Number.PLURAL;
            if (measureNumber != oldMeasureNumber)
                setMeasureNumber(measureNumber);
        });
    }

    @FXML
    private void onAddIngredient() {
        getGui().getDatabase().addIngredient(new Ingredient(
                nameField.getText(),
                measureBox.getValue(),
                Double.parseDouble(quantityField.getText())
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

    private void setPluralMeasures() {
        measureBox.setConverter(new StringConverter<Measure>() {
            @Override
            public String toString(Measure object) {
                return object.getPluralName();
            }

            @Override
            public Measure fromString(String string) {
                for (Measure measure : measureBox.getItems()) {
                    if (measure.getPluralName().equals(string))
                        return measure;
                }
                return null;
            }
        });
    }

    public void setIngredientName(String text) {
        nameField.setText(text);
    }


}
