package org.pancakelab.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.pancakelab.dto.PancakeDTO;
import org.pancakelab.model.client.Disciple;
import org.pancakelab.repository.PancakeOrderRepository;
import org.pancakelab.repository.impl.PancakeOrderRepositoryImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CancelOrderServiceImplTest {

    private final PancakeOrderRepository pancakeOrderRepository = PancakeOrderRepositoryImpl.getInstance();

    @Test
    public void GivenDiscipleCreatesAnOrder_WhenHeCancelsIt_TheOrderIsRemovedFromTheDB_() {
        // setup
        Disciple disciple = new Disciple("Steven", 2, 7);
        disciple.createOrder(2, 7);
        disciple.addPancake(new PancakeDTO("salty", List.of("cheese", "walnuts")));

        assertEquals(1, pancakeOrderRepository.listAllOrders().size());
        // exercise
        boolean cancellation = disciple.cancelOrder();
        // verify
        assertTrue(cancellation);
        assertTrue(pancakeOrderRepository.listAllOrders().isEmpty());
        assertNull(pancakeOrderRepository.getDiscipleOrder(disciple));
        // tear down
    }
}
