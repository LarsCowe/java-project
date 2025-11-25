package org.example.javaproject.service;

import org.example.javaproject.model.Match;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;



@Service
public class MatchScraperScheduler {

    private final VolleyScraperService scraperService;
    private final MatchBroadcastService broadcastService;

    public MatchScraperScheduler(VolleyScraperService scraperService, MatchBroadcastService broadcastService) {
        this.scraperService = scraperService;
        this.broadcastService = broadcastService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onStartup() {
        System.out.println("Application ready - performing initial scrape");
        scrapeAndBroadcast();
    }

    // Scrape elke 30 seconden
    @Scheduled(fixedDelayString = "30000")
    public void scheduledScrape() {
        System.out.println("Scheduled scrape triggered");
        scrapeAndBroadcast();
    }

    private void scrapeAndBroadcast() {
        try {
            Match match = scraperService.scrapeMatch();

            if (match != null) {
                broadcastService.broadcastIfChanged(match);
            } else {
                System.out.println("Scrape returned null");
            }
        } catch (Exception e) {
            System.out.println("Scheduler returned null");
        }
    }
}
