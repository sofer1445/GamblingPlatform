package org.example.entities;

public class Bet {
    private int idBet;
    private String secretUser;
    private Match match;
    private String predictedResult;
    private FootballClub predictedWinner;

    public Bet(String secretUser, Match match, String predictedResult) {
        this.secretUser = secretUser;
        this.match = match;
        this.predictedResult = predictedResult;
    }

    public Bet(String secretUser, Match match, FootballClub predictedWinner) {
        this.secretUser = secretUser;
        this.match = match;
        this.predictedWinner = predictedWinner;
    }

    public String getSecretUser() {
        return secretUser;
    }

    public Match getMatch() {
        return match;
    }

    public String getPredictedResult() {
        return predictedResult;
    }

    public void setSecretUser(String secretUser) {
        this.secretUser = secretUser;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public void setPredictedResult(String predictedResult) {
        this.predictedResult = predictedResult;
    }

    public FootballClub getPredictedWinner() {
        return predictedWinner;
    }

    public void setPredictedWinner(FootballClub predictedWinner) {
        this.predictedWinner = predictedWinner;
    }

    public int getIdBet() {
        return idBet;
    }

    public void setIdBet(int idBet) {
        this.idBet = idBet;
    }
}