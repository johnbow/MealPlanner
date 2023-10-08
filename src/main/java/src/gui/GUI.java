package src.gui;

import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import src.data.Config;
import src.data.Database;
import src.data.RecipeInfo;
import src.gui.controllers.Controller;
import src.gui.controllers.Screen;

import java.io.IOException;

public class GUI {

    private final Stage mainStage;
    private final Config config;
    private final Database database;

    private RecipeInfo dragContent;
    private Controller controller;
    private Screen currentScreen;

    public GUI(Stage mainStage, Config config, Database database) {
        this.mainStage = mainStage;
        this.config = config;
        this.database = database;
        this.currentScreen = null;
    }

    public void loadApplication() {
        mainStage.setWidth(config.getInitialWidth());
        mainStage.setHeight(config.getInitialHeight());
        loadScreen(Screen.CALENDAR);
        mainStage.show();
    }

    public void loadScreen(Screen screen) {
        try {
            if (controller != null)
                controller.onClose();
            Controller newController = screen.instantiateController(this);
            Scene scene = ResourceLoader.loadScene(newController);
            mainStage.setScene(scene);
            newController.setStage(mainStage);
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
            dialog.initOwner(mainStage);
            dialog.setScene(scene);
            dialogController.setStage(dialog);
            return dialogController;
        } catch (IOException e) {
            System.err.printf("Failed loading dialog %s\n", screen.getFilename());
            e.printStackTrace();
        }
        return null;
    }

    public Stage getMainStage() {
        return mainStage;
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
        causes scene to be rescaled incorrectly. Changing the window size
        programmatically solves this issue.
        If the stage is maximized, changing window size has no effect.
        Instead, un-maximize and maximize again
         */
        mainStage.setWidth(mainStage.getWidth() + 0.0001);
        if (mainStage.isMaximized()) {
            mainStage.hide();
            mainStage.setMaximized(false);
            mainStage.setMaximized(true);
            mainStage.show();
        }
    }

    public void setDragContent(RecipeInfo recipeInfo) {
        dragContent = recipeInfo;
    }

    public RecipeInfo getDragContent() {
        return dragContent;
    }

}
