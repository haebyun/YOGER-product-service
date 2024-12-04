package com.yoger.productserviceorganization.product.mapper;

import com.yoger.productserviceorganization.product.domain.model.Product;
import com.yoger.productserviceorganization.product.domain.model.ProductState;
import com.yoger.productserviceorganization.product.adapters.web.dto.request.DemoProductRequestDTO;
import com.yoger.productserviceorganization.product.adapters.persistence.jpa.ProductEntity;

public class ProductMapper {
    private ProductMapper() {}

    public static ProductEntity toEntityFrom(Product product) {
        return ProductEntity.of(
                product.getId(),
                product.getName(),
                product.getPriceByQuantities(),
                product.getDescription(),
                product.getImageUrl(),
                product.getThumbnailImageUrl(),
                product.getState(),
                product.getCreatorId(),
                product.getCreatorName(),
                product.getDueDate(),
                product.getInitialStockQuantity(),
                product.getStockQuantity()
        );
    }

    public static Product toDomainFrom(ProductEntity productEntity) {
        return Product.of(
                productEntity.getId(),
                productEntity.getName(),
                productEntity.getPriceByQuantities(),
                productEntity.getDescription(),
                productEntity.getImageUrl(),
                productEntity.getThumbnailImageUrl(),
                productEntity.getState(),
                productEntity.getCreatorId(),
                productEntity.getCreatorName(),
                productEntity.getDueDate(),
                productEntity.getInitialStockQuantity(),
                productEntity.getStockQuantity()
        );
    }

    public static Product toDomainFrom(
            Long creatorId,
            DemoProductRequestDTO demoProductRequestDTO,
            String imageUrl,
            String thumbnailImageUrl
    ) {
        return Product.of(
                null, // ID는 아직 생성되지 않았으므로 null
                demoProductRequestDTO.name(),
                null,
                demoProductRequestDTO.description(),
                imageUrl,
                thumbnailImageUrl,
                ProductState.DEMO,
                creatorId,
                demoProductRequestDTO.creatorName(),
                null,
                0,
                0
        );
    }
}
