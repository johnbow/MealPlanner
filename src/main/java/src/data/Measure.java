package src.data;

public record Measure(
        String singularName,
        String pluralName,
        String abbreviation,
        double defaultQuantity,
        double conversion   // weight in grams or milliliters
) {
    public enum Number {
        SINGULAR, PLURAL, NOT_DEFINED
    }

    public String getName(Measure.Number number) {
        return number == Number.SINGULAR ? singularName() : pluralName();
    }

    public String getNameByQuantity(double quantity) {
        return Math.abs(quantity - 1.0) < 0.0001 ?
                singularName() : pluralName();
    }

    public double convertQuantity(Measure other, double quantity) {
        return quantity * (other.conversion / this.conversion);
    }

    public double convertQuantityDefault(double quantity) {
        return quantity * this.conversion;
    }

    @Override
    public String toString() {
        return String.format("Measure(%s, %s, %s, %f)",
                singularName(), pluralName(), abbreviation(), defaultQuantity());
    }
}
