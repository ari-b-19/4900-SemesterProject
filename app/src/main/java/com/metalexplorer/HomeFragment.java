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
import com.github.loki.afro.metallum.search.API;
import com.github.loki.afro.metallum.search.query.entity.DiscQuery;
import com.metalexplorer.databinding.FragmentHomeBinding;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://10.0.2.2:8080")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        RecentReleasesAPI service = retrofit.create(RecentReleasesAPI.class);
//        Call<List<RecentReleasesData>> call = service.getAllData();
//        call.enqueue(new Callback<List<RecentReleasesData>>() {
//            @Override
//            public void onResponse(Call<List<RecentReleasesData>> call, Response<List<RecentReleasesData>> response) {
//                if (response.isSuccessful()) {
//                    List<RecentReleasesData> albums = response.body();
//
////                    StringBuilder albumText = new StringBuilder();
//                    assert albums != null;
//                    for (RecentReleasesData album : albums) {
//                        LocalDate date = LocalDate.parse(album.getReleaseDate());
//                        if (date.equals(today)) {
//                            Disc disc = API.getDiscById(album.getId());
//                            todaysReleaases.add(disc);
////                            albumText.append(album.getId()).append(" by ").append(album.getBandName()).append("\n");
//                        }
//                    }
//                    Log.d("HomeFragment", "Making requests to API");
////                    textView.setText(albumText.toString());
//
//                } else {
//                    // Handle unsuccessful response
//                    textView.setText("Failed to fetch recent and upcoming releases:");
//
//                }
//                fetchText.setVisibility(View.GONE);
//                progressBar.setVisibility(View.GONE);
//
//                homeScreenRecyclerViewAdapter = new HomeScreenRecyclerViewAdapter(todaysReleaases, null, recyclerView);
//                recyclerView.setAdapter(homeScreenRecyclerViewAdapter);
//                recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
//                recyclerView.setVisibility(View.VISIBLE);
//
//                homeScreenRecyclerViewAdapter.notifyDataSetChanged();
//            }

//            @Override
//            public void onFailure(Call<List<RecentReleasesData>> call, Throwable throwable) {
//                textView.setText("Failed to fetch recent and upcoming releases: " + throwable.getMessage());
//
//            }
//        });


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