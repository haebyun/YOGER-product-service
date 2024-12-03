package com.yoger.productserviceorganization.product.application;

import com.yoger.productserviceorganization.product.adapters.messaging.kafka.event.StockChangeEvent;
import com.yoger.productserviceorganization.product.domain.exception.InvalidStockException;
import com.yoger.productserviceorganization.product.domain.port.ProductRepository;
import com.yoger.productserviceorganization.product.domain.port.StockChangeUseCase;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockChangeService implements StockChangeUseCase {
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public void handleStockChangeEvent(StockChangeEvent event) {
        Integer flag = productRepository.updateStockInDB(event.productId(), event.quantity());
        if (flag == 0) {
            throw new InvalidStockException("상품이 판매가능 하지 않거나, 상품의 재고가 부족합니다.");
        }
    }
}
