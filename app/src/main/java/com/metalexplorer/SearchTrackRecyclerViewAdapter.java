package com.metalexplorer;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.loki.afro.metallum.entity.Track;
import com.github.loki.afro.metallum.search.query.entity.SearchTrackResult;

import java.util.ArrayList;

public class SearchTrackRecyclerViewAdapter extends RecyclerView.Adapter<SearchTrackRecyclerViewAdapter.ViewHolder> {
    private ArrayList<SearchTrackResult> trackData;

    private final RecyclerViewInterface recyclerViewInterface;

    private RecyclerView recyclerView;
    public SearchTrackRecyclerViewAdapter(ArrayList<SearchTrackResult> data, RecyclerViewInterface recyclerViewInterface) {
        this.trackData = data;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public SearchTrackRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_tracks_layout, parent, false);
        return new SearchTrackRecyclerViewAdapter.ViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(SearchTrackRecyclerViewAdapter.ViewHolder holder, int position) {
//        Track item = trackData.get(position);
        holder.track.setText(trackData.get(position).getName());
        holder.trackBand.setText(trackData.get(position).getBandName());



    }

    @Override
    public int getItemCount() {
        return trackData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView track;
        TextView trackBand;

        ViewHolder(View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            track = itemView.findViewById(R.id.track);
            trackBand = itemView.findViewById(R.id.trackBand);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get the position of the clicked item
                    if (recyclerViewInterface != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.OnItemClick(position, recyclerView);

                        }
                    }

                }

            });
        }
    }
}
