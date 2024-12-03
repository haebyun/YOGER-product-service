package com.yoger.productserviceorganization.product.adapters.messaging.kafka.consumer;

import com.yoger.productserviceorganization.product.adapters.messaging.kafka.event.StockChangeEvent;
import com.yoger.productserviceorganization.product.domain.port.MQConsumerService;
import com.yoger.productserviceorganization.product.domain.port.StockChangeUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService implements MQConsumerService {
    private final StockChangeUseCase stockChangeUseCase;

    @Override
    @KafkaListener(topics = "stock-change-topic", groupId = "product-stock-group")
    public void listenStockChangeEvent(StockChangeEvent event) {
        stockChangeUseCase.handleStockChangeEvent(event);
    }
}
