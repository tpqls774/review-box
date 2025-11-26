package com.java.assessment.reviewbox.domain.auth.persistence.repository;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RefreshTokenStorage {
    private final Cache<@NonNull Long, String> cache;

    public void save(long userId, String refreshToken) {
        cache.put(userId, refreshToken);
    }

    public String findByUserId(long userId) {
        return cache.getIfPresent(userId);
    }

    public void deleteByUserId(long userId) {
        cache.invalidate(userId);
    }
}
