package org.pancakelab.service.impl;

import org.pancakelab.model.order.DeliveryAddress;
import org.pancakelab.model.order.Order;
import org.pancakelab.model.order.OrderStatus;
import org.pancakelab.repository.PancakeOrderRepository;
import org.pancakelab.service.PrepareOrderService;

import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PrepareOrderServiceImpl implements PrepareOrderService {

    private final PancakeOrderRepository pancakeOrderRepository;

    private static final Logger logger = Logger.getLogger(PrepareOrderServiceImpl.class.getName());

    public PrepareOrderServiceImpl(PancakeOrderRepository pancakeOrderRepository) {
        this.pancakeOrderRepository = pancakeOrderRepository;
    }

    @Override
    public void prepareOrder(UUID orderId) {
        Order order = pancakeOrderRepository.removeCompletedPancakeOrder(orderId);

        if (order != null) {
            order.setOrderStatus(OrderStatus.PREPARED);
            pancakeOrderRepository.savePreparedPancakeOrder(orderId, order);
            logPrepareOrder(order);
        } else {
            String errorMessage = "Order %s not found and cannot be prepared by Chef.".formatted(orderId);
            logger.log(Level.SEVERE, errorMessage);
            throw new PancakeOrderNotFoundInDB(errorMessage);
        }

    }

    public Set<UUID> listPreparedOrders() {
        return pancakeOrderRepository.listPreparedOrders();
    }

    private void logPrepareOrder(Order order) {
        int pancakesPrepared = order.getPancakeItems().values().stream().mapToInt(Integer::intValue).sum();
        DeliveryAddress address = order.getDeliveryAddress();

        logger.log(Level.INFO, "Order %s with %d pancakes for building %d, room %d is prepared by Chef."
                .formatted(order.getId(), pancakesPrepared, address.buildingNumber(), address.roomNumber()));
    }
}
