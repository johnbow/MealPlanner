package src.gui.components;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.BorderPane;
import src.data.Ingredient;

public class IngredientListViewCell extends ListCell<Ingredient> {

    private BorderPane root;
    private Label nameLabel;
    private Label calorieLabel;

    public IngredientListViewCell() {
        root = new BorderPane();
        nameLabel = new Label();
        calorieLabel = new Label();
        root.setLeft(nameLabel);
        root.setRight(calorieLabel);
        setGraphic(root);
        getChildren().add(root);
    }

    @Override
    protected void updateItem(Ingredient ingredient, boolean empty) {
        super.updateItem(ingredient, empty);

        if(empty || ingredient == null) {

            setText(null);
            setContentDisplay(ContentDisplay.TEXT_ONLY);

        } else {
            // initialize listview
            nameLabel.setText(ingredient.name());
            calorieLabel.setText(String.format("%.1f kcal / %.1f %s",
                    ingredient.caloriesPerMeasureSize(),
                    ingredient.measureSize(),
                    ingredient.measure().abbreviation()));
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }

    }

}
