package com.assignment.urlshortner.service;

public interface UrlService {
    String shortenUrl(String originalUrl, String customAlias);
    String getOriginalUrl(String shortCode);
}