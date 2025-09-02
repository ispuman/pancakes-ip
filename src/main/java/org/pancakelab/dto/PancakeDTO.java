package org.pancakelab.dto;

import org.pancakelab.factory.PancakeFactoryImpl;
import org.pancakelab.mapper.PancakeMapper;
import org.pancakelab.model.pancake.Pancake;

import java.util.List;
import java.util.Objects;

public record PancakeDTO(String category, List<String> ingredients) {

    private static final PancakeMapper pancakeMapper = new PancakeMapper(new PancakeFactoryImpl());
    public static final String INGREDIENTS_CANNOT_BE_EMPTY = "A pancake must have at least one ingredient.";

    public PancakeDTO {
        Objects.requireNonNull(category, "Category cannot be null.");
        Objects.requireNonNull(ingredients, "Ingredients cannot be null.");
        if (ingredients.isEmpty()) {
            throw new IllegalArgumentException(INGREDIENTS_CANNOT_BE_EMPTY);
        }
    }

    public Pancake toDO() {
        return pancakeMapper.toDO(this);
    }

    public String description() {
        return "%s pancake with %s".formatted(category, String.join(", ", ingredients()));
    }
}
