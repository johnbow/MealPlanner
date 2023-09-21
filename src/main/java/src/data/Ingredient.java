package src.data;

public class Ingredient {

    private final String name;
    private final Measure measure;
    private final double measureSize;
    private final double caloriesPerMeasureSize;
    private double quantity;    // quantity per measure

    public Ingredient(String name, Measure measure, double measureSize, double caloriesPerMeasureSize) {
        this.name = name;
        this.measure = measure;
        this.measureSize = measureSize;
        this.caloriesPerMeasureSize = caloriesPerMeasureSize;
        this.quantity = measureSize;
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

    public double getCalories() {
        return quantity * caloriesPerMeasureSize / measureSize;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return getName() + "(PortionSize=" + String.valueOf(measureSize)
                + measure.getNameByQuantity(measureSize) + ", calories=" + caloriesPerMeasureSize + "kcal)";
    }
}
