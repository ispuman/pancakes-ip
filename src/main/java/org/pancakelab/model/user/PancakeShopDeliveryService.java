package org.pancakelab.model.user;

import org.pancakelab.dto.PancakeDTO;
import org.pancakelab.factory.OrderServiceFactoryImpl;
import org.pancakelab.factory.PancakeFactoryImpl;
import org.pancakelab.mapper.OrderMapper;
import org.pancakelab.mapper.PancakeMapper;
import org.pancakelab.model.client.PancakeShopCustomer;
import org.pancakelab.service.PancakeServiceFacade;

import java.util.Map;
import java.util.UUID;

public final class PancakeShopDeliveryService implements PancakeShopDelivery {

    private final UUID id;
    private final PancakeServiceFacade pancakeFacadeService =
            new OrderServiceFactoryImpl().createPancakeServiceFacade();
    private final OrderMapper orderMapper = new OrderMapper(new PancakeMapper(new PancakeFactoryImpl()));

    private PancakeShopDeliveryService() {
        this.id = UUID.randomUUID();
    }

    private static class BillPughSingletonHelper {
        private static final PancakeShopDeliveryService INSTANCE = new PancakeShopDeliveryService();
    }

    public static PancakeShopDeliveryService getInstance() {
        return PancakeShopDeliveryService.BillPughSingletonHelper.INSTANCE;
    }

    private Object readResolve() {
        return getInstance();
    }

    @Override
    public Map<PancakeDTO, Integer> deliverOrder(PancakeShopCustomer customer) {
        return orderMapper.toDTO(pancakeFacadeService.deliverOrder(customer));
    }
}
