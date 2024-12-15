package com.yoger.productserviceorganization.product.domain.exception;

public class InvalidQuantityException extends IllegalArgumentException {
    public InvalidQuantityException(String message) {
        super(message);
    }
}

