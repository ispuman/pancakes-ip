package org.pancakelab.model.pancake;

import java.util.EnumSet;

public sealed interface PancakeRecipe permits AbstractPancake,
        MeatPancake, SaltyPancake, SweetPancake, VegetarianPancake {

    void addIngredient(Ingredient ingredient);
    void removeIngredient(Ingredient ingredient);

    EnumSet<Ingredient> ingredients();
    EnumSet<Ingredient> availableIngredients();

    default String description() {
        String pancakeType = switch (this) {
            case SweetPancake sweetPancake -> getPancakeType(sweetPancake);
            case SaltyPancake saltyPancake -> getPancakeType(saltyPancake);
            case VegetarianPancake vegetarianPancake -> getPancakeType(vegetarianPancake);
            case MeatPancake meatPancake -> getPancakeType(meatPancake);
        };
        return "%s pancake with %s".formatted(pancakeType, String.join(", ",
                ingredients().stream().map(Enum::name).toList()));
    }

    private String getPancakeType(PancakeRecipe pancakeRecipe) {
        return pancakeRecipe.getClass().getSimpleName().replaceFirst("Pancake", "").toLowerCase();
    }
}
