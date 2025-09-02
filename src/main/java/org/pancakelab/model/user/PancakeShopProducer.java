package org.pancakelab.model.user;

import org.pancakelab.model.client.PancakeShopCustomer;

public sealed interface PancakeShopProducer permits Chef {

    void prepareOrder(PancakeShopCustomer customer);
}
