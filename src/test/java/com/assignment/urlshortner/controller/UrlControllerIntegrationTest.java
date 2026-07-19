package com.assignment.urlshortner.controller;

import com.assignment.urlshortner.dto.ShortenRequest;
import com.assignment.urlshortner.dto.ShortenResponse;
import com.assignment.urlshortener.UrlshortenerApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = UrlshortenerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UrlControllerIntegrationTest {

    @LocalServerPort
    private int port;

    private final RestTemplate restTemplate = new RestTemplate();

    private String getBaseUrl() {
        return "http://localhost:" + port;
    }

    @Test
    void shortenUrl_ValidRequest_ReturnsShortUrl() {
        ShortenRequest request = new ShortenRequest("https://example.com", null);
        ResponseEntity<ShortenResponse> response = restTemplate.postForEntity(getBaseUrl() + "/shorten", request, ShortenResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().shortUrl());
    }

    @Test
    void shortenUrl_WithCustomAlias_ReturnsCustomShortUrl() {
        ShortenRequest request = new ShortenRequest("https://example.com", "custom123");
        ResponseEntity<ShortenResponse> response = restTemplate.postForEntity(getBaseUrl() + "/shorten", request, ShortenResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().shortUrl().endsWith("custom123"));
    }

    @Test
    void shortenUrl_DuplicateAlias_ReturnsConflict() {
        ShortenRequest request1 = new ShortenRequest("https://example.com", "duplicate");
        ShortenRequest request2 = new ShortenRequest("https://example2.com", "duplicate");

        restTemplate.postForEntity(getBaseUrl() + "/shorten", request1, ShortenResponse.class);
        
        try {
            restTemplate.postForEntity(getBaseUrl() + "/shorten", request2, String.class);
            fail("Expected exception");
        } catch (HttpClientErrorException ex) {
            assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
        }
    }

    @Test
    void redirect_NonExistingCode_ReturnsNotFound() {
        try {
            restTemplate.getForEntity(getBaseUrl() + "/nonexisting", Void.class);
            fail("Expected exception");
        } catch (HttpClientErrorException ex) {
            assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        }
    }
}


