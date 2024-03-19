package org.example.entities;

import java.util.Map;

public class League {
    private int id;
    private FootballClub[] clubs;
    private int matchesPlayed;
    private int wins;
    private int draws;
    private int losses;
    private int goalsScored;
    private int goalsConceded;
    private int difference;
    private int points;
    private Map<String, Integer> clubPositions;


    public League(FootballClub[] clubs) {
        this.clubs = clubs;
    }

    public League() {
    }

    public FootballClub[] getClubs() {
        return clubs;
    }

    public void setClubs(FootballClub[] clubs) {
        this.clubs = clubs;
    }

    public int getMatchesPlayed() {
        return matchesPlayed;
    }

    public void setMatchesPlayed(int matchesPlayed) {
        this.matchesPlayed = matchesPlayed;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getDraws() {
        return draws;
    }

    public void setDraws(int draws) {
        this.draws = draws;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getGoalsScored() {
        return goalsScored;
    }

    public void setGoalsScored(int goalsScored) {
        this.goalsScored = goalsScored;
    }

    public int getGoalsConceded() {
        return goalsConceded;
    }

    public void setGoalsConceded(int goalsConceded) {
        this.goalsConceded = goalsConceded;
    }

    public int getDifference() {
        return difference;
    }

    public void setDifference(int difference) {
        this.difference = difference;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Map<String, Integer> getClubPositions() {
        return clubPositions;
    }

    public void setClubPositions(Map<String, Integer> clubPositions) {
        this.clubPositions = clubPositions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
