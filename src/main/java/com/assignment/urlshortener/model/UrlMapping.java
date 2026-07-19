package com.assignment.urlshortner.model;

import java.time.Instant;

public record UrlMapping(
    String shortCode, 
    String originalUrl, 
    Instant createdAt
) {}