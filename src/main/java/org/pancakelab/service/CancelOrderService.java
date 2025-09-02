package org.pancakelab.service;

import org.pancakelab.model.client.PancakeShopCustomer;

@FunctionalInterface
public interface CancelOrderService {
    boolean cancelOrder(PancakeShopCustomer customer);
}
