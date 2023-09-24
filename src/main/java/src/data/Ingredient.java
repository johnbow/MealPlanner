package src.data;

public record Ingredient (
        String name,
        Measure measure,
        double measureSize,
        double caloriesPerMeasureSize,
        int macroTypeInfo,
        double carbs,
        double fat,
        double protein
) {

    public static final int MACROS_NOT_SPECIFIED = 0;
    public static final int MACROS_IN_ABSOLUTE_UNITS = 1;
    public static final int MACROS_IN_PERCENT = 2;

    public Ingredient(String name, Measure measure, double measureSize, double caloriesPerMeasureSize) {
        this(name, measure, measureSize, caloriesPerMeasureSize, MACROS_NOT_SPECIFIED, 0.0, 0.0, 0.0);
    }

    public double calories(double quantity, Measure otherMeasure) {
        return caloriesPerMeasureSize / measureSize * quantity;
    }
}
