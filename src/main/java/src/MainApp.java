package src;

import src.data.Config;
import src.data.Database;
import src.gui.GUI;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;
import java.util.Objects;

public class MainApp extends Application {

    private GUI gui;
    private Config config;

    @Override
    public void start(Stage stage) {
        config = Config.loadConfig();
        Database database = new Database();
        if (Config.DO_CLEAN_INSTALL)
            doCleanUp(config);
        database.loadUserData(config);

        gui = new GUI(stage, config, database);
        gui.loadApplication();
    }

    @Override
    public void stop() {
        if (gui.getController() != null)
            gui.getController().onClose();
        config.save();
    }

    private void doCleanUp(Config config) {
        cleanDir(new File(config.getDataDirectory() + Config.RECIPE_FOLDER));
        cleanDir(new File(config.getDataDirectory() + Config.WEEK_FOLDER));
    }

    private boolean cleanDir(File dir) {
        if (!dir.isDirectory())
            return false;
        for(String s: Objects.requireNonNull(dir.list())) {
            File currentFile = new File(dir.getPath(), s);
            if (currentFile.delete())
                System.out.printf("Deleted %s.\n", currentFile.getName());
        }
        return dir.delete();
    }

    public static void main(String[] args) {
        launch(args);
    }
}