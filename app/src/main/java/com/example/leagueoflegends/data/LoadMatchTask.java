package com.example.leagueoflegends.data;

import android.os.AsyncTask;

import com.example.leagueoflegends.utils.*;

import java.io.IOException;
import java.util.List;


public class LoadMatchTask extends AsyncTask<String, Void, String>{

    private AsyncCallback mCallback;

    public interface AsyncCallback{
        void onLoadMatchFinished(List<LeagueMatchInfo> match);
    }

    LoadMatchTask(AsyncCallback callback){
        mCallback = callback;
    }

    @Override
    protected String doInBackground(String... strings){
        String url = strings[0];
        String MatchInfo = null;
        try{ MatchInfo = NetworkUtils.doHttpGet(url); }
        catch(IOException e){}
        return MatchInfo;
    }

    @Override
    protected void onPostExecute(String s){
        List<LeagueMatchInfo> searchResults = null;
        if(s != null)
            searchResults = LeagueUtils.parseMatchData(s);
        mCallback.onLoadMatchFinished(searchResults);
    }
}
