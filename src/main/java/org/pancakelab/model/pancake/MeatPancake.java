package org.pancakelab.model.pancake;

import java.util.EnumSet;
import java.util.List;

public final class MeatPancake extends AbstractPancake implements PancakeRecipe {

    private static final EnumSet<Ingredient> AVAILABLE_INGREDIENTS = EnumSet.of(
            Ingredient.MEAT, Ingredient.HAZELNUTS, Ingredient.WALNUTS,
            Ingredient.CHEESE, Ingredient.YELLOW_CHEESE, Ingredient.PARMESAN);

    private static final List<String> AVAILABLE_INGREDIENTS_LIST = AVAILABLE_INGREDIENTS.stream()
            .map(Enum::toString).toList();

    public MeatPancake() {
        addIngredient(Ingredient.MEAT);
    }

    @Override
    public void removeIngredient(Ingredient ingredient) {
        if (ingredient != Ingredient.MEAT) {
            removeIngredient(ingredient);
        }
    }

    @Override
    public EnumSet<Ingredient> availableIngredients() {
        return AVAILABLE_INGREDIENTS;
    }

    @Override
    public List<String> getMenuItemIngredients(String menuItemName) {
        if (this.getClass().getSimpleName().equals(menuItemName)) {
            return AVAILABLE_INGREDIENTS_LIST;
        }
        return List.of();
    }
}
