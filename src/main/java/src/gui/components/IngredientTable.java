package src.gui.components;

import javafx.beans.property.*;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.text.TextAlignment;
import javafx.util.converter.DoubleStringConverter;

import src.data.Database;
import src.data.Ingredient;
import src.data.Measure;
import src.data.QuantityIngredient;
import src.util.Converters;

import java.util.ArrayList;
import java.util.List;

public class IngredientTable extends TableView<IngredientTable.Row> {

    private final TableColumn<Row, String> nameCol;
    private final TableColumn<Row, Double> quantityCol;
    private final TableColumn<Row, Measure> measureCol;
    private final TableColumn<Row, String> calorieCol;

    private final Converters.MeasureAbbrevStringConverter measureStringConverter
            = new Converters.MeasureAbbrevStringConverter();

    public IngredientTable() {
        getStyleClass().add("ingredient-table");
        setEditable(true);
        getSelectionModel().setCellSelectionEnabled(true);
        nameCol = new TableColumn<>("Name");
        quantityCol = new TableColumn<>("quantity");
        measureCol = new TableColumn<>("measure");
        calorieCol = new TableColumn<>("Calories");

        quantityCol.setCellFactory(this::createQuantityCell);
        measureCol.setCellFactory(this::createMeasureCell);

        nameCol.setCellValueFactory(col -> col.getValue().ingredient.map(Ingredient::name));
        quantityCol.setCellValueFactory(col -> col.getValue().quantity.asObject());
        measureCol.setCellValueFactory(col -> col.getValue().measure);
        calorieCol.setCellValueFactory(col -> col.getValue().calories.asString("%.1f kcal"));

        getColumns().add(nameCol);
        getColumns().add(quantityCol);
        getColumns().add(measureCol);
        getColumns().add(calorieCol);
    }

    private TableCell<Row, Measure> createMeasureCell(TableColumn<Row, Measure> col) {
        ComboBoxTableCell<Row, Measure> cell = new ComboBoxTableCell<>(measureStringConverter);
        cell.getItems().addAll(Database.getMeasures());
        cell.setTextAlignment(TextAlignment.LEFT);
        cell.setStyle("-fx-alignment: CENTER-LEFT;");
        return cell;
    }

    private QuantityCell<Row> createQuantityCell(TableColumn<Row, Double> col) {
        QuantityCell<Row> cell = new QuantityCell<>(new DoubleStringConverter());
        cell.setTextAlignment(TextAlignment.RIGHT);
        cell.setStyle("-fx-alignment: CENTER-RIGHT;");
        return cell;
    }

    private void selectQuantityCell(Row row) {
        int rowIndex = getItems().indexOf(row);
        edit(rowIndex, quantityCol);
    }

    public void addIngredient(Ingredient ingredient) {
        Row newRow = new Row(ingredient);
        getItems().add(newRow);
        selectQuantityCell(newRow);
    }

    public boolean containsIngredient(Ingredient ingredient) {
        for (Row row : getItems())
            if (row.ingredient.get().name().equals(ingredient.name()))
                return true;
        return false;
    }

    public List<QuantityIngredient> getAllIngredients() {
        List<QuantityIngredient> qIngredients = new ArrayList<>();
        getItems().forEach(item -> qIngredients.add(item.toQuantityIngredient()));
        return qIngredients;
    }

    static class Row {
        final ReadOnlyObjectWrapper<Ingredient> ingredient;
        final SimpleDoubleProperty quantity;
        final SimpleObjectProperty<Measure> measure;
        final SimpleDoubleProperty calories;

        Row(Ingredient ingredient, double quantity) {
            this.ingredient = new ReadOnlyObjectWrapper<>(ingredient);
            this.quantity = new SimpleDoubleProperty(quantity);
            this.measure = new SimpleObjectProperty<>(ingredient.measure());
            this.calories = new SimpleDoubleProperty(ingredient.calories(quantity));

            this.measure.addListener((observable, oldValue, newValue) -> {
                this.calories.set(this.ingredient.get().calories(newValue, this.quantity.get()));
            });
            this.quantity.addListener((observable, oldValue, newValue) -> {
                this.calories.set(this.ingredient.get().calories(measure.get(), (double) newValue));
            });
        }

        Row(Ingredient ingredient) {
            this(ingredient, ingredient.measureSize());
        }

        QuantityIngredient toQuantityIngredient() {
            return new QuantityIngredient(quantity.get(), ingredient.get().changeMeasure(measure.get()));
        }

    }


}
