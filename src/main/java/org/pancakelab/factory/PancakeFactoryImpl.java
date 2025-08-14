package org.pancakelab.factory;

import org.pancakelab.model.pancakes.*;

import java.util.List;

public class PancakeFactoryImpl implements PancakeFactory {
    @Override
    public Pancake createPancake(String type) {
        PancakeRecipe pancake = switch (type) {
            case "salty" -> new SaltyPancake();
            case "sweet" -> new SweetPancake();
            case "vegetarian" -> new VegetarianPancake();
            case "meat" -> new MeatPancake();
            default -> throw new IllegalStateException("Unexpected value for pancake type : " + type);
        };
        return new Pancake(pancake);
    }

    @Override
    public List<String> getMenu() {
        return new SaltyPancake().getMenu();
    }

    @Override
    public List<String> getMenuItemIngredients(String menuItem) {
        return switch (menuItem) {
            case "SaltyPancake" -> new SaltyPancake().getMenuItemIngredients(menuItem);
            case "SweetPancake" -> new SweetPancake().getMenuItemIngredients(menuItem);
            case "VegetarianPancake" -> new VegetarianPancake().getMenuItemIngredients(menuItem);
            case "MeatPancake" -> new MeatPancake().getMenuItemIngredients(menuItem);
            default -> List.of();
        };
    }
}
