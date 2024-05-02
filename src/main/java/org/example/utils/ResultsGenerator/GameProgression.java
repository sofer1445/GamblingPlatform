package org.example.utils.ResultsGenerator;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GameProgression {
    private int id;
    private Map<String,String> result;
    private String team1InitialStrength;
    private String team2InitialStrength;
    private String team1FinalStrength;
    private String team2FinalStrength;
    private Map<String, List<Integer>> goalTimes;

    public GameProgression(Map<String,String> result, String team1InitialStrength, String team2InitialStrength, String team1FinalStrength, String team2FinalStrength) {
        this.result = result;
        this.team1InitialStrength = team1InitialStrength;
        this.team2InitialStrength = team2InitialStrength;
        this.team1FinalStrength = team1FinalStrength;
        this.team2FinalStrength = team2FinalStrength;
    }

    public GameProgression() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
                "goalTimes=" + goalTimes +
                '}';
    }

    public String getWinningTeamName() {
        if (team1InitialStrength == null || team2InitialStrength == null) {
            // Handle the case where team strengths are not set
            return "Team strengths not set";
        }

        String[] resultArray = result.keySet().iterator().next().split("-");
        int team1Goals = Integer.parseInt(resultArray[0]);
        int team2Goals = Integer.parseInt(resultArray[1]);
        if (team1Goals > team2Goals) {
            return team1InitialStrength.split(":")[0].trim();
        } else if (team1Goals < team2Goals) {
            return team2InitialStrength.split(":")[0].trim();
        } else {
            return "Draw";
        }
    }


    public void setGoalTimes(Map<String, String> goalTimes) {
        this.goalTimes = goalTimes.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> {
                            if (e.getValue() == null || e.getValue().isEmpty()) {
                                return new ArrayList<Integer>();
                            } else {
                                return Arrays.stream(e.getValue().split(","))
                                        .map(Integer::parseInt)
                                        .collect(Collectors.toList());
                            }
                        }
                ));
    }


    public Map<String, String> getGoalTimes() {
        return goalTimes.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().stream()
                                .map(String::valueOf)
                                .collect(Collectors.joining(","))
                ));
    }
}
