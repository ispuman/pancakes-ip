package org.pancakelab.model.pancake;

import java.util.List;



public sealed interface PancakeMenu permits AbstractPancake {
    List<String> getMenu();
    List<String> getMenuItemIngredients(String menuItemName);
}
