package org.pancakelab.factory;

import org.pancakelab.service.*;

public interface OrderClientFactory {
    CreateOrderService createCreateOrderService();
    CancelOrderService createCancelOrderService();
    CompleteOrderService createCompleteOrderService();

    PancakeClientFacade createPancakeClientFacade();
}
