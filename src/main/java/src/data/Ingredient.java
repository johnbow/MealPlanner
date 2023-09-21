package src.data;

public class Ingredient {

    private String name;
    private Measure measure;
    private double quantity;

    public Ingredient(String name, Measure measure, double quantity) {
        this.name = name;
        this.measure = measure;
        this.quantity = quantity;
    }

    public Ingredient() {
        this("", null, 0.0);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Measure getMeasure() {
        return measure;
    }

    public void setMeasure(Measure measure) {
        this.measure = measure;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return getName() + "(Portion size = " + String.valueOf(quantity) + " " + measure.getNameByQuantity(quantity) + ")";
    }
}
