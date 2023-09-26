package src.data;

import java.util.List;

public record Recipe (
        RecipeInfo info,
        String description,
        List<QuantityIngredient> ingredients
) {}
