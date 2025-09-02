package org.pancakelab.model.client;

import org.pancakelab.dto.OrderDTO;
import org.pancakelab.dto.PancakeDTO;
import org.pancakelab.factory.OrderClientFactoryImpl;
import org.pancakelab.model.order.DeliveryAddress;
import org.pancakelab.service.PancakeClientFacade;

import java.util.Objects;
import java.util.UUID;

public final class Disciple implements PancakeShopCustomer {

    private final UUID id;
    private final String name;
    private final DeliveryAddress address;
    private final PancakeClientFacade pancakeClientFacade = new OrderClientFactoryImpl().createPancakeClientFacade();

    public Disciple(String name, int buildingNumber, int roomNumber) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank.");
        }
        this.name = name.trim();
        this.address = new DeliveryAddress(buildingNumber, roomNumber);
        this.id = UUID.randomUUID();
    }

    @Override
    public OrderDTO createOrder(int buildingNumber, int roomNumber) {
        return pancakeClientFacade.createOrder(buildingNumber, roomNumber, this).toDTO();
    }

    @Override
    public boolean cancelOrder() {
        return pancakeClientFacade.cancelOrder(this);
    }

    @Override
    public boolean completeOrder() {
        return pancakeClientFacade.completeOrder(this);
    }

    @Override
    public void addPancake(PancakeDTO pancake) {
        pancakeClientFacade.addPancake(pancake.toDO());
    }

    @Override
    public void removePancake(PancakeDTO pancake) {
        pancakeClientFacade.removePancake(pancake.toDO());
    }

    @Override
    public void addIngredient(PancakeDTO pancake, String ingredient) {
        pancake.toDO().addIngredient(ingredient);
    }

    @Override
    public void removeIngredient(PancakeDTO pancake, String ingredient) {
        pancake.toDO().removeIngredient(ingredient);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Disciple disciple) {
            return Objects.equals(id, disciple.id);
        }
       return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return String.format("disciple %s(building %d, room %d)", name, address.buildingNumber(), address.roomNumber());
    }
}
