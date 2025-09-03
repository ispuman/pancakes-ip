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

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DeliverOrderServiceImplTest {

    OrderClientFactory factory = new OrderClientFactoryImpl();
    private final PancakeClientFacade pancakeClientService = factory.createPancakeClientFacade();

    OrderServiceFactory serviceFactory = new OrderServiceFactoryImpl();
    private final PancakeServiceFacade pancakeService = serviceFactory.createPancakeServiceFacade();

    private final PancakeOrderRepository pancakeOrderRepository = PancakeOrderRepositoryImpl.getInstance();

    private final PancakeFactory pancakeFactory = new PancakeFactoryImpl();
    private final PancakeMapper pancakeMapper = new PancakeMapper(pancakeFactory);

    @Test
    public void GivenOrderExists_WhenDeliveringOrder_ThenCorrectOrderReturnedAndOrderRemovedFromTheDatabase_Test() {
        // setup
        Disciple disciple = new Disciple("John", 1, 1);
        OrderDTO orderDTO = addPancakes(disciple);
        pancakeClientService.completeOrder(disciple);
        pancakeService.prepareOrder(disciple);

        // exercise
        UUID orderId = pancakeOrderRepository.getDiscipleOrder(disciple).getId();
        Map<Pancake, Integer> deliveredPancakes = pancakeService.deliverOrder(disciple);

        // verify
        Set<UUID> completedOrders = pancakeOrderRepository.listCompletedOrders();
        assertFalse(completedOrders.contains(orderId));

        Set<UUID> preparedOrders = pancakeOrderRepository.listPreparedOrders();
        assertFalse(preparedOrders.contains(orderId));

        assertEquals(7, deliveredPancakes.values().stream().mapToInt(Integer::intValue).sum());
        assertEquals(List.of("salty pancake with %s, %s".formatted(Ingredient.CHEESE.toString(), Ingredient.WALNUTS.toString()),
                        "sweet pancake with %s, %s".formatted(Ingredient.HONEY.toString(), Ingredient.WALNUTS.toString()),
                        "sweet pancake with %s, %s".formatted(Ingredient.MILK_CHOCOLATE.toString(), Ingredient.HAZELNUTS.toString()),
                        "sweet pancake with %s, %s".formatted(Ingredient.FRUIT_JAM.toString(), Ingredient.STRAWBERRIES.toString()),
                        "vegetarian pancake with %s, %s".formatted(Ingredient.HAZELNUTS.toString(), Ingredient.YELLOW_CHEESE.toString()),
                        "meat pancake with %s, %s".formatted(Ingredient.PARMESAN.toString(), Ingredient.MEAT.toString())),
                deliveredPancakes.keySet().stream().map(Pancake::description).collect(toList()));

        // tear down
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
