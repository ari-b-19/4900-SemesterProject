package com.metalexplorer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.github.loki.afro.metallum.entity.Band;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private final RecyclerViewInterface recyclerViewInterface;
    private List<Band> bandData; //

    // Constructor to initialize the data and inflater
    public RecyclerViewAdapter(List<Band> data, RecyclerViewInterface recyclerViewInterface) {
        this.bandData = data;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bands_layout, parent, false);
        return new MyViewHolder(view, recyclerViewInterface);
    }

    // Bind data to the views inside each item
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textView.setText(bandData.get(position).getName());
        holder.textView2.setText(bandData.get(position).getGenre());
//        SearchBandResult band = mData.get(position);
//        holder.bind(band);
    }


    // Return the total number of items in the data set
    @Override
    public int getItemCount() {

        return bandData.size();
    }


    // ViewHolder class to hold references to the views inside each item
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        TextView textView2;

        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            textView = itemView.findViewById(R.id.textview);
            textView2 = itemView.findViewById(R.id.textview2);
            // Set OnClickListener for the item view
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get the position of the clicked item
                    if (recyclerViewInterface != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.OnItemClick(position);

                        }
                    }

                }

            });

//        public void bind(SearchBandResult bandName) {
//
//            textView.setText(bandName.getName());
//        }


        }
    }

}