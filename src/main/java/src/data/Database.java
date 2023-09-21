package src.data;

import java.util.Arrays;
import java.util.List;

public class Database {

    private List<Measure> measures = Arrays.asList(
            new Measure("Gram", "Grams", "g", 100.0),
            new Measure("Kilogram", "Kilograms", "kg", 1.0),
            new Measure("Liter", "Liters", "l", 1.0),
            new Measure("Milliliter", "Milliliters", "ml", 100.0)
    );

    public List<Measure> getMeasures() {
        return measures;
    }

    public void addIngredient(Ingredient ingredient) {
        System.out.printf("Added %s to database.\n", ingredient.toString());
        // TODO: implement
    }

}
