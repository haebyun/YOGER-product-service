package com.yoger.productserviceorganization.product.adapters.web.dto.request;

import java.util.List;

public record ExpiredProductIdsDTO(
        List<Long> productIds
) {
}
