package org.pancakelab.model.pancake;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class Pancake {

    private final PancakeRecipe pancakeRecipe;
    private final String type;

    private static final String POSTFIX = "Pancake";

    public Pancake(PancakeRecipe recipe) {
        this.pancakeRecipe = recipe;
        this.type = recipe.getClass().getSimpleName().replaceFirst(POSTFIX, "").toLowerCase();
    }

    public void addIngredient(Ingredient ingredient) {
        pancakeRecipe.addIngredient(ingredient);
    }

    public void removeIngredient(Ingredient ingredient) {
        pancakeRecipe.removeIngredient(ingredient);
    }

    public void addIngredient(String ingredient) {
        if (validateIngredient(ingredient)) {
            pancakeRecipe.addIngredient(Ingredient.valueOf(ingredient));
        }
    }

    public void removeIngredient(String ingredient) {
        if (validateIngredient(ingredient)) {
            pancakeRecipe.removeIngredient(Ingredient.valueOf(ingredient));
        }
    }

    private boolean validateIngredient(String ingredient) {
        Objects.requireNonNull(ingredient, "Ingredient cannot be null.");
        if (ingredient.trim().isBlank()) {
            throw new IllegalArgumentException("Ingredient cannot be blank.");
        }
        if (pancakeRecipe.availableIngredients().stream().map(Enum::toString).noneMatch(ingredient::equalsIgnoreCase)) {
            throw new IllegalArgumentException("Ingredient cannot be of unknown or unavailable value for the pancake type.");
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
