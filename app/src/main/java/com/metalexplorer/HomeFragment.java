package com.metalexplorer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.loki.afro.metallum.entity.Disc;
import com.github.loki.afro.metallum.entity.Track;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class HomeFragment extends Fragment implements RecyclerViewInterface {
    

    private RecyclerView recyclerView;

    private HomeScreenRecyclerViewAdapter homeScreenRecyclerViewAdapter;

    private TextView fetchText;

    private ProgressBar progressBar;

    private View view;

    private ArrayList<Disc> todaysReleases = new ArrayList<>();

    private final LocalDate today = LocalDate.now();

    private TextView date;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    private DataViewModel dataViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);


        dataViewModel = new ViewModelProvider(requireActivity()).get(DataViewModel.class);
        date = view.findViewById(R.id.todaysDate);
        date.setText(today.toString());

        fetchText = view.findViewById(R.id.fetching);
        progressBar = view.findViewById(R.id.progressBarHome);

        recyclerView = view.findViewById(R.id.todaysReleases);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        homeScreenRecyclerViewAdapter = new HomeScreenRecyclerViewAdapter(todaysReleases, this, recyclerView);
        recyclerView.setAdapter(homeScreenRecyclerViewAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        dataViewModel.getDataList().observe(getViewLifecycleOwner(), data -> {
            // Update RecyclerView with new data
            fetchText.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
           homeScreenRecyclerViewAdapter.setData(data);
           recyclerView.setVisibility(View.VISIBLE);
        });



        return view;


    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        view = null;
    }

    @Override
    public void OnItemClick(int position, RecyclerView recyclerView) {
        AlbumDetailsFragment albumDetailsFragment = new AlbumDetailsFragment();
        Bundle bundle = new Bundle();

        List<Track> trackList = new ArrayList<>(todaysReleases.get(position).getTrackList());

        ParcelableTracklist parcelableTracklist = new ParcelableTracklist(trackList);

        bundle.putParcelable("TRACKS", parcelableTracklist);

        bundle.putString("ALBUM_NAME", todaysReleases.get(position).getName());
        bundle.putLong("ALBUM", todaysReleases.get(position).getId());

        bundle.putString("RELEASE_DATE", todaysReleases.get(position).getReleaseDate());



        Optional<byte[]> optionalPhoto = todaysReleases.get(position).getArtwork();

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

}