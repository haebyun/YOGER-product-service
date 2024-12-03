package com.yoger.productserviceorganization.product.domain.port;

import com.yoger.productserviceorganization.product.adapters.messaging.kafka.event.StockChangeEvent;

public interface StockChangeUseCase {
    void handleStockChangeEvent(StockChangeEvent event);
}
