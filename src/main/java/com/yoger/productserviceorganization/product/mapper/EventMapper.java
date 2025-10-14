package com.yoger.productserviceorganization.product.mapper;

import com.yoger.productserviceorganization.product.adapters.messaging.kafka.consumer.event.ConfirmProductReservationEvent;
import com.yoger.productserviceorganization.product.adapters.messaging.kafka.consumer.event.OrderCanceledEvent;
import com.yoger.productserviceorganization.product.adapters.messaging.kafka.consumer.event.OrderCreatedEvent;
import com.yoger.productserviceorganization.product.application.port.in.command.ConfirmProductReservationCommand;
import com.yoger.productserviceorganization.product.application.port.in.command.ConfirmProductReservationCommand.OrderItem;
import com.yoger.productserviceorganization.product.application.port.in.command.DeductStockCommand;
import com.yoger.productserviceorganization.product.application.port.in.command.DeductStockCommandFromOrder;
import com.yoger.productserviceorganization.product.application.port.in.command.IncreaseStockCommand;
import java.util.List;

public final class EventMapper {
    private EventMapper() {}

    public static DeductStockCommandFromOrder toCommand(OrderCreatedEvent event) {
        DeductStockCommand deductCommand = new DeductStockCommand(
                event.data().productId(),
                event.data().orderQuantity(),
                event.occurrenceDateTime()
        );
        return new DeductStockCommandFromOrder(event.orderId(), deductCommand);
    }

    public static IncreaseStockCommand toCommand(OrderCanceledEvent event){
        return new IncreaseStockCommand(
                event.data().productId(),
                event.data().orderQuantity(),
                event.occurrenceDateTime()
        );
    }

    public static ConfirmProductReservationCommand toConfirmProductReservationCommand(
            ConfirmProductReservationEvent event
    ) {
        List<OrderItem> items = event.data().orderItems()
                .stream()
                .map(orderItemData -> new ConfirmProductReservationCommand.OrderItem(
                        orderItemData.productId(),
                        orderItemData.quantity()
                ))
                .toList();

        return new ConfirmProductReservationCommand(
                event.orderId(),
                event.eventId(),
                event.data().userId(),
                items,
                event.occurrenceDateTime()
        );
    }
}
