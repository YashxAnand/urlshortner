package com.assignment.urlshortner.strategy;

import org.springframework.stereotype.Component;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class Base62GeneratorStrategy implements ShortCodeGenerator {
    
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int BASE = ALPHABET.length();
    
    // Starting at 100,000 avoids generating very short (1-2 character) URLs initially.
    private final AtomicLong counter = new AtomicLong(100_000L);

    @Override
    public String generate() {
        long id = counter.incrementAndGet();
        return encodeBase62(id);
    }

    private String encodeBase62(long value) {
        StringBuilder sb = new StringBuilder();
        while (value > 0) {
            sb.append(ALPHABET.charAt((int) (value % BASE)));
            value /= BASE;
        }
        return sb.reverse().toString();
    }
}