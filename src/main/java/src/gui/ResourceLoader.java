package src.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import src.gui.controllers.Controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

/**
 * Used for loading project resources like fxml files, images and css files.
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
        String path = Objects.requireNonNull(ResourceLoader.class.getResource(IMAGE_PATH + filename)).getPath();
        InputStream stream;
        try {
            stream = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return new Image(stream);
    }

    /**
     * Loads image from resource folder with specified parameters.
     * @param filename Name of image with file extension, e.g. 'image.png'
     * @return The specified image.
     */
    public static Image loadImage(String filename,
                                  double requestedWidth,
                                  double requestedHeight,
                                  boolean preserveRatio,
                                  boolean smooth) {
        String path = Objects.requireNonNull(ResourceLoader.class.getResource(IMAGE_PATH + filename)).getPath();
        InputStream stream;
        try {
            stream = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return new Image(stream, requestedWidth, requestedHeight, preserveRatio, smooth);
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
        Scene scene = new Scene(root);
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
