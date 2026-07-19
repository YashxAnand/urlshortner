package com.assignment.urlshortner.service;

import com.assignment.urlshortner.exception.GlobalExceptionHandler.AliasAlreadyExistsException;
import com.assignment.urlshortner.exception.GlobalExceptionHandler.InvalidUrlException;
import com.assignment.urlshortner.model.UrlMapping;
import com.assignment.urlshortner.repository.UrlRepository;
import com.assignment.urlshortner.strategy.ShortCodeGenerator;
import com.assignment.urlshortner.util.UrlValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UrlServiceImplTest {

    @Mock
    private UrlRepository repository;
    @Mock
    private ShortCodeGenerator generator;
    @Mock
    private UrlValidator validator;

    private UrlServiceImpl urlService;

    @BeforeEach
    void setUp() {
        urlService = new UrlServiceImpl(repository, generator, validator);
        ReflectionTestUtils.setField(urlService, "domain", "http://localhost:8080/");
    }

    @Test
    void shortenUrl_ValidUrlWithoutAlias_ReturnsShortUrl() {
        String originalUrl = "https://google.com";
        String generatedCode = "abcd";
        
        when(validator.isValid(originalUrl)).thenReturn(true);
        when(generator.generate()).thenReturn(generatedCode);
        when(repository.saveIfAbsent(any(UrlMapping.class))).thenReturn(true);

        String result = urlService.shortenUrl(originalUrl, null);

        assertEquals("http://localhost:8080/abcd", result);
        
        ArgumentCaptor<UrlMapping> captor = ArgumentCaptor.forClass(UrlMapping.class);
        verify(repository).saveIfAbsent(captor.capture());
        assertEquals(generatedCode, captor.getValue().shortCode());
        assertEquals(originalUrl, captor.getValue().originalUrl());
    }

    @Test
    void shortenUrl_ValidUrlWithCustomAlias_ReturnsCustomShortUrl() {
        String originalUrl = "https://google.com";
        String customAlias = "myalias";
        
        when(validator.isValid(originalUrl)).thenReturn(true);
        when(repository.saveIfAbsent(any(UrlMapping.class))).thenReturn(true);

        String result = urlService.shortenUrl(originalUrl, customAlias);

        assertEquals("http://localhost:8080/myalias", result);
        verify(generator, never()).generate();
    }

    @Test
    void shortenUrl_InvalidUrl_ThrowsException() {
        String originalUrl = "invalid";
        when(validator.isValid(originalUrl)).thenReturn(false);

        assertThrows(InvalidUrlException.class, () -> urlService.shortenUrl(originalUrl, null));
        verify(repository, never()).saveIfAbsent(any());
    }

    @Test
    void shortenUrl_AliasAlreadyExists_ThrowsException() {
        String originalUrl = "https://google.com";
        String customAlias = "myalias";
        
        when(validator.isValid(originalUrl)).thenReturn(true);
        when(repository.saveIfAbsent(any(UrlMapping.class))).thenReturn(false);

        assertThrows(AliasAlreadyExistsException.class, () -> urlService.shortenUrl(originalUrl, customAlias));
    }

    @Test
    void getOriginalUrl_ExistingCode_ReturnsUrl() {
        String code = "abcd";
        String originalUrl = "https://google.com";
        UrlMapping mapping = new UrlMapping(code, originalUrl, java.time.Instant.now());
        
        when(repository.findByCode(code)).thenReturn(Optional.of(mapping));

        String result = urlService.getOriginalUrl(code);

        assertEquals(originalUrl, result);
    }

    @Test
    void getOriginalUrl_NonExistingCode_ReturnsNull() {
        String code = "abcd";
        when(repository.findByCode(code)).thenReturn(Optional.empty());

        String result = urlService.getOriginalUrl(code);

        assertNull(result);
    }
}
