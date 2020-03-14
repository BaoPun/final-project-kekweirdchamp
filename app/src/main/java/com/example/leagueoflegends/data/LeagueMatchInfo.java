package com.example.leagueoflegends.data;

import java.io.Serializable;

public class LeagueMatchInfo implements Serializable {
    public int teamId;
    public int championId;
    public String summonerName;
    public String summonerId;
    public String championName; //not in json directly, but retrieved from championId
}


