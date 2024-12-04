package com.yoger.productserviceorganization.product.application;

import com.yoger.productserviceorganization.product.adapters.persistence.UpdateLocation;
import com.yoger.productserviceorganization.product.adapters.web.dto.request.DemoProductRequestDTO;
import com.yoger.productserviceorganization.product.adapters.web.dto.request.UpdatedDemoProductRequestDTO;
import com.yoger.productserviceorganization.product.adapters.web.dto.response.DemoProductResponseDTO;
import com.yoger.productserviceorganization.product.adapters.web.dto.response.SellableProductResponseDTO;
import com.yoger.productserviceorganization.product.adapters.web.dto.response.SimpleDemoProductResponseDTO;
import com.yoger.productserviceorganization.product.adapters.web.dto.response.SimpleSellableProductResponseDTO;
import com.yoger.productserviceorganization.product.domain.exception.InvalidStockException;
import com.yoger.productserviceorganization.product.domain.model.PriceByQuantity;
import com.yoger.productserviceorganization.product.domain.model.Product;
import com.yoger.productserviceorganization.product.domain.model.ProductState;
import com.yoger.productserviceorganization.product.domain.port.ImageStorageService;
import com.yoger.productserviceorganization.product.domain.port.ProductRepository;
import com.yoger.productserviceorganization.product.mapper.ProductMapper;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ImageStorageService imageStorageService;

    public List<SimpleSellableProductResponseDTO> findSimpleSellableProducts() {
        return productRepository.findByState(ProductState.SELLABLE).stream()
                .map(this::mapToSellableDTO)
                .toList();
    }

    private SimpleSellableProductResponseDTO mapToSellableDTO(Product product) {
        return SimpleSellableProductResponseDTO.from(product);
    }

    @Transactional
    public DemoProductResponseDTO saveDemoProduct(@Valid DemoProductRequestDTO demoProductRequestDTO) {
        String imageUrl = imageStorageService.uploadImage(demoProductRequestDTO.image());
        String thumbnailImageUrl = imageStorageService.uploadImage(demoProductRequestDTO.thumbnailImage());

        registerTransactionSynchronizationForImageDeletion(imageUrl, thumbnailImageUrl);

        Product product = ProductMapper.toDomainFrom(demoProductRequestDTO, imageUrl, thumbnailImageUrl);
        return DemoProductResponseDTO.from(productRepository.save(product));
    }

    private void registerTransactionSynchronizationForImageDeletion(String imageUrl, String thumbnailImageUrl) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCompletion(int status) {
                if (status == TransactionSynchronization.STATUS_ROLLED_BACK) {
                    deleteUploadedImages(imageUrl, thumbnailImageUrl);
                }
            }
        });
    }

    private void deleteUploadedImages(String... imageUrls) {
        for (String imageUrl : imageUrls) {
            imageStorageService.deleteImage(imageUrl);
        }
    }

    public List<SimpleDemoProductResponseDTO> findSimpleDemoProducts() {
        return productRepository.findByState(ProductState.DEMO).stream()
                .map(this::mapToDemoDTO)
                .toList();
    }

    private SimpleDemoProductResponseDTO mapToDemoDTO(Product product) {
        return SimpleDemoProductResponseDTO.from(product);
    }

    public SellableProductResponseDTO findSellableProduct(Long productId) {
        Product product = productRepository.findById(productId);
        product.validateUnexpectedState(ProductState.SELLABLE);
        return SellableProductResponseDTO.from(product);
    }

    public Boolean isDemoProduct(Long productId) {
        return productRepository.findById(productId).isDemo();
    }

    public DemoProductResponseDTO findDemoProduct(Long productId) {
        Product product = productRepository.findById(productId);
        product.validateUnexpectedState(ProductState.DEMO);
        return DemoProductResponseDTO.from(product);
    }

    @Transactional
    public DemoProductResponseDTO updateDemoProduct(
            Long productId,
            Long creatorId,
            UpdatedDemoProductRequestDTO updatedDemoProductRequestDTO
    ) {
        Product product = productRepository.findByIdWithLock(productId);
        product.validateUnexpectedState(ProductState.DEMO);
        product.validateCreatorId(creatorId);

        String updatedProductName = product.getName();
        if (updatedDemoProductRequestDTO.name() != null) {
            updatedProductName = updatedDemoProductRequestDTO.name();
        }
        String updatedProductDescription = product.getDescription();
        if (updatedDemoProductRequestDTO.description() != null) {
            updatedProductDescription = updatedDemoProductRequestDTO.description();
        }
        String updatedImageUrl = imageStorageService.updateImage(
                updatedDemoProductRequestDTO.image(),
                product.getImageUrl()
        );
        String updatedThumbnailImageUrl = imageStorageService.updateImage(
                updatedDemoProductRequestDTO.thumbnailImage(),
                product.getThumbnailImageUrl()
        );
        Product updatedProduct = Product.updatedDemoProduct(
                product,
                updatedProductName,
                updatedProductDescription,
                updatedImageUrl,
                updatedThumbnailImageUrl
        );

        Product savedProduct = productRepository.save(updatedProduct);
        return DemoProductResponseDTO.from(savedProduct);
    }

    @Transactional
    public void updateDemoToSellable(Long productId, Long creatorId, List<PriceByQuantity> priceByQuantities) {
        Product demoProduct = productRepository.findById(productId);
        demoProduct.validateCreatorId(creatorId);

        Product sellableProduct = Product.toSellableFrom(demoProduct, priceByQuantities, LocalDate.MAX.atStartOfDay());
        productRepository.save(sellableProduct);
    }


    @Transactional
    public void deleteDemoProduct(Long productId, Long creatorId) {
        Product product = productRepository.findByIdWithLock(productId);
        product.validateUnexpectedState(ProductState.DEMO);
        product.validateCreatorId(creatorId);

        imageStorageService.deleteImage(product.getImageUrl());
        imageStorageService.deleteImage(product.getThumbnailImageUrl());
        productRepository.deleteById(productId);
    }

    @Transactional
    public void changeSellableProductStock(Long productId, Integer quantity) {
        UpdateLocation updateLocation = productRepository.updateStock(productId, quantity);
        if(UpdateLocation.ERROR.equals(updateLocation)) {
            throw new InvalidStockException("상품이 판매가능 하지 않거나, 상품의 재고가 부족합니다.");
        }
    }
}
