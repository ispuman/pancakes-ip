package org.pancakelab.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.pancakelab.dto.OrderDTO;
import org.pancakelab.factory.OrderClientFactory;
import org.pancakelab.factory.OrderClientFactoryImpl;
import org.pancakelab.factory.PancakeFactory;
import org.pancakelab.factory.PancakeFactoryImpl;
import org.pancakelab.mapper.PancakeMapper;
import org.pancakelab.model.client.Disciple;
import org.pancakelab.model.order.Order;
import org.pancakelab.model.pancake.Ingredient;
import org.pancakelab.model.pancake.Pancake;
import org.pancakelab.repository.PancakeOrderRepository;
import org.pancakelab.repository.impl.PancakeOrderRepositoryImpl;
import org.pancakelab.service.PancakeClientFacade;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CompleteOrderServiceImplTest {

    OrderClientFactory factory = new OrderClientFactoryImpl();
    private final PancakeClientFacade pancakeClientService = factory.createPancakeClientFacade();

    private final PancakeOrderRepository pancakeOrderRepository = PancakeOrderRepositoryImpl.getInstance();

    private final PancakeFactory pancakeFactory = new PancakeFactoryImpl();
    private final PancakeMapper pancakeMapper = new PancakeMapper(pancakeFactory);

    @Test
    @org.junit.jupiter.api.Order(20)
    public void GivenOrderExists_WhenAddingPancakes_ThenCorrectNumberOfPancakesAdded_Test() {
        // setup
        Disciple disciple = new Disciple("John", 1, 1);
        OrderDTO orderDTO = addPancakes(disciple);

        // exercise
        boolean found = pancakeClientService.completeOrder(disciple);

        // verify
        Order order = pancakeOrderRepository.removeCompletedPancakeOrder(disciple);

        assertTrue(found);
        assertEquals(7, order.getPancakeItems().values().stream().mapToInt(Integer::intValue).sum());
        assertEquals(List.of("salty pancake with %s, %s".formatted(Ingredient.CHEESE.toString(), Ingredient.WALNUTS.toString()),
                        "sweet pancake with %s, %s".formatted(Ingredient.HONEY.toString(), Ingredient.WALNUTS.toString()),
                        "sweet pancake with %s, %s".formatted(Ingredient.MILK_CHOCOLATE.toString(), Ingredient.HAZELNUTS.toString()),
                        "sweet pancake with %s, %s".formatted(Ingredient.FRUIT_JAM.toString(), Ingredient.STRAWBERRIES.toString()),
                        "vegetarian pancake with %s, %s".formatted(Ingredient.HAZELNUTS.toString(), Ingredient.YELLOW_CHEESE.toString()),
                        "meat pancake with %s, %s".formatted(Ingredient.PARMESAN.toString(), Ingredient.MEAT.toString())),
                order.getPancakeItems().keySet().stream().map(Pancake::description).collect(toList()));

        // tear down
        pancakeOrderRepository.removeCustomerOrder(disciple);
    }

    @Test
    @org.junit.jupiter.api.Order(40)
    public void GivenOrderExists_WhenCompletingOrder_ThenOrderCompleted_Test() {
        // setup
        Disciple disciple = new Disciple("John", 1, 1);
        OrderDTO orderDTO = addPancakes(disciple);

        // exercise
        pancakeClientService.completeOrder(disciple);

        // verify
        UUID orderId = pancakeOrderRepository.getDiscipleOrder(disciple).getId();
        Set<UUID> completedOrders = pancakeOrderRepository.listCompletedOrders();
        assertTrue(completedOrders.contains(orderId));
        assertEquals(1, completedOrders.size());
        // tear down
        pancakeOrderRepository.removeCompletedPancakeOrder(disciple);
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
