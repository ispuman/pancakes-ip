package org.pancakelab.model.pancake;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.pancakelab.factory.PancakeFactory;
import org.pancakelab.factory.PancakeFactoryImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.pancakelab.model.pancake.Pancake.UNAVAILABLE_INGREDIENT_FOR_PANCAKE_TYPE;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PancakeTest {

    @Test
    public void GivenOrderingPancake_WhenAddingValidButUnsupportedIngredient_TheAppropriateErrorMessageIsDisplayed() {
        // setup
        Pancake pancake = new Pancake(new VegetarianPancake());
        pancake.addIngredient(Ingredient.BANANAS);
        // exercise
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> pancake.addIngredient(Ingredient.MEAT));
        // verify
        assertEquals(UNAVAILABLE_INGREDIENT_FOR_PANCAKE_TYPE, exception.getMessage() );
        // tear down
    }

    @Test
    public void GivenOrderingPancake_WhenAddingValidButUnsupportedIngredientAsString_TheAppropriateErrorMessageIsDisplayed() {
        // setup
        PancakeFactory pancakeFactory = new PancakeFactoryImpl();
        Pancake pancake = pancakeFactory.createPancake("vegetarian");
        pancake.addIngredient("bananas");
        // exercise
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> pancake.addIngredient("meat"));
        // verify
        assertEquals(UNAVAILABLE_INGREDIENT_FOR_PANCAKE_TYPE, ex.getMessage() );
        // tear down
    }
}
