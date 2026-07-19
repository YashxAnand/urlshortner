package com.assignment.urlshortner.repository;

import com.assignment.urlshortner.model.UrlMapping;
import java.util.Optional;

public interface UrlRepository {
    /**
     * Saves the mapping if the short code does not already exist.
     * @return true if saved successfully, false if a collision occurred.
     */
    boolean saveIfAbsent(UrlMapping mapping);

    /**
     * Retrieves the mapping by its short code.
     * @return an Optional containing the mapping if found.
     */
    Optional<UrlMapping> findByCode(String code);
}