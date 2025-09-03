package org.pancakelab.model.order;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.pancakelab.dto.OrderDTO;
import org.pancakelab.dto.PancakeDTO;
import org.pancakelab.factory.PancakeFactory;
import org.pancakelab.factory.PancakeFactoryImpl;
import org.pancakelab.mapper.PancakeMapper;
import org.pancakelab.model.client.Disciple;
import org.pancakelab.model.pancake.Ingredient;
import org.pancakelab.model.pancake.Pancake;
import org.pancakelab.repository.PancakeOrderRepository;
import org.pancakelab.repository.impl.PancakeOrderRepositoryImpl;
import org.pancakelab.service.impl.PancakeOrderNotFoundInDB;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderTest {

    private final PancakeOrderRepository pancakeOrderRepository = PancakeOrderRepositoryImpl.getInstance();

    private final PancakeFactory pancakeFactory = new PancakeFactoryImpl();
    private final PancakeMapper pancakeMapper = new PancakeMapper(pancakeFactory);

    @Test
    public void GivenOrderExists_WhenCompletingOrder_ThenOrderCannotBeCancelled_Test() {
        // setup
        Disciple disciple = new Disciple("Louise", 4, 2);
        OrderDTO orderDTO = addPancakes(disciple);
        boolean completion = disciple.completeOrder();
        assertTrue(completion);
        assertEquals(2, orderDTO.orderItems().values().stream().mapToInt(Integer::intValue).sum());

        // exercise
        PancakeOrderNotFoundInDB exception = assertThrows(PancakeOrderNotFoundInDB.class, disciple::cancelOrder);

        // verify
        assertTrue(exception.getMessage().contains("not found and cannot be cancelled."));
        // tear down
        pancakeOrderRepository.removeCompletedPancakeOrder(disciple);
        pancakeOrderRepository.removeCustomerOrder(disciple);
    }

    @Test
    public void GivenDiscipleCreatesAnOrder_WhenSheRemovesAllAddedPancakes_ThenCompletionFails_Test() {
        // setup
        Disciple disciple = new Disciple("Elizabeth", 5, 7);
        disciple.createOrder(5, 7);
        PancakeDTO pancake = new PancakeDTO("sweet", List.of("milk_chocolate", "chocolate_chip"));
        disciple.addPancake(pancake);
        // exercise
        disciple.removePancake(pancake);
        boolean completion = disciple.completeOrder();
        // verify
        assertFalse(completion);
        assertEquals(OrderStatus.REMOVING_PANECAKES, pancakeOrderRepository.getDiscipleOrder(disciple).getStatus());
        assertTrue(pancakeOrderRepository.listCompletedOrders().isEmpty());
        assertEquals(1, pancakeOrderRepository.listAllOrders().size());
        // tear down
        pancakeOrderRepository.removePendingPancakeOrder(disciple);
        pancakeOrderRepository.removeCustomerOrder(disciple);
    }

    private OrderDTO addPancakes(Disciple customer) {
        OrderDTO orderDTO = customer.createOrder(4, 6);

        Pancake sap = pancakeFactory.createPancake("salty");
        sap.addIngredient(Ingredient.YELLOW_CHEESE);
        customer.addPancake(pancakeMapper.toDTO(sap));

        Pancake swp = pancakeFactory.createPancake("sweet");
        swp.addIngredient(Ingredient.CHOCOLATE_CHIP);
        swp.addIngredient(Ingredient.WHIPPED_CREAM);
        customer.addPancake(pancakeMapper.toDTO(swp));

        return pancakeOrderRepository.getDiscipleOrder(customer).toDTO();
    }
}
