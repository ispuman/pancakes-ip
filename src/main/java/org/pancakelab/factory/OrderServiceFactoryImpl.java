package org.pancakelab.factory;

import org.pancakelab.repository.PancakeOrderRepository;
import org.pancakelab.repository.impl.PancakeOrderRepositoryImpl;
import org.pancakelab.service.*;
import org.pancakelab.service.impl.*;

public class OrderServiceFactoryImpl implements OrderServiceFactory {

    private final PancakeOrderRepository repository = PancakeOrderRepositoryImpl.getInstance();

    @Override
    public CreateOrderService createCreateOrderService() {
        return new CreateOrderServiceImpl(repository);
    }

    @Override
    public CancelOrderService createCancelOrderService() {
        return new CancelOrderServiceImpl(repository);
    }

    @Override
    public CompleteOrderService createCompleteOrderService() {
        return new CompleteOrderServiceImpl(repository);
    }

    @Override
    public PrepareOrderService createPrepareOrderService() {
        return new PrepareOrderServiceImpl(repository);
    }

    @Override
    public DeliverOrderService createDeliverOrderService() {
        return new DeliverOrderServiceImpl(repository);
    }

    @Override
    public PancakeFacadeService createPancakeFacadeService() {
        return new PancakeFacadeServiceImpl(createCreateOrderService(),
                                            createCancelOrderService(),
                                            createCompleteOrderService(),
                                            createPrepareOrderService(),
                                            createDeliverOrderService());
    }
}
