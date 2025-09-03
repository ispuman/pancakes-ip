package org.pancakelab.service.impl;

import org.pancakelab.mapper.OrderMapper;
import org.pancakelab.model.order.DeliveryAddress;
import org.pancakelab.model.order.Order;
import org.pancakelab.model.pancake.Pancake;
import org.pancakelab.model.client.Disciple;
import org.pancakelab.model.user.PancakeShopCustomer;
import org.pancakelab.repository.PancakeOrderRepository;
import org.pancakelab.service.CreateOrderService;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CreateOrderServiceImpl implements CreateOrderService {

    private final PancakeOrderRepository pancakeOrderRepository;
    private final OrderMapper orderMapper;

    private static final Logger logger = Logger.getLogger(CreateOrderServiceImpl.class.getName());

    private Order order;

    public CreateOrderServiceImpl(PancakeOrderRepository pancakeOrderRepository, OrderMapper orderMapper) {
        this.pancakeOrderRepository = pancakeOrderRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    public Order createOrder(int building, int room, PancakeShopCustomer customer) {
        if (customer instanceof Disciple disciple) {
            Order previousOrder = pancakeOrderRepository.getDiscipleOrder(disciple);
            if (previousOrder == null) {
                order = new Order(building, room, customer, orderMapper);
                pancakeOrderRepository.savePendingPancakeOrder(order, disciple);
                logCreateOrder(order);
                return order;
            } else {
                throw newPancakeOrderCannotBeCreatedYet(previousOrder.getId());
            }
        } else {
            throw new IllegalArgumentException("Customer type not supported.");
        }
    }

    @Override
    public void addPancake(Pancake pancake) {
        order.addPancake(pancake);
        logAddRemovePancake(order, pancake, "Added");
    }

    @Override
    public void removePancake(Pancake pancake) {
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

    private NewPancakeOrderCannotBeCreatedYet newPancakeOrderCannotBeCreatedYet(UUID orderId) {
        String errorMessage =
                "New pancake order cannot be created until existing one %s is cancelled or delivered.".formatted(orderId);
        logger.log(Level.SEVERE, errorMessage);
        return new NewPancakeOrderCannotBeCreatedYet(errorMessage);
    }

    private void logAddRemovePancake(Order order, Pancake pancake, String action) {
        int pancakesCount = order.getPancakeItems().values().stream().mapToInt(Integer::intValue).sum();
        DeliveryAddress address = order.getDeliveryAddress();

        logger.log(Level.INFO, "%s pancake with description '%s' to order %s containing %d pancakes, for building %d, room %d."
                .formatted(action, pancake.description(), order.getId(), pancakesCount, address.buildingNumber(), address.roomNumber()));
    }
}
