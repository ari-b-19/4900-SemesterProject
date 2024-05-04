package com.metalexplorer;

import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.loki.afro.metallum.entity.Disc;
import com.github.loki.afro.metallum.entity.Track;

import java.util.ArrayList;

public class TrackRecyclerViewAdapter extends RecyclerView.Adapter<TrackRecyclerViewAdapter.ViewHolder> {
    private ArrayList<Track> mData;
    private LayoutInflater mInflater;

    private final RecyclerViewInterface recyclerViewInterface;

    private RecyclerView recyclerView;

    // Constructor for the adapter
    public TrackRecyclerViewAdapter(ArrayList<Track> data, RecyclerViewInterface recyclerViewInterface) {
        this.mData = data;
        this.recyclerViewInterface = recyclerViewInterface;
//        this.recyclerView = recyclerView;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public TrackRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tracks_layout, parent, false);
        return new TrackRecyclerViewAdapter.ViewHolder(view, recyclerViewInterface);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(TrackRecyclerViewAdapter.ViewHolder holder, int position) {
        Track item = mData.get(position);
        int trackNumber = item.getTrackNumber();
        String trackName = trackNumber + ". " + item.toString().split("name=")[1].split("\\)")[0];
        if (!item.isInstrumental()) {
        holder.myTextView.setText(trackName);
        holder.myTextView.setPaintFlags(holder.myTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        } else {
            holder.myTextView.setText(trackName);
        }


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // Stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView myTextView;

        ViewHolder(View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.textview7);

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
