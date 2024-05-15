package com.metalexplorer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.loki.afro.metallum.entity.Band;
import com.github.loki.afro.metallum.entity.Disc;
import com.github.loki.afro.metallum.enums.DiscType;
import com.github.loki.afro.metallum.search.API;
import com.github.loki.afro.metallum.search.query.entity.SearchBandResult;
import com.github.loki.afro.metallum.search.query.entity.SearchDiscResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HomeScreenRecyclerViewAdapter extends RecyclerView.Adapter<HomeScreenRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Disc> discData;

    private final RecyclerViewInterface recyclerViewInterface;

    private RecyclerView recyclerView;
    public HomeScreenRecyclerViewAdapter(ArrayList<Disc> data, RecyclerViewInterface recyclerViewInterface, RecyclerView recyclerView) {
        this.discData = data;
        this.recyclerViewInterface = recyclerViewInterface;
        this.recyclerView = recyclerView;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_releases_layout, parent, false);
        return new ViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Band band = API.getBandById(discData.get(position).getBand().getId());
        String forFansOf = "For fans of: " + band.getSimilarArtists();
        setupAlbumArt(holder, position);
        holder.band.setText(band.getName());
        holder.genre.setText(band.getGenre());
        holder.similarArtists.setText(forFansOf);
//        }
    }

    public void setupAlbumArt(ViewHolder holder, int position) {
        Optional<byte[]> optionalPhoto = discData.get(position).getArtwork();


        if (optionalPhoto.isPresent()) {
            // Unwrap optional value
            byte[] photoBytes = optionalPhoto.get();

            // Decode byte array into Bitmap
            Bitmap bitmap = BitmapFactory.decodeByteArray(photoBytes, 0, photoBytes.length);
            int newWidth = 295;
            int newHeight = 235;
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);

            // Set Bitmap to ImageView
            holder.albumArt.setImageBitmap(scaledBitmap);
        }

    }

    public void setData(List<Disc> data) {
        discData.clear();
        discData.addAll(data);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return discData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView band;

        TextView genre;

        TextView similarArtists;

        ImageView albumArt;

        ViewHolder(View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            band = itemView.findViewById(R.id.Band);
            genre = itemView.findViewById(R.id.Genre);
            similarArtists = itemView.findViewById(R.id.similarArtists);
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
