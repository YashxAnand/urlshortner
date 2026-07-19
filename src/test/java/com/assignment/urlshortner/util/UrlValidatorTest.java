package com.assignment.urlshortner.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UrlValidatorTest {
    private final UrlValidator validator = new UrlValidator();

    @Test
    void isValid_ValidHttpUrl_ReturnsTrue() {
        assertTrue(validator.isValid("http://example.com"));
    }

    @Test
    void isValid_ValidHttpsUrl_ReturnsTrue() {
        assertTrue(validator.isValid("https://example.com"));
    }

    @Test
    void isValid_InvalidUrl_ReturnsFalse() {
        assertFalse(validator.isValid("invalid-url"));
        assertFalse(validator.isValid("ftp://example.com"));
        assertFalse(validator.isValid(""));
        assertFalse(validator.isValid(null));
    }
}
