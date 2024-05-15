package com.metalexplorer;

import static com.github.loki.afro.metallum.search.API.getBandById;
import static com.github.loki.afro.metallum.search.API.getMemberByName;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.loki.afro.metallum.entity.Band;
import com.github.loki.afro.metallum.entity.Disc;
import com.github.loki.afro.metallum.enums.DiscType;
import com.github.loki.afro.metallum.search.API;
import com.github.loki.afro.metallum.search.query.entity.BandQuery;
import com.github.loki.afro.metallum.search.query.entity.DiscQuery;
import com.github.loki.afro.metallum.search.query.entity.SearchBandResult;
import com.github.loki.afro.metallum.search.query.entity.SearchMemberResult;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SearchFragment extends Fragment implements RecyclerViewInterface {

    private TextView defaultSearchResult;

    private ProgressBar progressBar;

    private SearchView searchView;

    private RecyclerView recyclerView;

    private RecyclerViewAdapter itemAdapter;

    private AlbumRecyclerViewAdapter albumRecyclerViewAdapter;

    private ArrayList<SearchBandResult> itemList = new ArrayList<SearchBandResult>();

    private ArrayList<Disc> discList = new ArrayList<>();

    private ArrayList<SearchMemberResult> memberList = new ArrayList<SearchMemberResult>();

    private ArrayAdapter<String> adapterDropdown;

    private ExecutorService executorService = Executors.newCachedThreadPool();

    private String[] dropdownSelections = {"Artist", "Album", "Member"};

    private AutoCompleteTextView autoCompleteTextView;

    private String currentSelection = "Artist";

    private Boolean isAlbumRecyclerView = false;

    private Boolean isBandRecyclerView = false;

    private ArrayList<Integer> memberIds = new ArrayList<>();

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);

        autoCompleteTextView = view.findViewById(R.id.autocomplete_textview);
        TextInputLayout textInputLayout = (TextInputLayout) view.findViewById(R.id.filter_box);
        textInputLayout.setHint("Artist");

        searchView = view.findViewById(R.id.search_view);
        searchView.clearFocus();
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        defaultSearchResult = view.findViewById(R.id.defaultSearchResult);
        progressBar = view.findViewById((R.id.progressBar2));

        adapterDropdown = new ArrayAdapter<String>(requireContext(), R.layout.selection_list, dropdownSelections);

        autoCompleteTextView.setAdapter(adapterDropdown);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String selection = adapterView.getItemAtPosition(position).toString();
//                Toast.makeText(requireContext(), "Selection: " + selection, Toast.LENGTH_SHORT).show();
                textInputLayout.setHint(selection);
                setCurrentSelection(selection);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                itemList.clear();
                progressBar.setVisibility(View.VISIBLE);
                searchResults(query);
                Log.d("SearchFragment", "Query made");
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

        return view;

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    @Override
    public void OnItemClick(int position, RecyclerView recyclerView) {

        if (isAlbumRecyclerView) {
            executorService.execute(() -> {
            AlbumDetailsFragment albumDetailsFragment = new AlbumDetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putString("ALBUM_NAME", discList.get(position).getName());
            bundle.putLong("ALBUM", discList.get(position).getId());


            Optional<byte[]> optionalPhoto = discList.get(position).getArtwork();

            if (optionalPhoto.isPresent()) {
                byte[] photoBytes = optionalPhoto.get();
                bundle.putByteArray("ALBUM_ART", photoBytes);
            } else {
                // Handle the case where the photo is not present
                bundle.putByteArray("ALBUM_ART", (byte[]) null);
            }

            albumDetailsFragment.setArguments(bundle);

            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, albumDetailsFragment);
            fragmentTransaction.setReorderingAllowed(true);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            isAlbumRecyclerView = false;

            Log.d("ItemClick", "Item clicked at position: " + position);

            });

        } else if (isBandRecyclerView) {
            executorService.execute(() -> {
            BandProfileFragment bandProfileFragment = new BandProfileFragment();
            Bundle bundle = new Bundle();
            List<Disc> discList1 = new ArrayList<>();
            Band band = getBandById(itemList.get(position).getId());

            DiscQuery query = DiscQuery.builder()
                    .discType(DiscType.FULL_LENGTH)
                    .bandName(itemList.get(position).getName())
                    .build();

                for (Disc discResult : API.getDiscsFully(query)) {
                    if (discResult.getBand().getId() == itemList.get(position).getId()) {
                        discList1.add(discResult);
                    }
                }

            ParcelableDisc parcelableDisc = new ParcelableDisc(discList1);

            bundle.putParcelable("DISCOGRAPHY", parcelableDisc);

            ParcelableLineup parcelableLineup = new ParcelableLineup(band.getCurrentMembers());

            bundle.putParcelable("LINEUP", parcelableLineup);



            Optional<byte[]> optionalPhoto = band.getLogo();

            if (optionalPhoto.isPresent()) {
                byte[] photoBytes = optionalPhoto.get();
                bundle.putByteArray("IMAGE", photoBytes);

            } else {
                bundle.putByteArray("IMAGE", (byte[]) null);
            }

            bandProfileFragment.setArguments(bundle);

            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, bandProfileFragment);
            fragmentTransaction.setReorderingAllowed(true);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            isBandRecyclerView = false;

            Log.d("ItemClick", "Item clicked at position: " + position);

            });

            autoCompleteTextView.setText("");

        }



    }
    public void searchResults(String query) {
        if (getCurrentSelection().equals("Artist")) {

            BandQuery bandName = BandQuery.byName(query, false);
            executorService.execute(() -> {

            for (SearchBandResult result : API.getBands(bandName)){
                itemList.add(result);
                Log.d("Search", "Band: " + result.getName());

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
                recyclerView.post(() -> {

            progressBar.setVisibility(View.GONE);
            itemAdapter = new RecyclerViewAdapter(itemList, this, recyclerView);
            isBandRecyclerView = true;
            recyclerView.setAdapter(itemAdapter);
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

            itemAdapter.notifyDataSetChanged();

                    if (itemAdapter.getItemCount() == 0) {
                        defaultSearchResult.setVisibility(TextView.VISIBLE);
                    } else {
                        defaultSearchResult.setVisibility(TextView.GONE);
                    }
                });
            });

        } else if (getCurrentSelection().equals("Album")) {

            DiscQuery discName = DiscQuery.builder()
                    .name(query)
                    .build();

            executorService.execute(() -> {

            for (Disc result : API.getDiscsFully(discName)) {
                discList.add(result);
                Log.d("Search", "Album: " + result.getName());
            }

                recyclerView.post(() -> {

            progressBar.setVisibility(View.GONE);
            albumRecyclerViewAdapter = new AlbumRecyclerViewAdapter(discList, this, recyclerView);
            isAlbumRecyclerView = true;
            recyclerView.setAdapter(albumRecyclerViewAdapter);
            recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

            albumRecyclerViewAdapter.notifyDataSetChanged();

                });
            });

//
        } else if (getCurrentSelection().equals("Member")) {


        for (SearchMemberResult result : getMemberByName(query)) {
            result.getId();
            memberList.add(result);

        }


    }


        }



    public void setCurrentSelection(String currentSelection) {
        this.currentSelection = currentSelection;
    }

    public String getCurrentSelection() {
        return currentSelection;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        view = null;
    }


}