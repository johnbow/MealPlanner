package src.gui.components;

import javafx.geometry.Insets;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import src.data.RecipeInfo;
import src.gui.GUI;

public class WeekdayCell extends VBox {

    private GUI gui;

    public WeekdayCell() {
        setDragTarget();
        setPadding(new Insets(5));
        setSpacing(5);
    }

    public void setGui(GUI gui) {
        this.gui = gui;
    }

    private void setDragTarget() {
        setOnDragOver(event -> {
            if (event.getDragboard().hasUrl())
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            event.consume();
        });
        setOnDragEntered(this::startHighlighting);
        setOnDragExited(this::stopHighlighting);
        setOnDragDropped(event -> {
            final Dragboard db = event.getDragboard();
            boolean success = db.hasUrl();
            if (success)
                addRecipeInfo(gui.getDragContent());
            event.setDropCompleted(success);
        });
    }

    private void startHighlighting(DragEvent event) {

    }

    private void stopHighlighting(DragEvent event) {

    }

    public void addRecipeInfo(RecipeInfo recipeInfo) {
        RecipeInfoCell cell = new RecipeInfoCell(gui, recipeInfo);
        // cell.maxWidthProperty().bind(widthProperty());
        getChildren().add(cell);
    }
}
