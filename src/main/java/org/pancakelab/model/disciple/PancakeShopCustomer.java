package org.pancakelab.model.disciple;

import org.pancakelab.model.pancakes.Ingredient;
import org.pancakelab.model.pancakes.Pancake;

import java.util.Map;
import java.util.UUID;

public interface PancakeShopCustomer {
    UUID createOrder(int buildingNumber, int roomNumber);
    void cancelOrder();
    void completeOrder();
    void prepareOrder();
    Map<Pancake, Integer> deliverOrder();

    void addPancake(Pancake pancake);
    void removePancake(Pancake pancake);
    void addIngredient(Pancake pancake, Ingredient ingredient);
    void removeIngredient(Pancake pancake, Ingredient ingredient);
}
