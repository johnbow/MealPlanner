package src.data;

public record RecipeInfo(
    String name,
    String filename,
    int servings,
    double total_calories
) {

    public RecipeInfo(String name, int servings, double total_calories) {
        this(name, generateFilename(name), servings, total_calories);
    }

    public static String generateFilename(String name) {
        return name.replaceAll("\\s+", "");
    }
}
