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

    public double calories(Measure other, double quantity) {
        return (caloriesPerMeasureSize / measureSize) * measure.convertQuantity(other, quantity);
    }

    public double calories(double quantity) {
        return caloriesPerMeasureSize / measureSize * quantity;
    }

    public Ingredient changeMeasure(Measure newMeasure) {
        return new Ingredient(
                name,
                newMeasure,
                newMeasure.convertQuantity(measure, measureSize),
                caloriesPerMeasureSize
        );
    }
}
