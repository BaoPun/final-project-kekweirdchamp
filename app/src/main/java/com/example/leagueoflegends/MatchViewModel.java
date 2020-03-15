package com.example.leagueoflegends;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.leagueoflegends.data.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MatchViewModel extends ViewModel{
    private static final String TAG = MatchViewModel.class.getSimpleName();

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
    public void loadSummonerNameURL(String summoner) {
        mRepository.loadSummonerNameURL(summoner);
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
