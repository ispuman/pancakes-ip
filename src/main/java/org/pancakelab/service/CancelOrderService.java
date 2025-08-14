package org.pancakelab.service;

import java.util.UUID;

@FunctionalInterface
public interface CancelOrderService {
    void cancelOrder(UUID orderId);
}
