package com.assignment.urlshortner.controller;

import com.assignment.urlshortner.dto.ShortenRequest;
import com.assignment.urlshortner.dto.ShortenResponse;
import com.assignment.urlshortner.service.UrlService;
import io.micrometer.core.annotation.Timed;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/shorten")
    @Timed(value = "url.shorten.requests", description = "Time taken to generate a short URL")
    public ResponseEntity<ShortenResponse> shortenUrl(@Valid @RequestBody ShortenRequest request) {
        String shortUrl = urlService.shortenUrl(request.originalUrl(), request.customAlias());
        return ResponseEntity.ok(new ShortenResponse(shortUrl));
    }

    @GetMapping("/{code}")
    @Timed(value = "url.redirect.requests", description = "Time taken to process redirects")
    public ResponseEntity<Void> redirect(@PathVariable String code) {
        String originalUrl = urlService.getOriginalUrl(code);
        
        if (originalUrl == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
                .location(URI.create(originalUrl))
                .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, max-age=0, must-revalidate")
                .build();
    }
}