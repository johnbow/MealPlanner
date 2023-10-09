package src.gui.components;

import javafx.geometry.Insets;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import src.data.RecipeInfo;
import src.gui.GUI;

public class CalendarColumn extends VBox {

    private GUI gui;
    private final Region highlightBox;

    public CalendarColumn() {
        highlightBox = new Region();
        highlightBox.getStyleClass().add("highlight-region");

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
        getChildren().add(highlightBox);
    }

    private void stopHighlighting(DragEvent event) {
        // always uses the same highlightBox:
        getChildren().removeIf(node -> node == highlightBox);
    }

    public void addRecipeInfo(RecipeInfo recipeInfo) {
        getChildren().add(new RecipeInfoBox(gui, recipeInfo));
    }
}
