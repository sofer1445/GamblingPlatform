package org.example.utils.ResultsGenerator;

import org.example.entities.FootballClub;
import org.example.entities.Bet;
import org.example.entities.Match;
import org.example.entities.User;
import org.example.utils.ResultsGenerator.GameResultGenerator;

import java.util.HashMap;
import java.util.Map;
public class BettingSystem {

    public static void main(String[] args) {
        BettingSystem bettingSystem = new BettingSystem();
        User user1 = new User("user1", "password1" , "mail1");
        User user2 = new User("user2", "password2" , "mail2");
        FootballClub team1 = new FootballClub("HomeTeam");
        FootballClub team2 = new FootballClub("AwayTeam");
        Match match = new Match(team1, team2, "1:0");
        bettingSystem.placeBet(user1.getSecret(), match, "1:0");
        bettingSystem.placeBetOnWin(user2.getSecret(), match, team1);
        bettingSystem.generateResults();

    }
    private Map<String, Bet> bets = new HashMap<>();
    private GameResultGenerator gameResultGenerator = new GameResultGenerator();

    public void placeBet(String secretUser, Match match, String predictedResult) {
        Bet bet = new Bet(secretUser, match, predictedResult);
        bets.put(secretUser, bet);
    }

    public void placeBetOnWin(String secretUser, Match match, FootballClub predictedWinner) {
        Bet bet = new Bet(secretUser, match, predictedWinner);
        bets.put(secretUser, bet);
    }

    public void generateResults() {
        for (Bet bet : bets.values()) {
            FootballClub team1 = bet.getMatch().getHomeTeam();
            FootballClub team2 = bet.getMatch().getAwayTeam();
            String predictedResult = bet.getPredictedResult();
            FootballClub predictedWinner = bet.getPredictedWinner();
            GameResult gameResult = gameResultGenerator.generateResult(team1, team2);
            String actualResult = gameResult.getResult();
            String winningTeam = gameResult.getWinningTeamName();
            System.out.println("User: " + bet.getSecretUser());
            if (predictedResult != null) {
                System.out.println("Predicted result: " + predictedResult);
            } else if (predictedWinner != null) {
                System.out.println("Predicted winning team: " + predictedWinner.getName());
            }
            System.out.println("Actual result: " + actualResult);
            System.out.println("Winning team: " + winningTeam);
            if (predictedResult != null && predictedResult.equals(actualResult)) {
                System.out.println("Congratulations! You won the bet!");
            } else if (predictedWinner != null && winningTeam.equals(predictedWinner.getName())) {
                System.out.println("Congratulations! You won the bet!");
            } else {
                System.out.println("Better luck next time!");
            }
            System.out.println("-------------------------------");
        }
    }



}
