package src;

import javafx.scene.control.TextFormatter;
import net.harawata.appdirs.AppDirs;
import net.harawata.appdirs.AppDirsFactory;

import java.util.function.UnaryOperator;

public class Config {
    public static final String APPLICATION_NAME = "MealPlanner";
    public static final String AUTHOR_NAME = "Klein";
    public static final String CONFIG_FILE = "config.json";
    public static final String DATABASE_FILE = "ingredients.db";    // also stores measures

    private int initialHeight = 600;
    private int initialWidth = 800;
    private String userDataDirectory = "";
    private final UnaryOperator<TextFormatter.Change> doubleFilter = change -> {
        String newText = change.getControlNewText();
        if (newText.matches("([0-9]*(\\.[0-9]{0,6})?)?")) {
            return change;
        }
        return null;
    };

    private Config() {}

    public static Config loadConfig() {
        Config config = new Config();

        AppDirs appDirs = AppDirsFactory.getInstance();
        String configFilePath = appDirs.getUserConfigDir(APPLICATION_NAME, CONFIG_FILE, AUTHOR_NAME);
        // load stuff...
        if (config.getUserDataDirectory().isBlank()) {
            String userDataDir = appDirs.getUserDataDir(APPLICATION_NAME, "", AUTHOR_NAME);
            config.setUserDataDirectory(userDataDir);
        }
        return config;
    }

    public String getUserDataDirectory() {
        return userDataDirectory;
    }

    public void setUserDataDirectory(String userDataDirectory) {
        this.userDataDirectory = userDataDirectory;
    }

    public int getInitialHeight() {
        return initialHeight;
    }

    public void setInitialHeight(int initialHeight) {
        this.initialHeight = initialHeight;
    }

    public int getInitialWidth() {
        return initialWidth;
    }

    public void setInitialWidth(int initialWidth) {
        this.initialWidth = initialWidth;
    }

    public UnaryOperator<TextFormatter.Change> getDoubleFilter() {
        return doubleFilter;
    }

}
