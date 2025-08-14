package org.pancakelab.service;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.pancakelab.factory.OrderServiceFactory;
import org.pancakelab.factory.OrderServiceFactoryImpl;
import org.pancakelab.factory.PancakeFactory;
import org.pancakelab.factory.PancakeFactoryImpl;
import org.pancakelab.model.order.Order;
import org.pancakelab.model.pancakes.*;
import org.pancakelab.repository.PancakeOrderRepository;
import org.pancakelab.repository.impl.PancakeOrderRepositoryImpl;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static java.util.stream.Collectors.toList;
import static org.pancakelab.model.order.DeliveryAddress.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PancakeServiceTest {
    OrderServiceFactory factory = new OrderServiceFactoryImpl();
    private final PancakeFacadeService pancakeService = factory.createPancakeFacadeService();

    private final PancakeOrderRepository pancakeOrderRepository = PancakeOrderRepositoryImpl.getInstance();

    @Test
    @org.junit.jupiter.api.Order(4)
    public void GivenNonPositiveAddress_WhenCreatingNewOrder_TheAppropriateErrorMessageIsDisplayed() {

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> pancakeService.createOrder(12, 0));
        assertEquals(ERROR_ADDRESS_NUMBERS_SHOULD_BE_POSITIVE, exception.getMessage());
    }

    @Test
    @org.junit.jupiter.api.Order(5)
    public void GivenNonExistingRoomAddress_WhenCreatingNewOrder_TheAppropriateErrorMessageIsDisplayed() {

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> pancakeService.createOrder(10, 30));
        assertEquals(ERROR_ROOM_NUMBER_TOO_LARGE, exception.getMessage());
    }

    @Test
    @org.junit.jupiter.api.Order(6)
    public void GivenNonExistingBuildingAddress_WhenCreatingNewOrder_TheAppropriateErrorMessageIsDisplayed() {

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> pancakeService.createOrder(28, 10));
        assertEquals(ERROR_BUILDING_NUMBER_TOO_LARGE, exception.getMessage());
    }

    @Test
    @org.junit.jupiter.api.Order(10)
    public void GivenOrderDoesNotExist_WhenCreatingOrder_ThenOrderCreatedWithCorrectData_Test() {
        // setup

        // exercise
        UUID orderId = pancakeService.createOrder(10, 20);
        Order order = pancakeOrderRepository.removePendingPancakeOrder(orderId);

        assertEquals(10, order.getDeliveryAddress().buildingNumber());
        assertEquals(20, order.getDeliveryAddress().roomNumber());

        // verify

        // tear down
    }

    @Test
    @org.junit.jupiter.api.Order(20)
    public void GivenOrderExists_WhenAddingPancakes_ThenCorrectNumberOfPancakesAdded_Test() {
        // setup

        // exercise
        UUID orderId = addPancakes();
        pancakeService.completeOrder(orderId);

        // verify
        Order order = pancakeOrderRepository.removeCompletedPancakeOrder(orderId);

        assertEquals(7, order.getPancakeItems().values().stream().mapToInt(Integer::intValue).sum());
        assertEquals(List.of("salty pancake with %s, %s".formatted(Ingredient.CHEESE.toString(), Ingredient.WALNUTS.toString()),
                             "sweet pancake with %s, %s".formatted(Ingredient.HONEY.toString(), Ingredient.WALNUTS.toString()),
                             "sweet pancake with %s, %s".formatted(Ingredient.MILK_CHOCOLATE.toString(), Ingredient.HAZELNUTS.toString()),
                             "sweet pancake with %s, %s".formatted(Ingredient.FRUIT_JAM.toString(), Ingredient.STRAWBERRIES.toString()),
                             "vegetarian pancake with %s, %s".formatted(Ingredient.HAZELNUTS.toString(), Ingredient.YELLOW_CHEESE.toString()),
                             "meat pancake with %s, %s".formatted(Ingredient.PARMESAN.toString(), Ingredient.MEAT.toString())),
                order.getPancakeItems().keySet().stream().map(Pancake::description).collect(toList()));

        // tear down
    }

    @Test
    @org.junit.jupiter.api.Order(30)
    public void GivenPancakesExists_WhenRemovingPancakes_ThenCorrectNumberOfPancakesRemoved_Test() {
        // setup
        UUID orderId = addPancakes();
        SaltyPancake saltyPancake = new SaltyPancake();
        Pancake sap = new Pancake(saltyPancake);
        sap.addIngredient(Ingredient.CHEESE);
        sap.addIngredient(Ingredient.WALNUTS);

        MeatPancake meatPancake = new MeatPancake();
        Pancake mp = new Pancake(meatPancake);
        mp.addIngredient(Ingredient.MEAT);
        mp.addIngredient(Ingredient.PARMESAN);

        // exercise
        pancakeService.removePancakes(sap);
        pancakeService.removePancakes(sap);
        pancakeService.removePancakes(mp);
        pancakeService.completeOrder(orderId);

        // verify
        Order order = pancakeOrderRepository.removeCompletedPancakeOrder(orderId);

        assertEquals(4, order.getPancakeItems().values().stream().mapToInt(Integer::intValue).sum());
        assertEquals(List.of("sweet pancake with %s, %s".formatted(Ingredient.HONEY.toString(), Ingredient.WALNUTS.toString()),
                        "sweet pancake with %s, %s".formatted(Ingredient.MILK_CHOCOLATE.toString(), Ingredient.HAZELNUTS.toString()),
                        "sweet pancake with %s, %s".formatted(Ingredient.FRUIT_JAM.toString(), Ingredient.STRAWBERRIES.toString()),
                        "vegetarian pancake with %s, %s".formatted(Ingredient.HAZELNUTS.toString(), Ingredient.YELLOW_CHEESE.toString())),
                order.getPancakeItems().keySet().stream().map(Pancake::description).collect(toList()));

        // tear down
    }

    @Test
    @org.junit.jupiter.api.Order(40)
    public void GivenOrderExists_WhenCompletingOrder_ThenOrderCompleted_Test() {
        // setup
        UUID orderId = addPancakes();

        // exercise
        pancakeService.completeOrder(orderId);

        // verify
        Set<UUID> completedOrders = pancakeOrderRepository.listCompletedOrders();
        assertTrue(completedOrders.contains(orderId));

        // tear down
    }

    @Test
    @org.junit.jupiter.api.Order(50)
    public void GivenOrderExists_WhenPreparingOrder_ThenOrderPrepared_Test() {
        // setup
        UUID orderId = addPancakes();

        // exercise
        pancakeService.completeOrder(orderId);
        pancakeService.prepareOrder(orderId);

        // verify
        Set<UUID> completedOrders = pancakeOrderRepository.listCompletedOrders();
        assertFalse(completedOrders.contains(orderId));

        Set<UUID> preparedOrders = pancakeOrderRepository.listPreparedOrders();
        assertTrue(preparedOrders.contains(orderId));

        // tear down
    }

    @Test
    @org.junit.jupiter.api.Order(60)
    public void GivenOrderExists_WhenDeliveringOrder_ThenCorrectOrderReturnedAndOrderRemovedFromTheDatabase_Test() {
        // setup
        UUID orderId = addPancakes();
        pancakeService.completeOrder(orderId);
        pancakeService.prepareOrder(orderId);

        // exercise
        Map<Pancake, Integer> deliveredPancakes = pancakeService.deliverOrder(orderId);

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

    @Test
    @org.junit.jupiter.api.Order(70)
    public void GivenOrderExists_WhenCancellingOrder_ThenOrderAndPancakesRemoved_Test() {
        // setup
        UUID orderId = addPancakes();

        // exercise
        pancakeService.cancelOrder(orderId);

        // verify
        Set<UUID> completedOrders = pancakeOrderRepository.listCompletedOrders();
        assertFalse(completedOrders.contains(orderId));

        Set<UUID> preparedOrders = pancakeOrderRepository.listPreparedOrders();
        assertFalse(preparedOrders.contains(orderId));

        Order order = pancakeOrderRepository.removePendingPancakeOrder(orderId);
        assertNull(order);

        // tear down
    }

    private UUID addPancakes() {
        UUID orderId = pancakeService.createOrder(2, 3);

        PancakeFactory factory = new PancakeFactoryImpl();
        for (int i = 0; i < 2; i++) {
            Pancake sap = factory.createPancake("salty");
            sap.addIngredient(Ingredient.CHEESE);
            sap.addIngredient(Ingredient.WALNUTS);
            pancakeService.addPancake(sap);
        }

        Pancake swp1 = factory.createPancake("sweet");
        swp1.addIngredient(Ingredient.HONEY);
        swp1.addIngredient(Ingredient.WALNUTS);
        pancakeService.addPancake(swp1);
        Pancake swp2 = factory.createPancake("sweet");
        swp2.addIngredient(Ingredient.MILK_CHOCOLATE);
        swp2.addIngredient(Ingredient.HAZELNUTS);
        pancakeService.addPancake(swp2);
        Pancake swp3 = factory.createPancake("sweet");
        swp3.addIngredient(Ingredient.FRUIT_JAM);
        swp3.addIngredient(Ingredient.STRAWBERRIES);
        pancakeService.addPancake(swp3);

        Pancake vp = factory.createPancake("vegetarian");
        vp.addIngredient(Ingredient.YELLOW_CHEESE);
        vp.addIngredient(Ingredient.HAZELNUTS);
        pancakeService.addPancake(vp);

        Pancake mp = factory.createPancake("meat");
        mp.addIngredient(Ingredient.MEAT);
        mp.addIngredient(Ingredient.PARMESAN);
        pancakeService.addPancake(mp);

        return orderId;
    }
}
