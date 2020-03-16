package com.example.leagueoflegends.data;

import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.text.TextUtils;
import android.util.Log;

import com.example.leagueoflegends.MatchViewModel;
import com.example.leagueoflegends.utils.*;

public class MatchRepository implements LoadMatchTask.AsyncCallback, EncryptedIDTask.AsyncCallback{

    private static final String TAG = MatchRepository.class.getSimpleName();
    private MutableLiveData<List<LeagueMatchInfo>> mLiveMatch;
    private MutableLiveData<Status> mLoadingStatus;
    public String encryptedID;



    public MatchRepository(){
        mLiveMatch = new MutableLiveData<>();
        mLiveMatch.setValue(null);

        mLoadingStatus = new MutableLiveData<>();
        mLoadingStatus.setValue(Status.SUCCESS);
    }

    public void loadSummonerNameURL(String url) {
        new EncryptedIDTask(this).execute(url);
    }

    public void loadLiveGameURL(String encrypt) {
        String url = LeagueUtils.buildSpectatorURL(encrypt);
        System.out.println("Loading results: " + url);
        mLiveMatch.setValue(null);
        mLoadingStatus.setValue(Status.LOADING);
        new LoadMatchTask(this).execute(url);
    }

    public LiveData<List<LeagueMatchInfo>> getSearchResults() {
        return mLiveMatch;
    }

    public LiveData<Status> getLoadingStatus() {
        return mLoadingStatus;
    }

    @Override
    public void onLoadMatchFinished(List<LeagueMatchInfo> searchResults) {
        mLiveMatch.setValue(searchResults);
        if (searchResults != null)
            mLoadingStatus.setValue(Status.SUCCESS);
        else
            mLoadingStatus.setValue(Status.ERROR);
    }

    @Override
    public void onRetrievedEncryptedID(String id){
        if(id != null) {
            encryptedID = id;
            Log.d(TAG, "eID: " + encryptedID);
            loadLiveGameURL(encryptedID);  //chaining different async tasks together
        } else
            mLoadingStatus.setValue(Status.ERROR);
    }
}
