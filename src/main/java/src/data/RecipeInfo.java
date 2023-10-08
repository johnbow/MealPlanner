package src.data;

public record RecipeInfo(
    String name,
    String filename,
    int servings,
    double total_calories   // in x servings
) {

    public RecipeInfo(String name, int servings, double total_calories) {
        this(name, generateFilename(name), servings, total_calories);
    }

    public double caloriesPerServing() {
        return total_calories / servings;
    }

    public RecipeInfo changeServings(int newServings) {
        return new RecipeInfo(name, filename, newServings, (total_calories / servings) * newServings);
    }

    public static String generateFilename(String name) {
        return name.replaceAll("\\s+", "");
    }

}
