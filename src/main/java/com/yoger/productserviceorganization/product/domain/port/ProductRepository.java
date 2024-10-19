package com.yoger.productserviceorganization.product.domain.port;

import com.yoger.productserviceorganization.product.domain.model.Product;
import com.yoger.productserviceorganization.product.domain.model.ProductState;
import java.util.List;

public interface ProductRepository {
    Product findById(Long id);

    Product save(Product product);

    List<Product> findByState(ProductState state);

    List<Product> findAll();

    void deleteById(Long productId);
}
