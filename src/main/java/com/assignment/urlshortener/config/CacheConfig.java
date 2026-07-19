package com.assignment.urlshortner.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.assignment.urlshortner.model.UrlMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {

    @Bean
    public Cache<String, UrlMapping> urlCache() {
        return Caffeine.newBuilder()
                .initialCapacity(1000)
                .maximumSize(100_000)
                .expireAfterAccess(30, TimeUnit.DAYS)
                .recordStats() 
                .build();
    }
}