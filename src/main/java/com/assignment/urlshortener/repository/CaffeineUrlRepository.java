package com.assignment.urlshortner.repository;

import com.github.benmanes.caffeine.cache.Cache;
import com.assignment.urlshortner.model.UrlMapping;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CaffeineUrlRepository implements UrlRepository {

    private final Cache<String, UrlMapping> cache;

    public CaffeineUrlRepository(Cache<String, UrlMapping> cache) {
        this.cache = cache;
    }

    @Override
    public boolean saveIfAbsent(UrlMapping mapping) {
        // asMap().putIfAbsent is an atomic, thread-safe operation in Caffeine.
        // It returns null if there was no existing mapping (success).
        return cache.asMap().putIfAbsent(mapping.shortCode(), mapping) == null;
    }

    @Override
    public Optional<UrlMapping> findByCode(String code) {
        // getIfPresent returns null if the cache doesn't contain the key.
        // Optional.ofNullable safely wraps this result.
        return Optional.ofNullable(cache.getIfPresent(code));
    }
}