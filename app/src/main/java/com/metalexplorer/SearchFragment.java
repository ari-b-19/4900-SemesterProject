package com.metalexplorer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.github.loki.afro.metallum.entity.Member;
import com.github.loki.afro.metallum.entity.partials.PartialDisc;
import com.github.loki.afro.metallum.entity.partials.PartialMember;
import com.github.loki.afro.metallum.search.API;
import com.github.loki.afro.metallum.search.query.entity.BandQuery;
import com.github.loki.afro.metallum.search.query.entity.DiscQuery;
import com.github.loki.afro.metallum.search.query.entity.SearchBandResult;
import com.github.loki.afro.metallum.search.query.entity.SearchDiscResult;
import com.metalexplorer.databinding.FragmentSearchBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SearchFragment extends Fragment implements RecyclerViewInterface {

    private SearchView searchView;

    private RecyclerView recyclerView;

    private RecyclerViewAdapter itemAdapter;

    private AlbumRecyclerViewAdapter albumRecyclerViewAdapter;

    private MemberRecyclerViewAdapter memberRecyclerViewAdapter;

    private ArrayList<Band> itemList = new ArrayList<>();

    private ArrayList<Disc> discList = new ArrayList<>();

    private ArrayList<Member> memberList = new ArrayList<>();

    private ArrayAdapter<String> adapterDropdown;

    private ExecutorService searchService;

    private String[] dropdownSelections = {"Artist", "Album", "Member"};

    private AutoCompleteTextView autoCompleteTextView;

    private String currentSelection;

//    private ArrayList<PartialDisc> albums = new ArrayList<>();
//
//    private ArrayList<PartialMember> members = new ArrayList<>();
//
//    private ArrayList<String> memberRoles = new ArrayList<>();
//
//    private ArrayList<Integer> albumIds = new ArrayList<>();

    private ArrayList<Integer> memberIds = new ArrayList<>();

    private View view;

    private long id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        binding = FragmentSearchBinding.inflate(inflater, container, false);
        view = inflater.inflate(R.layout.fragment_search, container, false);

        autoCompleteTextView = view.findViewById(R.id.autocomplete_textview);
        searchView = view.findViewById(R.id.search_view);
        searchView.clearFocus();
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        adapterDropdown = new ArrayAdapter<String>(requireContext(), R.layout.selection_list, dropdownSelections);

        autoCompleteTextView.setAdapter(adapterDropdown);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String selection = adapterView.getItemAtPosition(position).toString();
                Toast.makeText(requireContext(), "Selection: " + selection, Toast.LENGTH_SHORT).show();
                setCurrentSelection(selection);
            }
        });



//        searchService = Executors.newSingleThreadExecutor();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchResults(query);

                Log.d("SearchFragment", "Query made");

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

//        itemAdapter = new RecyclerViewAdapter(itemList, this, recyclerView);
//        recyclerView.setAdapter(itemAdapter);


        return view;

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    @Override
    public void OnItemClick(int position, RecyclerView recyclerView) {


        BandProfileFragment bandProfileFragment = new BandProfileFragment();
        Bundle bundle = new Bundle();

        ParcelableDisc parcelableDisc = new ParcelableDisc(itemList.get(position).getDiscs());

        bundle.putParcelable("DISCOGRAPHY", parcelableDisc);


//        bundle.putIntegerArrayList("LINEUP", memberIds);

//        bundle.putIntegerArrayList("DISCOGRAPHY", getAlbums(itemList.get(position).getId()));

//        bundle.putIntegerArrayList("LINEUP", getMembers(itemList.get(position).getId()));

                Optional<byte[]> optionalPhoto = itemList.get(position).getLogo();

                if (optionalPhoto.isPresent()) {
                    byte[] photoBytes = optionalPhoto.get();
                    bundle.putByteArray("IMAGE", photoBytes);
                } else {
                    // Handle the case where the photo is not present, maybe by putting a placeholder or null
                    bundle.putByteArray("IMAGE", (byte[]) null); // Or some placeholder byte array
                }

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
//        long id;
//        searchService.execute(new Runnable() {
//        @Override
//
//        public void run(){

        itemList.clear();
//        BandQuery band = BandQuery.byName(query, true);
//        BandQuery bandName = BandQuery.builder()
////                .name(query)
////                .build();
        if (getCurrentSelection().equals("Artist")) {

            BandQuery bandName = BandQuery.byName(query, false);

            for (Band result : API.getBandsFully(bandName)){
//            setBandId(result.getId());
//            Band band = API.getBandById(id);
                itemList.add(result);
            }
            itemAdapter = new RecyclerViewAdapter(itemList, this, recyclerView);
            recyclerView.setAdapter(itemAdapter);
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

            itemAdapter.notifyDataSetChanged();

        } else if (getCurrentSelection().equals("Album")) {

            DiscQuery discName = DiscQuery.builder()
                    .name(query)
                    .build();

            for (Disc result : API.getDiscsFully(discName)) {
//            setBandId(result.getId());
//            Band band = API.getBandById(id);
                discList.add(result);
            }

            albumRecyclerViewAdapter = new AlbumRecyclerViewAdapter(discList, this, recyclerView);
            albumRecyclerViewAdapter.isFromSearchFragment(true);
            recyclerView.setAdapter(albumRecyclerViewAdapter);
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

            albumRecyclerViewAdapter.notifyDataSetChanged();


        }



//        }
//        });

        }


    public void setMembers(Long id) {
        Band band = API.getBandById(id);
//        members.addAll(band.getCurrentMembers());


        for (Band.PartialMember memberResult : band.getCurrentMembers()) {
//            String stringId = memberResult.toString().split("id=")[1].split(",")[0];
//            int intId = Integer.parseInt(stringId);
            Long intId = memberResult.getId();
            memberIds.add(Math.toIntExact(intId));
        }
//        for (int i = 0; i < members.size(); i++) {
//            String stringId = members.get(i).toString().split("id=")[1].split(",")[0];
//            int intId = Integer.parseInt(stringId);
//            memberIds.add(intId);
//        }

//        return memberIds;
    }

//    public ArrayList<String> getMemberRoles(Long id) {
//        Band band = API.getBandById(id);
//
//        for (int j = 0; j < band.getCurrentMembers().size(); j++) {
//            String role = band.getCurrentMembers().get(j).getRole();
//            memberRoles.add(role);
//        }
//
//        return memberRoles;
//    }

//    public long setBandId(long id) {
//        this.id = id;
//
//        return this.id;
//    }
//
//    public long getBandId() {
//        return id;
//    }

    public void setCurrentSelection(String currentSelection) {
        if (currentSelection == null) {
            this.currentSelection = "Artist";
        } else {
            this.currentSelection = currentSelection;
        }
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