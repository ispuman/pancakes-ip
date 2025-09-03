package org.pancakelab.model.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.pancakelab.dto.PancakeDTO;
import org.pancakelab.model.client.Disciple;
import org.pancakelab.model.order.Order;
import org.pancakelab.repository.PancakeOrderRepository;
import org.pancakelab.repository.impl.PancakeOrderRepositoryImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ChefTest {

    private final PancakeOrderRepository pancakeOrderRepository = PancakeOrderRepositoryImpl.getInstance();

    @Test
    public void GivenDiscipleCompletesAnOrder_WhenChefPreparesIt_ANewOrderIsReadyForDelivery() {
        // setup
        Disciple disciple = new Disciple("Maria", 5, 5);
        disciple.createOrder(2, 3);
        disciple.addPancake(new PancakeDTO("sweet", List.of("blueberries")));
        disciple.completeOrder();
        Order order = pancakeOrderRepository.getDiscipleOrder(disciple);

        Chef chef = Chef.getInstance();
        assertFalse(pancakeOrderRepository.listPreparedOrders().contains(order.getId()));
        // exercise
        chef.prepareOrder(disciple);
        // verify
        assertTrue(pancakeOrderRepository.listPreparedOrders().contains(order.getId()));
        // tear down
        pancakeOrderRepository.removePreparedPancakeOrder(disciple);
        pancakeOrderRepository.removeCustomerOrder(disciple);
    }
}
