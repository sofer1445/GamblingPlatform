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

        ratios.put("1", roundToTwoDecimalPlaces(calculateOdds(homeWinProbability) * homeTeamAveragePoints));
        ratios.put("X", roundToTwoDecimalPlaces(calculateOdds(drawProbability)));
        ratios.put("2", roundToTwoDecimalPlaces(calculateOdds(awayWinProbability) * awayTeamAveragePoints));

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

        return (double) totalGoals / matchCount;
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
        // Ensure the odds are within a reasonable range
        if (probability >= 0.95) {
            return 1.05;
        } else if (probability <= 0.05) {
            return 2.4;
        } else {
            return 1 / probability;
        }
    }
}