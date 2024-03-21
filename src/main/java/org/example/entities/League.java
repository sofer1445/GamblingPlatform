package org.example.entities;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class League {
    private String name;
    private Map<String, FootballClub> footballClubs;

    public League(String name, FootballClub... footballClubs) {
        this.name = name;
        this.footballClubs = Arrays.stream(footballClubs).collect(Collectors.toMap(FootballClub::getName, club -> club));
    }

    public League(String name) {
        this.name = name;
    }

    public League() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, FootballClub> getFootballClubs() {
        return footballClubs;
    }

    public void setFootballClubs(Map<String, FootballClub> footballClubs) {
        this.footballClubs = footballClubs;
    }




}