package src.data;

import java.util.Arrays;
import java.util.List;

public class Database {

    private List<Measure> measures = Arrays.asList(
            new Measure("Gram", "Grams"),
            new Measure("Liter", "Liters")
    );

    public List<Measure> getMeasures() {
        return measures;
    }

    public void addIngredient(Ingredient ingredient) {
        System.out.printf("Added %s to database.\n", ingredient.toString());
        // TODO: implement
    }

}
