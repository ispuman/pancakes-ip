package org.pancakelab.service;

import org.pancakelab.model.user.PancakeShopCustomer;
import org.pancakelab.model.pancake.Pancake;

import java.util.Map;

@FunctionalInterface
public interface DeliverOrderService {

    Map<Pancake, Integer> deliverOrder(PancakeShopCustomer customer);
}
