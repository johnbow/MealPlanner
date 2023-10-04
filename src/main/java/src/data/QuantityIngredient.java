package src.data;

public record QuantityIngredient(
        double quantity,
        Ingredient ingredient
) {

    public double calories(Measure other) {
        return ingredient.calories(other, quantity);
    }

    public double calories() {
        return ingredient.calories(quantity);
    }
}
