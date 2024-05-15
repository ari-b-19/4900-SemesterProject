package com.metalexplorer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.loki.afro.metallum.entity.Disc;
import com.github.loki.afro.metallum.search.API;
import com.github.loki.afro.metallum.search.query.entity.SearchDiscResult;


import java.util.ArrayList;
import java.util.Optional;


public class AlbumRecyclerViewAdapter extends RecyclerView.Adapter<AlbumRecyclerViewAdapter.ViewHolder> {

    private ArrayList<SearchDiscResult> discData;
    private LayoutInflater mInflater;

    private final RecyclerViewInterface recyclerViewInterface;

    private RecyclerView recyclerView;

    private String albumData;

    private Boolean flag = false;

    public AlbumRecyclerViewAdapter(ArrayList<SearchDiscResult> data, RecyclerViewInterface recyclerViewInterface, RecyclerView recyclerView) {
        this.discData = data;
        this.recyclerViewInterface = recyclerViewInterface;
        this.recyclerView = recyclerView;
    }

    public AlbumRecyclerViewAdapter(ArrayList<SearchDiscResult> data, RecyclerViewInterface recyclerViewInterface, RecyclerView recyclerView, Boolean flag) {
        this.discData = data;
        this.recyclerViewInterface = recyclerViewInterface;
        this.recyclerView = recyclerView;
        this.flag = flag;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.albums_layout, parent, false);
        return new ViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        Disc disc = API.getDiscById(discData.get(position).getId());
        String releaseYear = "";
        String artist = "By: " + discData.get(position).getBandName();
        String album = discData.get(position).getName();
        if (flag == true) {
            holder.myTextView.setText(album);
            holder.myTextView2.setText(artist);
        } else if (flag == false) {
            holder.myTextView.setText(album);
            holder.myTextView2.setText(releaseYear);
            flag = false;
        }
    }

    @Override
    public int getItemCount() {
        return discData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView myTextView;
        TextView myTextView2;

        TextView band;

        TextView genre;

        ImageView albumArt;

        ViewHolder(View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.textview3);
            myTextView2 = itemView.findViewById(R.id.textview4);
            band = itemView.findViewById(R.id.Band);
            genre = itemView.findViewById(R.id.Genre);
            albumArt = itemView.findViewById(R.id.AlbumArt);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

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
