package org.pancakelab.repository.impl;

import org.pancakelab.model.order.Order;
import org.pancakelab.repository.PancakeOrderRepository;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PancakeOrderRepositoryImpl implements PancakeOrderRepository {

    private final Map<UUID, Order> pendingPancakeOrders = new ConcurrentHashMap<>();
    private final Map<UUID, Order> completedPancakeOrders = new ConcurrentHashMap<>();
    private final Map<UUID, Order> preparedPancakeOrders = new ConcurrentHashMap<>();

    private PancakeOrderRepositoryImpl() {}

    private static class BillPughSingletonHelper {
        private static final PancakeOrderRepositoryImpl INSTANCE = new PancakeOrderRepositoryImpl();
    }

    public static PancakeOrderRepositoryImpl getInstance() {
        return BillPughSingletonHelper.INSTANCE;
    }

    protected Object readResolve() {
        return getInstance();
    }

    @Override
    public void savePendingPancakeOrder(UUID orderId, Order order) {
        pendingPancakeOrders.put(orderId, order);
    }

    @Override
    public Order removePendingPancakeOrder(UUID orderId) {
        return pendingPancakeOrders.remove(orderId);
    }

    @Override
    public void saveCompletedPancakeOrder(UUID orderId, Order order) {
        completedPancakeOrders.put(orderId, order);
    }

    @Override
    public Set<UUID> listCompletedOrders() {
        return completedPancakeOrders.keySet();
    }

    @Override
    public Order removeCompletedPancakeOrder(UUID orderId) {
        return completedPancakeOrders.remove(orderId);
    }

    @Override
    public void savePreparedPancakeOrder(UUID orderId, Order order) {
        preparedPancakeOrders.put(orderId, order);
    }

    @Override
    public Set<UUID> listPreparedOrders() {
        return preparedPancakeOrders.keySet();
    }

    @Override
    public Order removePreparedPancakeOrder(UUID orderId) {
        return preparedPancakeOrders.remove(orderId);
    }
}
