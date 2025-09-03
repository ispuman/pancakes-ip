package org.pancakelab.service.impl;

import org.pancakelab.model.order.DeliveryAddress;
import org.pancakelab.model.order.Order;
import org.pancakelab.model.order.OrderStatus;
import org.pancakelab.model.pancake.Pancake;
import org.pancakelab.model.client.Disciple;
import org.pancakelab.model.user.PancakeShopCustomer;
import org.pancakelab.repository.PancakeOrderRepository;
import org.pancakelab.service.CompleteOrderService;

import java.util.Map;
import java.util.Objects;
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
    public boolean completeOrder(PancakeShopCustomer customer) {
        if (customer instanceof Disciple disciple) {
            Order order = pancakeOrderRepository.getDiscipleOrder(disciple);
            Objects.requireNonNull(order, "%s order for completion cannot be found.".formatted(disciple.toString()));
            UUID orderId = order.getId();

            if (isActionAuthorized(orderId, disciple)) {
                order.setOrderStatus(OrderStatus.COMPLETED);
                pancakeOrderRepository.saveCompletedPancakeOrder(orderId, order);
                logCompleteOrder(order, order.getPancakeItems());
                pancakeOrderRepository.removePendingPancakeOrder(disciple);
                return true;
            } else {
                logger.log(Level.SEVERE, " Unauthorized completion attempt of Order %s by %s.".formatted(orderId, disciple.toString()));
                return false;
            }
        } else {
            throw new IllegalArgumentException("Customer type not supported.");
        }
    }

    public Set<UUID> listCompletedOrders() {
        return pancakeOrderRepository.listCompletedOrders();
    }

    private boolean isActionAuthorized(UUID orderId, PancakeShopCustomer customer) {
        Order pendingOrder = pancakeOrderRepository.getPendingPancakeOrder(orderId);
        if (pendingOrder != null) {
            return pendingOrder.isCompletionAuthorized(customer);
        }
        throw pancakeOrderNotFoundInDB(orderId);
    }

    private void logCompleteOrder(Order order, Map<Pancake, Integer> pancakes) {
        int pancakesForPreparation = pancakes.values().stream().mapToInt(Integer::intValue).sum();
        DeliveryAddress address = order.getDeliveryAddress();

        logger.log(Level.INFO, "Order %s with %d pancakes for building %d, room %d can be prepared by the Chef."
                .formatted(order.getId(), pancakesForPreparation, address.buildingNumber(), address.roomNumber()));
    }

    private PancakeOrderNotFoundInDB pancakeOrderNotFoundInDB(UUID orderId) {
        String errorMessage = "Order %s not found and cannot be completed.".formatted(orderId);
        logger.log(Level.SEVERE, errorMessage);
        return new PancakeOrderNotFoundInDB(errorMessage);
    }
}
