package com.metalexplorer;

import static android.content.Intent.getIntent;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.github.loki.afro.metallum.entity.Disc;
import com.github.loki.afro.metallum.search.API;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.metalexplorer.databinding.ActivityMainBinding;

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

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private LocalDate today;

    private ArrayList<Disc> todaysReleases = new ArrayList<>();

    private TextView textView;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    private DataViewModel dataViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
//        fetchRecentReleases();
        today = LocalDate.of(2024, 5, 15);



        binding.bottomNavigation.setOnItemSelectedListener(item ->  {

            int itemId = item.getItemId();
            if (itemId == R.id.HomeFragment) {
                fragmentReplacer(new HomeFragment());
            } else if (itemId == R.id.SearchFragment) {
                fragmentReplacer(new SearchFragment());
            } else if (itemId == R.id.SavedFragment) {
                fragmentReplacer(new SavedFragment());
            }

            return true;
        });



    }

    private void fetchRecentReleases() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RecentReleasesAPI service = retrofit.create(RecentReleasesAPI.class);
        Call<List<RecentReleasesData>> call = service.getAllData();
        call.enqueue(new Callback<List<RecentReleasesData>>() {
            @Override
            public void onResponse(Call<List<RecentReleasesData>> call, Response<List<RecentReleasesData>> response) {
                if (response.isSuccessful()) {
                    List<RecentReleasesData> albums = response.body();

//                    StringBuilder albumText = new StringBuilder();
                    assert albums != null;
                    for (RecentReleasesData album : albums) {
                        LocalDate date = LocalDate.parse(album.getReleaseDate());
                        if (date.equals(today)) {
                            Disc disc = API.getDiscById(album.getId());
                            todaysReleases.add(disc);
//                            albumText.append(album.getId()).append(" by ").append(album.getBandName()).append("\n");
                        }
                    }
                    List<Disc> fetchedData = getTodaysReleases();

                    dataViewModel.setDataList(fetchedData);
                    Log.d("MainActivity", "Making requests to API");
//                    textView.setText(albumText.toString());

                } else {
                    // Handle unsuccessful response
                    Log.d("MainActivity", "Failed to fetch today's releases");

                }
            }
            @Override
            public void onFailure(Call<List<RecentReleasesData>> call, Throwable throwable) {
//                textView.setText("Failed to fetch recent and upcoming releases: " + throwable.getMessage());
                Log.d("MainActivity", "Failed to fetch today's releases" + throwable.getMessage());

            }

        });
    }

    public ArrayList<Disc> getTodaysReleases() {
        return todaysReleases;
    }


    private void fragmentReplacer(Fragment f) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, f);
        fragmentTransaction.commit();
    }

}