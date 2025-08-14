package org.pancakelab.model.pancakes;

import java.util.EnumSet;
import java.util.List;

public final class SweetPancake extends AbstractPancake {

    private static final EnumSet<Ingredient> AVAILABLE_INGREDIENTS = EnumSet.complementOf(EnumSet.of(
            Ingredient.CHEESE, Ingredient.YELLOW_CHEESE, Ingredient.PARMESAN,
            Ingredient.DARK_CHOCOLATE, Ingredient.MEAT));

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
