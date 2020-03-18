package com.example.leagueoflegends;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
    private static final String TAG = MainActivity.class.getSimpleName();

    //Private members
    private RecyclerView mRecyclerLiveData;
    private EditText mSearchBar;
    private TextView mErrorMessage;
    private ProgressBar mProgressBar;
    private MatchAdapter mAdapter;
    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;
    private ActionBarDrawerToggle mDrawerToggle;

    private MatchViewModel mLiveMatchDataViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Find the toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Now get the entire action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_nav_menu);

        getSupportActionBar().setElevation(0);


        //Find the drawer
        mDrawerLayout = findViewById(R.id.drawer_layout);

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
                if(!TextUtils.isEmpty(summoner))
                    queryForMatchInfo(summoner);
                    //searchSummoner(); //change back to queryForMatchInfo
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSearchResultClicked(LeagueMatchInfo info){
        // Create intent here (NOT COMPLETE FOR NOW)
        System.out.println("Who is this: " + info.summonerName);
        System.out.println("Champion: " + info.championName);
        System.out.println("Team: " + (info.teamId == 100 ? "Blue" : "Red"));

        searchSummoner(info.summonerName);
        //searchChampion(info.championName);
    }

    /*
        This function does two things:
            1. Extract the unique summoner ID from the summoner name
            2. Using that unique summoner ID, get the list of all the players in a Summoners Rift game with that summoner name
     */
    public void queryForMatchInfo(String summoner){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String server = preferences.getString(
                getString(R.string.pref_server_key),
                getString(R.string.pref_server_default_value)
        );

        LeagueUtils.setServer(server);

        // build the summoner name query url
        String summonerID_url = LeagueUtils.buildSummonerNameURL(summoner);
        Log.d(TAG, "searching with url: " + summonerID_url);

        // Get JSON content from first url and then extract its encrypted ID
        // because both tasks are async, we have to chain the async tasks sequentially on their override methods in MatchRepository.java
        mLiveMatchDataViewModel.loadSummonerNameURL(summonerID_url);
    }

    //created searchSummoner function 3/15/2020
    public void searchSummoner(String summoner) {
        //String summoner = mSearchBar.getText().toString();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String server = preferences.getString(
                getString(R.string.pref_server_key),
                getString(R.string.pref_server_default_value));
        server = convertServer(server);
        String url = "https://" + server + ".op.gg/summoner/userName=" + summoner;

        Intent intent = new Intent();
        //intent.setPackage("com.android.chrome");
        intent.setAction(Intent.ACTION_VIEW);

        PackageManager packageManager = getPackageManager();
        packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    public void searchChampion(String champion) {
        //String summoner = mSearchBar.getText().toString();
        champion = champion.replaceAll("\\s", ""); //removes all spaces in champ name
        champion = champion.toLowerCase();

        String url = "https://u.gg/lol/champions/" + champion + "/build";

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);

        PackageManager packageManager = getPackageManager();
        packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    public String convertServer(String server) {
        switch(server) {
            case "ru":
                server = "ru";
                return server;
            case "kr":
                server = "";
                return server;
            case "br1":
                server = "br";
                return server;
            case "oc1":
                server = "oce";
                return server;
            case "jp1":
                server = "jp";
                return server;
            case "na1":
                server = "na";
                return server;
            case "eun1":
                server = "eune";
                return server;
            case "euw1":
                server = "euw";
                return server;
            case "tr1":
                server = "tr";
                return server;
            case "la1":
                server = "lan";
                return server;
            case "la2":
                server = "las";
                return server;
            default:
                return("invalid server setting found");
        }
    }
}
