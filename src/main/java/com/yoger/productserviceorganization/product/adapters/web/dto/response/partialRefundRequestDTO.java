package com.yoger.productserviceorganization.product.adapters.web.dto.response;

public record partialRefundRequestDTO(
        Long productId,
        Integer originalMaxPrice,
        Integer confirmedPrice
) {
}
