package org.pancakelab.service;

import org.pancakelab.model.order.Order;
import org.pancakelab.model.client.PancakeShopCustomer;
import org.pancakelab.model.pancake.Pancake;

public final class PancakeClientFacadeImpl implements PancakeClientFacade {

    private final CreateOrderService createOrderService;
    private final CompleteOrderService completeOrderService;
    private final CancelOrderService cancelOrderService;

    public PancakeClientFacadeImpl(CreateOrderService createOrderService, CancelOrderService cancelOrderService,
                                   CompleteOrderService completeOrderService) {
        this.createOrderService = createOrderService;
        this.cancelOrderService = cancelOrderService;
        this.completeOrderService = completeOrderService;
    }

    @Override
    public Order createOrder(int building, int room, PancakeShopCustomer customer) {
        return createOrderService.createOrder(building, room, customer);
    }

    @Override
    public boolean cancelOrder(PancakeShopCustomer customer) {
        return cancelOrderService.cancelOrder(customer);
    }

    @Override
    public void addPancake(Pancake pancake) {
        createOrderService.addPancake(pancake);
    }

    @Override
    public void removePancake(Pancake pancake) {
        createOrderService.removePancake(pancake);
    }

    @Override
    public boolean completeOrder(PancakeShopCustomer customer) {
        return completeOrderService.completeOrder(customer);
    }
}
