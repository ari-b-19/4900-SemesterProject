package com.metalexplorer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.loki.afro.metallum.entity.Band;
import com.github.loki.afro.metallum.entity.Disc;
import com.github.loki.afro.metallum.entity.Track;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BandProfileFragment extends Fragment implements RecyclerViewInterface {

    private ArrayList<Disc> itemList = new ArrayList<>();

    private ArrayList<Band.PartialMember> memberList = new ArrayList<>();

    private View view;

    private ImageView bandImageView;
    private ExecutorService executorService = Executors.newCachedThreadPool();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_band_profile, container, false);

        bandImageView = view.findViewById(R.id.imageView);

        setupBandImage();
        setupAlbumRecyclerView();
        setupLineupRecyclerView();


        return view;

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("BandProfileFragment", "onCreate() called");
    }

    public void setupBandImage() {
        byte[] photoBytes = getArguments().getByteArray("IMAGE");

        if (photoBytes != null) {
            // Convert the byte array to a Bitmap
            Bitmap bitmap = BitmapFactory.decodeByteArray(photoBytes, 0, photoBytes.length);
            int newWidth = 385;
            int newHeight = 80;
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);

            bandImageView.setImageBitmap(scaledBitmap);
        } else {
            bandImageView.setImageResource(R.drawable.default_band_image);
        }
    }

    public void setupAlbumRecyclerView() {
        executorService.execute(() -> {
        if (!itemList.isEmpty()) {
            itemList.clear();
        }
        RecyclerView recyclerView = view.findViewById(R.id.albumRecyclerView);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setNestedScrollingEnabled(false);

        AlbumRecyclerViewAdapter itemAdapter = new AlbumRecyclerViewAdapter(itemList, this, recyclerView);
        recyclerView.setAdapter(itemAdapter);

        Bundle bundle = getArguments();
        if (bundle != null) {
            ParcelableDisc parcelableDiscList = bundle.getParcelable("DISCOGRAPHY");

            if (parcelableDiscList != null) {
                List<Disc> discs = parcelableDiscList.getUserList();

                itemList.addAll(discs);

            }
            }
        });
    }

    public void setupLineupRecyclerView() {
        if (!memberList.isEmpty()) {
            memberList.clear();
        }
        RecyclerView memberRecyclerView = view.findViewById(R.id.lineupRecyclerView);
        memberRecyclerView.setHasFixedSize(true);
        memberRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        MemberRecyclerViewAdapter memberAdapter = new MemberRecyclerViewAdapter(memberList);
        memberRecyclerView.setAdapter(memberAdapter);

        Bundle bundle = getArguments();
        if (bundle != null) {
            ParcelableLineup parcelableLineup = bundle.getParcelable("LINEUP");

            if (parcelableLineup != null) {
                List<Band.PartialMember> members = parcelableLineup.getlineup();

                memberList.addAll(members);

            }
        }
    }
    @Override
    public void OnItemClick(int position, RecyclerView recyclerView) {

        if (recyclerView == view.findViewById(R.id.albumRecyclerView)) {
            AlbumDetailsFragment albumDetailsFragment = new AlbumDetailsFragment();
            Bundle bundle = new Bundle();

            List<Track> trackList = new ArrayList<>(itemList.get(position).getTrackList());

            ParcelableTracklist parcelableTracklist = new ParcelableTracklist(trackList);

            bundle.putParcelable("TRACKS", parcelableTracklist);

            bundle.putString("ALBUM_NAME", itemList.get(position).getName());
            bundle.putLong("ALBUM", itemList.get(position).getId());


            Optional<byte[]> optionalPhoto = itemList.get(position).getArtwork();

            if (optionalPhoto.isPresent()) {
                byte[] photoBytes = optionalPhoto.get();
                bundle.putByteArray("ALBUM_ART", photoBytes);
            } else {
                bundle.putByteArray("ALBUM_ART", (byte[]) null);
            }

            albumDetailsFragment.setArguments(bundle);

            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, albumDetailsFragment);
            fragmentTransaction.setReorderingAllowed(true);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

        Log.d("ItemClick", "Item clicked at position: " + position);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        view = null;
    }

}
