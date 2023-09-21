package src.data;

public class Measure {

    public enum Number {
        SINGULAR, PLURAL
    };

    private String singularName;
    private String pluralName;

    public Measure(String singularName, String pluralName) {
        this.singularName = singularName;
        this.pluralName = pluralName;
    }

    public String getSingularName() {
        return singularName;
    }

    public void setSingularName(String singularName) {
        this.singularName = singularName;
    }

    public String getPluralName() {
        return pluralName;
    }

    public void setPluralName(String pluralName) {
        this.pluralName = pluralName;
    }

    public String getName(Measure.Number number) {
        return number == Number.SINGULAR ? getSingularName() : getPluralName();
    }

    public String getNameByQuantity(double quantity)  {
        return Math.abs(quantity - 1.0) < 0.0001 ?
                getSingularName() : getPluralName();
    }
}
