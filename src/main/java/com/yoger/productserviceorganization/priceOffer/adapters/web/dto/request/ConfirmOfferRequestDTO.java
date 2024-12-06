package com.yoger.productserviceorganization.priceOffer.adapters.web.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record ConfirmOfferRequestDTO(
        @NotNull
        Long companyId,

        @NotNull
        LocalDateTime dueDate
) {
}
