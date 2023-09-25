package src.data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JSONLoader {

    private static final String EXTENSION = ".json";
    private final String dataDir;
    private final String configDir;
    private final Gson gson;

    public JSONLoader(String dataDir, String configDir) {
        this.dataDir = dataDir;
        this.configDir = configDir;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }


    public boolean write(Recipe recipe) {
        String path = String.join("", dataDir, Config.RECIPE_FOLDER, recipe.info().filename(), EXTENSION);
        File jsonFile = new File(path);
        if (jsonFile.isFile())
            return false;
        if(jsonFile.getParentFile().mkdirs())
            System.out.println("Created " + dataDir + Config.RECIPE_FOLDER);
        try (FileWriter writer = new FileWriter(jsonFile)) {
            writer.write(gson.toJson(recipe));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
