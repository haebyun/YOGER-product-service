package com.yoger.productserviceorganization.product.adapters.persistence;

import com.yoger.productserviceorganization.product.adapters.persistence.jpa.JpaProductRepository;
import com.yoger.productserviceorganization.product.adapters.persistence.jpa.ProductEntity;
import com.yoger.productserviceorganization.product.domain.port.ProductRepository;
import com.yoger.productserviceorganization.product.domain.exception.ProductNotFoundException;
import com.yoger.productserviceorganization.product.domain.model.Product;
import com.yoger.productserviceorganization.product.domain.model.ProductState;
import com.yoger.productserviceorganization.product.mapper.ProductMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {
    private final JpaProductRepository jpaProductRepository;

    @Override
    public Product findById(Long productId) {
        ProductEntity productEntity = jpaProductRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        return ProductMapper.toDomainFrom(productEntity);
    }

    @Override
    public Product save(Product product) {
        ProductEntity productEntity = ProductMapper.toEntityFrom(product);
        ProductEntity savedEntity = jpaProductRepository.save(productEntity);
        return ProductMapper.toDomainFrom(savedEntity);
    }

    @Override
    public List<Product> findByState(ProductState state) {
        return jpaProductRepository.findByState(state).stream()
                .map(ProductMapper::toDomainFrom)
                .toList();
    }

    @Override
    public List<Product> findAll() {
        return jpaProductRepository.findAll().stream()
                .map(ProductMapper::toDomainFrom)
                .toList();  //toList() 메서드는 불변 리스트를 반환 만약 변경 해야하는 상황이 발생하면 수정 필요
    }

    @Override
    public void deleteById(Long productId) {
        jpaProductRepository.deleteById(productId);
    }

    @Override
    public Product findByIdWithLock(Long productId) {
        ProductEntity productEntity = jpaProductRepository.findByIdWithLock(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        return ProductMapper.toDomainFrom(productEntity);
    }

    @Override
    public Integer updateStock(Long productId, Integer quantity) {
        return jpaProductRepository.updateStock(productId, quantity);
    }

    @Override
    public Boolean existsById(Long productId) {
        return jpaProductRepository.existsById(productId);
    }

    @Override
    public List<Product> findByCreatorId(Long creatorId) {
        return jpaProductRepository.findAllByCreatorId(creatorId)
                .stream()
                .map(ProductMapper::toDomainFrom)
                .toList();
    }
}
