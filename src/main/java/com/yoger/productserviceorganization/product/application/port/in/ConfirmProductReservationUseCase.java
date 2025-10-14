package com.yoger.productserviceorganization.product.application.port.in;

import com.yoger.productserviceorganization.product.application.port.in.command.ConfirmProductReservationCommand;

/**
 * 결제 성공 후 주문서비스가 발행한 ConfirmProductReservation 이벤트를 처리하여
 * 재고 확정(차감 확정/실패)을 수행하는 유스케이스 포트.
 *
 * 구현부에서는 아이템 단위 분해, productId 기준 그룹핑, 청크 트랜잭션, 멱등 처리 등을 수행한다.
 */
public interface ConfirmProductReservationUseCase {

    /**
     * Confirm 이벤트 처리 진입점.
     *
     * @param command        주문/아이템 정보
     * @param tracingProps   직렬화된 트레이싱 컨텍스트(예: W3C traceparent 등이 Properties 문자열로 포함)
     */
    void confirm(ConfirmProductReservationCommand command, String tracingProps);
}
