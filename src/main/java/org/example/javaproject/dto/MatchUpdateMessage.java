package org.example.javaproject.dto;

import org.example.javaproject.model.SetScore;

import java.time.LocalDateTime;
import java.util.List;

public class MatchUpdateMessage {
    private String homeTeam;
    private String awayTeam;
    private int homeScore;
    private int awayScore;
    private List<SetScore> sets;
    private LocalDateTime timestamp;
    private String message;

    public MatchUpdateMessage() {
        this.timestamp = LocalDateTime.now();
    }

    public MatchUpdateMessage(List<SetScore> sets, int awayScore, int homeScore, String awayTeam, String homeTeam, String message) {
        this.sets = sets;
        this.awayScore = awayScore;
        this.homeScore = homeScore;
        this.awayTeam = awayTeam;
        this.homeTeam = homeTeam;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(int homeScore) {
        this.homeScore = homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(int awayScore) {
        this.awayScore = awayScore;
    }

    public List<SetScore> getSets() {
        return sets;
    }

    public void setSets(List<SetScore> sets) {
        this.sets = sets;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
