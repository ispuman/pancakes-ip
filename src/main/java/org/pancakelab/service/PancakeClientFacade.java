package org.pancakelab.service;

public sealed interface PancakeClientFacade extends
        CreateOrderService, CancelOrderService, CompleteOrderService
        permits PancakeClientFacadeImpl {
}
