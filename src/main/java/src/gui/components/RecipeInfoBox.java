package src.gui.components;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
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
    private Separator sep;
    private ScrollPane imagePane;
    private ImageView imageView;

    private boolean imageDisplayVisible = false;
    private TransferMode dragMode = TransferMode.COPY;

    private ChangeListener<Number> widthListener = (observable, oldValue, newValue) -> {
        double val = (double) newValue;
        imageView.setFitWidth(val);
        imageView.setFitHeight(val * Config.IMAGE_ASPECT_RATIO);
        imagePane.setMinHeight(imageView.getFitHeight());
    };

    public RecipeInfoBox(GUI gui, RecipeInfo recipeInfo) {
        this.gui = gui;
        this.recipeInfo = recipeInfo;

        header = new Label(recipeInfo.name());
        sep = new Separator();
        imagePane = new ScrollPane();
        imageView = new ImageView();

        setAlignment(Pos.CENTER);
        header.setTextAlignment(TextAlignment.CENTER);
        header.setAlignment(Pos.CENTER);
        header.setWrapText(true);
        header.setTextOverrun(OverrunStyle.CLIP);
        header.setMaxWidth(Double.MAX_VALUE);

        imageView.setSmooth(true);
        imageView.setCache(true);

        imagePane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        imagePane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        toggleImageDisplay(false);

        imagePane.setContent(imageView);
        getChildren().add(header);
        getChildren().add(sep);
        getChildren().add(imagePane);

        setOnDragDetected(this::startDrag);
        getStyleClass().add("recipe-info-box");
    }

    public void setRecipeInfo(RecipeInfo recipeInfo) {
        this.recipeInfo = recipeInfo;
        header.setText(recipeInfo.name());
    }

    public RecipeInfo getRecipeInfo() {
        return recipeInfo;
    }

    private void startDrag(MouseEvent event) {
        Dragboard db = startDragAndDrop(dragMode);
        db.setDragView(Config.getDragAndDropImage());
        db.setDragViewOffsetX(50);
        db.setDragViewOffsetY(50);

        ClipboardContent content = new ClipboardContent();
        content.putUrl(gui.getConfig().getDataDirectory() + Config.RECIPE_FOLDER + recipeInfo.filename());

        Clipboard.getSystemClipboard().setContent(content);
        db.setContent(content);
        gui.setDragContent(recipeInfo);

        event.consume();
    }

    public void toggleImageDisplay(boolean visible) {
        if (visible)
            imagePane.widthProperty().addListener(widthListener);
        else if (imageDisplayVisible)   // if change from visible to invisible
            imagePane.widthProperty().removeListener(widthListener);
        imageDisplayVisible = visible;
        sep.setVisible(visible);
        sep.setManaged(visible);
        imagePane.setVisible(visible);
        imagePane.setManaged(visible);
    }

    public TransferMode getDragMode() {
        return dragMode;
    }

    public void setDragMode(TransferMode dragMode) {
        this.dragMode = dragMode;
    }

    public void setImage(Image image) {
        if (image == null) return;
        if (!imageDisplayVisible)
            toggleImageDisplay(true);
        imageView.setImage(image);
    }

    public Image getImage() {
        return imageView.getImage();
    }

}