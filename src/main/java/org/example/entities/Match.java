package org.example.entities;

import org.example.utils.ResultsGenerator.GameProgression;

public class Match {
    // המשחק צריך לקבל קבוצה בית וקבוצה חוץ ותוצאה
    private int idMatch;
    private FootballClub homeTeam;
    private FootballClub awayTeam;
    private String result;
    private GameProgression gameProgression;
    private String secretUser;

    public Match(FootballClub homeTeam, FootballClub awayTeam, String result) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.result = result;
    }
    public Match(FootballClub homeTeam, FootballClub awayTeam) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
    }
    public Match(){

    }
    public Match(String secretUser, FootballClub homeTeam, FootballClub awayTeam, String result) {
        this.secretUser = secretUser;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.result = result;
    }

    public FootballClub getHomeTeam() {
        return homeTeam;
    }
    public void setHomeTeam(FootballClub homeTeam) {
        this.homeTeam = homeTeam;
    }
    public FootballClub getAwayTeam() {
        return awayTeam;
    }
    public void setAwayTeam(FootballClub awayTeam) {
        this.awayTeam = awayTeam;
    }
    public String getResult() {
        return result;
    }
    public void setResult(String result) {
        this.result = result;
    }
    public int getIdMatch() {
        return idMatch;
    }
    public void setIdMatch(int idMatch) {
        this.idMatch = idMatch;
    }
    public GameProgression getGameProgression() {
        return gameProgression;
    }

    public void setGameProgression(GameProgression gameProgression) {
        this.gameProgression = gameProgression;
    }

    public String getSecretUser() {
        return secretUser;
    }

    public void setSecretUser(String secretUser) {
        this.secretUser = secretUser;
    }

    public String getWinningTeamName() {
        String[] scores = result.split("-");
        int homeScore = Integer.parseInt(scores[0]);
        int awayScore = Integer.parseInt(scores[1]);

        if (homeScore > awayScore) {
            return homeTeam.getName();
        } else if (awayScore > homeScore) {
            return awayTeam.getName();
        } else {
            return "Draw";
        }
    }
}
