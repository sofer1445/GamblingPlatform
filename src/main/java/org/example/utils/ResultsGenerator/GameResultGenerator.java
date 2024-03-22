package org.example.utils.ResultsGenerator;

import org.example.entities.FootballClub;

import java.util.Map;
import java.util.Random;

public class GameResultGenerator {

    private  final int RANDOM_FACTOR_RANGE = 20; // -10 to 10
    private  final Random RANDOM = new Random();

    public static void main(String[] args) {
        GameResultGenerator gameResultGenerator = new GameResultGenerator();
        FootballClub team1 = new FootballClub("Team 1", 30);
        FootballClub team2 = new FootballClub("Team 2", 70);
        System.out.println(gameResultGenerator.generateResult(team1, team2));
    }

    public GameResult generateResult(FootballClub team1, FootballClub team2) {
        Map<String, Integer> randomFactorTeam1 = getRandomFactor();
        Map<String, Integer> randomFactorTeam2 = getRandomFactor();
        String keyTeam1 = randomFactorTeam1.keySet().iterator().next();
        String keyTeam2 = randomFactorTeam2.keySet().iterator().next();

        int team1Strength = team1.getTeamStrength() + randomFactorTeam1.get(keyTeam1);
        int team2Strength = team2.getTeamStrength() + randomFactorTeam2.get(keyTeam2);

        int totalStrength = team1Strength + team2Strength;

        double team1WinProbability = (double) team1Strength / totalStrength;
        double team2WinProbability = (double) team2Strength / totalStrength;

        int team1Goals = (int) Math.round(RANDOM.nextGaussian() * team1WinProbability * 3);
        int team2Goals = (int) Math.round(RANDOM.nextGaussian() * team2WinProbability * 3);

        team1Goals = Math.max(0, team1Goals); // Ensure goals are not negative
        team2Goals = Math.max(0, team2Goals); // Ensure goals are not negative

        String result = team1Goals + "-" + team2Goals;

        if (team1Goals > team2Goals) {
            updateStrengths(team1, team2, team1.getName() + " wins");
        } else if (team1Goals < team2Goals) {
            updateStrengths(team1, team2, team2.getName() + " wins");
        }

        GameResult gameResult = new GameResult();
        gameResult.setResult(result);
        gameResult.setTeam1InitialStrength("Team1: " + team1.getName() + " Initial Strength: " + team1.getTeamStrength() + " Random Factor (" + keyTeam1 + "): " + randomFactorTeam1.get(keyTeam1) + " Final Strength: " + team1Strength);
        gameResult.setTeam2InitialStrength("Team2: " + team2.getName() + " Initial Strength: " + team2.getTeamStrength() + " Random Factor (" + keyTeam2 + "): " + randomFactorTeam2.get(keyTeam2) + " Final Strength: " + team2Strength);
        gameResult.setTeam1FinalStrength("Team1: " + team1.getName() + " Strength after match: " + team1.getTeamStrength());
        gameResult.setTeam2FinalStrength("Team2: " + team2.getName() + " Strength after match: " + team2.getTeamStrength());

        return gameResult;
    }

    private  Map<String,Integer> getRandomFactor() {
        String[] keys = {"weather", "injuries", "morale"};
        String randomKey = keys[RANDOM.nextInt(keys.length)];
        int randomFactor = RANDOM.nextInt(RANDOM_FACTOR_RANGE) - RANDOM_FACTOR_RANGE / 2;
        return Map.of(randomKey, randomFactor);
    }

    public  void updateStrengths(FootballClub team1, FootballClub team2, String result) {
        if (result.equals(team1.getName() + " wins")) {
            team1.setTeamStrength(team1.getTeamStrength() + 1); // Increase team1's strength
            team2.setTeamStrength(Math.max(0, team2.getTeamStrength() - 1)); // Decrease team2's strength, but not below 0
        } else if (result.equals(team2.getName() + " wins")) {
            team2.setTeamStrength(team2.getTeamStrength() + 1); // Increase team2's strength
            team1.setTeamStrength(Math.max(0, team1.getTeamStrength() - 1)); // Decrease team1's strength, but not below 0
        }
    }

}