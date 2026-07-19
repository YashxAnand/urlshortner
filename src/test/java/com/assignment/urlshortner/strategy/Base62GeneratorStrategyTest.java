package com.assignment.urlshortner.strategy;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class Base62GeneratorStrategyTest {
    private final Base62GeneratorStrategy strategy = new Base62GeneratorStrategy();

    @Test
    void generate_ReturnsUniqueCodes() {
        String code1 = strategy.generate();
        String code2 = strategy.generate();
        assertNotNull(code1);
        assertNotNull(code2);
        assertNotEquals(code1, code2);
    }
}
