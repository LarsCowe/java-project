package org.example.javaproject.service;

import org.example.javaproject.dto.MatchUpdateMessage;
import org.example.javaproject.model.Match;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicReference;

@Service
public class MatchBroadcastService {
    private static final String TOPIC = "/topic/match";

    private final SimpMessagingTemplate messagingTemplate;

    private final AtomicReference<Match> lastMatchState = new AtomicReference<>();

    public MatchBroadcastService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void broadcastIfChanged(Match newMatch) {
        if (newMatch == null) {
            System.out.println("Null match: skipping");
            return;
        }

        Match previous = lastMatchState.get();

        if (previous == null || !previous.equals(newMatch)) {
            broadcastUpdate(newMatch);
            lastMatchState.set(newMatch);
            System.out.println("Match changed: broadcasted update");
        } else {
            System.out.println("No changes - skipping broadcast");
        }
    }

    public void broadcastUpdate(Match match) {
        if (match == null) {
            return;
        }

        MatchUpdateMessage message = new MatchUpdateMessage(
                match.getSets(),
                match.getHomeScore(),
                match.getAwayScore(),
                match.getHomeTeam(),
                match.getAwayTeam(),
                "Match update"
        );


        messagingTemplate.convertAndSend(TOPIC, message);
        System.out.println("Sent to " + TOPIC);

    }

    public Match getCurrentMatch() {
        return lastMatchState.get();
    }


}
