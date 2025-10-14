package com.yoger.productserviceorganization.product.adapters.messaging.kafka.consumer;

import com.yoger.productserviceorganization.global.config.TraceUtil;
import com.yoger.productserviceorganization.product.adapters.messaging.kafka.consumer.event.ConfirmProductReservationEvent;
import com.yoger.productserviceorganization.product.application.port.in.ConfirmProductReservationUseCase;
import com.yoger.productserviceorganization.product.application.port.in.command.ConfirmProductReservationCommand;
import com.yoger.productserviceorganization.product.mapper.EventMapper;
import io.opentelemetry.context.Scope;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class ConfirmProductReservationEventConsumer {

    private final EventDeduplicateService eventDeduplicateService;
    private final ConfirmProductReservationUseCase confirmProductReservationUseCase;

    @KafkaListener(
            topics = "${event.topic.order.created}",
            containerFactory = "kafkaConfirmProductReservationEventListenerContainerFactory"
    )
    public void consumeConfirmProductReservationEvent(
            final ConsumerRecord<String, ConfirmProductReservationEvent> record,
            final Acknowledgment ack
    ) {
        if (isSkippable(record)) {
            ack.acknowledge();
            return;
        }

        final ConfirmProductReservationEvent event = record.value();

        try (final Scope scope = TraceUtil.extractFromKafkaHeaders(record.headers()).makeCurrent()) {

            processMessage(event);

            handleSuccess(event, ack);

        } catch (Exception ex) {
            // 실패 시 로그를 남기고 예외를 다시 던져 재시도/DLQ 처리 유도
            log.error("Failed to process ConfirmProductReservationEvent. eventId: {}, orderId: {}. Triggering retry/DLQ.",
                    event.eventId(), event.orderId(), ex);
            throw ex;
        }
    }

    /**
     * 메시지 처리를 시작하기 전에 건너뛸 조건인지 확인합니다.
     * - 메시지 값이 null (tombstone 등)
     * - 이미 처리된 중복 이벤트
     */
    private boolean isSkippable(final ConsumerRecord<String, ConfirmProductReservationEvent> record) {
        if (record.value() == null) {
            log.warn("Received a record with a null value (tombstone record?). key: {}. Skipping.", record.key());
            return true;
        }
        if (eventDeduplicateService.isDuplicate(record.value().eventId())) {
            log.info("Skipping duplicate event. eventId: {}", record.value().eventId());
            return true;
        }
        return false;
    }

    private void processMessage(final ConfirmProductReservationEvent event) {
        final String tracingProps = TraceUtil.serializedTracingProperties();
        final ConfirmProductReservationCommand command = EventMapper.toConfirmProductReservationCommand(event);

        log.info("Processing product reservation confirmation. eventId: {}, orderId: {}", event.eventId(), event.orderId());
        confirmProductReservationUseCase.confirm(command, tracingProps);
    }

    private void handleSuccess(final ConfirmProductReservationEvent event, final Acknowledgment ack) {
        eventDeduplicateService.putKey(event.eventId());
        ack.acknowledge();
        log.info("Successfully processed and acknowledged event. eventId: {}, orderId: {}", event.eventId(), event.orderId());
    }
}
