package com.yoger.productserviceorganization.product.adapters.messaging.kafka.consumer.event;

import com.yoger.productserviceorganization.product.adapters.messaging.kafka.consumer.event.data.OrderItemData;
import java.time.LocalDateTime;
import java.util.List;

public record ConfirmProductReservationEvent(
        String orderId,
        String eventId,
        String eventType,
        ConfirmProductReservationData data,
        LocalDateTime occurrenceDateTime
) {
    public record ConfirmProductReservationData(
            Long userId,
            List<OrderItemData> orderItems
    ) {
        public static ConfirmProductReservationData of(
                Long userId,
                List<OrderItemData> orderItems
        ) {
            return new ConfirmProductReservationData(userId, List.copyOf(orderItems));
        }
    }
}
