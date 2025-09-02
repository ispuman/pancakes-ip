package org.pancakelab.service;

import org.pancakelab.model.client.PancakeShopCustomer;
import org.pancakelab.model.pancake.Pancake;

import java.util.Map;

@FunctionalInterface
public interface DeliverOrderService {

    Map<Pancake, Integer> deliverOrder(PancakeShopCustomer customer);
}
