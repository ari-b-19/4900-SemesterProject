package com.metalexplorer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.loki.afro.metallum.entity.Band;
import com.github.loki.afro.metallum.entity.Member;
import com.github.loki.afro.metallum.search.API;

import java.util.ArrayList;

public class MemberRecyclerViewAdapter extends RecyclerView.Adapter<MemberRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Member> mData;
    private LayoutInflater mInflater;

    // Constructor for the adapter
    public MemberRecyclerViewAdapter(ArrayList<Member> data) {
        this.mData = data;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lineup_layout, parent, false);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Member item = mData.get(position);
        holder.myTextView.setText(item.getName());

//        Long partialMemberId = mData.get(position).getId();

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

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.textview5);
//            myTextView2 = itemView.findViewById(R.id.textview6);
        }
    }
}
