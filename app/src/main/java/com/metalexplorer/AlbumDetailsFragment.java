package com.metalexplorer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.loki.afro.metallum.entity.Disc;
import com.github.loki.afro.metallum.entity.Member;
import com.github.loki.afro.metallum.entity.Track;
import com.github.loki.afro.metallum.search.API;

import java.util.ArrayList;
import java.util.Optional;

public class AlbumDetailsFragment extends Fragment implements RecyclerViewInterface {


    private View view;

    private TextView tracks;
    private TextView albumName;


    private RecyclerView trackRecyclerView;

    private TrackRecyclerViewAdapter trackAdapter;

    private ArrayList<Track> trackList = new ArrayList<>();

    private ImageView albumArtView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_album_details, container, false);

//        tracks = view.findViewById(R.id.Tracks);
        albumName = view.findViewById(R.id.AlbumHeader);
        albumArtView = view.findViewById(R.id.albumArtView);

//        tracks.setText(requireArguments().getStringArrayList("TRACKS").toString());

        albumName.setText(requireArguments().getString("ALBUM_NAME"));

        setupAlbumArt();
        setupTracksRecyclerView();

        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("AlbumDetailsFragment", "onCreate() called");

//        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavHostFragment.findNavController(HomeFragment.this)
//                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
//            }
//        });
    }

    public void setupAlbumArt() {
        byte[] photoBytes = getArguments().getByteArray("ALBUM_ART");

        if (photoBytes != null) {
            // Convert the byte array to a Bitmap
            Bitmap bitmap = BitmapFactory.decodeByteArray(photoBytes, 0, photoBytes.length);
            int newWidth = 295;
            int newHeight = 225;
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);

            // Set the Bitmap to the ImageView
            albumArtView.setImageBitmap(scaledBitmap);
        } else {
            // Handle the case where the photo is not present, maybe by setting a default image
            albumArtView.setImageResource(R.drawable.default_band_image);

        }


    }

    public void setupTracksRecyclerView() {
        if (!trackList.isEmpty()) {
            trackList.clear();
        }
        trackRecyclerView = view.findViewById(R.id.tracksRecyclerView);
        trackRecyclerView.setHasFixedSize(true);
        trackRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        trackAdapter = new TrackRecyclerViewAdapter(trackList, this);
        trackRecyclerView.setAdapter(trackAdapter);

        long discId = requireArguments().getLong("ALBUM");

        Disc disc = API.getDiscById(discId);

        trackList.addAll(disc.getTrackList());



//        for (int i = 0; i < requireArguments().getIntegerArrayList("DISCOGRAPHY").size(); i ++) {
//            Disc disc = API.getDiscById(requireArguments().getIntegerArrayList("DISCOGRAPHY").get(i));
////            String name = disc.toString().split("name=")[1].split("\\)")[0];
//            trackList.add(disc.getTrackList().get(i));
//        }

//        for (int j = 0; j < requireArguments().getStringArrayList("ROLES").size(); j ++) {
////            String role = requireArguments().getStringArrayList("ROLES").get(j);
//            roleTextView = trackRecyclerView.findViewById(R.id.textview6);
//            roleTextView.setText(requireArguments().getStringArrayList("ROLES").get(j));
//        }
    }

    @Override
    public void OnItemClick(int position, RecyclerView recyclerView) {

//        if (recyclerView == this.recyclerView) {
            LyricsFragment lyricsFragment = new LyricsFragment();
            Bundle bundle = new Bundle();

            if (trackList.get(position).isInstrumental()) {
                bundle.putString("LYRICS", "Instrumental");
        } else {
                bundle.putString("LYRICS", trackList.get(position).getLyrics().toString());
            }
//            bundle.putString("LYRICS", trackList.get(position).getLyrics().toString());
            bundle.putString("BAND_NAME", trackList.get(position).getBandName());
            bundle.putString("TRACK_NAME", trackList.get(position).getName());


            lyricsFragment.setArguments(bundle);

            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, lyricsFragment);
            fragmentTransaction.setReorderingAllowed(true);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        Log.d("ItemClick", "Item clicked at position: " + position);
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        view = null;
    }

}