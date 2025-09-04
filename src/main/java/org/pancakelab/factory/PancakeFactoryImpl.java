package org.pancakelab.factory;

import org.pancakelab.model.pancake.*;

import java.util.List;

public class PancakeFactoryImpl implements PancakeFactory {

    private static final List<String> menu = new SaltyPancake().getMenu().stream()
            .map(p -> p.replaceFirst("Pancake", "").toLowerCase()).toList();

    public static final String INVALID_PANCAKE_TYPE = "Please select valid pancake type : %s".formatted(menu);

    @Override
    public Pancake createPancake(String type) {
        PancakeRecipe pancake = switch (type) {
            case "salty" -> new SaltyPancake();
            case "sweet" -> new SweetPancake();
            case "vegetarian" -> new VegetarianPancake();
            case "meat" -> new MeatPancake();
            default -> throw new IllegalArgumentException(INVALID_PANCAKE_TYPE);
        };
        return new Pancake(pancake);
    }

    @Override
    public List<String> getMenu() {
        return menu;
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
