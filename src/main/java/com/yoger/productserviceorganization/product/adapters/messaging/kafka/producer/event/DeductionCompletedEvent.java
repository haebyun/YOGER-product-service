package com.yoger.productserviceorganization.product.adapters.messaging.kafka.producer.event;

import com.yoger.productserviceorganization.product.application.port.in.command.DeductStockCommandFromOrderEvent;
import java.time.LocalDateTime;
import java.util.UUID;

public record DeductionCompletedEvent(
        String eventId,
        Long productId,
        String eventType,
        DeductionCompletedEventData data,
        LocalDateTime occurrenceDateTime

) {
    public record DeductionCompletedEventData(
            Long orderId,
            Integer orderQuantity
    ) {
        private static DeductionCompletedEventData of(Long orderId, Integer orderQuantity) {
            return new DeductionCompletedEventData(orderId, orderQuantity);
        }
    }

    public static DeductionCompletedEvent from(Long productId, DeductStockCommandFromOrderEvent deductStockCommandFromOrderEvent) {
        return new DeductionCompletedEvent(
                UUID.randomUUID().toString(),
                productId,
                "deductionCompleted",
                DeductionCompletedEventData.of(
                        deductStockCommandFromOrderEvent.orderId(),
                        deductStockCommandFromOrderEvent.deductStockCommand().quantity()
                ),
                LocalDateTime.now()
        );
    }
}
