package org.example.utils.ResultsGenerator;

import java.util.Map;

public class GameResult {
    private Map<String,String> result;
    private String team1InitialStrength;
    private String team2InitialStrength;
    private String team1FinalStrength;
    private String team2FinalStrength;

    public GameResult(Map<String,String> result, String team1InitialStrength, String team2InitialStrength, String team1FinalStrength, String team2FinalStrength) {
        this.result = result;
        this.team1InitialStrength = team1InitialStrength;
        this.team2InitialStrength = team2InitialStrength;
        this.team1FinalStrength = team1FinalStrength;
        this.team2FinalStrength = team2FinalStrength;
    }

    public GameResult() {
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

    public Map<String, String> getResult() {
        return result;
    }

    public void setResult(Map<String, String> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "GameResult{" +
                " result='" + result + '\n' +
                "team1 Initial Strength='" + team1InitialStrength + '\n' +
                "team2 Initial Strength='" + team2InitialStrength + '\n' +
                "team1 Final Strength='" + team1FinalStrength + '\n' +
                "team2 Final Strength='" + team2FinalStrength + '\n' +
                '}';
    }

    public String getWinningTeamName() {
        String[] resultArray = result.keySet().iterator().next().split("-");
        int team1Goals = Integer.parseInt(resultArray[0]);
        int team2Goals = Integer.parseInt(resultArray[1]);
        if (team1Goals > team2Goals) {
            return team1InitialStrength.split(":")[1].trim();
        } else if (team1Goals < team2Goals) {
            return team2InitialStrength.split(":")[1].trim();
        } else {
            return "Draw";
        }
    }
}
