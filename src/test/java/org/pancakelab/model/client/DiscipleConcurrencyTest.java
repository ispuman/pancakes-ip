package org.pancakelab.model.client;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.pancakelab.dto.PancakeDTO;
import org.pancakelab.repository.PancakeOrderRepository;
import org.pancakelab.repository.impl.PancakeOrderRepositoryImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DiscipleConcurrencyTest {

    private final PancakeOrderRepository pancakeOrderRepository = PancakeOrderRepositoryImpl.getInstance();

    private static final Logger logger = Logger.getLogger(DiscipleConcurrencyTest.class.getName());

    @Test
    public void GivenTenOrdersAreCreated_WhenTenDisciplesAddAPancakeSimultaneously_ThenNoPancakesAreMissing_Test()
            throws InterruptedException, ExecutionException {
        // setup
        int numberOfDisciples = 10;
        CountDownLatch latch = new CountDownLatch(numberOfDisciples);
        CyclicBarrier barrier = new CyclicBarrier(numberOfDisciples,null);

        List<Future<Disciple>> disciples = new ArrayList<>(numberOfDisciples);
        // exercise
        try (ExecutorService vte = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 1; i <= numberOfDisciples; i++) {
                Future<Disciple> disciple = vte.submit(new DiscipleCreatesAnOrderAndAddsPancakesTask(barrier, latch, i));
                disciples.add(disciple);
            }
        }
        // verify
        latch.await();
        for (Future<Disciple> disciple : disciples) {
            int discipleNumber = Integer.parseInt(disciple.get().getName().replaceFirst("disciple", ""));
            int expected = disciplePancakesCount(discipleNumber);
            assertEquals(expected, pancakeOrderRepository.getDiscipleOrder(disciple.get()).getPancakeItems().values()
                                .stream().mapToInt(Integer::intValue).sum(),
                        "%s was expected to have %d pancakes".formatted(disciple.get(), expected));
        }
        // tear down
        for (Future<Disciple> disciple : disciples) {
             disciple.get().cancelOrder();
        }
    }

    private record DiscipleCreatesAnOrderAndAddsPancakesTask(CyclicBarrier barrier, CountDownLatch latch,
                                                             int counter) implements Callable<Disciple> {
        @Override
        public Disciple call() {
            Disciple disciple = new Disciple("disciple" + counter, counter, counter);
            try {
                PancakeDTO salty = new PancakeDTO("salty", List.of("cheese", "walnuts"));
                PancakeDTO sweet = new PancakeDTO("sweet", List.of("milk_chocolate", "hazelnuts"));
                logger.log(Level.INFO, disciple + " is waiting at the barrier.");

                barrier.await();

                disciple.createOrder(counter, counter);
                disciple.addPancake(salty);

                if (counter % 2 == 0) {
                    disciple.addPancake(sweet);
                }
                if (counter % 3 == 0) {
                    disciple.removePancake(salty);
                }
                if (counter % 4 == 0) {
                    disciple.addPancake(salty);
                }
                logger.log(Level.INFO, disciple + " created order and added pancake.");

                latch.countDown();
            } catch (InterruptedException | BrokenBarrierException ex) {
                logger.log(Level.SEVERE, ex.getMessage(), ex);
            }
            return disciple;
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
}
