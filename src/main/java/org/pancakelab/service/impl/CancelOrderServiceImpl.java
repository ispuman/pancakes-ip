package org.pancakelab.service.impl;

import org.pancakelab.model.order.DeliveryAddress;
import org.pancakelab.model.order.Order;
import org.pancakelab.model.order.OrderStatus;
import org.pancakelab.repository.PancakeOrderRepository;
import org.pancakelab.service.CancelOrderService;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CancelOrderServiceImpl implements CancelOrderService {

    private final PancakeOrderRepository pancakeOrderRepository;

    private static final Logger logger = Logger.getLogger(CancelOrderServiceImpl.class.getName());

    public CancelOrderServiceImpl(PancakeOrderRepository pancakeOrderRepository) {
        this.pancakeOrderRepository = pancakeOrderRepository;
    }

    @Override
    public void cancelOrder(UUID orderId) {
        Order order = pancakeOrderRepository.removePendingPancakeOrder(orderId);

        if (order != null) {
            order.setOrderStatus(OrderStatus.CANCELLED);
            logCancelOrder(order);
        } else {
            String errorMessage = "Order %s not found and cannot be cancelled.".formatted(orderId);
            logger.log(Level.SEVERE, errorMessage);
            throw new PancakeOrderNotFoundInDB(errorMessage);
        }
    }

    private void logCancelOrder(Order order) {
        int pancakesToCancel = order.getPancakeItems().values().stream().mapToInt(Integer::intValue).sum();
        DeliveryAddress address = order.getDeliveryAddress();

        logger.log(Level.INFO, "Cancelled order %s with %d pancakes for building %d, room %d."
                .formatted(order.getId(), pancakesToCancel, address.buildingNumber(), address.roomNumber()));
    }
}
