package org.pancakelab.service.impl;

public class PancakeOrderNotFoundInDB extends RuntimeException {
    public PancakeOrderNotFoundInDB(String message) {
        super(message);
    }
}
