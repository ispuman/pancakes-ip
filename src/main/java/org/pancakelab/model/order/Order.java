package org.pancakelab.model.order;

import org.pancakelab.dto.OrderDTO;
import org.pancakelab.mapper.OrderMapper;
import org.pancakelab.model.pancake.Pancake;
import org.pancakelab.model.client.Disciple;
import org.pancakelab.model.user.PancakeShopCustomer;

import java.util.*;

public class Order {

    private final UUID id;
    private OrderStatus status;
    private final Map<Pancake, Integer> pancakeItems;
    private final DeliveryAddress deliveryAddress;
    private final PancakeShopCustomer customer;
    private final OrderMapper orderMapper;

    private final EnumSet<OrderStatus> pendingOrderStatus =  EnumSet.of(OrderStatus.PENDING,
            OrderStatus.ADDING_PANECAKES, OrderStatus.REMOVING_PANECAKES);
    private final EnumSet<OrderStatus> toBeCompletedOrderStatus =  EnumSet.of(OrderStatus.ADDING_PANECAKES,
            OrderStatus.REMOVING_PANECAKES);

    public Order(int building, int room, PancakeShopCustomer customer, OrderMapper orderMapper) {
        this.deliveryAddress = new DeliveryAddress(building, room);
        this.customer = Objects.requireNonNull(customer, "Customer cannot be null");
        this.id = UUID.randomUUID();
        this.status = OrderStatus.PENDING;
        this.pancakeItems = new LinkedHashMap<>();
        this.orderMapper = orderMapper;
    }

    public void addPancake(Pancake pancake) {
        status = OrderStatus.ADDING_PANECAKES;
        pancakeItems.put(pancake, pancakeItems.getOrDefault(pancake, 0) + 1);
    }

    public void removePancake(Pancake pancake) {
        status = OrderStatus.REMOVING_PANECAKES;
        pancakeItems.computeIfPresent(pancake, (k, v) -> v == 1 ? null : v - 1);
    }

    public UUID getId() {
        return id;
    }

    public DeliveryAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setOrderStatus(OrderStatus status) {
        this.status = status;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Map<Pancake, Integer> getPancakeItems() {
        return new LinkedHashMap<>(pancakeItems);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Order order) {
            return Objects.equals(id, order.id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public boolean isCancellationAuthorized(PancakeShopCustomer customer) {
        if (customer instanceof Disciple d && pendingOrderStatus.contains(status)) {
            return d.equals(this.customer);
        }
        return false;
    }

    public boolean isCompletionAuthorized(PancakeShopCustomer customer) {
        if (customer instanceof Disciple d && toBeCompletedOrderStatus.contains(status) && !pancakeItems.isEmpty()) {
            return d.equals(this.customer);
        }
        return false;
    }

    public OrderDTO toDTO() {
        return orderMapper.toDTO(this);
    }
}
