package com.yoger.productserviceorganization.product.adapters.persistence.jpa;

import com.yoger.productserviceorganization.product.domain.model.ProductState;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaProductRepository extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findByState(ProductState state);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM ProductEntity p WHERE p.id = :id")
    Optional<ProductEntity> findByIdWithLock(Long id);

    @Modifying
    @Query("UPDATE ProductEntity p SET p.stockQuantity = p.stockQuantity + :quantity " +
            "WHERE p.id = :id AND p.state = 'SELLABLE' AND p.stockQuantity + :quantity >= 0")
    Integer updateStock(@Param("id") Long id, @Param("quantity") Integer quantity);

    List<ProductEntity> findAllByCreatorId(Long creatorId);
}
