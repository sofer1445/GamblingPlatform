package org.example.entities;
import org.example.entities.FootballClub;

public class Bet {
    private String user;
    private FootballClub team1;
    private FootballClub team2;
    private String predictedResult;

    public Bet(String user, FootballClub team1, FootballClub team2, String predictedResult) {
        this.user = user;
        this.team1 = team1;
        this.team2 = team2;
        this.predictedResult = predictedResult;
    }

    public String getUser() {
        return user;
    }

    public FootballClub getTeam1() {
        return team1;
    }

    public FootballClub getTeam2() {
        return team2;
    }

    public String getPredictedResult() {
        return predictedResult;
    }

}
