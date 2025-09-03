package org.pancakelab.model.pancake;

import java.util.List;
import java.util.Objects;

public final class Pancake {

    private final PancakeRecipe pancakeRecipe;
    private final String type;

    private static final String POSTFIX = "Pancake";
    public static final String UNAVAILABLE_INGREDIENT_FOR_PANCAKE_TYPE =
            "Ingredient cannot be of unavailable value for the pancake type.";

    public Pancake(PancakeRecipe recipe) {
        this.pancakeRecipe = recipe;
        this.type = recipe.getClass().getSimpleName().replaceFirst(POSTFIX, "").toLowerCase();
    }

    public void addIngredient(Ingredient ingredient) {
        if (pancakeRecipe.availableIngredients().contains(ingredient)) {
            if (!pancakeRecipe.ingredients().contains(ingredient)) {
                pancakeRecipe.addIngredient(ingredient);
            }
        } else {
            throw new IllegalArgumentException(UNAVAILABLE_INGREDIENT_FOR_PANCAKE_TYPE);
        }
    }

    public void removeIngredient(Ingredient ingredient) {
        pancakeRecipe.removeIngredient(ingredient);
    }

    public void addIngredient(String ingredient) {
        if (validateIngredient(ingredient)) {
            pancakeRecipe.addIngredient(Ingredient.valueOf(ingredient.toUpperCase()));
        }
    }

    public void removeIngredient(String ingredient) {
        if (validateIngredient(ingredient)) {
            pancakeRecipe.removeIngredient(Ingredient.valueOf(ingredient.toUpperCase()));
        }
    }

    private boolean validateIngredient(String ingredient) {
        Objects.requireNonNull(ingredient, "Ingredient cannot be null.");
        if (ingredient.trim().isBlank()) {
            throw new IllegalArgumentException("Ingredient cannot be blank.");
        }
        if (!pancakeRecipe.availableIngredients().contains(Ingredient.valueOf(ingredient.toUpperCase()))) {
            throw new IllegalArgumentException(UNAVAILABLE_INGREDIENT_FOR_PANCAKE_TYPE);
        }
        return true;
    }

    public String getType() {
        return type;
    }

    public String description() {
        return pancakeRecipe.description();
    }

    public List<String> getIngredients() {
        return pancakeRecipe.ingredients().stream().map(Enum::toString).toList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pancake pancake = (Pancake) o;
        if (pancakeRecipe.ingredients().size() != pancake.pancakeRecipe.ingredients().size()) return false;
        return Objects.equals(pancakeRecipe.ingredients(), pancake.pancakeRecipe.ingredients());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(pancakeRecipe.ingredients());
    }
}
