package org.pancakelab.service.impl;

import org.pancakelab.model.order.DeliveryAddress;
import org.pancakelab.model.order.Order;
import org.pancakelab.model.order.OrderStatus;
import org.pancakelab.model.pancakes.Pancake;
import org.pancakelab.repository.PancakeOrderRepository;
import org.pancakelab.service.CompleteOrderService;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.logging.Level;

public class CompleteOrderServiceImpl implements CompleteOrderService {

    private final PancakeOrderRepository pancakeOrderRepository;

    private static final Logger logger = Logger.getLogger(CompleteOrderServiceImpl.class.getName());

    public CompleteOrderServiceImpl(PancakeOrderRepository pancakeOrderRepository) {
        this.pancakeOrderRepository = pancakeOrderRepository;
    }

    @Override
    public void completeOrder(UUID orderId) {
        Order order = pancakeOrderRepository.removePendingPancakeOrder(orderId);

        if (order != null) {
            order.setOrderStatus(OrderStatus.COMPLETED);
            pancakeOrderRepository.saveCompletedPancakeOrder(orderId, order);
            logCompleteOrder(order, order.getPancakeItems());
        } else {
            String errorMessage = "Order %s not found and cannot be prepared by Chef.".formatted(orderId);
            logger.log(Level.SEVERE, errorMessage);
            throw new PancakeOrderNotFoundInDB(errorMessage);
        }
    }

    public Set<UUID> listCompletedOrders() {
        return pancakeOrderRepository.listCompletedOrders();
    }

    private void logCompleteOrder(Order order, Map<Pancake, Integer> pancakes) {
        int pancakesForPreparation = pancakes.values().stream().mapToInt(Integer::intValue).sum();
        DeliveryAddress address = order.getDeliveryAddress();

        logger.log(Level.INFO, "Order %s with %d pancakes for building %d, room %d can be prepared by the Chef."
                .formatted(order.getId(), pancakesForPreparation, address.buildingNumber(), address.roomNumber()));
    }
}
