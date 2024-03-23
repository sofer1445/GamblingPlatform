package org.example.entities;

public class Match {
    private int idMatch;
    private FootballClub homeTeam;
    private FootballClub awayTeam;
    private String result;

    public Match(FootballClub homeTeam, FootballClub awayTeam, String result) {
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
}
