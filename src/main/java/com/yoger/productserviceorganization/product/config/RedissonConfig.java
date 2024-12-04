package com.yoger.productserviceorganization.product.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://localhost:6379") // Redis 서버 주소
                .setConnectTimeout(2000) // 연결 타임아웃 (밀리초 단위)
                .setTimeout(1000); // 일반 타임아웃 (밀리초 단위)

        return Redisson.create(config);
    }
}