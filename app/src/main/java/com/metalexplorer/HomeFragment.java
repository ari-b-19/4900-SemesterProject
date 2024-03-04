package com.metalexplorer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.metalexplorer.R;
import com.metalexplorer.databinding.FragmentHomeBinding;
import com.github.loki.afro.metallum.entity.Band;
import com.github.loki.afro.metallum.search.API;
import com.github.loki.afro.metallum.search.query.entity.BandQuery;

import androidx.appcompat.widget.SearchView;
import android.widget.Toast;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        TextView bands = (TextView) view.findViewById(R.id.textview_first);
        String bandNames = "Bands";
        TextView bandName = (TextView) view.findViewById(R.id.bandName);
        TextView genreName = (TextView) view.findViewById(R.id.genreName);

        bands.setText(bandNames);

        String bandString = "Anubis Gate";
        BandQuery band1 = BandQuery.byName(bandString, true);
        for (final Band fullBand : API.getBandsFully(band1)) {
            binding.bandName.setText(fullBand.getName());
            binding.genreName.setText(fullBand.getGenre());
        }

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



//        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavHostFragment.findNavController(FirstFragment.this)
//                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
//            }
//        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void performSearch(String query) {
        // Your search logic here
        Toast.makeText(requireContext(), "Searching for: " + query, Toast.LENGTH_SHORT).show();
    }

}