package org.example.javaproject.service;

import org.example.javaproject.model.Match;
import org.example.javaproject.model.SetScore;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class VolleyScraperService {
    private String matchUrl = "https://volleymatch-livescore-webclient.vercel.app/match/volleyball-match-2025";

    public Match scrapeMatch() {
        try {
            System.out.println(("Scraping match data from: {}" + matchUrl));

            Document document = Jsoup.connect(matchUrl)
                    .userAgent("Mozilla/5.0")
                    .timeout(10000)
                    .get();

            Match match = new Match();

            Elements teamElements = document.select("[data-team-name]");
            if (teamElements.size() >= 2) {
                match.setHomeTeam(teamElements.get(0).attr("data-team-name"));
                match.setAwayTeam(teamElements.get(1).attr("data-team-name"));
            }

            Elements scoreElements = document.select("[data-sets]");
            if (scoreElements.size() >= 2) {
                try {
                    match.setHomeScore(Integer.parseInt(scoreElements.get(0).attr("data-sets")));
                    match.setAwayScore(Integer.parseInt(scoreElements.get(1).attr("data-sets")));
                } catch (NumberFormatException e) {
                    System.out.println(("Could not parse overall scores: " + e.getMessage()));
                }
            }

            List<SetScore> sets = new ArrayList<>();

            Elements setContainers = document.select("[data-content='set-history'] [data-set-number]");
            for (Element setContainer : setContainers) {
                try {
                    int setNumber = Integer.parseInt(setContainer.attr("data-set-number"));
                    Elements teamScores = setContainer.select("[data-score]");
                    if (teamScores.size() >= 2) {
                        int homeSetScore = Integer.parseInt(teamScores.get(0).attr("data-score"));
                        int awaySetScore = Integer.parseInt(teamScores.get(1).attr("data-score"));
                        sets.add(new SetScore(setNumber, homeSetScore, awaySetScore));
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Could not parse completed set score: "+ e.getMessage());
                }
            }

            Elements liveScoreElements = document.select(".grid [data-score][data-team]");
            if (liveScoreElements.size() >= 2) {
                try {
                    // Find the set number for the live set
                    Element liveSetNumberElement = document.selectFirst("[data-status='live']");
                    if (liveSetNumberElement != null) {
                        // Extract set number from text like "Set 3 - Live"
                        String liveSetText = liveSetNumberElement.text();
                        String[] parts = liveSetText.split(" ");
                        if (parts.length >= 2) {
                            int liveSetNumber = Integer.parseInt(parts[1]);
                            int homeLiveScore = Integer.parseInt(liveScoreElements.get(0).attr("data-score"));
                            int awayLiveScore = Integer.parseInt(liveScoreElements.get(1).attr("data-score"));
                            sets.add(new SetScore(liveSetNumber, homeLiveScore, awayLiveScore));
                        }
                    }
                } catch (NumberFormatException e) {
                    System.out.println(("Could not parse live set score: " + e.getMessage()));
                }
            }

            match.setSets(sets);

            System.out.println("Successfully scraped match: " + match.getHomeTeam() + " vs " + match.getAwayTeam() +
                    " (" + match.getHomeScore() + ":" + match.getAwayScore() + ")");

            return match;

        } catch (IOException e) {
            System.out.println("Failed to scrape match data from " + matchUrl + ": " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.out.println("Unexpected error while scraping match data: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
