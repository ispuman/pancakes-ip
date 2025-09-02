package org.pancakelab.model.client;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.pancakelab.dto.OrderDTO;
import org.pancakelab.dto.PancakeDTO;
import org.pancakelab.factory.PancakeFactory;
import org.pancakelab.factory.PancakeFactoryImpl;
import org.pancakelab.mapper.PancakeMapper;
import org.pancakelab.model.order.Order;
import org.pancakelab.model.pancake.Ingredient;
import org.pancakelab.model.pancake.MeatPancake;
import org.pancakelab.model.pancake.Pancake;
import org.pancakelab.model.pancake.SaltyPancake;
import org.pancakelab.repository.PancakeOrderRepository;
import org.pancakelab.repository.impl.PancakeOrderRepositoryImpl;
import org.pancakelab.service.impl.NewPancakeOrderCannotBeCreatedYet;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DiscipleTest {

    private final PancakeFactory pancakeFactory = new PancakeFactoryImpl();
    private final PancakeMapper pancakeMapper = new PancakeMapper(pancakeFactory);

    private final PancakeOrderRepository pancakeOrderRepository = PancakeOrderRepositoryImpl.getInstance();

    @Test
    @org.junit.jupiter.api.Order(10)
    public void GivenOrderDoesNotExist_WhenCreatingOrder_ThenOrderCreatedWithCorrectData_Test() {
        // setup
        Disciple disciple = new Disciple("John", 1, 1);

        // exercise
        OrderDTO orderDTO = disciple.createOrder(10, 20);

        // verify
        assertEquals(10, orderDTO.building());
        assertEquals(20, orderDTO.room());

        // tear down
        boolean result = disciple.cancelOrder();
        assertTrue(result);
    }

    @Test
    @org.junit.jupiter.api.Order(30)
    public void GivenPancakesExists_WhenRemovingPancakes_ThenCorrectNumberOfPancakesRemoved_Test() {
        // setup
        Disciple disciple = new Disciple("John", 1, 1);
        OrderDTO orderDTO = addPancakes(disciple);

        SaltyPancake saltyPancake = new SaltyPancake();
        Pancake sap = new Pancake(saltyPancake);
        sap.addIngredient(Ingredient.CHEESE);
        sap.addIngredient(Ingredient.WALNUTS);

        MeatPancake meatPancake = new MeatPancake();
        Pancake mp = new Pancake(meatPancake);
        mp.addIngredient(Ingredient.MEAT);
        mp.addIngredient(Ingredient.PARMESAN);

        // exercise
        disciple.removePancake(pancakeMapper.toDTO(sap));
        disciple.removePancake(pancakeMapper.toDTO(sap));
        disciple.removePancake(pancakeMapper.toDTO(mp));
        boolean completion = disciple.completeOrder();
        assertTrue(completion);

        OrderDTO result = pancakeOrderRepository.getDiscipleOrder(disciple).toDTO();
        // verify
        assertEquals(4, result.orderItems().values().stream().mapToInt(Integer::intValue).sum());
        assertEquals(List.of("sweet pancake with %s, %s".formatted(Ingredient.HONEY.toString(), Ingredient.WALNUTS.toString()),
                        "sweet pancake with %s, %s".formatted(Ingredient.MILK_CHOCOLATE.toString(), Ingredient.HAZELNUTS.toString()),
                        "sweet pancake with %s, %s".formatted(Ingredient.FRUIT_JAM.toString(), Ingredient.STRAWBERRIES.toString()),
                        "vegetarian pancake with %s, %s".formatted(Ingredient.HAZELNUTS.toString(), Ingredient.YELLOW_CHEESE.toString())),
                result.orderItems().keySet().stream().map(PancakeDTO::description).collect(toList()));

        // tear down
        pancakeOrderRepository.removeCompletedPancakeOrder(disciple);
        pancakeOrderRepository.removeCustomerOrder(disciple);
    }

    @Test
    @org.junit.jupiter.api.Order(70)
    public void GivenOrderExists_WhenCancellingOrder_ThenOrderAndPancakesRemoved_Test() {
        // setup
        Disciple disciple = new Disciple("John", 1, 1);
        OrderDTO orderDTO = addPancakes(disciple);
        UUID orderId = pancakeOrderRepository.getDiscipleOrder(disciple).getId();

        // exercise
        disciple.cancelOrder();

        // verify
        Order order = pancakeOrderRepository.getPendingPancakeOrder(orderId);
        assertNull(order);

        Set<UUID> completedOrders = pancakeOrderRepository.listCompletedOrders();
        assertFalse(completedOrders.contains(orderId));

        Set<UUID> preparedOrders = pancakeOrderRepository.listPreparedOrders();
        assertFalse(preparedOrders.contains(orderId));

        // tear down
    }

    @Test
    @org.junit.jupiter.api.Order(80)
    public void GivenOrderExists_WhenCompletingSecondOrder_ThenSumOfCompletedOrdersMustBeCorrect_Test() {
        // setup
        Disciple disciple = new Disciple("John", 1, 1);
        OrderDTO orderDTO = addPancakes(disciple);
        disciple.completeOrder();

        // exercise
        Disciple disciple2 = new Disciple("Henry", 3, 2);
        OrderDTO orderDTO2 = addPancakesForAnotherDisciple(disciple2);
        disciple2.completeOrder();

        // verify
        Set<UUID> completedOrders = pancakeOrderRepository.listCompletedOrders();
        assertEquals(2, completedOrders.size());
        assertEquals(7, orderDTO.orderItems().values().stream().mapToInt(Integer::intValue).sum());
        assertEquals(2, pancakeOrderRepository.listAllOrders().size());
        assertEquals(3, orderDTO2.orderItems().values().stream().mapToInt(Integer::intValue).sum());

        // tear down
        pancakeOrderRepository.removeCompletedPancakeOrder(disciple);
        pancakeOrderRepository.removeCompletedPancakeOrder(disciple2);
        pancakeOrderRepository.removeCustomerOrder(disciple);
        pancakeOrderRepository.removeCustomerOrder(disciple2);
    }

    @Test
    @org.junit.jupiter.api.Order(100)
    public void GivenOrderExists_NewOrderCannotBeCreated_UntilExistingOneIsCancelledOrDelivered_Test() {
        // setup
        Disciple disciple = new Disciple("Tom", 2, 5);
        OrderDTO orderDTO = addPancakes(disciple);
        boolean completion = disciple.completeOrder();
        assertTrue(completion);
        assertEquals(7, orderDTO.orderItems().values().stream().mapToInt(Integer::intValue).sum());

        // exercise
        NewPancakeOrderCannotBeCreatedYet exception = assertThrows(NewPancakeOrderCannotBeCreatedYet.class,
                () -> addPancakesForNewOrder(disciple));

        // verify
        assertTrue(exception.getMessage().contains("New pancake order cannot be created until existing one"));
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

        return pancakeOrderRepository.getDiscipleOrder(customer).toDTO();
    }

    private OrderDTO addPancakesForAnotherDisciple(Disciple customer) {
        OrderDTO orderDTO = customer.createOrder(4, 6);

        Pancake sap = pancakeFactory.createPancake("salty");
        sap.addIngredient(Ingredient.YELLOW_CHEESE);
        customer.addPancake(pancakeMapper.toDTO(sap));

        Pancake swp = pancakeFactory.createPancake("sweet");
        swp.addIngredient(Ingredient.CHOCOLATE_CHIP);
        swp.addIngredient(Ingredient.WHIPPED_CREAM);
        customer.addPancake(pancakeMapper.toDTO(swp));

        Pancake vg = pancakeFactory.createPancake("vegetarian");
        vg.addIngredient(Ingredient.STRAWBERRIES);
        vg.addIngredient(Ingredient.HAZELNUTS);
        customer.addPancake(pancakeMapper.toDTO(vg));

        return pancakeOrderRepository.getDiscipleOrder(customer).toDTO();
    }

    private void addPancakesForNewOrder(Disciple customer) {
        customer.createOrder(5, 7);

        Pancake vg = pancakeFactory.createPancake("vegetarian");
        vg.addIngredient(Ingredient.STRAWBERRIES);
        vg.addIngredient(Ingredient.HAZELNUTS);
        customer.addPancake(pancakeMapper.toDTO(vg));
    }
}
