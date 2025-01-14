package com.yoger.productserviceorganization.product.adapters.web.dto.request;

import com.yoger.productserviceorganization.product.adapters.web.dto.validation.ValidImage;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public record UpdatedDemoProductRequestDTO(
        @Size(min = 2, max = 50, message = "상품 이름은 2글자 이상 50글자 이하만 가능합니다.")
        @Pattern(
                regexp = "^[가-힣a-zA-Z0-9\\-\\_ ]+$",
                message = "상품 이름은 한글, 영어, 숫자, '-', '_' 만 사용할 수 있습니다."
        )
        String name,

        @Size(min = 10, max = 500, message = "상품 상세 설명은 10글자 이상 500글자 이하만 가능합니다.")
        String description,

        @ValidImage(emptyable = true)
        MultipartFile image,

        @ValidImage(emptyable = true)
        MultipartFile thumbnailImage
) {
}
