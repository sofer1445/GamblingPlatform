package org.example.utils.ResultsGenerator;

public class GameResult {
    private String result;
    private String team1InitialStrength;
    private String team2InitialStrength;
    private String team1FinalStrength;
    private String team2FinalStrength;

    public GameResult(String result, String team1InitialStrength, String team2InitialStrength, String team1FinalStrength, String team2FinalStrength) {
        this.result = result;
        this.team1InitialStrength = team1InitialStrength;
        this.team2InitialStrength = team2InitialStrength;
        this.team1FinalStrength = team1FinalStrength;
        this.team2FinalStrength = team2FinalStrength;
    }

    public GameResult() {
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getTeam1InitialStrength() {
        return team1InitialStrength;
    }

    public void setTeam1InitialStrength(String team1InitialStrength) {
        this.team1InitialStrength = team1InitialStrength;
    }

    public String getTeam2InitialStrength() {
        return team2InitialStrength;
    }

    public void setTeam2InitialStrength(String team2InitialStrength) {
        this.team2InitialStrength = team2InitialStrength;
    }

    public String getTeam1FinalStrength() {
        return team1FinalStrength;
    }

    public void setTeam1FinalStrength(String team1FinalStrength) {
        this.team1FinalStrength = team1FinalStrength;
    }

    public String getTeam2FinalStrength() {
        return team2FinalStrength;
    }

    public void setTeam2FinalStrength(String team2FinalStrength) {
        this.team2FinalStrength = team2FinalStrength;
    }

    @Override
    public String toString() {
        return "GameResult{" +
                "result='" + result + '\'' +
                ", team1InitialStrength='" + team1InitialStrength + '\'' +
                ", team2InitialStrength='" + team2InitialStrength + '\'' +
                ", team1FinalStrength='" + team1FinalStrength + '\'' +
                ", team2FinalStrength='" + team2FinalStrength + '\'' +
                '}';
    }
}
