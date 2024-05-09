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

        List<Match> allMatches = persist.getMatches();

        double homeTeamGoalAverage = calculateTeamGoalAverage(match.getHomeTeam(), allMatches);
        double awayTeamGoalAverage = calculateTeamGoalAverage(match.getAwayTeam(), allMatches);

        if (homeTeamGoalAverage == 0 && awayTeamGoalAverage == 0) {
            ratios.put("1", roundToTwoDecimalPlaces(1.05 + (match.getHomeTeam().getTeamStrength() / 100.0) * 2.95));
            ratios.put("X", roundToTwoDecimalPlaces(1.05 + Math.random() * 2.95));
            ratios.put("2", roundToTwoDecimalPlaces(1.05 + (match.getAwayTeam().getTeamStrength() / 100.0) * 2.95));
            return ratios;
        }

        double homeWinProbability = homeTeamGoalAverage / (homeTeamGoalAverage + awayTeamGoalAverage);
        double awayWinProbability = awayTeamGoalAverage / (homeTeamGoalAverage + awayTeamGoalAverage);
        double drawProbability = 1 - Math.abs(homeWinProbability - awayWinProbability);

        double homeRatio = calculateOdds(homeWinProbability) * calculateTeamStrength(match.getHomeTeam());
        double drawRatio = calculateOdds(drawProbability) * calculateTeamStrength(null);
        double awayRatio = calculateOdds(awayWinProbability) * calculateTeamStrength(match.getAwayTeam());

        ratios.put("1", roundToTwoDecimalPlaces(homeRatio));
        ratios.put("X", roundToTwoDecimalPlaces(drawRatio));
        ratios.put("2", roundToTwoDecimalPlaces(awayRatio));

        return ratios;
    }

    private double calculateTeamGoalAverage(FootballClub team, List<Match> allMatches) {
        double totalGoals = 0;
        int matchCount = 0;

        for (Match match : allMatches) {
            if (match.getHomeTeam().equals(team) || match.getAwayTeam().equals(team)) {
                String[] scores = match.getResult().split("-");
                int homeScore = Integer.parseInt(scores[0]);
                int awayScore = Integer.parseInt(scores[1]);

                if (match.getHomeTeam().equals(team)) {
                    totalGoals += homeScore;
                } else {
                    totalGoals += awayScore;
                }
                matchCount++;
            }
        }

        return matchCount > 0 ? totalGoals / matchCount : 0; // Return 0 if no matches have been played
    }

    private double calculateOdds(double probability) {
        if (probability <= 0) {
            return roundToTwoDecimalPlaces(3.0);
        } else if (probability == 1) {
            return roundToTwoDecimalPlaces(3.0);
        } else {
            double odds = 1 / probability;
            return roundToTwoDecimalPlaces(Math.min(Math.max(odds, 1.05), 4.0));
        }
    }

    private double calculateTeamStrength(FootballClub team) {
        if (team != null) {
            return roundToTwoDecimalPlaces(Math.max(team.getTeamStrength() / 25.0, 1.05));
        } else {
            return roundToTwoDecimalPlaces(1.05); // Default strength for a draw
        }
    }

    private double roundToTwoDecimalPlaces(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

}