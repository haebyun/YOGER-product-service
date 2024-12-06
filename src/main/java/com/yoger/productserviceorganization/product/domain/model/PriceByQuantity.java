package com.yoger.productserviceorganization.product.domain.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PriceByQuantity(
        //TODO: jakarta와 관련된 어노테이션을 추후 모두 삭제 해야함..
        // 도메인 계층은 런타임에 작동하는 외부 프레임워크와 의존관계가 존재하면 안 됨(헥사고날 아키텍처).
        @NotNull(message = "수량에 대한 정보를 입력해주세요.")
        @Min(value = 0, message = "수량은 0 이상이어야 합니다.")
        int quantity,

        @NotNull(message = "가격에 대한 정보를 입력해주세요.")
        @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
        int price
) {
    public Boolean isLargerThenSoldAmount(Integer soldAmount) {
        return soldAmount <= this.quantity;
    }
}
