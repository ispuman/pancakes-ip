package org.pancakelab.repository;

import org.pancakelab.model.order.Order;
import org.pancakelab.model.client.Disciple;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface PancakeOrderRepository {
    void savePendingPancakeOrder(Order order, Disciple customer);
    Order removePendingPancakeOrder(Disciple customer);
    Order getPendingPancakeOrder(UUID orderId);

    void saveCompletedPancakeOrder(UUID orderId, Order order);
    Set<UUID> listCompletedOrders();
    Order removeCompletedPancakeOrder(Disciple customer);

    void savePreparedPancakeOrder(UUID orderId, Order order);
    Set<UUID> listPreparedOrders();
    Order removePreparedPancakeOrder(Disciple customer);

    Order getDiscipleOrder(Disciple customer);
    List<Order> listAllOrders();
    Order removeCustomerOrder(Disciple customer);
}
