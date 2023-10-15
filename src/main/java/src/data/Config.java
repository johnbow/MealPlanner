package src.data;

import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import net.harawata.appdirs.AppDirs;
import net.harawata.appdirs.AppDirsFactory;
import src.gui.ResourceLoader;

import java.io.File;
import java.time.DayOfWeek;
import java.util.Locale;
import java.util.Objects;
import java.util.function.UnaryOperator;

public final class Config {

    public static final boolean DO_CLEAN_INSTALL = false;

    public static final double IMAGE_WIDTH = 120;
    public static final double IMAGE_HEIGHT = 80;

    public static final String APPLICATION_NAME = "MealPlanner";
    public static final String AUTHOR_NAME = "Klein";
    public static final String CONFIG_FILE = "config";
    public static final String DATABASE_FILE = "food.db";
    public static final String RECIPE_FOLDER = "recipes/";
    public static final String WEEK_FOLDER = "weeks/";
    public static final String IMAGE_FOLDER = "images/";

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

    private static Image dragAndDropImage;

    // configurable:
    private int initialHeight = 600;
    private int initialWidth = 800;
    private int queryResultsLimit = 20;
    private String dataDirectory = "";
    private Locale language = Locale.ENGLISH;
    private DayOfWeek weekStart = DayOfWeek.MONDAY;

    private transient String configDirectory;
    private transient JSONLoader jsonLoader;

    Config() {}

    public static Config loadConfig() {
        dragAndDropImage = ResourceLoader.loadImage("dragAndDrop.png", 100, 100, true, true);
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
        config.prepareImageFolder();
        json.setDataDir(config.getDataDirectory());
        config.setJsonLoader(json);
        return config;
    }

    private static void deleteConfigFile(String configDir) {
        File configFile = new File(configDir + CONFIG_FILE + JSONLoader.EXTENSION);
        if (configFile.isFile() && configFile.delete())
            System.out.printf("Deleted %s.\n", configFile.getName());
    }

    public void save() {
        getJsonLoader().write(this);
    }

    public Locale getLanguage() {
        return language;
    }

    public void setLanguage(Locale language) {
        this.language = language;
    }

    public DayOfWeek getWeekStart() {
        return weekStart;
    }

    public void setWeekStart(DayOfWeek weekStart) {
        this.weekStart = weekStart;
    }

    public String getConfigDirectory() {
        return configDirectory;
    }

    public void setConfigDirectory(String configDirectory) {
        this.configDirectory = configDirectory;
    }

    public int getQueryResultsLimit() {
        return queryResultsLimit;
    }

    public void setQueryResultsLimit(int queryResultsLimit) {
        this.queryResultsLimit = queryResultsLimit;
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
        prepareImageFolder();
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

    public static Image getDragAndDropImage() {
        return dragAndDropImage;
    }

    public boolean prepareImageFolder() {
        File dir = new File(dataDirectory + IMAGE_FOLDER);
        if (dir.mkdirs()) {
            System.out.println("Created " + dir.getName() + " folder.");
            return true;
        }
        return false;
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
