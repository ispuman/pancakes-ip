package org.pancakelab.model.order;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.pancakelab.model.client.Disciple;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.pancakelab.model.order.DeliveryAddress.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DeliveryAddressTest {

    @Test
    public void GivenNonPositiveAddress_WhenCreatingNewOrder_TheAppropriateErrorMessageIsDisplayed() {

        Disciple disciple = new Disciple("John", 1, 1);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> disciple.createOrder(12, 0));

        assertEquals(ERROR_ADDRESS_NUMBERS_SHOULD_BE_POSITIVE, exception.getMessage());
    }

    @Test
    public void GivenNonExistingRoomAddress_WhenCreatingNewOrder_TheAppropriateErrorMessageIsDisplayed() {

        Disciple disciple = new Disciple("John", 1, 1);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> disciple.createOrder(10, 30));

        assertEquals(ERROR_ROOM_NUMBER_TOO_LARGE, exception.getMessage());
    }

    @Test
    public void GivenNonExistingBuildingAddress_WhenCreatingNewOrder_TheAppropriateErrorMessageIsDisplayed() {

        Disciple disciple = new Disciple("John", 1, 1);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> disciple.createOrder(28, 10));

        assertEquals(ERROR_BUILDING_NUMBER_TOO_LARGE, exception.getMessage());
    }
}
