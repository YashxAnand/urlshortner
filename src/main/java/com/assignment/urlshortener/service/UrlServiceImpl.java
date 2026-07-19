package com.assignment.urlshortner.service;

import com.assignment.urlshortner.exception.GlobalExceptionHandler.AliasAlreadyExistsException;
import com.assignment.urlshortner.exception.GlobalExceptionHandler.InvalidUrlException;
import com.assignment.urlshortner.model.UrlMapping;
import com.assignment.urlshortner.repository.UrlRepository;
import com.assignment.urlshortner.strategy.ShortCodeGenerator;
import com.assignment.urlshortner.util.UrlValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class UrlServiceImpl implements UrlService {

    private final UrlRepository repository;
    private final ShortCodeGenerator generator;
    private final UrlValidator validator;

    // Defaults to localhost:8080 if not set in application.properties
    @Value("${app.domain:http://localhost:8080/}")
    private String domain;

    public UrlServiceImpl(UrlRepository repository, ShortCodeGenerator generator, UrlValidator validator) {
        this.repository = repository;
        this.generator = generator;
        this.validator = validator;
    }

    @Override
    public String shortenUrl(String originalUrl, String customAlias) {
        if (!validator.isValid(originalUrl)) {
            throw new InvalidUrlException("Invalid URL format. Please provide a valid HTTP/HTTPS URL.");
        }

        // Use custom alias if provided, otherwise generate a new Base62 code
        String code = (customAlias != null && !customAlias.isBlank()) ? customAlias : generator.generate();
        
        UrlMapping mapping = new UrlMapping(code, originalUrl, Instant.now());

        // Attempt to save to the datastore
        boolean saved = repository.saveIfAbsent(mapping);
        if (!saved) {
            // If the code is already in the cache, throw an exception
            throw new AliasAlreadyExistsException("The alias '" + code + "' is already in use.");
        }

        return domain + code;
    }

    @Override
    public String getOriginalUrl(String shortCode) {
        return repository.findByCode(shortCode)
                .map(UrlMapping::originalUrl)
                .orElse(null);
    }
}