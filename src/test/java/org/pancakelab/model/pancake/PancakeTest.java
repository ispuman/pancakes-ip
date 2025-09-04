package org.pancakelab.model.pancake;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.pancakelab.dto.PancakeDTO;
import org.pancakelab.factory.PancakeFactory;
import org.pancakelab.factory.PancakeFactoryImpl;
import org.pancakelab.model.client.Disciple;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.pancakelab.factory.PancakeFactoryImpl.INVALID_PANCAKE_TYPE;
import static org.pancakelab.model.pancake.Pancake.PANCAKE_INGREDIENT_TYPO;
import static org.pancakelab.model.pancake.Pancake.UNAVAILABLE_INGREDIENT_FOR_PANCAKE_TYPE;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PancakeTest {

    @Test
    public void GivenOrderingPancake_WhenInvalidTypeIsRequested_TheAppropriateErrorMessageIsDisplayed() {
        // setup
        Disciple disciple = new Disciple("Tom", 2, 3);
        disciple.createOrder(2, 3);
        // exercise
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> disciple.addPancake(new PancakeDTO("sweat", List.of("honey", "bananas"))));
        // verify
        assertEquals(INVALID_PANCAKE_TYPE, ex.getMessage());
        // tear down
    }

    @Test
    public void GivenOrderingPancake_WhenAddingInvalidIngredient_TheAppropriateErrorMessageIsDisplayed() {
        // setup
        Pancake pancake = new Pancake(new SaltyPancake());
        pancake.addIngredient("cheese");
        // exercise
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> pancake.addIngredient("hazelnut"));
        // verify
        assertEquals(PANCAKE_INGREDIENT_TYPO.formatted(Ingredient.VALUES), ex.getMessage());
        // tear down
    }

    @Test
    public void GivenOrderingPancake_WhenAddingValidButUnsupportedIngredient_TheAppropriateErrorMessageIsDisplayed() {
        // setup
        Pancake pancake = new Pancake(new VegetarianPancake());
        pancake.addIngredient(Ingredient.BANANAS);
        // exercise
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> pancake.addIngredient(Ingredient.MEAT));
        // verify
        assertEquals(UNAVAILABLE_INGREDIENT_FOR_PANCAKE_TYPE.formatted(pancake.getAvailableIngredients(), pancake.getType()),
                exception.getMessage());
        // tear down
    }

    @Test
    public void GivenOrderingPancake_WhenAddingValidButUnsupportedIngredientAsString_TheAppropriateErrorMessageIsDisplayed() {
        // setup
        PancakeFactory pancakeFactory = new PancakeFactoryImpl();
        Pancake pancake = pancakeFactory.createPancake("sweet");
        pancake.addIngredient("honey");
        // exercise
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> pancake.addIngredient("cheese"));
        // verify
        assertEquals(UNAVAILABLE_INGREDIENT_FOR_PANCAKE_TYPE.formatted(pancake.getAvailableIngredients(), pancake.getType()),
                ex.getMessage());
        // tear down
    }
}
