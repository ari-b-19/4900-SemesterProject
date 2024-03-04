package com.metalexplorer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.github.loki.afro.metallum.entity.Band;
import com.github.loki.afro.metallum.search.API;
import com.github.loki.afro.metallum.search.query.entity.BandQuery;
import com.github.loki.afro.metallum.search.query.entity.SearchBandResult;
import com.metalexplorer.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        TextView band = view.findViewById(R.id.textview_band);
        TextView genre = view.findViewById(R.id.textview_genre);

        String bandString = "Anubis Gate";
//        BandQuery band1 = BandQuery.byName(bandString, true);
//        for (SearchBandResult result : API.getBands(band1)) {
//            band.setText(result.getName());
//            genre.setText(String.valueOf(result.getId()));
//        }

        band.setText(API.getBandById(14633).getName());
        genre.setText(API.getBandById(14633).getGenre());

        return binding.getRoot();


    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavHostFragment.findNavController(HomeFragment.this)
//                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
//            }
//        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}