package src.gui;

import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import src.data.Config;
import src.data.Database;
import src.data.JSONLoader;
import src.gui.controllers.Controller;
import src.gui.controllers.Screen;

import java.io.IOException;

public class GUI {

    private Stage stage;
    private Config config;
    private Database database;
    private Controller controller;
    private Screen currentScreen;

    public GUI(Stage stage, Config config, Database database) {
        this.stage = stage;
        this.config = config;
        this.database = database;
        this.currentScreen = null;
    }

    public void loadApplication() {
        stage.setWidth(config.getInitialWidth());
        stage.setHeight(config.getInitialHeight());
        loadScreen(Screen.CALENDAR);
        stage.show();
    }

    public void loadScreen(Screen screen) {
        try {
            if (controller != null)
                controller.closeAllDialogs();
            Controller newController = screen.instantiateController(this);
            Scene scene = ResourceLoader.loadScene(newController);
            stage.setScene(scene);
            newController.setStage(stage);
            forceRefresh();
            controller = newController;
            currentScreen = screen;
        } catch (IOException e) {
            System.err.printf("Failed loading scene %s\n", screen.getFilename());
            e.printStackTrace();
        }
    }

    public Controller loadDialog(Screen screen, StageStyle stageStyle, Modality modality) {
        try {
            Controller dialogController = screen.instantiateController(this);
            dialogController.setDialog(true);
            Scene scene = ResourceLoader.loadScene(dialogController);
            Stage dialog = new Stage(stageStyle);
            dialog.initModality(modality);
            dialog.initOwner(stage);
            dialog.setScene(scene);
            dialogController.setStage(dialog);
            return dialogController;
        } catch (IOException e) {
            System.err.printf("Failed loading dialog %s\n", screen.getFilename());
            e.printStackTrace();
        }
        return null;
    }

    public Stage getStage() {
        return stage;
    }

    public Config getConfig() {
        return config;
    }

    public Controller getController() {
        return controller;
    }

    public Database getDatabase() {
        return database;
    }

    private void forceRefresh() {
        /*
        Fixes the dpi scaling issue on windows.
        If dpi is set to anything other than 100%, changing scene
        cause scene to be rescaled incorrectly. Changing the window size
        programmatically solves this issue.
         */
        stage.setWidth(stage.getWidth() + 0.0001);
        if (stage.isMaximized()) {
            stage.hide();
            stage.setMaximized(false);
            stage.setMaximized(true);
            stage.show();
        }
    }

}
