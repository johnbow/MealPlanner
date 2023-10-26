package src.gui.components;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import src.data.Config;
import src.data.RecipeInfo;
import src.gui.GUI;

public class CalendarColumn extends VBox {

    private GUI gui;
    private final Region highlightBox;

    public CalendarColumn() {
        highlightBox = new Region();
        highlightBox.getStyleClass().add("highlight-region");
        highlightBox.widthProperty().addListener((observable, oldValue, newValue) -> {
            highlightBox.setPrefHeight((double)newValue * Config.IMAGE_ASPECT_RATIO);
        });

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
            if (success && event.getGestureSource() instanceof RecipeInfoBox oldBox)
                dropRecipeInfo(oldBox);
            event.setDropCompleted(success);
        });
    }

    public void dropRecipeInfo(RecipeInfoBox source) {
        RecipeInfoBox newBox = addRecipeInfo(gui.getDragContent());
        if (source.getDragMode() == TransferMode.MOVE) { // remove box from other column
            if (source.getImage() != null)
                newBox.setImage(source.getImage());
            ((Pane) source.getParent()).getChildren().remove(source);
        }
    }

    public RecipeInfoBox addRecipeInfo(RecipeInfo recipeInfo) {
        RecipeInfoBox box = new RecipeInfoBox(gui, recipeInfo);
        box.setDragMode(TransferMode.MOVE);
        getChildren().add(box);
        return box;
    }

    public void loadImages() {
        for (Node node : getChildren()) {
            if (node instanceof RecipeInfoBox box) {
                final String key = box.getRecipeInfo().filename();
                if (!gui.getImagePool().containsKey(key))
                    gui.getImagePool().put(key, gui.loadImage(key));
                box.setImage(gui.getImagePool().get(key));
            }
        }
    }
}
