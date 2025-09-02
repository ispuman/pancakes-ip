package org.pancakelab;

import org.pancakelab.factory.OrderClientFactory;
import org.pancakelab.factory.OrderClientFactoryImpl;
import org.pancakelab.factory.PancakeFactory;
import org.pancakelab.factory.PancakeFactoryImpl;
import org.pancakelab.model.client.Disciple;
import org.pancakelab.model.pancake.Pancake;
import org.pancakelab.repository.PancakeOrderRepository;
import org.pancakelab.repository.impl.PancakeOrderRepositoryImpl;
import org.pancakelab.service.PancakeClientFacade;

import java.util.*;

public class Main {
    public static void main(String[] args) {

        OrderClientFactory orderFactory = new OrderClientFactoryImpl();
        final PancakeClientFacade pancakeClientFacade = orderFactory.createPancakeClientFacade();

        final PancakeOrderRepository pancakeOrderRepository = PancakeOrderRepositoryImpl.getInstance();

        Disciple disciple = new Disciple("John", 1, 1);

        PancakeFactory pancakeFactory = new PancakeFactoryImpl();
        Pancake sap = pancakeFactory.createPancake("salty");
        Pancake swp = pancakeFactory.createPancake("sweet");
        Pancake vp = pancakeFactory.createPancake("vegetarian");
        Pancake mp = pancakeFactory.createPancake("meat");

        System.out.println("Welcome to PancakeLab");
        List<String> menu = pancakeFactory.getMenu();
        Map<Integer, String> menuItems = new LinkedHashMap<>();
        Integer i = 1;
        for (String item : menu) {
            menuItems.put(i, item);
            i++;
        }
        System.out.println("The available pancake menu: " + menuItems);
        System.out.println("Display available ingredients for a pancake type (1 through 4 and else for exit): ");
        Scanner s = new Scanner(System.in);
        while (true) {
            int choice = s.nextInt();
            switch (choice) {
                case 1, 2, 3, 4 -> System.out.println(pancakeFactory.getMenuItemIngredients(menuItems.get(choice)));
                default -> {
                    return;
                }
            }
        }
    }
}