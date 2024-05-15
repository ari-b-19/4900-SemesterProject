package com.metalexplorer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.loki.afro.metallum.entity.Band;
import com.github.loki.afro.metallum.entity.Link;

import java.util.ArrayList;

public class BandLinksRecyclerViewAdapter extends RecyclerView.Adapter<BandLinksRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Link> linkData;

    public BandLinksRecyclerViewAdapter(ArrayList<Link> data) {
        this.linkData = data;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.links_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        linkData.get(position).getLinks();
        holder.myTextView.setText(linkData.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return linkData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.link);
        }
    }
}