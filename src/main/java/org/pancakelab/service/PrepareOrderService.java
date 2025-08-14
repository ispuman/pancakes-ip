package org.pancakelab.service;

import java.util.UUID;

@FunctionalInterface
public interface PrepareOrderService {
    void prepareOrder(UUID orderId);
}
