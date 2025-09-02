package org.pancakelab.factory;

import org.pancakelab.mapper.OrderMapper;
import org.pancakelab.mapper.PancakeMapper;
import org.pancakelab.repository.PancakeOrderRepository;
import org.pancakelab.service.PancakeClientFacadeImpl;
import org.pancakelab.repository.impl.PancakeOrderRepositoryImpl;
import org.pancakelab.service.*;
import org.pancakelab.service.impl.*;

public class OrderClientFactoryImpl implements OrderClientFactory {

    private final PancakeOrderRepository repository = PancakeOrderRepositoryImpl.getInstance();
    private final OrderMapper orderMapper = new OrderMapper(new PancakeMapper(new PancakeFactoryImpl()));

    @Override
    public CreateOrderService createCreateOrderService() {
        return new CreateOrderServiceImpl(repository, orderMapper);
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
    public PancakeClientFacade createPancakeClientFacade() {
        return new PancakeClientFacadeImpl(createCreateOrderService(),
                                            createCancelOrderService(),
                                            createCompleteOrderService());
    }
}
