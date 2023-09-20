package src.gui;

import javafx.beans.value.ChangeListener;
import javafx.scene.Scene;
import javafx.stage.Stage;
import src.Config;
import src.gui.controllers.Controller;
import src.gui.controllers.Screen;

import java.io.IOException;

public class GUI {

    private Stage stage;
    private Config config;
    private Controller controller;
    private Screen currentScreen;

    public GUI(Stage stage, Config config) {
        this.stage = stage;
        this.config = config;
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
            controller = screen.instantiateController(this);
            Scene scene = ResourceLoader.loadScene(controller);
            stage.setScene(scene);
            forceRefresh();
            currentScreen = screen;
        } catch (IOException e) {
            System.err.printf("Failed loading scene %s\n", screen.getFilename());
            e.printStackTrace();
        }
    }

    public Stage getStage() {
        return stage;
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
