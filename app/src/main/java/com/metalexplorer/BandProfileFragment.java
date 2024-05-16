package com.metalexplorer;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.loki.afro.metallum.entity.Band;
import com.github.loki.afro.metallum.entity.Disc;
import com.github.loki.afro.metallum.entity.Link;
import com.github.loki.afro.metallum.entity.Track;
import com.github.loki.afro.metallum.search.API;
import com.github.loki.afro.metallum.search.query.entity.SearchDiscResult;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BandProfileFragment extends Fragment implements RecyclerViewInterface {

    private ArrayList<SearchDiscResult> itemList = new ArrayList<>();

    private ArrayList<Band.PartialMember> memberList = new ArrayList<>();

    private ArrayList<Link> links = new ArrayList<>();

    private View view;

    private ImageView bandImageView;

    private ImageView bandPhoto;
    private ExecutorService executorService = Executors.newCachedThreadPool();

    private Button button;

    private TextView formed, country, genre;

    List<Long> ids = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_band_profile, container, false);

        List<Long> retrievedLongList = UserBookmarks.getLongList(requireContext(), "user123");
        ids.addAll(retrievedLongList);
        Log.d("BandProfileFragment", "Bookmarked bands: " + retrievedLongList.toString());
        bandImageView = view.findViewById(R.id.imageView);
        bandPhoto = view.findViewById(R.id.imageView2);
        formed = view.findViewById(R.id.formed);
        country = view.findViewById(R.id.countryOfOrigin);
        genre = view.findViewById(R.id.bandGenre);
        String yearFormedIn = "Year formed in: " + getArguments().getString("FORMED");

        formed.setText(yearFormedIn);
        country.setText(getArguments().getString("COUNTRY"));
        genre.setText(getArguments().getString("GENRE"));


        setupBandImage();
        setupAlbumRecyclerView();
        setupLineupRecyclerView();

        Button buttonOptions = view.findViewById(R.id.buttonOptions);
        buttonOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptionsDialog();
            }
        });

//        setupLinksRecyclerView();

//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                assert getArguments() != null;
//                long id = getArguments().getLong("BAND_ID");
//
//                Bundle bundle = new Bundle();
//
//                bundle.
//
//                // Your code to handle button click goes here
//                // For example, you can show a Toast message
////                Toast.makeText(getApplicationContext(), "Button clicked", Toast.LENGTH_SHORT).show();
//            }
//        });
        return view;

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("BandProfileFragment", "onCreate() called");
    }

    public void showOptionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_options, null);
        builder.setView(dialogView);

        Button buttonBookmark = dialogView.findViewById(R.id.buttonBookmark);

        if (ids.contains(getArguments().getLong("ID"))) {
            buttonBookmark.setText("Bookmarked");
        }
        buttonBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ids.contains(getArguments().getLong("ID"))) {
                    assert getArguments() != null;
                    ids.add(getArguments().getLong("ID"));
                    UserBookmarks.saveLongList(requireContext(), "user123", ids);
                    buttonBookmark.setText("Bookmarked");
                }

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void setupBandImage() {
        byte[] photoBytes = getArguments().getByteArray("IMAGE");


        if (photoBytes != null) {
            // Convert the byte array to a Bitmap
            Bitmap bitmap = BitmapFactory.decodeByteArray(photoBytes, 0, photoBytes.length);
            int newWidth = 385;
            int newHeight = 80;
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);

            bandImageView.setImageBitmap(bitmap);
        } else {
            bandImageView.setImageResource(R.drawable.default_band_image);
        }

        byte[] photoBytes2 = getArguments().getByteArray("BAND_PHOTO");

        if (photoBytes2 != null) {
            // Convert the byte array to a Bitmap
            Bitmap bitmap2 = BitmapFactory.decodeByteArray(photoBytes2, 0, photoBytes2.length);

            bandPhoto.setImageBitmap(bitmap2);
        } else {
            bandPhoto.setImageResource(R.drawable.default_band_image);
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
        recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

        Bundle bundle = getArguments();
        if (bundle != null) {
            ParcelableDisc parcelableDiscList = bundle.getParcelable("DISCOGRAPHY");

            if (parcelableDiscList != null) {
                List<SearchDiscResult> discs = parcelableDiscList.getUserList();

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

        MemberRecyclerViewAdapter memberAdapter = new MemberRecyclerViewAdapter(memberList, this, memberRecyclerView);
        memberRecyclerView.setAdapter(memberAdapter);
        memberRecyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

        Bundle bundle = getArguments();
        if (bundle != null) {
            ParcelableLineup parcelableLineup = bundle.getParcelable("LINEUP");

            if (parcelableLineup != null) {
                List<Band.PartialMember> members = parcelableLineup.getlineup();

                memberList.addAll(members);

            }
        }
    }

//    public void setupLinksRecyclerView() {
//        if (!links.isEmpty()) {
//            links.clear();
//        }
//        RecyclerView bandLinksRecyclerView = view.findViewById(R.id.linksRecyclerView);
//        bandLinksRecyclerView.setHasFixedSize(true);
//        bandLinksRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
//
//        BandLinksRecyclerViewAdapter linksAdapter = new BandLinksRecyclerViewAdapter(links);
//        bandLinksRecyclerView.setAdapter(linksAdapter);
//
//        Bundle bundle = getArguments();
//        if (bundle != null) {
//            ParcelableLink parcelableLink = bundle.getParcelable("LINKS");
//
//            if (parcelableLink != null) {
//                List<Link> bandLinks = parcelableLink.getLinks();
//
//                links.addAll(bandLinks);
//
//            }
//        }
//    }
    @Override
    public void OnItemClick(int position, RecyclerView recyclerView) {

        if (recyclerView == view.findViewById(R.id.albumRecyclerView)) {
            AlbumDetailsFragment albumDetailsFragment = new AlbumDetailsFragment();
            Bundle bundle = new Bundle();
            Disc disc = API.getDiscById(itemList.get(position).getId());

            List<Track> trackList = new ArrayList<>(disc.getTrackList());

            ParcelableTracklist parcelableTracklist = new ParcelableTracklist(trackList);

            bundle.putParcelable("TRACKS", parcelableTracklist);

            bundle.putString("ALBUM_NAME", itemList.get(position).getName());
            bundle.putLong("ALBUM", itemList.get(position).getId());

            ParcelableLineupDisc parcelableLineupDisc = new ParcelableLineupDisc(disc.getLineup());

            bundle.putParcelable("LINEUP", parcelableLineupDisc);

            String releaseYear = itemList.get(position).getReleaseDate().orElseGet(() -> "No release date available.");
            String dateInWords;
            if (releaseYear.contains("-00")) {
                dateInWords = releaseYear.substring(0, 4);
                bundle.putString("RELEASE_DATE", dateInWords);
            } else {
                LocalDate date = LocalDate.parse(releaseYear);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy", Locale.ENGLISH);
                dateInWords = formatter.format(date);
                bundle.putString("RELEASE_DATE", dateInWords);
            }



            Optional<byte[]> optionalPhoto = disc.getArtwork();

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
