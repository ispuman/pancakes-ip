package org.pancakelab.model.client;

import org.pancakelab.dto.OrderDTO;
import org.pancakelab.dto.PancakeDTO;

public sealed interface PancakeShopCustomer permits Disciple {
    OrderDTO createOrder(int buildingNumber, int roomNumber);
    boolean cancelOrder();
    boolean completeOrder();

    void addPancake(PancakeDTO pancake);
    void removePancake(PancakeDTO pancake);
    void addIngredient(PancakeDTO pancake, String ingredient);
    void removeIngredient(PancakeDTO pancake, String ingredient);
}
