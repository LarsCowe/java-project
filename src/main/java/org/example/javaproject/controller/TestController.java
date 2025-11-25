package org.example.javaproject.controller;

import org.example.javaproject.model.Match;
import org.example.javaproject.service.MatchBroadcastService;
import org.example.javaproject.service.VolleyScraperService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final VolleyScraperService scraperService;
    private final MatchBroadcastService broadcastService;

    public TestController(VolleyScraperService scraperService, MatchBroadcastService broadcastService) {
        this.scraperService = scraperService;
        this.broadcastService = broadcastService;
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

    // REST API endpoint om te broadcasten
    @GetMapping("/test/broadcast")
    public ResponseEntity<String> testBroadcast() {
        Match match = scraperService.scrapeMatch();
        if (match != null) {
            broadcastService.broadcastUpdate(match);
            return ResponseEntity.ok("Broadcasted!");
        }
        return ResponseEntity.internalServerError().body("Scraping failed");
    }
}