package org.pancakelab.model.pancake;

import java.util.EnumSet;

public enum Ingredient {
    DARK_CHOCOLATE,
    MILK_CHOCOLATE,
    HAZELNUTS,
    WHIPPED_CREAM,
    HONEY,
    CHEESE,
    PARMESAN,
    WALNUTS,
    YELLOW_CHEESE,
    CHOCOLATE_CHIP,
    FRUIT_JAM,
    BANANAS,
    STRAWBERRIES,
    BLUEBERRIES,
    MEAT;

    public static final Ingredient[] VALUES = Ingredient.values();

    public EnumSet<Ingredient> getAllIngredients() {
        return EnumSet.allOf(Ingredient.class);
    }
}
