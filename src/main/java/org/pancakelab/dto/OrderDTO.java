package org.pancakelab.dto;

import java.util.Map;
import java.util.Objects;

import static org.pancakelab.model.order.DeliveryAddress.ERROR_ADDRESS_NUMBERS_SHOULD_BE_POSITIVE;

public record OrderDTO(int building, int room, Map<PancakeDTO, Integer> orderItems) {
    public OrderDTO {
        if (building <=0 || room <= 0) {
            throw new IllegalArgumentException(ERROR_ADDRESS_NUMBERS_SHOULD_BE_POSITIVE);
        }
        Objects.requireNonNull(orderItems, "Order items cannot be null.");
    }
}
