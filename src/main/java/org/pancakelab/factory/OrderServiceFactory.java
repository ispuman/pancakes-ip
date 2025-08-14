package org.pancakelab.factory;

import org.pancakelab.service.*;

public interface OrderServiceFactory {
    CreateOrderService createCreateOrderService();
    CancelOrderService createCancelOrderService();
    CompleteOrderService createCompleteOrderService();
    PrepareOrderService createPrepareOrderService();
    DeliverOrderService createDeliverOrderService();

    PancakeFacadeService createPancakeFacadeService();
}
