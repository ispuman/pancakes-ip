package org.pancakelab.service;

import org.pancakelab.model.order.Order;
import org.pancakelab.model.pancake.Pancake;
import org.pancakelab.model.client.PancakeShopCustomer;

public interface CreateOrderService {
    Order createOrder(int building, int room, PancakeShopCustomer customer);

    void addPancake(Pancake pancake);
    void removePancake(Pancake pancake);
}
