package org.pancakelab.model.pancakes;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

public sealed abstract class AbstractPancake implements PancakeRecipe, PancakeMenu permits MeatPancake, SaltyPancake, SweetPancake, VegetarianPancake {

    protected final EnumSet<Ingredient> ingredients = EnumSet.noneOf(Ingredient.class);

    public abstract EnumSet<Ingredient> availableIngredients();

    public abstract List<String> getMenuItemIngredients(String menuItemName);


    @Override
    public void addIngredient(Ingredient ingredient) {
        if (availableIngredients().contains(ingredient)) {
            ingredients.add(ingredient);
        }
    }

    @Override
    public void removeIngredient(Ingredient ingredient) {
        ingredients.remove(ingredient);
    }

    @Override
    public EnumSet<Ingredient> ingredients() {
        return EnumSet.copyOf(ingredients);
    }

    @Override
    public List<String> getMenu() {
        return Arrays.stream(AbstractPancake.class.getPermittedSubclasses())
                .map(Class::getSimpleName).toList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractPancake pancake = (AbstractPancake) o;
        if (ingredients.size() != pancake.ingredients.size()) return false;
        return Objects.equals(ingredients, pancake.ingredients);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(ingredients);
    }
}
