package com.example.leagueoflegends;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.leagueoflegends.data.*;

import java.util.List;

public class MatchViewModel extends ViewModel{

    private MatchRepository mRepository;
    private LiveData<List<LeagueMatchInfo>> Match;
    private LiveData<Status> mLoadingStatus;
    private String encryptedID;

    public MatchViewModel(){
        mRepository = new MatchRepository();
        Match = mRepository.getSearchResults();
        mLoadingStatus = mRepository.getLoadingStatus();
    }


    // Get the encrypted ID given the summoner name
    public void loadSummonerNameURL(String summoner){
        mRepository.loadSummonerNameURL(summoner);

        // If user exists in the region (preset to NA), get the encrypted id
        if(mRepository.encryptedID != null)
            encryptedID = mRepository.encryptedID;
    }

    // Get the list of participants in the match
    public void loadLiveGameURL(String encrypted){
        mRepository.loadLiveGameURL(encrypted);

    }

    // Getter
    public String getEncryptedID(){
        return encryptedID;
    }


    public LiveData<List<LeagueMatchInfo>> getSearchResults() {
        return Match;
    }

    public LiveData<Status> getLoadingStatus() {
        return mLoadingStatus;
    }

}
