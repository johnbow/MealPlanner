package src.gui.controllers;

import src.gui.GUI;

/**
 * Base class for all controllers.
 * @author John Klein
 */
public abstract class Controller {

    /** Associated gui */
    private GUI gui;

    /** Name of fxml file associated with controller */
    private String filename;
    /** Name of css file associated with controller */
    private String stylesheet;

    Controller() {}

    void setGUI(GUI gui) {
        this.gui = gui;
    }

    void setFilename(String filename) {
        this.filename = filename;
    }

    void setStylesheet(String stylesheet) {
        this.stylesheet = stylesheet;
    }

    public GUI getGui() {
        return gui;
    }

    public String getFilename() {
        return filename;
    }

    public String getStylesheet() {
        return stylesheet;
    }
}
