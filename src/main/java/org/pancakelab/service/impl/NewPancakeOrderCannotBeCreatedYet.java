package org.pancakelab.service.impl;

public class NewPancakeOrderCannotBeCreatedYet  extends IllegalStateException {
    public NewPancakeOrderCannotBeCreatedYet(String message) {
        super(message);
    }
}
