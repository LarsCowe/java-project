package org.example.javaproject.controller;

import org.example.javaproject.model.Match;
import org.example.javaproject.service.VolleyScraperService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final VolleyScraperService scraperService;

    public TestController(VolleyScraperService scraperService) {
        this.scraperService = scraperService;
    }

    // REST API endpoint om de scraper te testen
    @GetMapping("/test/scrape")
    public ResponseEntity<Match> testScrape() {
        Match match = scraperService.scrapeMatch();
        if (match != null) {
            return ResponseEntity.ok(match);
        }
        return ResponseEntity.internalServerError().build();
    }
}