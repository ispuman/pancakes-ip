package org.pancakelab.service;

public sealed interface PancakeServiceFacade extends PrepareOrderService, DeliverOrderService
        permits PancakeServiceFacadeImpl {
}
