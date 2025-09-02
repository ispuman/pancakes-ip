package org.pancakelab.factory;

import org.pancakelab.model.pancake.Pancake;

import java.util.List;

public interface PancakeFactory {
    Pancake createPancake(String type);

    List<String> getMenu();
    List<String> getMenuItemIngredients(String menuItemName);
}
