package com.example.leagueoflegends.data;

import android.os.AsyncTask;

import com.example.leagueoflegends.utils.*;

import java.io.IOException;
import java.util.List;


public class EncryptedIDTask extends AsyncTask<String, Void, String> {

    private AsyncCallback mCallback;

    public interface AsyncCallback{
        void onRetrievedEncryptedID(String s);
    }

    EncryptedIDTask(AsyncCallback callback){
        mCallback = callback;
    }

    @Override
    protected String doInBackground(String... strings){
        String url = strings[0];
        String SummonerInfo = null;
        try{
            SummonerInfo = NetworkUtils.doHttpGet(url);
        }
        catch (IOException e){
            //e.printStackTrace();
        }
        return SummonerInfo;
    }

    @Override
    protected void onPostExecute(String s){
        //System.out.println("Contents of summoner json: " + s);
        String id = "";
        if(s != null)
            id = LeagueUtils.getIdFromURL(s);
        mCallback.onRetrievedEncryptedID(id);
    }
}
