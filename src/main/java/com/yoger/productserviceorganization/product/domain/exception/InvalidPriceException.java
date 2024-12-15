package com.yoger.productserviceorganization.product.domain.exception;

public class InvalidPriceException extends IllegalArgumentException {
    public InvalidPriceException(String message) {
        super(message);
    }
}
