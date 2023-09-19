package src.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import src.gui.controllers.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

/**
 * Used for loading resources like fxml files, images and css files.
 * @author John Klein
 */
public class ResourceLoader {

    public static final String IMAGE_PATH = "/images/";
    public static final String FXML_PATH = "/fxml/";
    public static final String CSS_PATH = "/css/";

    /**
     * Loads image from resource folder.
     * @param filename Name of image with file extension, e.g. 'image.png'
     * @return The specified image.
     */
    public static Image loadImage(String filename) {
        return new Image(IMAGE_PATH + filename);
    }

    /**
     * Loads fxml file from resource folder.
     * @param filename name of the file with file extension. e.g. 'main.fxml'
     * @return The parent loaded from the fxml file.
     * @throws IOException if an error occurs during loading.
     */
    public static Parent loadFXML(String filename) throws IOException {
        return FXMLLoader.load(Objects.requireNonNull(ResourceLoader.class.getResource(FXML_PATH + filename)));
    }

    /**
     * Loads the scene corresponding to the given controller.
     * @param controller The associated controller.
     * @return The loaded scene.
     * @throws IOException if fxml fails to load.
     */
    public static Scene loadScene(Controller controller) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                ResourceLoader.class.getResource(FXML_PATH + controller.getFilename())
        );
        fxmlLoader.setController(controller);
        Parent root = fxmlLoader.load();
        final Scene scene = new Scene(root);
        URL stylesheetURL = null;
        if (controller.getStylesheet() != null) {
            stylesheetURL = ResourceLoader.class.getResource(CSS_PATH + controller.getStylesheet());
            if (stylesheetURL != null)
                scene.getStylesheets().add(stylesheetURL.toString());
            else
                System.err.printf("Failed to load stylesheet %s\n", controller.getStylesheet());
        }

        return scene;
    }
}
