package src.data;

import javafx.scene.control.TextFormatter;
import net.harawata.appdirs.AppDirs;
import net.harawata.appdirs.AppDirsFactory;

import java.io.File;
import java.util.Objects;
import java.util.function.UnaryOperator;

public final class Config {

    public static final boolean DO_CLEAN_INSTALL = true;

    public static final String APPLICATION_NAME = "MealPlanner";
    public static final String AUTHOR_NAME = "Klein";
    public static final String CONFIG_FILE = "config";
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

    // configurable:
    private int initialHeight = 600;
    private int initialWidth = 800;
    private String dataDirectory = "";

    private transient String configDirectory;
    private transient JSONLoader jsonLoader;

    Config() {}

    public static Config loadConfig() {
        AppDirs appDirs = AppDirsFactory.getInstance();
        String configDir = appDirs.getUserConfigDir(APPLICATION_NAME, "", AUTHOR_NAME);
        JSONLoader json = new JSONLoader(configDir);

        if (DO_CLEAN_INSTALL)
            deleteConfigFile(configDir);

        Config config = json.readConfig();
        if (config == null) {
            System.out.println("Using default config.");
            config = new Config();
        }
        config.setConfigDirectory(configDir);

        if (config.getDataDirectory().isBlank()) {
            String userDataDir = appDirs.getUserDataDir(APPLICATION_NAME, "", AUTHOR_NAME);
            config.setDataDirectory(userDataDir);
        }
        json.setDataDir(config.getDataDirectory());
        config.setJsonLoader(json);
        return config;
    }

    private static void deleteConfigFile(String configDir) {
        File configFile = new File(configDir + CONFIG_FILE + JSONLoader.EXTENSION);
        System.out.println(configFile.isFile());
        if (configFile.isFile() && configFile.delete())
            System.out.printf("Deleted %s.\n", configFile.getName());
    }

    public void save() {
        getJsonLoader().write(this);
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

    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (!(other instanceof Config otherConfig)) return false;
        return initialHeight == otherConfig.getInitialHeight() && initialWidth == otherConfig.getInitialWidth()
                && getDataDirectory().equals(otherConfig.getDataDirectory())
                && getConfigDirectory().equals(otherConfig.getConfigDirectory());
    }

    @Override
    public int hashCode() {
        return Objects.hash(initialHeight, initialWidth, dataDirectory, configDirectory);
    }

}
