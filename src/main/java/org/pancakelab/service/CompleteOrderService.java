package org.pancakelab.service;

import org.pancakelab.model.user.PancakeShopCustomer;

@FunctionalInterface
public interface CompleteOrderService {
    boolean completeOrder(PancakeShopCustomer customer);
}
