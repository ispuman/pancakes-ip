package org.pancakelab.model.user;

import org.pancakelab.dto.PancakeDTO;
import org.pancakelab.model.client.PancakeShopCustomer;

import java.util.Map;

public sealed interface PancakeShopDelivery permits PancakeShopDeliveryService {

    Map<PancakeDTO, Integer> deliverOrder(PancakeShopCustomer customer);
}
