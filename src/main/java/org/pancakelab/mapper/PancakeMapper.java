package org.pancakelab.mapper;

import org.pancakelab.dto.PancakeDTO;
import org.pancakelab.factory.PancakeFactory;
import org.pancakelab.model.pancake.*;

import java.util.List;
import java.util.Objects;

import static org.pancakelab.dto.PancakeDTO.INGREDIENTS_CANNOT_BE_EMPTY;

public class PancakeMapper implements Mapper<Pancake, PancakeDTO> {

    private final PancakeFactory pancakeFactory;

    public PancakeMapper(PancakeFactory pancakeFactory) {
        this.pancakeFactory = pancakeFactory;
    }

    @Override
    public PancakeDTO toDTO(Pancake source) {
        Objects.requireNonNull(source, "Pancake cannot be null.");
        if (source.getIngredients().isEmpty()) {
            throw new IllegalArgumentException(INGREDIENTS_CANNOT_BE_EMPTY);
        }
        return new PancakeDTO(source.getType(), List.copyOf(source.getIngredients()));
    }

    public Pancake toDO(PancakeDTO source) {
        Objects.requireNonNull(source, "PancakeDTO cannot be null.");

        Pancake pancake = pancakeFactory.createPancake(source.category());
        for (String ingredient : source.ingredients()) {
            pancake.addIngredient(Ingredient.valueOf(ingredient));
        }
        return pancake;
    }
}
