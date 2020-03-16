package com.example.leagueoflegends;

import android.service.autofill.FieldClassification;
import android.view.LayoutInflater;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.leagueoflegends.data.*;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.SearchResultViewHolder>{
    private List<LeagueMatchInfo> LiveMatchInfo;
    private OnSearchResultClickListener listener;

    interface OnSearchResultClickListener{
        void onSearchResultClicked(LeagueMatchInfo info);
    }

    public MatchAdapter(OnSearchResultClickListener click){
        listener = click;
    }

    public void updateSearchResults(List<LeagueMatchInfo> searchResultsList) {
        //for(int i = 0; i < searchResultsList.size(); i++)
        //    searchResultsList.get(i).championName = getNameFromId(searchResultsList.get(i).championId);
        LiveMatchInfo = searchResultsList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount(){
        if(LiveMatchInfo != null)
            return LiveMatchInfo.size();
        return 0;
    }

    @NonNull
    @Override
    public SearchResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.search_result_item, parent, false);
        return new SearchResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchResultViewHolder holder, int pos){
        holder.bind(LiveMatchInfo.get(pos));
        holder.text.setTextColor((LiveMatchInfo.get(pos).teamId == 100 ? holder.text.getContext().getColor(R.color.blueSide): holder.text.getContext().getColor(R.color.redSide)));
    }

    class SearchResultViewHolder extends RecyclerView.ViewHolder{
        private TextView text;

        SearchResultViewHolder(View itemView){
            super(itemView);
            text = itemView.findViewById(R.id.tv_search_result);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    listener.onSearchResultClicked(LiveMatchInfo.get(getAdapterPosition()));
                }
            });
        }

        void bind(LeagueMatchInfo match){
            text.setText((match.teamId == 100 ? "Blue: " : "Red: ") + match.summonerName);
            //text.setTextColor((match.teamId == 100 ? Color.parseColor("#87CEFA") : Color.parseColor("#FF4500")));
        }

    }

}
