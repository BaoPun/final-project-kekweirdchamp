package com.example.leagueoflegends.utils;

import android.net.Uri;
import android.text.TextUtils;

import java.net.MalformedURLException;

import com.example.leagueoflegends.BuildConfig;
import com.example.leagueoflegends.data.*;
import com.google.gson.Gson;

import java.util.ArrayList;

import androidx.annotation.Nullable;

public class LeagueUtils {
    // Query by summoner name
    private final static String https = "https://";
    private final static String server = "na1";
    private final static String getSummonerName = "api.riotgames.com/lol/summoner/v4/summoners/";
    private final static String byName_param = "by-name";
    private final static String api_key_param = "api_key";

    // This must be changed daily!!!!!!!!!!
    private final static String apiKey = "RGAPI-73f52130-9bd7-4574-914d-5676af18c255";

    // Each summoner has specific info.  The only one we care about is their unique id
    class SummonerInfoResults{
        String id;
    }

    // Getting the unique summoner id is step 1.  Due to this, we need to create an object of this to reference for the next query
    private static SummonerInfoResults summonerId;

    // Extract the id
    public static String buildSummonerNameURL(String SummonerName){
        return Uri.parse(https + server + "." + getSummonerName).buildUpon().appendPath(byName_param).appendPath(SummonerName).appendQueryParameter(api_key_param, apiKey)
                .build().toString();
    }

    // From
    public static String getIdFromSummonerNameJson(String json){
        Gson gson = new Gson();
        summonerId = gson.fromJson(json, SummonerInfoResults.class);
        return summonerId.id;
    }

    // Once we get the summoner's id, we need to query for live ingame data.
    private final static String spectateGame = "api.riotgames.com/lol/spectator/v4/active-games";
    private final static String bySummoner_param = "by-summoner";

    // Get in-game information url
    public static String buildSpectatorURL(String encryptedID){
        return Uri.parse(https + server + "." + spectateGame).buildUpon().appendPath(bySummoner_param).appendPath(encryptedID).appendQueryParameter(api_key_param, apiKey)
                .build().toString();
    }


    static class MatchData{
        ArrayList<LeagueMatchInfo> participants;
    }

    public static ArrayList<LeagueMatchInfo> parseMatchData(String json){
        Gson gson = new Gson();
        MatchData results = gson.fromJson(json, MatchData.class);
        if (results != null && results.participants != null) {
            return results.participants;
        } else {
            return null;
        }
    }


}
