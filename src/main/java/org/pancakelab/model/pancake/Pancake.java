package org.pancakelab.model.pancake;

import java.util.List;
import java.util.Objects;

public final class Pancake {

    private final PancakeRecipe pancakeRecipe;
    private final String type;
    private final List<String> availableIngredients;

    private static final String POSTFIX = "Pancake";
    public static final String UNAVAILABLE_INGREDIENT_FOR_PANCAKE_TYPE =
            "Please choose from the available ingredients %s for the pancake type %s.";
    public static final String PANCAKE_INGREDIENT_TYPO =
            "Please choose a correct value from the available ingredients: %s".formatted(Ingredient.VALUES);

    public Pancake(PancakeRecipe recipe) {
        this.pancakeRecipe = Objects.requireNonNull(recipe, "A pancake cannot be null.");
        this.type = recipe.getClass().getSimpleName().replaceFirst(POSTFIX, "").toLowerCase();
        this.availableIngredients = pancakeRecipe.availableIngredients().stream()
                .map(Enum::toString).map(String::toLowerCase).toList();
    }

    public void addIngredient(Ingredient ingredient) {
        if (pancakeRecipe.availableIngredients().contains(ingredient)) {
            if (!pancakeRecipe.ingredients().contains(ingredient)) {
                pancakeRecipe.addIngredient(ingredient);
            }
        } else {
            throw new IllegalArgumentException(UNAVAILABLE_INGREDIENT_FOR_PANCAKE_TYPE.formatted(availableIngredients, type));
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
        Objects.requireNonNull(ingredient, "A pancake ingredient cannot be null.");
        if (ingredient.trim().isBlank()) {
            throw new IllegalArgumentException("A pancake ingredient cannot be blank.");
        }
        if (!Ingredient.VALUES.contains(ingredient.toLowerCase())) {
            throw new IllegalArgumentException(PANCAKE_INGREDIENT_TYPO.formatted(Ingredient.VALUES));
        }
        if (!pancakeRecipe.availableIngredients().contains(Ingredient.valueOf(ingredient.toUpperCase()))) {
            throw new IllegalArgumentException(UNAVAILABLE_INGREDIENT_FOR_PANCAKE_TYPE.formatted(availableIngredients, type));
        }
        return true;
    }

    public String getType() {
        return type;
    }

    public List<String> getAvailableIngredients() {
        return List.copyOf(availableIngredients);
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
