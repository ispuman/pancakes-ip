package org.pancakelab.factory;

import org.pancakelab.service.DeliverOrderService;
import org.pancakelab.service.PancakeServiceFacade;
import org.pancakelab.service.PrepareOrderService;

public interface OrderServiceFactory {

    PrepareOrderService createPrepareOrderService();
    DeliverOrderService createDeliverOrderService();

    PancakeServiceFacade createPancakeServiceFacade();
}
