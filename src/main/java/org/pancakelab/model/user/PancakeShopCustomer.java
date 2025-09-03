package org.pancakelab.model.user;

import org.pancakelab.dto.OrderDTO;
import org.pancakelab.dto.PancakeDTO;

public interface PancakeShopCustomer {
    OrderDTO createOrder(int buildingNumber, int roomNumber);
    boolean cancelOrder();
    boolean completeOrder();

    void addPancake(PancakeDTO pancake);
    void removePancake(PancakeDTO pancake);
    void addIngredient(PancakeDTO pancake, String ingredient);
    void removeIngredient(PancakeDTO pancake, String ingredient);
}
