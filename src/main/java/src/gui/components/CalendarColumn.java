package src.gui.components;

import javafx.geometry.Insets;
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
        setOnDragEntered(event -> getChildren().add(highlightBox));
        setOnDragExited(event -> getChildren().removeIf(node -> node == highlightBox));
        setOnDragDropped(event -> {
            final Dragboard db = event.getDragboard();
            boolean success = db.hasUrl();
            if (success)
                addRecipeInfo(gui.getDragContent());
            event.setDropCompleted(success);
        });
    }

    public void addRecipeInfo(RecipeInfo recipeInfo) {
        getChildren().add(new RecipeInfoBox(gui, recipeInfo));
    }
}
