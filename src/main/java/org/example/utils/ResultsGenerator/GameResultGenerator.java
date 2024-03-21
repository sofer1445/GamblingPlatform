package org.example.utils.ResultsGenerator;

import org.example.entities.FootballClub;

import java.util.Map;
import java.util.Random;

public class GameResultGenerator {

    private  final int RANDOM_FACTOR_RANGE = 20; // -10 to 10
    private  final Random RANDOM = new Random();

//    public static void main(String[] args) {
//        FootballClub team1 = new FootballClub("Team1", 30);
//        FootballClub team2 = new FootballClub("Team2", 90);
//        System.out.println(generateResult(team1, team2));
//    }

    public  String generateResult(FootballClub team1, FootballClub team2) {
        System.out.println("Team1: " + team1.getName()+" Strength" + team1.getTeamStrength() +" " + " vs Team2: " + team2.getName() + " Strength" + team2.getTeamStrength());
        Map<String, Integer> randomFactorTeam1 = getRandomFactor();
        Map<String, Integer> randomFactorTeam2 = getRandomFactor();
        String keyTeam1 = randomFactorTeam1.keySet().iterator().next();
        String keyTeam2 = randomFactorTeam2.keySet().iterator().next();

        int team1Strength = team1.getTeamStrength() + randomFactorTeam1.get(keyTeam1);
        int team2Strength = team2.getTeamStrength() + randomFactorTeam2.get(keyTeam2);

        System.out.println("Team1 strength: " + team1Strength + " " + keyTeam1 + ": " + randomFactorTeam1.get(keyTeam1));
        System.out.println("Team2 strength: " + team2Strength + " " + keyTeam2 + ": " + randomFactorTeam2.get(keyTeam2));
        int totalStrength = team1Strength + team2Strength;

        double team1WinProbability = (double) team1Strength / totalStrength;
        double team2WinProbability = (double) team2Strength / totalStrength;
        double drawProbability = 1 - Math.abs(team1WinProbability - team2WinProbability);

        double random = Math.random();
        if (random < team1WinProbability) {
            updateStrengths(team1, team2, team1.getName() + " wins");
            return team1.getName() + " wins" + team1.getTeamStrength();
        } else if (random < team1WinProbability + drawProbability) {
            return "Draw";
        } else {
            updateStrengths(team1, team2, team2.getName() + " wins");
            return team2.getName() + " wins" + team2.getTeamStrength();
        }
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