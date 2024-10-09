package com.yoger.productserviceorganization.product.mapper;

import com.yoger.productserviceorganization.product.domain.model.ProductState;
import com.yoger.productserviceorganization.product.dto.request.DemoProductRequestDTO;
import com.yoger.productserviceorganization.product.persistence.ProductEntity;

public class ProductMapper {
    public static ProductEntity toPersistenceFrom(
            DemoProductRequestDTO productRequestDTO,
            String imageUrl,
            String thumbnailImageUrl
    ) {
        return ProductEntity.of(
                productRequestDTO.name(),
                null, // 나중에 설정
                productRequestDTO.description(),
                imageUrl,
                ProductState.DEMO,
                thumbnailImageUrl,
                productRequestDTO.creatorId(),
                productRequestDTO.creatorName(),
                null,
                0,
                0
        );
    }
}
