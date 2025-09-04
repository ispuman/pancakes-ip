package org.pancakelab.model.pancake;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

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

    public static final List<String> VALUES = Arrays.stream(Ingredient.values())
            .map(Enum::toString).map(String::toLowerCase).toList();

    public EnumSet<Ingredient> getAllIngredients() {
        return EnumSet.allOf(Ingredient.class);
    }
}
