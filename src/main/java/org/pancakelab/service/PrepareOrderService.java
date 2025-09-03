package org.pancakelab.service;

import org.pancakelab.model.user.PancakeShopCustomer;

@FunctionalInterface
public interface PrepareOrderService {
    void prepareOrder(PancakeShopCustomer customer);
}
