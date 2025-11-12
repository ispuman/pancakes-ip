package org.pancakelab.repository.impl;

import org.pancakelab.model.order.Order;
import org.pancakelab.model.client.Disciple;
import org.pancakelab.model.order.OrderStatus;
import org.pancakelab.repository.PancakeOrderRepository;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public final class PancakeOrderRepositoryImpl implements PancakeOrderRepository {

    private final Map<UUID, Order> pendingPancakeOrders = new ConcurrentHashMap<>();
    private final Map<UUID, Order> completedPancakeOrders = new ConcurrentHashMap<>();
    private final Map<UUID, Order> preparedPancakeOrders = new ConcurrentHashMap<>();
    private final Map<Disciple, Order> discipleOrders = new ConcurrentHashMap<>();

    private static final AtomicBoolean instanceCreated = new AtomicBoolean(false);

    private PancakeOrderRepositoryImpl() {
        if (instanceCreated.getAndSet(true)) {
            throw new IllegalStateException("The singleton repository is already instantiated.");
        }
    }

    private static class BillPughSingletonHelper {
        private static final PancakeOrderRepositoryImpl INSTANCE = new PancakeOrderRepositoryImpl();
    }

    public static PancakeOrderRepository getInstance() {
        return BillPughSingletonHelper.INSTANCE;
    }

    private Object readResolve() {
        return getInstance();
    }

    @Override
    public void savePendingPancakeOrder(Order order, Disciple customer) {
        pendingPancakeOrders.put(order.getId(), order);
        discipleOrders.put(customer, order);
    }

    @Override
    public Order removePendingPancakeOrder(Disciple customer) {
        UUID orderId = discipleOrders.get(customer).getId();
        Order order = pendingPancakeOrders.remove(orderId);
        if (!OrderStatus.COMPLETED.equals(order.getStatus())) {
            discipleOrders.remove(customer);
        }
        return order;
    }

    @Override
    public Order getPendingPancakeOrder(UUID orderId) {
        return pendingPancakeOrders.get(orderId);
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
    public Order removeCompletedPancakeOrder(Disciple customer) {
        UUID orderId = discipleOrders.get(customer).getId();
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
    public Order removePreparedPancakeOrder(Disciple customer) {
        UUID orderId = discipleOrders.get(customer).getId();
        discipleOrders.remove(customer);
        return preparedPancakeOrders.remove(orderId);
    }

    @Override
    public Order getDiscipleOrder(Disciple customer) {
        return discipleOrders.get(customer);
    }

    @Override
    public List<Order> listAllOrders() {
        return discipleOrders.values().stream().toList();
    }

    @Override
    public Order removeCustomerOrder(Disciple customer) {
        return discipleOrders.remove(customer);
    }
}
