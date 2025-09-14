package org.pancakelab.service.impl;

import org.pancakelab.mapper.OrderMapper;
import org.pancakelab.model.order.DeliveryAddress;
import org.pancakelab.model.order.Order;
import org.pancakelab.model.order.OrderStatus;
import org.pancakelab.model.pancake.Pancake;
import org.pancakelab.model.client.Disciple;
import org.pancakelab.model.user.PancakeShopCustomer;
import org.pancakelab.repository.PancakeOrderRepository;
import org.pancakelab.service.CreateOrderService;

import java.util.EnumSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CreateOrderServiceImpl implements CreateOrderService {

    private final PancakeOrderRepository pancakeOrderRepository;
    private final OrderMapper orderMapper;

    private static final Logger logger = Logger.getLogger(CreateOrderServiceImpl.class.getName());
    private final EnumSet<OrderStatus> canUpdatePancakesStates = EnumSet.of(OrderStatus.PENDING,
            OrderStatus.ADDING_PANECAKES, OrderStatus.REMOVING_PANECAKES);

    private Order order;
    private final Lock lock = new ReentrantLock();

    public CreateOrderServiceImpl(PancakeOrderRepository pancakeOrderRepository, OrderMapper orderMapper) {
        this.pancakeOrderRepository = pancakeOrderRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    public Order createOrder(int building, int room, PancakeShopCustomer customer) {
        try {
            if (lock.tryLock(10, TimeUnit.SECONDS)) {
                if (customer instanceof Disciple disciple) {
                    if (!newPancakeOrderCannotBeCreatedYet(disciple)) {
                        order = new Order(building, room, customer, orderMapper);
                        pancakeOrderRepository.savePendingPancakeOrder(order, disciple);
                        logCreateOrder(order);
                        return order;
                    }
                } else {
                    throw new IllegalArgumentException("Customer type not supported.");
                }
            }
            return null;
        } catch (InterruptedException ex) {
            logger.log(Level.SEVERE, "Thread interrupted while waiting for lock.");
            Thread.currentThread().interrupt();
            throw new RuntimeException(ex.getMessage(), ex.getCause());
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void addPancake(Pancake pancake) {
        if (canUpdatePancakesStates.contains(order.getStatus())) {
            order.addPancake(pancake);
            logAddRemovePancake(order, pancake, "Added");
        }
    }

    @Override
    public void removePancake(Pancake pancake) {
        if (canUpdatePancakesStates.contains(order.getStatus()) && order.getPancakeItems().containsKey(pancake)) {
            order.removePancake(pancake);
            logAddRemovePancake(order, pancake, "Removed");
        }
    }

    private void logCreateOrder(Order order) {
        DeliveryAddress address = order.getDeliveryAddress();

        logger.log(Level.INFO, "Order %s for building %d, room %d is created."
                .formatted(order.getId(), address.buildingNumber(), address.roomNumber()));
    }

    private boolean newPancakeOrderCannotBeCreatedYet(Disciple disciple) {
        Order previousOrder = pancakeOrderRepository.getDiscipleOrder(disciple);
        if (previousOrder == null) {
            return false;
        }
        String errorMessage = "New pancake order cannot be created until existing one %s is cancelled or delivered."
                .formatted(previousOrder.getId());
        logger.log(Level.SEVERE, errorMessage);
        throw new NewPancakeOrderCannotBeCreatedYet(errorMessage);
    }

    private void logAddRemovePancake(Order order, Pancake pancake, String action) {
        int pancakesCount = order.getPancakeItems().values().stream().mapToInt(Integer::intValue).sum();
        DeliveryAddress address = order.getDeliveryAddress();

        logger.log(Level.INFO, "%s pancake with description '%s' to order %s containing %d pancakes, for building %d, room %d."
                .formatted(action, pancake.description(), order.getId(), pancakesCount, address.buildingNumber(), address.roomNumber()));
    }
}
