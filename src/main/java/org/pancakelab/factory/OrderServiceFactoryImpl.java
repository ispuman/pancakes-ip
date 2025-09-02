package org.pancakelab.factory;

import org.pancakelab.repository.PancakeOrderRepository;
import org.pancakelab.repository.impl.PancakeOrderRepositoryImpl;
import org.pancakelab.service.DeliverOrderService;
import org.pancakelab.service.PancakeServiceFacade;
import org.pancakelab.service.PancakeServiceFacadeImpl;
import org.pancakelab.service.PrepareOrderService;
import org.pancakelab.service.impl.DeliverOrderServiceImpl;
import org.pancakelab.service.impl.PrepareOrderServiceImpl;

public class OrderServiceFactoryImpl implements OrderServiceFactory {

    private final PancakeOrderRepository repository = PancakeOrderRepositoryImpl.getInstance();

    @Override
    public PrepareOrderService createPrepareOrderService() {
        return new PrepareOrderServiceImpl(repository);
    }

    @Override
    public DeliverOrderService createDeliverOrderService() {
        return new DeliverOrderServiceImpl(repository);
    }

    @Override
    public PancakeServiceFacade createPancakeServiceFacade() {
        return new PancakeServiceFacadeImpl(createPrepareOrderService(), createDeliverOrderService());
    }
}
