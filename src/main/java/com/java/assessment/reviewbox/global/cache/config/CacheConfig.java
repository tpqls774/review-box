package com.java.assessment.reviewbox.global.cache.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.java.assessment.reviewbox.global.security.jwt.properties.JwtProperties;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class CacheConfig {
    private final JwtProperties jwtProperties;

    @Bean
    public Cache<@NonNull Long, String> refreshTokenCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(jwtProperties.getRefreshExpiration(), TimeUnit.SECONDS)
                .maximumSize(10_000)
                .build();
    }
}
