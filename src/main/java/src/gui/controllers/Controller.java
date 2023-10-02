package src.gui.controllers;

import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import src.gui.GUI;

import java.util.ArrayList;
import java.util.List;

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
    private List<Controller> dialogs;
    private boolean isDialog = false;
    private Stage stage;

    Controller() {
        dialogs = new ArrayList<Controller>();
    }

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

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    public Controller openDialog(Screen screen, StageStyle stageStyle, Modality modality) {
        Controller dialog = getGui().loadDialog(screen, stageStyle, modality);
        dialogs.add(dialog);
        dialog.getStage().show();
        return dialog;
    }

    public void closeAllDialogs() {
        for (Controller dialog : dialogs)
            dialog.closeThisDialog();
    }

    public void closeThisDialog() {
        if(!isDialog) return;
        stage.close();
    }

    public void setDialog(boolean isDialog) {
        this.isDialog = isDialog;
    }

    public boolean isDialog() {
        return isDialog;
    }
}
