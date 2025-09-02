package org.pancakelab.model.pancake;

import java.util.EnumSet;
import java.util.List;

public final class SaltyPancake extends AbstractPancake implements PancakeRecipe {

    private static final EnumSet<Ingredient> AVAILABLE_INGREDIENTS = EnumSet.of(
            Ingredient.CHEESE, Ingredient.YELLOW_CHEESE, Ingredient.PARMESAN,
            Ingredient.HAZELNUTS, Ingredient.WALNUTS, Ingredient.MEAT);

    private static final List<String> AVAILABLE_INGREDIENTS_LIST = AVAILABLE_INGREDIENTS.stream()
            .map(Enum::toString).toList();

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
