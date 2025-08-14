package org.pancakelab.model.pancakes;

import java.util.List;



public sealed interface PancakeMenu permits AbstractPancake {
    List<String> getMenu();
    List<String> getMenuItemIngredients(String menuItemName);
}
