package src;

import src.gui.GUI;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {
    private GUI gui;

    @Override
    public void start(Stage stage) throws IOException {
        Config config = new Config();
        gui = new GUI(stage, config);
        gui.loadApplication();

    }

    @Override
    public void stop() {
        // TODO
    }

    public static void main(String[] args) {
        launch(args);
    }
}