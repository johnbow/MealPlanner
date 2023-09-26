package src.data;

import java.io.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JSONLoader {

    public static final String EXTENSION = ".json";

    private String dataDir;
    private String configDir;
    private final Gson gson;

    public JSONLoader(String dataDir, String configDir) {
        this.dataDir = dataDir;
        this.configDir = configDir;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    // config should be able to read without specifying a dataDir:
    protected JSONLoader(String configDir) {
        this(null, configDir);
    }


    public boolean write(Recipe recipe) {
        if (dataDir == null) return false;
        String path = String.join("", dataDir, Config.RECIPE_FOLDER, recipe.info().filename(), EXTENSION);
        File jsonFile = new File(path);
        if (jsonFile.isFile())
            return false;
        return writeObjectToFile(jsonFile, recipe);
    }

    public boolean write(Config config) {
        String path = String.join("", configDir, Config.CONFIG_FILE, EXTENSION);
        File file = new File(path);
        return writeObjectToFile(file, config);
    }

    private boolean writeObjectToFile(File file, Object object) {
        if(file.getParentFile().mkdirs())
            System.out.println("Created " + file.getParentFile().getName());
        try (FileWriter writer = new FileWriter(file, false)) {
            writer.write(gson.toJson(object));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Config readConfig() {
        File file = new File(configDir + Config.CONFIG_FILE + EXTENSION);
        if (!file.isFile())
            return null;
        try {
            return gson.fromJson(new FileReader(file), Config.class);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    public Recipe readRecipe(String filename) {
        return null;
    }

    public void setDataDir(String dir) {
        this.dataDir = dir;
    }

    public void setConfigDir(String dir) {
        this.configDir = dir;
    }

}
