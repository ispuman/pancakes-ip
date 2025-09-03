package org.pancakelab.service;

import org.pancakelab.model.user.PancakeShopCustomer;

@FunctionalInterface
public interface CancelOrderService {
    boolean cancelOrder(PancakeShopCustomer customer);
}
