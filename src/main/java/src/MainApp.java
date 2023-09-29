package src;

import src.data.Config;
import src.data.Database;
import src.gui.GUI;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;
import java.util.Objects;

public class MainApp extends Application {

    private Config config;

    @Override
    public void start(Stage stage) {
        config = Config.loadConfig();
        Database database = new Database();
        if (Config.DO_CLEAN_INSTALL)
            doCleanUp(config);
        GUI gui = new GUI(stage, config, database);
        gui.loadApplication();
        database.loadUserData(config);
    }

    @Override
    public void stop() {
        config.save();
    }

    private void doCleanUp(Config config) {
        cleanUpRecipeDir(config);
    }

    private void cleanUpRecipeDir(Config config) {
        File recipeDir = new File(config.getDataDirectory() + Config.RECIPE_FOLDER);
        if (!recipeDir.isDirectory())
            return;
        for(String s: Objects.requireNonNull(recipeDir.list())) {
            File currentFile = new File(recipeDir.getPath(), s);
            if(currentFile.delete())
                System.out.printf("Deleted %s.\n", currentFile.getName());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}