package org.pancakelab.service;

import java.util.UUID;

@FunctionalInterface
public interface CompleteOrderService {
    void completeOrder(UUID orderId);
}
