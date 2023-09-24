package src.data;

public class Ingredient {

    private final String name;
    private final Measure measure;
    private final double measureSize;
    private final double caloriesPerMeasureSize;

    // optional values:
    private final boolean macrosInPercent;    // whether
    private final double carbs, fat, protein;

    public Ingredient(String name, Measure measure, double measureSize, double caloriesPerMeasureSize) {
        this.name = name;
        this.measure = measure;
        this.measureSize = measureSize;
        this.caloriesPerMeasureSize = caloriesPerMeasureSize;

        this.macrosInPercent = false;
        this.carbs = 0.0;
        this.fat = 0.0;
        this.protein = 0.0;
    }

    public String getName() {
        return name;
    }

    public Measure getMeasure() {
        return measure;
    }

    public double getMeasureSize() {
        return measureSize;
    }

    public double getCaloriesPerMeasureSize() {
        return caloriesPerMeasureSize;
    }

    public double getCalories(double quantity, Measure otherMeasure) {
        return caloriesPerMeasureSize / measureSize * quantity;
    }

    @Override
    public String toString() {
        return getName() + "(PortionSize=" + String.valueOf(measureSize)
                + measure.getNameByQuantity(measureSize) + ", calories=" + caloriesPerMeasureSize + "kcal)";
    }
}
