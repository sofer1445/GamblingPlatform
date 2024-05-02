package org.example.utils.ResultsGenerator;

import org.example.entities.FootballClub;
import org.example.entities.Bet;
import org.example.entities.Match;
import org.example.entities.User;
import org.example.utils.Persist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BettingSystem {

    @Autowired
    private Persist persist;

    public Map<String, Double> ratioCalculation(Match match) {
        Map<String, Double> ratios = new HashMap<>();

        double homeTeamStrength = calculateTeamStrength(match.getHomeTeam());
        double awayTeamStrength = calculateTeamStrength(match.getAwayTeam());

        double homeWinProbability = homeTeamStrength / (homeTeamStrength + awayTeamStrength);
        double awayWinProbability = awayTeamStrength / (homeTeamStrength + awayTeamStrength);
        double drawProbability = 1 - Math.max(homeWinProbability, awayWinProbability);

        double homeTeamAveragePoints = calculateAveragePoints(match.getHomeTeam());
        double awayTeamAveragePoints = calculateAveragePoints(match.getAwayTeam());

        double homeRatio = roundToTwoDecimalPlaces(calculateOdds(homeWinProbability) * homeTeamAveragePoints);
        double drawRatio = roundToTwoDecimalPlaces(calculateOdds(drawProbability));
        double awayRatio = roundToTwoDecimalPlaces(calculateOdds(awayWinProbability) * awayTeamAveragePoints);

        // Check if ratios are 0 and if so, assign a random value between 1 and 4
        homeRatio = homeRatio == 0 ? roundToTwoDecimalPlaces(Math.random() * 3 + 1) : homeRatio;
        drawRatio = drawRatio == 0 ? roundToTwoDecimalPlaces(Math.random() * 3 + 1) : drawRatio;
        awayRatio = awayRatio == 0 ? roundToTwoDecimalPlaces(Math.random() * 3 + 1) : awayRatio;

        ratios.put("1", homeRatio);
        ratios.put("X", drawRatio);
        ratios.put("2", awayRatio);

        return ratios;
    }

    private double roundToTwoDecimalPlaces(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    private double calculateTeamStrength(FootballClub team) {
        List<Match> matches = persist.getMatches();
        int totalGoals = 0;
        int matchCount = 0;

        for (Match match : matches) {
            if (match.getHomeTeam().getName().equals(team.getName()) || match.getAwayTeam().getName().equals(team.getName())) {
                String[] scores = match.getResult().split("-");
                int homeScore = Integer.parseInt(scores[0]);
                int awayScore = Integer.parseInt(scores[1]);

                if (match.getHomeTeam().getName().equals(team.getName())) {
                    totalGoals += homeScore;
                } else if (match.getAwayTeam().getName().equals(team.getName())) {
                    totalGoals += awayScore;
                }
                matchCount++;
            }
        }

        double teamStrength = matchCount > 0 ? (double) totalGoals / matchCount : 0.5; // Default value if no matches have been played
        return teamStrength;
    }

    private double calculateAveragePoints(FootballClub team) {
        List<Match> matches = persist.getMatches();
        int totalPoints = 0;
        int matchCount = 0;

        for (Match match : matches) {
            if (match.getHomeTeam().getName().equals(team.getName()) || match.getAwayTeam().getName().equals(team.getName())) {
                String[] scores = match.getResult().split("-");
                int homeScore = Integer.parseInt(scores[0]);
                int awayScore = Integer.parseInt(scores[1]);

                if (match.getHomeTeam().getName().equals(team.getName())) {
                    if (homeScore > awayScore) {
                        totalPoints += 3;
                    } else if (homeScore == awayScore) {
                        totalPoints += 1;
                    }
                } else if (match.getAwayTeam().getName().equals(team.getName())) {
                    if (awayScore > homeScore) {
                        totalPoints += 3;
                    } else if (homeScore == awayScore) {
                        totalPoints += 1;
                    }
                }
                matchCount++;
            }
        }

        return (double) totalPoints / matchCount;
    }

    private double calculateOdds(double probability) {
        // Ensure the odds are not less than 1.05
        if (probability <= 0) {
            return 1.05;
        } else {
            double odds = 1 / probability;
            return Math.max(odds, 1.05);
        }
    }
}