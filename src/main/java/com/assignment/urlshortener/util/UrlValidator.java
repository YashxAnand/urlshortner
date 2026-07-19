package com.assignment.urlshortner.util;

import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;

@Component
public class UrlValidator {
    
    public boolean isValid(String url) {
        if (url == null || url.isBlank()) {
            return false;
        }
        
        try {
            URI uri = new URI(url);
            return uri.getScheme() != null && 
                   (uri.getScheme().equalsIgnoreCase("http") || uri.getScheme().equalsIgnoreCase("https"));
        } catch (URISyntaxException e) {
            return false;
        }
    }
}