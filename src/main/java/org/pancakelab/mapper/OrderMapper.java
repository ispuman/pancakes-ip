package org.pancakelab.mapper;

import org.pancakelab.dto.OrderDTO;
import org.pancakelab.dto.PancakeDTO;
import org.pancakelab.model.order.DeliveryAddress;
import org.pancakelab.model.order.Order;
import org.pancakelab.model.pancake.Pancake;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class OrderMapper implements Mapper<Order, OrderDTO> {

    private final PancakeMapper pancakeMapper;

    public OrderMapper(PancakeMapper pancakeMapper) {
        this.pancakeMapper = pancakeMapper;
    }

    @Override
    public OrderDTO toDTO(Order source) {
        Objects.requireNonNull(source, "Order cannot be null.");
        DeliveryAddress address = source.getDeliveryAddress();

        return new OrderDTO(address.buildingNumber(), address.roomNumber(), toDTO(source.getPancakeItems()));
    }

    public Map<PancakeDTO, Integer> toDTO(Map<Pancake, Integer> orderItems) {
        Map<PancakeDTO, Integer> orderDtoItems = new LinkedHashMap<>();
        orderItems.forEach((pancake, quantity) -> orderDtoItems.put(pancakeMapper.toDTO(pancake), quantity));
        return orderDtoItems;
    }
}
