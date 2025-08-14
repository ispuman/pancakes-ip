package org.pancakelab.repository;

import org.pancakelab.model.order.Order;

import java.util.Set;
import java.util.UUID;

public interface PancakeOrderRepository {
    void savePendingPancakeOrder(UUID orderId, Order order);
    Order removePendingPancakeOrder(UUID orderId);

    void saveCompletedPancakeOrder(UUID orderId, Order order);
    Set<UUID> listCompletedOrders();
    Order removeCompletedPancakeOrder(UUID orderId);

    void savePreparedPancakeOrder(UUID orderId, Order order);
    Set<UUID> listPreparedOrders();
    Order removePreparedPancakeOrder(UUID orderId);
}
