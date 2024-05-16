package com.metalexplorer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.github.loki.afro.metallum.entity.Band;
import com.github.loki.afro.metallum.search.query.entity.SearchBandResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private final RecyclerViewInterface recyclerViewInterface;
    private List<SearchBandResult> bandData;
    private List<Band> bandData2;//

    private RecyclerView recyclerView;

    private boolean flag = false;

    // Constructor to initialize the data and inflater
    public RecyclerViewAdapter(ArrayList<SearchBandResult> data, RecyclerViewInterface recyclerViewInterface, RecyclerView recyclerView) {
        this.bandData = data;
        this.recyclerViewInterface = recyclerViewInterface;
        this.recyclerView = recyclerView;
    }

    public RecyclerViewAdapter(ArrayList<Band> data2, RecyclerViewInterface recyclerViewInterface, RecyclerView recyclerView, boolean flag) {
        this.bandData2 = data2;
        this.recyclerViewInterface = recyclerViewInterface;
        this.recyclerView = recyclerView;
        this.flag = flag;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bands_layout, parent, false);
        return new MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (flag != true) {
            Optional<String> optionalGenre = bandData.get(position).getGenre();
            String empty = "No matches found";

//        Band band;
//        for (int i = 0; i < band.getCurrentMembers(); i++) {
//            band.getCurrentMembers().get(i).
//        }

            if (optionalGenre.isPresent()) {
                String genre = optionalGenre.get();
                holder.bandGenre.setText(genre);
            } else {
                holder.bandGenre.setText("");
            }
            if (bandData.get(position) == null) {
                holder.band.setText(empty);
            } else {
                holder.band.setText(bandData.get(position).getName());
            }
        } else {
            holder.band.setText(bandData2.get(position).getName());
            holder.bandGenre.setText(bandData2.get(position).getGenre());
        }

    }


    @Override
    public int getItemCount() {
        if (flag != true) {
            return bandData.size();

        } else {
            return bandData2.size();
        }

    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView band;
        TextView bandGenre;

        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            band = itemView.findViewById(R.id.textview);
            bandGenre = itemView.findViewById(R.id.textview2);

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