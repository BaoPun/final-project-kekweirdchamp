package com.example.leagueoflegends.utils;

import android.net.Uri;
import android.text.TextUtils;

import java.net.MalformedURLException;

import com.example.leagueoflegends.BuildConfig;
import com.example.leagueoflegends.data.*;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.Nullable;

public class LeagueUtils {
    // Query by summoner name
    private final static String https = "https://";
    private static String server = "na1";
    private final static String getSummonerName = "api.riotgames.com/lol/summoner/v4/summoners/";
    private final static String byName_param = "by-name";
    private final static String api_key_param = "api_key";

    // This must be changed daily!!!!!!!!!!
    private final static String apiKey = "RGAPI-271eff44-35e0-4440-b3d3-3a4cc344dd40";

    // Create hash map to get champion name from id
    private static HashMap<Integer, String> idToName = new HashMap<>();

    private static void createMap(){
        // Manually get each champion's name from their id
        idToName.put(266, "Aatrox");
        idToName.put(103, "Ahri");
        idToName.put(84, "Akali");
        idToName.put(12, "Alistar");
        idToName.put(32, "Amumu");
        idToName.put(34, "Anivia");
        idToName.put(1, "Annie");
        idToName.put(523, "Aphelios");
        idToName.put(22, "Ashe");
        idToName.put(136, "Aurelion Sol");
        idToName.put(268, "Azir");
        idToName.put(432, "Bard");
        idToName.put(53, "Blitzcrank");
        idToName.put(63, "Brand");
        idToName.put(201, "Braum");
        idToName.put(51, "Caitlyn");
        idToName.put(164, "Camille");
        idToName.put(69, "Cassiopeia");
        idToName.put(31, "Cho'gath");
        idToName.put(42, "Corki");
        idToName.put(122, "Darius");
        idToName.put(131, "Diana");
        idToName.put(119, "Draven");
        idToName.put(36, "Dr. Mundo");
        idToName.put(245, "Ekko");
        idToName.put(60, "Elise");
        idToName.put(28, "Evelynn");
        idToName.put(81, "Ezreal");
        idToName.put(9, "Fiddlesticks");
        idToName.put(114, "Fiora");
        idToName.put(105, "Fizz");
        idToName.put(3, "Galio");
        idToName.put(41, "Gangplank");
        idToName.put(86, "Garen");
        idToName.put(150, "Gnar");
        idToName.put(79, "Gragas");
        idToName.put(104, "Graves");
        idToName.put(120, "Hecarim");
        idToName.put(74, "Heimerdinger");
        idToName.put(420, "Illaoi");
        idToName.put(39, "Irelia");
        idToName.put(427, "Ivern");
        idToName.put(40, "Janna");
        idToName.put(59, "Jarvan IV");
        idToName.put(24, "Jax");
        idToName.put(126, "Jayce");
        idToName.put(202, "Jhin");
        idToName.put(222, "Jinx");
        idToName.put(145, "Kai'sa");
        idToName.put(429, "Kalista");
        idToName.put(43, "Karma");
        idToName.put(30, "Karthus");
        idToName.put(38, "Kassadin");
        idToName.put(55, "Katarina");
        idToName.put(10, "Kayle");
        idToName.put(141, "Kayn");
        idToName.put(85, "Kennen");
        idToName.put(121, "Kha'zix");
        idToName.put(203, "Kindred");
        idToName.put(240, "Kled");
        idToName.put(96, "Kog'maw");
        idToName.put(7, "Leblanc");
        idToName.put(64, "Lee Sin");
        idToName.put(89, "Leona");
        idToName.put(127, "Lissandra");
        idToName.put(236, "Lucian");
        idToName.put(117, "Lulu");
        idToName.put(99, "Lux");
        idToName.put(54, "Malphite");
        idToName.put(90, "Malzahar");
        idToName.put(57, "Maokai");
        idToName.put(11, "Master Yi");
        idToName.put(21, "Miss Fortune");
        idToName.put(62, "Wukong"); //LUL they put him as MonkeyKing
        idToName.put(82, "Mordekaiser");
        idToName.put(25, "Morgana");
        idToName.put(267, "Nami");
        idToName.put(75, "Nasus");
        idToName.put(111, "Nautilus");
        idToName.put(518, "Neeko");
        idToName.put(76, "Nidalee");
        idToName.put(56, "Nocturne");
        idToName.put(20, "Nunu");
        idToName.put(2, "Olaf");
        idToName.put(61, "Orianna");
        idToName.put(516, "Ornn");
        idToName.put(80, "Pantheon");
        idToName.put(78, "Poppy");
        idToName.put(555, "Pyke");
        idToName.put(246, "Qiyanna");
        idToName.put(133, "Quinn");
        idToName.put(497, "Rakan");
        idToName.put(33, "Rammus");
        idToName.put(421, "Rek'sai");
        idToName.put(58, "Renekton");
        idToName.put(107, "Rengar");
        idToName.put(92, "Riven");
        idToName.put(68, "Rumble");
        idToName.put(13, "Ryze");
        idToName.put(113, "Sejuani");
        idToName.put(235, "Senna");
        idToName.put(875, "Sett");
        idToName.put(35, "Shaco");
        idToName.put(98, "Shen");
        idToName.put(102, "Shyvana");
        idToName.put(27, "Singed");
        idToName.put(14, "Sion");
        idToName.put(15, "Sivir");
        idToName.put(72, "Skarner");
        idToName.put(37, "Sona");
        idToName.put(16, "Soraka");
        idToName.put(50, "Swain");
        idToName.put(517, "Sylas");
        idToName.put(134, "Syndra");
        idToName.put(223, "Tahm Kench");
        idToName.put(163, "Taliyah");
        idToName.put(91, "Talon");
        idToName.put(44, "Taric");
        idToName.put(17, "Teemo");
        idToName.put(412, "Thresh");
        idToName.put(18, "Tristana");
        idToName.put(48, "Trundle");
        idToName.put(23, "Tryndamere");
        idToName.put(4, "Twisted Fate");
        idToName.put(29, "Twitch");
        idToName.put(77, "Udyr");
        idToName.put(6, "Urgot");
        idToName.put(110, "Varus");
        idToName.put(67, "Vayne");
        idToName.put(45, "Veigar");
        idToName.put(161, "Vel'koz");
        idToName.put(254, "Vi");
        idToName.put(112, "Viktor");
        idToName.put(8, "Vladimir");
        idToName.put(106, "Volibear");
        idToName.put(19, "Warwick");
        idToName.put(498, "Xayah");
        idToName.put(101, "Xerath");
        idToName.put(5, "Xin Zhao");
        idToName.put(157, "Yasuo");
        idToName.put(83, "Yorick");
        idToName.put(350, "Yuumi");
        idToName.put(154, "Zac");
        idToName.put(238, "Zed");
        idToName.put(115, "Ziggs");
        idToName.put(26, "Zilean");
        idToName.put(142, "Zoe");
        idToName.put(143, "Zyra");
    }

    private static String getNameFromId(int id){
        return idToName.get(id);
    }

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

    // Retrieve encrypted id from summoner name json
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
            createMap();
            for(int i = 0; i < results.participants.size(); i++)
                results.participants.get(i).championName = getNameFromId(results.participants.get(i).championId);
            return results.participants;
        } else {
            return null;
        }
    }

    public static void setServer(String s) {
        server = s;
    }
}
