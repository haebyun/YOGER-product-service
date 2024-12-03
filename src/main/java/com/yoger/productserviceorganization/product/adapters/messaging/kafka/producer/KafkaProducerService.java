package com.yoger.productserviceorganization.product.adapters.messaging.kafka.producer;

import com.yoger.productserviceorganization.product.adapters.messaging.kafka.event.StockChangeEvent;
import com.yoger.productserviceorganization.product.domain.port.MQProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService implements MQProducerService {
    private static final String STOCK_CHANGE_TOPIC = "stock-change-topic";
    private final KafkaTemplate<String, StockChangeEvent> stockRelateTemplate;

    public void sendStockChangeEvent(StockChangeEvent event) {
        stockRelateTemplate.send(STOCK_CHANGE_TOPIC, event.productId().toString(), event);
    }
}
