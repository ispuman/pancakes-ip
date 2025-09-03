package org.pancakelab.model.user;

import org.pancakelab.dto.PancakeDTO;

import java.util.Map;

public sealed interface PancakeShopDelivery permits PancakeShopDeliveryService {

    Map<PancakeDTO, Integer> deliverOrder(PancakeShopCustomer customer);
}
