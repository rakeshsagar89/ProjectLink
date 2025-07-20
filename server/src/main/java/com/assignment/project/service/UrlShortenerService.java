package com.assignment.project.service;

import com.assignment.project.model.ShortUrl;
import com.assignment.project.repository.ShortUrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UrlShortenerService {

    @Autowired
    private ShortUrlRepository repository;

    private static final int EXPIRY_MINUTES = 5;

    public String shortenUrl(String originalUrl) {
        if (!isValidUrl(originalUrl)) {
            throw new IllegalArgumentException("Invalid URL format");
        }

        ShortUrl existing = repository.findByOriginalUrl(originalUrl);

        if (existing != null) {
            // Check if it's expired
            boolean isExpired = Duration.between(existing.getCreatedAt(), LocalDateTime.now())
                    .toMinutes() >= EXPIRY_MINUTES;

            if (!isExpired) {
                // Not expired – return existing short code
                return existing.getShortCode();
            }

            // Expired – update with new short code and createdAt
            String newShortCode = UUID.randomUUID().toString().substring(0, 8);
            existing.setShortCode(newShortCode);
            existing.setCreatedAt(LocalDateTime.now());
            repository.save(existing);

            return newShortCode;
        }

        // Brand new URL – create new record
        String shortCode = UUID.randomUUID().toString().substring(0, 8);
        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setOriginalUrl(originalUrl);
        shortUrl.setShortCode(shortCode);
        shortUrl.setCreatedAt(LocalDateTime.now());
        repository.save(shortUrl);

        return shortCode;
    }


    public String getOriginalUrl(String shortCode) {
        ShortUrl shortUrl = repository.findByShortCode(shortCode);
        if (shortUrl == null) return "NOT_FOUND";

        Duration duration = Duration.between(shortUrl.getCreatedAt(), LocalDateTime.now());
        if (duration.toMinutes() >= EXPIRY_MINUTES) {
            return "EXPIRED";
        }

        return shortUrl.getOriginalUrl();
    }

    private boolean isValidUrl(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

