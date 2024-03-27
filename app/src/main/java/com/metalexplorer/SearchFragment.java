package com.metalexplorer;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.loki.afro.metallum.entity.Band;
import com.github.loki.afro.metallum.search.API;
import com.github.loki.afro.metallum.search.query.entity.BandQuery;
import com.github.loki.afro.metallum.search.query.entity.SearchBandResult;
import com.metalexplorer.databinding.FragmentSearchBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SearchFragment extends Fragment implements RecyclerViewInterface {

    private FragmentSearchBinding binding;

    private SearchView searchView;

    private RecyclerView recyclerView;

    private RecyclerViewAdapter itemAdapter;

    private List<Band> itemList = new ArrayList<>();

    private ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View view = inflater.inflate(R.layout.fragment_search, container, false);


        searchView = view.findViewById(R.id.search_view);
        searchView.clearFocus();
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchResults(query);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

        itemAdapter = new RecyclerViewAdapter(itemList, this);
        recyclerView.setAdapter(itemAdapter);

//        return binding.getRoot();

        return view;

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



//        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavHostFragment.findNavController(SearchFragment.this)
//                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
//            }
//        });
    }

//    public void searchResults(String query) {
//        long id;
//        itemList.clear();
//        BandQuery band1 = BandQuery.byName(query, true);
//        for (Band result : API.getBandsFully(band1)) {
//              itemList.add(result);
//        }
//        itemAdapter.notifyDataSetChanged();
//    }

    public static void setImageFromBytes(ImageView imageView, byte[] byteArray) {
        if (byteArray != null && byteArray.length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            imageView.setImageBitmap(bitmap);
        }
    }
    @Override
    public void OnItemClick(int position) {

        BandProfileFragment bandProfileFragment = new BandProfileFragment();
        Bundle bundle = new Bundle();

//        bundle.putStringArrayList("DISCOGRAPHY", itemList.get(position).getDiscs());

        Optional<byte[]> optionalPhoto = itemList.get(position).getLogo();

        if (optionalPhoto.isPresent()) {
            byte[] photoBytes = optionalPhoto.get();
            bundle.putByteArray("IMAGE", photoBytes);
        } else {
            // Handle the case where the photo is not present, maybe by putting a placeholder or null
            bundle.putByteArray("IMAGE", (byte[]) null); // Or some placeholder byte array
        }

//        bundle.putByteArray("photo", itemList.get(position).getPhoto().orElse(null));
        bandProfileFragment.setArguments(bundle);

        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, bandProfileFragment);
            fragmentTransaction.setReorderingAllowed(true);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();



        Log.d("ItemClick", "Item clicked at position: " + position);
    }
    public void searchResults(String query) {
        long id;
        itemList.clear();
//        BandQuery band1 = BandQuery.byName(query, true);
        BandQuery band1 = BandQuery.builder()
                .name(query)
                .build();
        for (SearchBandResult result : API.getBands(band1)) {
            id = result.getId();
            Band band = API.getBandById(id);
            itemList.add(band);
        }
        itemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}