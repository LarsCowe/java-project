package org.example.javaproject.controller;

import org.example.javaproject.model.Match;
import org.example.javaproject.service.MatchBroadcastService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MatchRestController {

    private final MatchBroadcastService broadcastService;

    public MatchRestController(MatchBroadcastService broadcastService) {
        this.broadcastService = broadcastService;
    }

    // API Endpoint voor huidige match data
    @GetMapping("/match")
    public ResponseEntity<Match> getCurrentMatch() {
        Match match = broadcastService.getCurrentMatch();
        if (match != null) {
            return ResponseEntity.ok(match);
        }
        return ResponseEntity.notFound().build();
    }

    // API endpoint voor check van broadcast service
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "UP");
        status.put("service", "Match Tracker");
        status.put("hasMatchData", broadcastService.getCurrentMatch() != null);
        return ResponseEntity.ok(status);
    }


}
