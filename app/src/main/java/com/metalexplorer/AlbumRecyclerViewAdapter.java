package com.metalexplorer;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.loki.afro.metallum.entity.Disc;

import java.util.ArrayList;

public class AlbumRecyclerViewAdapter extends RecyclerView.Adapter<AlbumRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Disc> mData;
    private LayoutInflater mInflater;

    private final RecyclerViewInterface recyclerViewInterface;

    private RecyclerView recyclerView;

    private String albumData;

    // Constructor for the adapter
    public AlbumRecyclerViewAdapter(ArrayList<Disc> data, RecyclerViewInterface recyclerViewInterface, RecyclerView recyclerView) {
        this.mData = data;
        this.recyclerViewInterface = recyclerViewInterface;
        this.recyclerView = recyclerView;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.albums_layout, parent, false);
        return new ViewHolder(view, recyclerViewInterface);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Disc item = mData.get(position);
        String releaseYear = mData.get(position).getReleaseDate();
        String artist = "By: " + mData.get(position).getBandName();
        String textView1 = item.toString().split("name=")[1].split("\\)")[0] + " - " + releaseYear;
        if (isFromSearchFragment(true)) {
            holder.myTextView.setText(textView1);
            holder.myTextView2.setText(artist);
        } else {
            holder.myTextView.setText(textView1);
        }
//        holder.myTextView.setText(item.toString().split("name=")[1].split("\\)")[0]);
//        holder.myTextView2.setText(releaseYear);
    }

    public boolean isFromSearchFragment(boolean flag) {
        return flag;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // Stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView myTextView;
        TextView myTextView2;

        ViewHolder(View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.textview3);
            myTextView2 = itemView.findViewById(R.id.textview4);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get the position of the clicked item
                    int clickedRecyclerViewId = v.getId();
                    String clickedRecyclerViewTag = (String) v.getTag();
                    Log.d("Tag", "Clicked RecyclerView tag: " + clickedRecyclerViewTag);
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
