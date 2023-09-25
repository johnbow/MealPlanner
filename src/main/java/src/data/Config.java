package src.data;

import javafx.scene.control.TextFormatter;
import net.harawata.appdirs.AppDirs;
import net.harawata.appdirs.AppDirsFactory;

import java.util.function.UnaryOperator;

public class Config {

    public static final boolean DO_CLEAN_INSTALL = true;

    public static final String APPLICATION_NAME = "MealPlanner";
    public static final String AUTHOR_NAME = "Klein";
    public static final String CONFIG_FILE = "config.json";
    public static final String DATABASE_FILE = "food.db";
    public static final String RECIPE_FOLDER = "recipes/";

    public static final UnaryOperator<TextFormatter.Change> INT_FILTER_2_PLACES = change -> {
        String newText = change.getControlNewText();
        if (newText.matches("\\d{0,2}")) {
            return change;
        }
        return null;
    };
    public static final UnaryOperator<TextFormatter.Change> DOUBLE_FILTER = change -> {
        String newText = change.getControlNewText();
        if (newText.matches("([0-9]*(\\.[0-9]{0,6})?)?")) {
            return change;
        }
        return null;
    };

    private int initialHeight = 600;
    private int initialWidth = 800;
    private String dataDirectory = "";
    private String configDirectory;
    private JSONLoader jsonLoader;

    private Config() {}

    public static Config loadConfig() {
        Config config = new Config();

        AppDirs appDirs = AppDirsFactory.getInstance();
        config.setConfigDirectory(appDirs.getUserConfigDir(APPLICATION_NAME, CONFIG_FILE, AUTHOR_NAME));

        if (config.getDataDirectory().isBlank()) {
            String userDataDir = appDirs.getUserDataDir(APPLICATION_NAME, "", AUTHOR_NAME);
            config.setDataDirectory(userDataDir);
        }
        config.setJsonLoader(new JSONLoader(config.getDataDirectory(), config.getConfigDirectory()));
        return config;
    }

    public String getConfigDirectory() {
        return configDirectory;
    }

    public void setConfigDirectory(String configDirectory) {
        this.configDirectory = configDirectory;
    }

    public JSONLoader getJsonLoader() {
        return jsonLoader;
    }

    public void setJsonLoader(JSONLoader jsonLoader) {
        this.jsonLoader = jsonLoader;
    }

    public String getDataDirectory() {
        return dataDirectory;
    }

    public void setDataDirectory(String dataDirectory) {
        this.dataDirectory = dataDirectory;
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

}
