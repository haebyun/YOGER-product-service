package com.yoger.productserviceorganization.product.application;

import com.yoger.productserviceorganization.product.adapters.web.dto.request.DemoProductRequestDTO;
import com.yoger.productserviceorganization.product.adapters.web.dto.request.UpdatedDemoProductRequestDTO;
import com.yoger.productserviceorganization.product.adapters.web.dto.response.DemoProductResponseDTO;
import com.yoger.productserviceorganization.product.adapters.web.dto.response.SellableProductResponseDTO;
import com.yoger.productserviceorganization.product.adapters.web.dto.response.SimpleDemoProductResponseDTO;
import com.yoger.productserviceorganization.product.adapters.web.dto.response.SimpleSellableProductResponseDTO;
import com.yoger.productserviceorganization.product.domain.model.PriceByQuantity;
import jakarta.validation.Valid;
import java.util.List;

public interface ProductService {
    List<SimpleSellableProductResponseDTO> findSimpleSellableProducts();

    DemoProductResponseDTO saveDemoProduct(Long creatorId, @Valid DemoProductRequestDTO demoProductRequestDTO);

    List<SimpleDemoProductResponseDTO> findSimpleDemoProducts();

    SellableProductResponseDTO findSellableProduct(Long productId);

    Boolean isDemoProduct(Long productId);

    DemoProductResponseDTO findDemoProduct(Long productId);

    DemoProductResponseDTO updateDemoProduct(Long productId, Long creatorId, UpdatedDemoProductRequestDTO updatedDemoProductRequestDTO);

    void updateDemoToSellable(Long productId, Long creatorId, List<PriceByQuantity> priceByQuantities);

    void deleteDemoProduct(Long productId, Long creatorId);

    void changeSellableProductStock(Long productId, Integer quantity);

    List<?> findSimpleDemoProductsByCreatorId(Long creatorId);
}
