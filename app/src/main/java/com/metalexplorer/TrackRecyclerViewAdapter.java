package com.metalexplorer;

import android.graphics.Color;
import android.graphics.Paint;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.loki.afro.metallum.entity.Track;

import java.util.ArrayList;

public class TrackRecyclerViewAdapter extends RecyclerView.Adapter<TrackRecyclerViewAdapter.ViewHolder> {
    private ArrayList<Track> trackData;

    private final RecyclerViewInterface recyclerViewInterface;

    private RecyclerView recyclerView;
    public TrackRecyclerViewAdapter(ArrayList<Track> data, RecyclerViewInterface recyclerViewInterface) {
        this.trackData = data;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public TrackRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tracks_layout, parent, false);
        return new TrackRecyclerViewAdapter.ViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(TrackRecyclerViewAdapter.ViewHolder holder, int position) {
//        Track item = trackData.get(position);

        int trackNumber = trackData.get(position).getTrackNumber();
        String trackName = trackNumber + ". " + trackData.get(position).toString().split("name=")[1].split("\\)")[0];
        SpannableString spannableString = new SpannableString(trackName);
        int start = 0;
        int end = trackName.length();
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#800080")), start, end, SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (!trackData.get(position).isInstrumental()) {
        holder.myTextView.setText(spannableString);
        holder.myTextView.setPaintFlags(holder.myTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        } else {
            holder.myTextView.setText(trackName);
        }


    }

    @Override
    public int getItemCount() {
        return trackData.size();
    }

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
