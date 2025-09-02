package org.pancakelab.service;

import org.pancakelab.model.client.PancakeShopCustomer;

@FunctionalInterface
public interface CompleteOrderService {
    boolean completeOrder(PancakeShopCustomer customer);
}
