package src.data;

public class Measure {

    public enum Number {
        SINGULAR, PLURAL
    };

    private final String singularName;
    private final String pluralName;
    private final String abbreviation;
    private final double defaultQuantity;

    public Measure(String singularName, String pluralName, String abbreviation, double defaultQuantity) {
        this.singularName = singularName;
        this.pluralName = pluralName;
        this.abbreviation = abbreviation;
        this.defaultQuantity = defaultQuantity;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public double getDefaultQuantity() {
        return defaultQuantity;
    }

    public String getSingularName() {
        return singularName;
    }

    public String getPluralName() {
        return pluralName;
    }

    public String getName(Measure.Number number) {
        return number == Number.SINGULAR ? getSingularName() : getPluralName();
    }

    public String getNameByQuantity(double quantity)  {
        return Math.abs(quantity - 1.0) < 0.0001 ?
                getSingularName() : getPluralName();
    }
}
