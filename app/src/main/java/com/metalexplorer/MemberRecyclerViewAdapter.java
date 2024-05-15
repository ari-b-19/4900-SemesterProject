package com.metalexplorer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.loki.afro.metallum.entity.Band;

import java.util.ArrayList;

public class MemberRecyclerViewAdapter extends RecyclerView.Adapter<MemberRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Band.PartialMember> memberData;
    private final RecyclerViewInterface recyclerViewInterface;

    private RecyclerView recyclerView;

    public MemberRecyclerViewAdapter(ArrayList<Band.PartialMember> data, RecyclerViewInterface recyclerViewInterface, RecyclerView recyclerView) {
        this.memberData = data;
        this.recyclerViewInterface = recyclerViewInterface;
        this.recyclerView = recyclerView;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lineup_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        Disc.PartialMember item = memberData.get(position);
        holder.myTextView.setText(memberData.get(position).getName());
        holder.myTextView2.setText(memberData.get(position).getRole());

//        Long partialMemberId = memberData.get(position).getId();

    }

    @Override
    public int getItemCount() {
        return memberData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView myTextView;
        TextView myTextView2;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.textview5);
            myTextView2 = itemView.findViewById(R.id.textview6);

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
