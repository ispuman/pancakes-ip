package org.pancakelab.service;

import org.pancakelab.model.client.PancakeShopCustomer;
import org.pancakelab.model.pancake.Pancake;

import java.util.Map;

public final class PancakeServiceFacadeImpl implements PancakeServiceFacade {

    private final PrepareOrderService prepareOrderService;
    private final DeliverOrderService deliverOrderService;

    public PancakeServiceFacadeImpl(PrepareOrderService prepareOrderService, DeliverOrderService deliverOrderService) {
        this.prepareOrderService = prepareOrderService;
        this.deliverOrderService = deliverOrderService;
    }

    @Override
    public void prepareOrder(PancakeShopCustomer customer) {
        prepareOrderService.prepareOrder(customer);
    }

    @Override
    public Map<Pancake, Integer> deliverOrder(PancakeShopCustomer customer) {
        return deliverOrderService.deliverOrder(customer);
    }
}
