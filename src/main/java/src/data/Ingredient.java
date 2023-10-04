package src.data;

public record Ingredient (
        String name,
        String measureName,
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

    public Ingredient(String name, String measureName, double measureSize, double caloriesPerMeasureSize) {
        this(name, measureName, measureSize, caloriesPerMeasureSize, MACROS_NOT_SPECIFIED, 0.0, 0.0, 0.0);
    }

    public double calories(Measure other, double quantity) {
        return (caloriesPerMeasureSize / measureSize) * Database.getMeasure(measureName).convertQuantity(other, quantity);
    }

    public double calories(double quantity) {
        return caloriesPerMeasureSize / measureSize * quantity;
    }

    public Measure measure() {
        return Database.getMeasure(measureName);
    }

    public Ingredient changeMeasure(Measure newMeasure) {
        return new Ingredient(
                name,
                newMeasure.singularName(),
                newMeasure.convertQuantity(measure(), measureSize),
                caloriesPerMeasureSize
        );
    }
}
