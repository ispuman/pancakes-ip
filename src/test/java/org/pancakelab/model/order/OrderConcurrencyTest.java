package org.pancakelab.model.order;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.pancakelab.dto.PancakeDTO;
import org.pancakelab.factory.PancakeFactoryImpl;
import org.pancakelab.mapper.OrderMapper;
import org.pancakelab.mapper.PancakeMapper;
import org.pancakelab.model.client.Disciple;
import org.pancakelab.model.pancake.Pancake;
import org.pancakelab.model.user.PancakeShopCustomer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderConcurrencyTest {

    private static final OrderMapper orderMapper = new OrderMapper(new PancakeMapper(new PancakeFactoryImpl()));

    private static final Logger logger = Logger.getLogger(OrderConcurrencyTest.class.getName());

    @Test
    public void GivenTenOrdersAreCreated_WhenTenDisciplesAddAPancakeSimultaneously_ThenNoPancakesAreMissing_Test()
            throws InterruptedException, ExecutionException {
        // setup
        int numberOfDisciples = 10;
        CountDownLatch latch = new CountDownLatch(numberOfDisciples);
        CyclicBarrier barrier = new CyclicBarrier(numberOfDisciples,null);

        List<Future<Order>> orders = new ArrayList<>(numberOfDisciples);
        // exercise
        try (ExecutorService vte = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 1; i <= numberOfDisciples; i++) {
                Future<Order> order = vte.submit(new DiscipleCreatesAnOrderAndAddsPancakesTask(barrier, latch, i));
                orders.add(order);
            }
        }
        // verify
        latch.await();
        for (Future<Order> order : orders) {
            PancakeShopCustomer customer = getCustomer(order.get());
            if (customer instanceof Disciple disciple) {
                int discipleNumber = Integer.parseInt(disciple.getName().replaceFirst("disciple", ""));
                int expected = disciplePancakesCount(discipleNumber);
                assertEquals(expected, order.get().getPancakeItems().values().stream().mapToInt(Integer::intValue).sum(),
                        "%s was expected to have %d pancakes".formatted(disciple, expected));
            }
        }
        // tear down
    }

    private record DiscipleCreatesAnOrderAndAddsPancakesTask(CyclicBarrier barrier, CountDownLatch latch,
                                                             int counter) implements Callable<Order> {
        @Override
        public Order call() {
            try {
                Disciple disciple = new Disciple("disciple" + counter, counter, counter);
                Pancake salty = new PancakeDTO("salty", List.of("cheese", "walnuts")).toDO();
                Pancake sweet = new PancakeDTO("sweet", List.of("milk_chocolate", "hazelnuts")).toDO();
                logger.log(Level.INFO, disciple + " is waiting at the barrier.");

                barrier.await();

                Order order = new Order(counter, counter, disciple, orderMapper);
                order.addPancake(salty);

                if (counter % 2 == 0) {
                    order.addPancake(sweet);
                }
                if (counter % 3 == 0) {
                    order.removePancake(salty);
                }
                if (counter % 4 == 0) {
                    order.addPancake(salty);
                }
                logger.log(Level.INFO, disciple + " created order and added pancake.");

                latch.countDown();
                return order;
            } catch (InterruptedException | BrokenBarrierException ex) {
                logger.log(Level.SEVERE, ex.getMessage(), ex);
            }
            return null;
        }
    }

    private int disciplePancakesCount(int discipleNumber) {
        return switch (discipleNumber) {
            case 1, 5, 6, 7 -> 1;
            case 2, 10 -> 2;
            case 4, 8 -> 3;
            case 3, 9 -> 0;
            default -> -100;
        };
    }

    private PancakeShopCustomer getCustomer(Order order) {
        try {
            Field customer = Order.class.getDeclaredField("customer");
            customer.setAccessible(true);
            PancakeShopCustomer result = (PancakeShopCustomer) customer.get(order);
            customer.setAccessible(false);
            return result;
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }
}
