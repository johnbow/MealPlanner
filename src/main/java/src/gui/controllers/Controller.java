package src.gui.controllers;

/**
 * Base class for all controllers.
 * @author John Klein
 */
public abstract class Controller {

    /** Name of fxml file associated with controller */
    private String filename;
    /** Name of css file associated with controller */
    private String stylesheet;

    Controller() {}

    void setFilename(String filename) {
        this.filename = filename;
    }

    void setStylesheet(String stylesheet) {
        this.stylesheet = stylesheet;
    }

    public String getFilename() {
        return filename;
    }

    public String getStylesheet() {
        return stylesheet;
    }
}
