package com.assignment.project.repository;

import com.assignment.project.model.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {
    ShortUrl findByShortCode(String shortCode);
    ShortUrl findByOriginalUrl(String originalUrl);
}

