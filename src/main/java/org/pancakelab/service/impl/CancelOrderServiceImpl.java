package org.pancakelab.service.impl;

import org.pancakelab.model.order.DeliveryAddress;
import org.pancakelab.model.order.Order;
import org.pancakelab.model.order.OrderStatus;
import org.pancakelab.model.client.Disciple;
import org.pancakelab.model.user.PancakeShopCustomer;
import org.pancakelab.repository.PancakeOrderRepository;
import org.pancakelab.service.CancelOrderService;

import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CancelOrderServiceImpl implements CancelOrderService {

    private final PancakeOrderRepository pancakeOrderRepository;

    private static final Logger logger = Logger.getLogger(CancelOrderServiceImpl.class.getName());

    public CancelOrderServiceImpl(PancakeOrderRepository pancakeOrderRepository) {
        this.pancakeOrderRepository = Objects.requireNonNull(pancakeOrderRepository,  "PancakeOrderRepository cannot be null");
    }

    @Override
    public boolean cancelOrder(PancakeShopCustomer customer) {
        if (customer instanceof Disciple disciple) {
            Order order = pancakeOrderRepository.getDiscipleOrder(disciple);
            Objects.requireNonNull(order, "%s Order for cancellation cannot be found.".formatted(disciple.toString()));
            UUID orderId = order.getId();

            if (isActionAuthorized(orderId, customer)) {
                pancakeOrderRepository.removePendingPancakeOrder(disciple);
                order.setOrderStatus(OrderStatus.CANCELLED);
                logCancelOrder(order);
                return true;
            } else {
                logger.log(Level.SEVERE, " Unauthorized cancellation attempt of Order %s by %s.".formatted(orderId, disciple.toString()));
                return false;
            }
        } else {
            throw new IllegalArgumentException("Customer type not supported.");
        }
    }

    private boolean isActionAuthorized(UUID orderId, PancakeShopCustomer customer) {
        Order pendingOrder = pancakeOrderRepository.getPendingPancakeOrder(orderId);
        if (pendingOrder != null) {
            return pendingOrder.isCancellationAuthorized(customer);
        }
        throw pancakeOrderNotFoundInDB(orderId);
    }

    private void logCancelOrder(Order order) {
        int pancakesToCancel = order.getPancakeItems().values().stream().mapToInt(Integer::intValue).sum();
        DeliveryAddress address = order.getDeliveryAddress();

        logger.log(Level.INFO, "Cancelled order %s with %d pancakes for building %d, room %d."
                .formatted(order.getId(), pancakesToCancel, address.buildingNumber(), address.roomNumber()));
    }

    private PancakeOrderNotFoundInDB pancakeOrderNotFoundInDB(UUID orderId) {
        String errorMessage = "Order %s not found and cannot be cancelled.".formatted(orderId);
        logger.log(Level.SEVERE, errorMessage);
        return new PancakeOrderNotFoundInDB(errorMessage);
    }
}
