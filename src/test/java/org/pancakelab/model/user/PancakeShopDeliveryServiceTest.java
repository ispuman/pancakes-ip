package org.pancakelab.model.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.pancakelab.dto.PancakeDTO;
import org.pancakelab.model.client.Disciple;
import org.pancakelab.model.order.Order;
import org.pancakelab.repository.PancakeOrderRepository;
import org.pancakelab.repository.impl.PancakeOrderRepositoryImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PancakeShopDeliveryServiceTest {

    private final PancakeOrderRepository pancakeOrderRepository = PancakeOrderRepositoryImpl.getInstance();

    @Test
    public void GivenChefPreparesAnOrder_WhenItIsDelivered_TheOrderIsRemovedFromTheDB_() {
        // setup
        Disciple disciple = new Disciple("Michael", 6, 8);
        disciple.createOrder(6, 8);
        disciple.addPancake(new PancakeDTO("meat", List.of("hazelnuts")));
        disciple.completeOrder();
        Order order = pancakeOrderRepository.getDiscipleOrder(disciple);

        Chef chef = Chef.getInstance();
        assertFalse(pancakeOrderRepository.listPreparedOrders().contains(order.getId()));
        chef.prepareOrder(disciple);
        assertTrue(pancakeOrderRepository.listPreparedOrders().contains(order.getId()));
        assertEquals(1, pancakeOrderRepository.listAllOrders().size());
        // exercise
        PancakeShopDeliveryService deliveryService = PancakeShopDeliveryService.getInstance();
        deliveryService.deliverOrder(disciple);
        // verify
        assertFalse(pancakeOrderRepository.listPreparedOrders().contains(order.getId()));
        assertTrue(pancakeOrderRepository.listAllOrders().isEmpty());
        // tear down
    }
}
