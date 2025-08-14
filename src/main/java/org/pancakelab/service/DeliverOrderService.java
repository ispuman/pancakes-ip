package org.pancakelab.service;

import org.pancakelab.model.pancakes.Pancake;

import java.util.Map;
import java.util.UUID;

@FunctionalInterface
public interface DeliverOrderService {

    Map<Pancake, Integer> deliverOrder(UUID orderId);
}
