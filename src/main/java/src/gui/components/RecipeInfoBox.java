package src.gui.components;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.Separator;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import src.data.Config;
import src.data.RecipeInfo;
import src.gui.GUI;

public class RecipeInfoBox extends VBox {

    private GUI gui;
    private RecipeInfo recipeInfo;
    private Label header;
    private ImageView image;

    public RecipeInfoBox(GUI gui) {
        this.gui = gui;
        header = new Label();
        image = new ImageView();

        setAlignment(Pos.CENTER);
        header.setTextAlignment(TextAlignment.CENTER);
        header.setWrapText(true);
        header.setTextOverrun(OverrunStyle.CLIP);

        getChildren().add(header);
        getChildren().add(new Separator());
        getChildren().add(image);

        setDragSource();

        getStyleClass().add("recipe-info-box");
    }

    public RecipeInfoBox(GUI gui, RecipeInfo recipeInfo) {
        this(gui);
        setRecipeInfo(recipeInfo);
    }

    public void setRecipeInfo(RecipeInfo recipeInfo) {
        this.recipeInfo = recipeInfo;
        header.setText(recipeInfo.name());
    }

    public RecipeInfo getRecipeInfo() {
        return recipeInfo;
    }

    private void setDragSource() {
        setOnDragDetected(event -> {
            Dragboard db = startDragAndDrop(TransferMode.COPY);
            db.setDragView(Config.getDragAndDropImage());
            db.setDragViewOffsetX(50);
            db.setDragViewOffsetY(50);

            ClipboardContent content = new ClipboardContent();
            content.putUrl(gui.getConfig().getDataDirectory() + Config.RECIPE_FOLDER + recipeInfo.filename());

            Clipboard.getSystemClipboard().setContent(content);
            db.setContent(content);
            gui.setDragContent(recipeInfo);

            event.consume();
        });
    }

}