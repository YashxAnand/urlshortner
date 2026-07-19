package com.assignment.urlshortner.dto;

import jakarta.validation.constraints.NotBlank;

public record ShortenRequest(
    @NotBlank(message = "URL cannot be empty") String originalUrl,
    String customAlias
) {}