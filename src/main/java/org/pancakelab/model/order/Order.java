package org.pancakelab.model.order;

import org.pancakelab.model.pancakes.Pancake;

import java.util.*;

public class Order {
    private final UUID id;
    private OrderStatus status;
    private final Map<Pancake, Integer> pancakeItems = new LinkedHashMap<>();
    private final DeliveryAddress deliveryAddress;

    public Order(int building, int room) {
        this.deliveryAddress = new DeliveryAddress(building, room);
        this.id = UUID.randomUUID();
        this.status = OrderStatus.PENDING;
    }

    public void addPancake(Pancake pancake) {
        status = OrderStatus.ADDING_PANECAKES;
        pancakeItems.put(pancake, pancakeItems.getOrDefault(pancake, 0) + 1);
    }

    public void removePancake(Pancake pancake) {
        status = OrderStatus.REMOVING_PANECAKES;
        int count = pancakeItems.getOrDefault(pancake, 0);
        if (count == 1) {
            pancakeItems.remove(pancake);
        } else {
            pancakeItems.put(pancake, count - 1);
        }
    }

    public UUID getId() {
        return id;
    }

    public DeliveryAddress getDeliveryAddress() {
        return deliveryAddress;
    }
    public OrderStatus getStatus() {
        return status;
    }

    public void setOrderStatus(OrderStatus status) {
        this.status = status;
    }

    public Map<Pancake, Integer> getPancakeItems() {
        return pancakeItems;  // Map.copyOf(pancakeItems);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
