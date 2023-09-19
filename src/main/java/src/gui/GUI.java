package src.gui;

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
    }

    public void loadApplication() {
        stage.setTitle(config.getTitle());
        stage.setWidth(config.getInitialWidth());
        stage.setHeight(config.getInitialHeight());
        loadScreen(Screen.CALENDAR);
    }

    public void loadScreen(Screen screen) {
        try {
            controller = screen.instantiateController();
            Scene scene = ResourceLoader.loadScene(controller);
            stage.setScene(scene);
            stage.show();
            currentScreen = screen;
        } catch (IOException e) {
            System.err.printf("Failed loading scene %s\n", screen.getFilename());
            e.printStackTrace();
        }
    }

}
