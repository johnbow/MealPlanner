package src.gui.components;


import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import src.data.Config;
import src.data.Text;

public class ImageSelector extends StackPane {

    private ImageView imageView;
    private Label dropLabel;

    public ImageSelector() {
        getStyleClass().add("image-selector");
        setAlignment(Pos.CENTER);

        imageView = new ImageView();
        imageView.setFitWidth(Config.IMAGE_WIDTH);
        imageView.setFitHeight(Config.IMAGE_HEIGHT);
        imageView.setSmooth(true);
        imageView.setCache(true);

        dropLabel = new Label(Text.dropImageLabel);

        getChildren().add(imageView);
        getChildren().add(dropLabel);
        setDragTarget();
    }

    public void setDragTarget() {
        setOnDragOver(event -> {
            if (event.getGestureSource() != this && event.getDragboard().hasImage()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });
        setOnDragEntered(event -> {
            // start highlight
        });
        setOnDragExited(event -> {
            // end highlight
        });
        setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = db.hasImage();
            if (success)
                selectImage(db.getImage());
            event.setDropCompleted(success);
            event.consume();
        });
    }

    public void selectImage(Image img) {

        dropLabel.setVisible(false);
        dropLabel.setManaged(false);
        imageView.setImage(img);
    }

    public Image getImage() {
        return imageView.getImage();
    }

}
