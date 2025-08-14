package org.pancakelab.model.disciple;

import org.pancakelab.model.order.DeliveryAddress;
import org.pancakelab.model.pancakes.Ingredient;
import org.pancakelab.model.pancakes.Pancake;
import org.pancakelab.service.PancakeFacadeService;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Disciple implements PancakeShopCustomer {

    private final UUID id;
    private final String name;
    private final DeliveryAddress address;
    private UUID orderId;
    private final PancakeFacadeService pancakeFacadeService;

    public Disciple(String name, int buildingNumber, int roomNumber, PancakeFacadeService pancakeFacadeService) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank.");
        }
        this.name = name.trim();
        this.address = new DeliveryAddress(buildingNumber, roomNumber);
        this.pancakeFacadeService = pancakeFacadeService;
        this.id = UUID.randomUUID();
    }

    @Override
    public UUID createOrder(int buildingNumber, int roomNumber) {
        orderId = pancakeFacadeService.createOrder(buildingNumber, roomNumber);
        return orderId;
    }

    @Override
    public void cancelOrder() {
        pancakeFacadeService.cancelOrder(orderId);
    }

    @Override
    public void completeOrder() {
        pancakeFacadeService.completeOrder(orderId);
    }

    @Override
    public void prepareOrder() {
        pancakeFacadeService.prepareOrder(orderId);
    }

    @Override
    public Map<Pancake, Integer> deliverOrder() {
        return pancakeFacadeService.deliverOrder(orderId);
    }

    @Override
    public void addPancake(Pancake pancake) {
        pancakeFacadeService.addPancake(pancake);
    }

    @Override
    public void removePancake(Pancake pancake) {
        pancakeFacadeService.removePancakes(pancake);
    }

    @Override
    public void addIngredient(Pancake pancake, Ingredient ingredient) {
        pancake.addIngredient(ingredient);
    }

    @Override
    public void removeIngredient(Pancake pancake, Ingredient ingredient) {
        pancake.removeIngredient(ingredient);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Disciple disciple = (Disciple) o;
        return Objects.equals(id, disciple.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return String.format("%s(B%d, R%d)", name, address.buildingNumber(), address.roomNumber());
    }
}
