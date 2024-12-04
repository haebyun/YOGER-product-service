package com.yoger.productserviceorganization.product.application;


import com.yoger.productserviceorganization.product.adapters.persistence.jpa.JpaProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.ValueOperations;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockWriteBackService {

    private static final String PRODUCT_ENTITY_STOCK = "productEntityStock : ";

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedissonClient redissonClient; // Redisson 클라이언트 주입
    private final JpaProductRepository jpaProductRepository;

    @Scheduled(fixedRate = 10000)
    @Transactional
    public void writeBackStock() {
        log.info("Write-Back 작업 시작");
        ScanOptions scanOptions = ScanOptions.scanOptions()
                .match(PRODUCT_ENTITY_STOCK + "*")
                .count(1000)
                .build();

        try (Cursor<byte[]> cursor = redisTemplate.executeWithStickyConnection(
                (RedisConnection connection) -> connection.scan(scanOptions))) {
            while (cursor.hasNext()) {
                String key = new String(cursor.next(), StandardCharsets.UTF_8);
                log.debug("Processing key: {}", key);
                processKeyWithRedissonLock(key);
            }
        } catch (Exception e) {
            // 로깅 프레임워크를 사용하여 예외 로깅
            log.error("Write-Back 작업 중 오류 발생", e);
        }
        log.info("Write-back 작업 종료");
    }

    private void processKeyWithRedissonLock(String key) {
        String lockName = "lock:" + key;
        RLock lock = redissonClient.getLock(lockName);
        boolean isLocked = false;

        try {
            // 락 획득 시도 (최대 1초 대기, 락 유지 시간 5초)
            isLocked = lock.tryLock(1, 5, TimeUnit.SECONDS);
            if (isLocked) {
                // Redis에서 수량 변화 가져오기
                ValueOperations<String, Object> valueOps = redisTemplate.opsForValue();
                Object quantityObj = valueOps.get(key);

                if (quantityObj instanceof Number) {
                    Long quantity = ((Number) quantityObj).longValue();
                    Long productId = extractProductIdFromKey(key);

                    if (productId != null) {
                        // DB에서 재고 업데이트
                        jpaProductRepository.changeStock(productId, quantity.intValue());
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // 인터럽트 상태 복원
            e.printStackTrace();
        } catch (Exception e) {
            // 적절한 로깅 처리
            e.printStackTrace();
        } finally {
            if (isLocked && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    /**
     * Redis 키에서 상품 ID를 추출합니다.
     * 키 형식은 "PRODUCT_ENTITY_STOCK:<productId>"라고 가정합니다.
     *
     * @param key Redis 키
     * @return 추출된 상품 ID 또는 파싱 실패 시 null
     */
    private Long extractProductIdFromKey(String key) {
        try {
            String[] parts = key.split(" : ");
            if (parts.length == 2) {
                return Long.parseLong(parts[1]);
            }
        } catch (NumberFormatException e) {
            // 에러 로깅 처리
            e.printStackTrace();
        }
        return null;
    }
}
