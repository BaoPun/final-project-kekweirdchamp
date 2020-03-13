package com.example.leagueoflegends;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.AsyncTask;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.net.MalformedURLException;
import java.net.*;
import java.io.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.MalformedURLException;
import java.net.URL;


import javax.net.ssl.HttpsURLConnection;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import android.os.Bundle;

import com.example.leagueoflegends.data.*;
import com.example.leagueoflegends.utils.*;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MatchAdapter.OnSearchResultClickListener{

    //Private members
    private RecyclerView mRecyclerLiveData;
    private EditText mSearchBar;
    private TextView mErrorMessage;
    private ProgressBar mProgressBar;
    private MatchAdapter mAdapter;
    private DrawerLayout mDrawerLayout;

    private MatchViewModel mLiveMatchDataViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Find the adapter
        mAdapter = new MatchAdapter(this);

        //Find the recycler view for the data to be listed in
        mRecyclerLiveData = findViewById(R.id.rv_search_results);
        mRecyclerLiveData.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerLiveData.setHasFixedSize(true);
        mRecyclerLiveData.setAdapter(mAdapter);

        //Find the search box
        mSearchBar = findViewById(R.id.et_search_box);

        //Find the error message
        mErrorMessage = findViewById(R.id.tv_error_message);

        //Find the progress bar
        mProgressBar = findViewById(R.id.pb_loading_indicator);


        //Set up the view model for the live match data
        mLiveMatchDataViewModel = new ViewModelProvider(this).get(MatchViewModel.class);
        mLiveMatchDataViewModel.getSearchResults().observe(this, new Observer<List<LeagueMatchInfo>>() {
            @Override
            public void onChanged(List<LeagueMatchInfo> leagueMatchInfos) {
                mAdapter.updateSearchResults(leagueMatchInfos);
            }
        });
        mLiveMatchDataViewModel.getLoadingStatus().observe(this, new Observer<Status>(){
            @Override
            public void onChanged(Status status){
                if(status == Status.LOADING)
                    mProgressBar.setVisibility(View.VISIBLE);
                else if(status == Status.SUCCESS){
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mRecyclerLiveData.setVisibility(View.VISIBLE);
                    mErrorMessage.setVisibility(View.INVISIBLE);
                }
                else{
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mRecyclerLiveData.setVisibility(View.INVISIBLE);
                    mErrorMessage.setVisibility(View.VISIBLE);
                }
            }
        });

        //Create the button and its click event
        Button searchButton = findViewById(R.id.btn_search);
        searchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String summoner = mSearchBar.getText().toString();
                // Extract a list of summoner names that are a part of the LIVE MATCH
                if(!TextUtils.isEmpty(summoner))
                    queryForMatchInfo(summoner);
            }
        });


    }

    @Override
    public void onSearchResultClicked(LeagueMatchInfo info){
        // Create intent here (NOT COMPLETE FOR NOW)
        System.out.println("Who is this: " + info.summonerName);
        System.out.println("Champion: " + info.championId);
    }

    /*
        This function does two things:
            1. Extract the unique summoner ID from the summoner name
            2. Using that unique summoner ID, get the list of all the players in a Summoners Rift game with that summoner name
     */
    public void queryForMatchInfo(String summoner){

        // Part 1: build the summoner name query url
        String summonerID_url = LeagueUtils.buildSummonerNameURL(summoner);

        // Get JSON content from first url and then extract its encrypted ID
        mLiveMatchDataViewModel.loadSummonerNameURL(summonerID_url);
        String encryptedID = mLiveMatchDataViewModel.getEncryptedID();

        // Part 2: using the encrypted id retrieved above, load match data.
        if(encryptedID != null)
            mLiveMatchDataViewModel.loadLiveGameURL(encryptedID);
    }



}
