package src.data;

public record Measure(String singularName, String pluralName, String abbreviation, double defaultQuantity) {

    public enum Number {
        SINGULAR, PLURAL
    };

    public String getName(Measure.Number number) {
        return number == Number.SINGULAR ? singularName() : pluralName();
    }

    public String getNameByQuantity(double quantity) {
        return Math.abs(quantity - 1.0) < 0.0001 ?
                singularName() : pluralName();
    }

    @Override
    public String toString() {
        return String.format("Measure(%s, %s, %s, %f)",
                singularName(), pluralName(), abbreviation(), defaultQuantity());
    }
}
