package com.metalexplorer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.loki.afro.metallum.entity.Disc;
import com.metalexplorer.databinding.FragmentHomeBinding;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class HomeFragment extends Fragment implements RecyclerViewInterface {

    private FragmentHomeBinding binding;

    private RecyclerView recyclerView;

    private HomeScreenRecyclerViewAdapter homeScreenRecyclerViewAdapter;

    private View view;

    private ArrayList<Disc> todaysReleases = new ArrayList<>();

    private LocalDate today;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    private DataViewModel dataViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);


        dataViewModel = new ViewModelProvider(requireActivity()).get(DataViewModel.class);

        recyclerView = view.findViewById(R.id.todaysReleases);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));


        homeScreenRecyclerViewAdapter = new HomeScreenRecyclerViewAdapter(todaysReleases, this, recyclerView);
        recyclerView.setAdapter(homeScreenRecyclerViewAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        dataViewModel.getDataList().observe(getViewLifecycleOwner(), data -> {
            // Update RecyclerView with new data
           homeScreenRecyclerViewAdapter.setData(data);
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

    }
}