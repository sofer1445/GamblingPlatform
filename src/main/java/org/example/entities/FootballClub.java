package org.example.entities;

public class FootballClub {
    private String name;
    private int points;
    private int wins;
    private int draws;
    private int losses;
    private int goalsScored;
    private int goalsConceded;
    private int matchesPlayed;
    private int teamStrength;

    public FootballClub(String name, int points, int wins, int draws, int losses, int goalsScored, int goalsConceded, int matchesPlayed, int teamStrength) {
        this.name = name;
        this.points = points;
        this.wins = wins;
        this.draws = draws;
        this.losses = losses;
        this.goalsScored = goalsScored;
        this.goalsConceded = goalsConceded;
        this.matchesPlayed = matchesPlayed;
        this.teamStrength = teamStrength;
    }

    public FootballClub(String name, int teamStrength) {
        this.name = name;
        this.teamStrength = teamStrength;
    }

    public FootballClub(String name) {
        this.name = name;
        this.points = 0;
        this.wins = 0;
        this.draws = 0;
        this.losses = 0;
        this.goalsScored = 0;
        this.goalsConceded = 0;
        this.matchesPlayed = 0;
    }

    public FootballClub() {
        this.points = 0;
        this.wins = 0;
        this.draws = 0;
        this.losses = 0;
        this.goalsScored = 0;
        this.goalsConceded = 0;
        this.matchesPlayed = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
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

    public int getMatchesPlayed() {
        return matchesPlayed;
    }

    public void setMatchesPlayed(int matchesPlayed) {
        this.matchesPlayed = matchesPlayed;
    }

    public int getTeamStrength() {
        return teamStrength;
    }

    public void setTeamStrength(int teamStrength) {
        this.teamStrength = teamStrength;
    }


    public void updateStrengths(FootballClub team1, FootballClub team2, String result) {
        if (result.equals(team1.getName() + " wins")) {
            team1.setTeamStrength(team1.getTeamStrength() + 1); // Increase team1's strength
            team2.setTeamStrength(Math.max(0, team2.getTeamStrength() - 1)); // Decrease team2's strength, but not below 0
        } else if (result.equals(team2.getName() + " wins")) {
            team2.setTeamStrength(team2.getTeamStrength() + 1); // Increase team2's strength
            team1.setTeamStrength(Math.max(0, team1.getTeamStrength() - 1)); // Decrease team1's strength, but not below 0
        }
    }

//    public boolean updateClubStats(Match match) {
//        String result = match.getResult();
//        String[] resultArray = result.split("-");
//        FootballClub homeTeamName = match.getHomeTeam();
//        FootballClub awayTeamName = match.getAwayTeam();
//        int homeTeamGoals = Integer.parseInt(resultArray[0]);
//        int awayTeamGoals = Integer.parseInt(resultArray[1]);
//        if (homeTeamGoals > awayTeamGoals) {
//            homeTeamName.setWins(homeTeamName.getWins() + 1);
//            awayTeamName.setLosses(awayTeamName.getLosses() + 1);
//            homeTeamName.setPoints(homeTeamName.getPoints() + 3);
//        } else if (homeTeamGoals < awayTeamGoals) {
//            awayTeamName.setWins(awayTeamName.getWins() + 1);
//            homeTeamName.setLosses(homeTeamName.getLosses() + 1);
//            awayTeamName.setPoints(awayTeamName.getPoints() + 3);
//        } else {
//            homeTeamName.setDraws(homeTeamName.getDraws() + 1);
//            awayTeamName.setDraws(awayTeamName.getDraws() + 1);
//            homeTeamName.setPoints(homeTeamName.getPoints() + 1);
//            awayTeamName.setPoints(awayTeamName.getPoints() + 1);
//        }
//        homeTeamName.setGoalsScored(homeTeamName.getGoalsScored() + homeTeamGoals);
//        homeTeamName.setGoalsConceded(homeTeamName.getGoalsConceded() + awayTeamGoals);
//        awayTeamName.setGoalsScored(awayTeamName.getGoalsScored() + awayTeamGoals);
//        awayTeamName.setGoalsConceded(awayTeamName.getGoalsConceded() + homeTeamGoals);
//        homeTeamName.setMatchesPlayed(homeTeamName.getMatchesPlayed() + 1);
//        awayTeamName.setMatchesPlayed(awayTeamName.getMatchesPlayed() + 1);
//        return true;
//
//    }



}
