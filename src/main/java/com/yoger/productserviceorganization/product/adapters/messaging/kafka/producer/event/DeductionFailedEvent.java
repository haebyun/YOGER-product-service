package com.yoger.productserviceorganization.product.adapters.messaging.kafka.producer.event;

import com.yoger.productserviceorganization.product.application.port.in.DeductStockCommand;
import java.time.LocalDateTime;
import java.util.UUID;

public record DeductionFailedEvent(
        String eventId,
        Long productId,
        String eventType,
        DeductionFailedEventData data,
        LocalDateTime occurrenceDateTime

) {
    public record DeductionFailedEventData(
            Long orderId,
            Integer orderQuantity
    ) {
        private static DeductionFailedEventData of(Long orderId, Integer orderQuantity) {
            return new DeductionFailedEventData(orderId, orderQuantity);
        }
    }

    public static DeductionFailedEvent from(Long productId, DeductStockCommand deductStockCommand) {
        return new DeductionFailedEvent(
                UUID.randomUUID().toString(),
                productId,
                "InventoryDeductionFailed",
                DeductionFailedEventData.of(
                        deductStockCommand.orderId(),
                        deductStockCommand.orderQuantity()
                ),
                LocalDateTime.now()
        );
    }
}