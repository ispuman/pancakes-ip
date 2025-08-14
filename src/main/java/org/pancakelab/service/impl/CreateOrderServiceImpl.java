package org.pancakelab.service.impl;

import org.pancakelab.model.order.DeliveryAddress;
import org.pancakelab.model.order.Order;
import org.pancakelab.model.pancakes.Pancake;
import org.pancakelab.repository.PancakeOrderRepository;
import org.pancakelab.service.CreateOrderService;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CreateOrderServiceImpl implements CreateOrderService {

    private final PancakeOrderRepository pancakeOrderRepository;

    private static final Logger logger = Logger.getLogger(CreateOrderServiceImpl.class.getName());

    private Order order;

    public CreateOrderServiceImpl(PancakeOrderRepository pancakeOrderRepository) {
        this.pancakeOrderRepository = pancakeOrderRepository;
    }

    @Override
    public UUID createOrder(int building, int room) {
        order = new Order(building, room);
        pancakeOrderRepository.savePendingPancakeOrder(order.getId(), order);
        logCreateOrder(order);
        return order.getId();
    }

    @Override
    public void addPancake(Pancake pancake) {
        order.addPancake(pancake);
        logAddRemovePancake(order, pancake, "Added");
    }

    @Override
    public void removePancakes(Pancake pancake) {
        if (order.getPancakeItems().containsKey(pancake)) {
            order.removePancake(pancake);
            logAddRemovePancake(order, pancake, "Removed");
        }
    }

    private void logCreateOrder(Order order) {
        DeliveryAddress address = order.getDeliveryAddress();

        logger.log(Level.INFO, "Order %s for building %d, room %d is created."
                .formatted(order.getId(), address.buildingNumber(), address.roomNumber()));
    }

    private void logAddRemovePancake(Order order, Pancake pancake, String action) {
        int pancakesCount = order.getPancakeItems().values().stream().mapToInt(Integer::intValue).sum();
        DeliveryAddress address = order.getDeliveryAddress();

        logger.log(Level.INFO, "%s pancake with description '%s' to order %s containing %d pancakes, for building %d, room %d."
                .formatted(action, pancake.description(), order.getId(), pancakesCount, address.buildingNumber(), address.roomNumber()));
    }
}
