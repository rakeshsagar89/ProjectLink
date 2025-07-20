package com.assignment.project.controller;

import com.assignment.project.service.UrlShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UrlShortenerController {


    @Autowired
    private UrlShortenerService service;

    @PostMapping("/shorten")
    public ResponseEntity<String> shorten(@RequestBody Map<String, String> request) {
        try {
            String url = request.get("originalUrl");
            String shortCode = service.shortenUrl(url);
            return ResponseEntity.ok("http://localhost:7070/" + shortCode);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid URL format");
        }
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<Map<String, String>> redirect(@PathVariable String shortUrl) {
        Map<String, String> response = new HashMap<>();
        String originalUrl = service.getOriginalUrl(shortUrl);
        switch (originalUrl) {
            case "NOT_FOUND":
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            case "EXPIRED":
                return ResponseEntity.status(HttpStatus.GONE).build();
            default:
                HttpHeaders headers = new HttpHeaders();
                headers.setLocation(URI.create(originalUrl));
                return new ResponseEntity<>(headers, HttpStatus.FOUND);
        }
    }
}
