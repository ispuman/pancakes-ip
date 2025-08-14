package org.pancakelab.service;

import org.pancakelab.model.pancakes.Pancake;

import java.util.UUID;

public interface CreateOrderService {
    UUID createOrder(int building, int room);

    void addPancake(Pancake pancake);
    void removePancakes(Pancake pancake);
}
