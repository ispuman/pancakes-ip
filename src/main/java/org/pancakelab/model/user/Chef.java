package org.pancakelab.model.user;

import org.pancakelab.factory.OrderServiceFactoryImpl;
import org.pancakelab.model.client.PancakeShopCustomer;
import org.pancakelab.service.PancakeServiceFacade;

import java.util.UUID;

public final class Chef implements PancakeShopProducer {

    private final UUID id;
    private final static PancakeServiceFacade pancakeFacadeService =
            new OrderServiceFactoryImpl().createPancakeServiceFacade();

    private Chef() {
        this.id = UUID.randomUUID();
    }

    private static class BillPughSingletonHelper {
        private static final Chef INSTANCE = new Chef();
    }

    public static Chef getInstance() {
        return Chef.BillPughSingletonHelper.INSTANCE;
    }

    private Object readResolve() {
        return getInstance();
    }

    @Override
    public void prepareOrder(PancakeShopCustomer customer) {
        pancakeFacadeService.prepareOrder(customer);
    }
}
