package org.example.utils.ResultsGenerator;

import org.example.entities.FootballClub;
import org.example.utils.ResultsGenerator.GameResultGenerator;
import org.example.entities.Bet;

import java.util.HashMap;
import java.util.Map;
public class BettingSystem {
    public static void main(String[] args) {
        BettingSystem bettingSystem = new BettingSystem();
        FootballClub team1 = new FootballClub("Team 1");
        FootballClub team2 = new FootballClub("Team 2");
        bettingSystem.placeBet("User 1", team1, team2, "1-0");
        bettingSystem.placeBet("User 2", team1, team2, "2-1");
        bettingSystem.placeBet("User 3", team1, team2, "0-0");
        bettingSystem.generateResults();
    }
    private Map<String, Bet> bets = new HashMap<>();
    private GameResultGenerator gameResultGenerator = new GameResultGenerator();

    public void placeBet(String user, FootballClub team1, FootballClub team2, String predictedResult) {
        Bet bet = new Bet(user, team1, team2, predictedResult);
        bets.put(user, bet);
    }

    public void generateResults() {
        for (Bet bet : bets.values()) {
            FootballClub team1 = bet.getTeam1();
            FootballClub team2 = bet.getTeam2();
            String predictedResult = bet.getPredictedResult();
            String actualResult = gameResultGenerator.generateResult(team1, team2).getResult();
            System.out.println("User: " + bet.getUser() + " Predicted: " + predictedResult + " Actual: " + actualResult);
        }
    }

}
