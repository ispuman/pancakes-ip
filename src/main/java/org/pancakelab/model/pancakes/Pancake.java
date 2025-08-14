package org.pancakelab.model.pancakes;

import java.util.List;
import java.util.Objects;

public class Pancake {

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

    public String getType() {
        return type;
    }

    public String description() {
        return pancakeRecipe.description();
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
