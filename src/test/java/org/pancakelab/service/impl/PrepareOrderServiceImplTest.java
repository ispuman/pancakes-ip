package org.pancakelab.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.pancakelab.dto.OrderDTO;
import org.pancakelab.factory.*;
import org.pancakelab.mapper.PancakeMapper;
import org.pancakelab.model.client.Disciple;
import org.pancakelab.model.pancake.Ingredient;
import org.pancakelab.model.pancake.Pancake;
import org.pancakelab.repository.PancakeOrderRepository;
import org.pancakelab.repository.impl.PancakeOrderRepositoryImpl;
import org.pancakelab.service.PancakeClientFacade;
import org.pancakelab.service.PancakeServiceFacade;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PrepareOrderServiceImplTest {

    OrderClientFactory factory = new OrderClientFactoryImpl();
    private final PancakeClientFacade pancakeClientService = factory.createPancakeClientFacade();

    OrderServiceFactory serviceFactory = new OrderServiceFactoryImpl();
    private final PancakeServiceFacade pancakeService = serviceFactory.createPancakeServiceFacade();

    private final PancakeOrderRepository pancakeOrderRepository = PancakeOrderRepositoryImpl.getInstance();

    private final PancakeFactory pancakeFactory = new PancakeFactoryImpl();
    private final PancakeMapper pancakeMapper = new PancakeMapper(pancakeFactory);

    @Test
    public void GivenOrderExists_WhenPreparingOrder_ThenOrderPrepared_Test() {
        // setup
        Disciple disciple = new Disciple("John", 1, 1);
        OrderDTO orderDTO = addPancakes(disciple);

        // exercise
        pancakeClientService.completeOrder(disciple);
        pancakeService.prepareOrder(disciple);

        // verify
        UUID orderId = pancakeOrderRepository.getDiscipleOrder(disciple).getId();
        Set<UUID> completedOrders = pancakeOrderRepository.listCompletedOrders();
        assertFalse(completedOrders.contains(orderId));

        Set<UUID> preparedOrders = pancakeOrderRepository.listPreparedOrders();
        assertTrue(preparedOrders.contains(orderId));

        // tear down
        pancakeOrderRepository.removePreparedPancakeOrder(disciple);
        pancakeOrderRepository.removeCustomerOrder(disciple);
    }

    private OrderDTO addPancakes(Disciple customer) {
        OrderDTO orderDTO = customer.createOrder(2, 3);

        for (int i = 0; i < 2; i++) {
            Pancake sap = pancakeFactory.createPancake("salty");
            sap.addIngredient(Ingredient.CHEESE);
            sap.addIngredient(Ingredient.WALNUTS);
            customer.addPancake(pancakeMapper.toDTO(sap));
        }

        Pancake swp1 = pancakeFactory.createPancake("sweet");
        swp1.addIngredient(Ingredient.HONEY);
        swp1.addIngredient(Ingredient.WALNUTS);
        customer.addPancake(pancakeMapper.toDTO(swp1));
        Pancake swp2 = pancakeFactory.createPancake("sweet");
        swp2.addIngredient(Ingredient.MILK_CHOCOLATE);
        swp2.addIngredient(Ingredient.HAZELNUTS);
        customer.addPancake(pancakeMapper.toDTO(swp2));
        Pancake swp3 = pancakeFactory.createPancake("sweet");
        swp3.addIngredient(Ingredient.FRUIT_JAM);
        swp3.addIngredient(Ingredient.STRAWBERRIES);
        customer.addPancake(pancakeMapper.toDTO(swp3));

        Pancake vp = pancakeFactory.createPancake("vegetarian");
        vp.addIngredient(Ingredient.YELLOW_CHEESE);
        vp.addIngredient(Ingredient.HAZELNUTS);
        customer.addPancake(pancakeMapper.toDTO(vp));

        Pancake mp = pancakeFactory.createPancake("meat");
        mp.addIngredient(Ingredient.MEAT);
        mp.addIngredient(Ingredient.PARMESAN);
        customer.addPancake(pancakeMapper.toDTO(mp));

        return orderDTO;
    }
}
