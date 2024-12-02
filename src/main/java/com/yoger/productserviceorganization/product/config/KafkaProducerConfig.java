package com.yoger.productserviceorganization.product.config;

import com.yoger.productserviceorganization.product.adapters.messaging.kafka.event.StockChangeEvent;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class KafkaProducerConfig {
    @Bean
    public ProducerFactory<String, StockChangeEvent> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        // Kafka 브로커 주소 설정
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        // 키 직렬화기 설정
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        // 벨류 직렬화기로 JSON 직렬화기 설정
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        // 추가 설정 가능 (예: 리트라이, 배치 크기 등)
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, StockChangeEvent> stockRelateTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
