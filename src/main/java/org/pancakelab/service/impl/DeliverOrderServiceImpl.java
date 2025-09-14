package org.pancakelab.service.impl;

import org.pancakelab.model.order.DeliveryAddress;
import org.pancakelab.model.order.Order;
import org.pancakelab.model.order.OrderStatus;
import org.pancakelab.model.pancake.Pancake;
import org.pancakelab.model.client.Disciple;
import org.pancakelab.model.user.PancakeShopCustomer;
import org.pancakelab.repository.PancakeOrderRepository;
import org.pancakelab.service.DeliverOrderService;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DeliverOrderServiceImpl implements DeliverOrderService {

    private final PancakeOrderRepository pancakeOrderRepository;

    private static final Logger logger = Logger.getLogger(DeliverOrderServiceImpl.class.getName());

    public DeliverOrderServiceImpl(PancakeOrderRepository pancakeOrderRepository) {
        this.pancakeOrderRepository = pancakeOrderRepository;
    }

    @Override
    public Map<Pancake, Integer> deliverOrder(PancakeShopCustomer customer) {
        if (customer instanceof Disciple disciple) {
            Order order = pancakeOrderRepository.removePreparedPancakeOrder(disciple);
            if (order != null)  {
                order.setOrderStatus(OrderStatus.SEND_FOR_DELIVERY);
                var pancakes = order.getPancakeItems();
                logDeliverOrder(order, pancakes);
                pancakeOrderRepository.removeCustomerOrder(disciple);
                return pancakes;
            }
            String errorMessage = "Order for %s not found and cannot be delivered.".formatted(disciple.toString());
            logger.log(Level.SEVERE, errorMessage);
            throw new PancakeOrderNotFoundInDB(errorMessage);
        } else {
            throw new IllegalArgumentException("Customer type not supported.");
        }
    }

    private void logDeliverOrder(Order order, Map<Pancake, Integer> pancakes) {
        int pancakesInOrder = pancakes.values().stream().mapToInt(Integer::intValue).sum();
        DeliveryAddress address = order.getDeliveryAddress();

        logger.log(Level.INFO, "Order %s with %d pancakes for building %d, room %d out for delivery."
                .formatted(order.getId(), pancakesInOrder, address.buildingNumber(), address.roomNumber()));
    }
}
