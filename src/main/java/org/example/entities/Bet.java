package org.example.entities;

public class Bet {
    // ההימור צריך לקבל סיקראט של המשתמש , משחק(בית וחוץ), אם הימור רגיל אז מי תנצח , הימור תוצאה = לקבל תוצאה מדויקת
    private int idBet;
    private String secretUser;
    private Match match;
    private String predictedResult;
    private FootballClub predictedWinner;
    private boolean draw;
    private boolean status;
    private int betAmount; // סכום ההימור
    private int roundNumber;

    public Bet(String secretUser, Match match, String predictedResult) {
        this.secretUser = secretUser;
        this.match = match;
        this.predictedResult = predictedResult;
        this.status = false;
    }
    public Bet(String secretUser, FootballClub predictedWinner){
        this.secretUser = secretUser;
        this.predictedWinner = predictedWinner;

    }
    public Bet(String secretUser, Match match, boolean draw) {
        this.secretUser = secretUser;
        this.match = match;
        this.draw = draw;
        this.status = false;
    }
    public Bet(){

    }


    public Bet(String secretUser, Match match, FootballClub predictedWinner) {
        this.secretUser = secretUser;
        this.match = match;
        this.predictedWinner = predictedWinner;
    }

    public Bet(String secret, boolean draw) {
        this.secretUser = secret;
        this.draw = draw;

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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isDraw() {
        return draw;
    }

    public int getBetAmount() {
        return betAmount;
    }

    public void setBetAmount(int betAmount) {
        this.betAmount = betAmount;
    }

    public void setDraw(boolean draw) {
        this.draw = draw;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }
}