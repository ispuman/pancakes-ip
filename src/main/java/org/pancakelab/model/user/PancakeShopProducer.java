package org.pancakelab.model.user;

public sealed interface PancakeShopProducer permits Chef {

    void prepareOrder(PancakeShopCustomer customer);
}
