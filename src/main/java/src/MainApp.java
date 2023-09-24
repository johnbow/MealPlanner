package src;

import src.data.Database;
import src.gui.GUI;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Config config = Config.loadConfig();
        Database database = new Database();
        GUI gui = new GUI(stage, config, database);
        gui.loadApplication();
        database.loadUserData(config);
    }

    @Override
    public void stop() {
        // TODO
    }

    public static void main(String[] args) {
        launch(args);
    }
}