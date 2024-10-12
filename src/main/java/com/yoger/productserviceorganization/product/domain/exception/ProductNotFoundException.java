package com.yoger.productserviceorganization.product.domain.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long productId) {
        super("Product not found with id : " + productId);
    }
}
