package com.yoger.productserviceorganization.product.adapters.messaging.kafka.event;

public record StockChangeEvent(
        Long productId,
        Integer quantity
) {
    public static StockChangeEvent of(Long productId, Integer quantity) {
        return new StockChangeEvent(productId, quantity);
    }

    @Override
    public String toString() {
        return "StockChangeEvent{" +
                "productId=" + productId +
                ", quantity=" + quantity +
                '}';
    }
}
