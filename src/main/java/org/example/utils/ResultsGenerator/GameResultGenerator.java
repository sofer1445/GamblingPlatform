package org.example.utils.ResultsGenerator;

import org.example.entities.FootballClub;
import java.util.stream.Collectors;

import java.util.*;

public class GameResultGenerator {

    private  final int RANDOM_FACTOR_RANGE = 20; // -10 to 10
    private  final Random RANDOM = new Random();
    private static final int MATCH_LENGTH = 90; // Length of a match in minutes


//    public static void main(String[] args) {
//        GameResultGenerator gameResultGenerator = new GameResultGenerator();
//        FootballClub team1 = new FootballClub("Team 1", 30);
//        FootballClub team2 = new FootballClub("Team 2", 90);
//        System.out.println(gameResultGenerator.generateResult(team1, team2).toString());
//    }

    public GameProgression generateResult(FootballClub team1, FootballClub team2) {
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
        String winningTeam ;
        GameProgression gameProgression = new GameProgression();
        gameProgression.setGoalTimes(generateGoalTimes(team1, team2, team1Goals, team2Goals));
        if (team1Goals > team2Goals) {
//            updateStrengths(team1, team2, team1.getName() + " wins");
            winningTeam = team1.getName();
        } else if (team1Goals < team2Goals) {
//            updateStrengths(team2, team1, team2.getName() + " wins");
            winningTeam = team2.getName();
        } else {
            winningTeam = "Draw";
        }
        gameProgression.setResult(Map.of(result, winningTeam));

        gameProgression.setTeam1InitialStrength("Team1: " + team1.getName() + ", Initial Strength: " + team1.getTeamStrength() + ", Random Factor (" + keyTeam1 + "): " + randomFactorTeam1.get(keyTeam1) + ", Final Strength: " + team1Strength);
        gameProgression.setTeam2InitialStrength("Team2: " + team2.getName() + ", Initial Strength: " + team2.getTeamStrength() + ", Random Factor (" + keyTeam2 + "): " + randomFactorTeam2.get(keyTeam2) + ", Final Strength: " + team2Strength);

        gameProgression.setTeam1FinalStrength("Team1: " + team1.getName() + ", Strength after match: " + team1.getTeamStrength());
        gameProgression.setTeam2FinalStrength("Team2: " + team2.getName() + ", Strength after match: " + team2.getTeamStrength());

        return gameProgression;
    }

    private  Map<String,Integer> getRandomFactor() {
        String[] keys = {"weather", "injuries", "morale"};
        String randomKey = keys[RANDOM.nextInt(keys.length)];
        int randomFactor = RANDOM.nextInt(RANDOM_FACTOR_RANGE) - RANDOM_FACTOR_RANGE / 2;
        return Map.of(randomKey, randomFactor);
    }

    public Map<String, String> generateGoalTimes(FootballClub team1, FootballClub team2, int team1Goals, int team2Goals) {
        Map<String, String> goalTimes = new HashMap<>();
        goalTimes.put(team1.getName(), generateRandomTimes(team1Goals));
        goalTimes.put(team2.getName(), generateRandomTimes(team2Goals));
        return goalTimes;
    }

    private String generateRandomTimes(int numberOfGoals) {
        List<Integer> times = new ArrayList<>();
        for (int i = 0; i < numberOfGoals; i++) {
            times.add(RANDOM.nextInt(MATCH_LENGTH) + 1); // +1 to avoid 0 minute
        }
        Collections.sort(times); // Sort the times in ascending order
        return times.stream().map(String::valueOf).collect(Collectors.joining(","));
    }



//    public  void updateStrengths(FootballClub team1, FootballClub team2, String result) {
//        if (result.equals(team1.getName() + " wins")) {
//            team1.setTeamStrength(team1.getTeamStrength() + 1); // Increase team1's strength
//            team2.setTeamStrength(Math.max(0, team2.getTeamStrength() - 1)); // Decrease team2's strength, but not below 0
//        } else if (result.equals(team2.getName() + " wins")) {
//            team2.setTeamStrength(team2.getTeamStrength() + 1); // Increase team2's strength
//            team1.setTeamStrength(Math.max(0, team1.getTeamStrength() - 1)); // Decrease team1's strength, but not below 0
//        }
//    }

}