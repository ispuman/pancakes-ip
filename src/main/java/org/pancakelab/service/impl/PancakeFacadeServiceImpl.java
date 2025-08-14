package org.pancakelab.service.impl;

import org.pancakelab.model.pancakes.Pancake;
import org.pancakelab.service.*;

import java.util.Map;
import java.util.UUID;

public class PancakeFacadeServiceImpl implements PancakeFacadeService {

    private final CreateOrderService createOrderService;
    private final CompleteOrderService completeOrderService;
    private final CancelOrderService cancelOrderService;
    private final PrepareOrderService prepareOrderService;
    private final DeliverOrderService deliverOrderService;

    public PancakeFacadeServiceImpl(CreateOrderService createOrderService, CancelOrderService cancelOrderService,
                                    CompleteOrderService completeOrderService, PrepareOrderService prepareOrderService,
                                    DeliverOrderService deliverOrderService) {
        this.createOrderService = createOrderService;
        this.cancelOrderService = cancelOrderService;
        this.completeOrderService = completeOrderService;
        this.prepareOrderService = prepareOrderService;
        this.deliverOrderService = deliverOrderService;
    }

    @Override
    public UUID createOrder(int building, int room) {
        return createOrderService.createOrder(building, room);
    }

    @Override
    public void cancelOrder(UUID orderId) {
        cancelOrderService.cancelOrder(orderId);
    }

    @Override
    public void addPancake(Pancake pancake) {
        createOrderService.addPancake(pancake);
    }

    @Override
    public void removePancakes(Pancake pancake) {
        createOrderService.removePancakes(pancake);
    }

    @Override
    public void completeOrder(UUID orderId) {
        completeOrderService.completeOrder(orderId);
    }

    @Override
    public void prepareOrder(UUID orderId) {
        prepareOrderService.prepareOrder(orderId);
    }

    @Override
    public Map<Pancake, Integer> deliverOrder(UUID orderId) {
        return deliverOrderService.deliverOrder(orderId);
    }
}
