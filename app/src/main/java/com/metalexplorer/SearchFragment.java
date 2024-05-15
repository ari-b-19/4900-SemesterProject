package com.metalexplorer;

import static com.github.loki.afro.metallum.search.API.getBandById;
import static com.github.loki.afro.metallum.search.API.getDiscById;
import static com.github.loki.afro.metallum.search.API.getMemberById;
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
import com.github.loki.afro.metallum.entity.Member;
import com.github.loki.afro.metallum.entity.partials.PartialBand;
import com.github.loki.afro.metallum.enums.DiscType;
import com.github.loki.afro.metallum.search.API;
import com.github.loki.afro.metallum.search.query.entity.BandQuery;
import com.github.loki.afro.metallum.search.query.entity.DiscQuery;
import com.github.loki.afro.metallum.search.query.entity.SearchBandResult;
import com.github.loki.afro.metallum.search.query.entity.SearchDiscResult;
import com.github.loki.afro.metallum.search.query.entity.SearchMemberResult;
import com.github.loki.afro.metallum.search.query.entity.SearchTrackResult;
import com.github.loki.afro.metallum.search.query.entity.TrackQuery;
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

    private SearchTrackRecyclerViewAdapter searchTrackRecyclerViewAdapter;

    private MemberRecyclerViewAdapter memberRecyclerViewAdapter;

    private ArrayList<SearchBandResult> itemList = new ArrayList<SearchBandResult>();

    private ArrayList<SearchDiscResult> discList = new ArrayList<SearchDiscResult>();

    private ArrayList<Band.PartialMember> memberList = new ArrayList<>();

    private ArrayList<SearchTrackResult> trackResult = new ArrayList<>();

    private ArrayAdapter<String> adapterDropdown;

    private ExecutorService executorService = Executors.newCachedThreadPool();

    private String[] dropdownSelections = {"Band", "Album", "Member", "Song"};

    private AutoCompleteTextView autoCompleteTextView;

    private String currentSelection = "Band";

    private Boolean isAlbumRecyclerView = false;

    private Boolean isBandRecyclerView = false;

    private Boolean isTrackRecyclerView = false;

    private Boolean isMemberRecyclerView = false;

    private Disc disc;

    private Band band;

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);

        autoCompleteTextView = view.findViewById(R.id.autocomplete_textview);
        TextInputLayout textInputLayout = (TextInputLayout) view.findViewById(R.id.filter_box);
        textInputLayout.setHint("Band");

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
            disc = getDiscById(discList.get(position).getId());
            bundle.putString("ALBUM_NAME", discList.get(position).getName());
            bundle.putLong("ALBUM", discList.get(position).getId());


            Optional<byte[]> optionalPhoto = disc.getArtwork();

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

            autoCompleteTextView.setText("");

        } else if (isBandRecyclerView) {
            executorService.execute(() -> {
            BandProfileFragment bandProfileFragment = new BandProfileFragment();
            Bundle bundle = new Bundle();
            List<SearchDiscResult> discList1 = new ArrayList<>();
            band = getBandById(itemList.get(position).getId());

            DiscQuery query = DiscQuery.builder()
                    .discType(DiscType.FULL_LENGTH)
                    .bandName(itemList.get(position).getName())
                    .build();

                for (SearchDiscResult discResult : API.getDiscs(query)) {
                    if (discResult.getBandId() == itemList.get(position).getId()) {
                        discList1.add(discResult);
                    }
                }

            ParcelableDisc parcelableDisc = new ParcelableDisc(discList1);

            bundle.putParcelable("DISCOGRAPHY", parcelableDisc);

            ParcelableLineup parcelableLineup = new ParcelableLineup(band.getCurrentMembers());

            bundle.putParcelable("LINEUP", parcelableLineup);

            ParcelableLink parcelableLinks = new ParcelableLink(band.getLinks());

            bundle.putParcelable("LINKS", parcelableLinks);



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

        } else if (isTrackRecyclerView) {
              AlbumDetailsFragment albumDetailsFragment = new AlbumDetailsFragment();
              Bundle bundle = new Bundle();
              bundle.putString("ALBUM_NAME", trackResult.get(position).getDiscName());
              bundle.putLong("ALBUM", trackResult.get(position).getDiscId());


                    Optional<byte[]> optionalPhoto = getDiscById(trackResult.get(position).getDiscId()).getArtwork();

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

                    isTrackRecyclerView = false;

                    Log.d("ItemClick", "Item clicked at position: " + position);

                    autoCompleteTextView.setText("");

            }  else if (isMemberRecyclerView) {
                MemberFragment memberFragment = new MemberFragment();
                Member member = getMemberById(memberList.get(position).getId());
                Bundle bundle = new Bundle();
                bundle.putString("MEMBER_NAME", memberList.get(position).getName());
                bundle.putLong("MEMBER", member.getId());


                Optional<byte[]> optionalPhoto = member.getPhoto();

                if (optionalPhoto.isPresent()) {
                    byte[] photoBytes = optionalPhoto.get();
                    bundle.putByteArray("MEMBER_PHOTO", photoBytes);
                } else {
                // Handle the case where the photo is not present
                    bundle.putByteArray("MEMBER_PHOTO", (byte[]) null);
                }

                memberFragment.setArguments(bundle);

                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, memberFragment);
                fragmentTransaction.setReorderingAllowed(true);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                isMemberRecyclerView = false;

                Log.d("ItemClick", "Item clicked at position: " + position);
                Log.d("ItemClick", "PartialMember ID is:  " + memberList.get(position).getId());
                Log.d("ItemClick", "Member ID is:  " + member.getId());

                autoCompleteTextView.setText("");
        }



    }
    public void searchResults(String query) {
        switch (getCurrentSelection()) {
            case "Band":
                if (!itemList.isEmpty()) {
                    itemList.clear();
                }
                BandQuery bandName = BandQuery.byName(query, false);
                executorService.execute(() -> {

                    for (SearchBandResult result : API.getBands(bandName)) {
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

                break;
            case "Album":
                if (!discList.isEmpty()) {
                    discList.clear();
                }
                DiscQuery discName = DiscQuery.builder()
                        .name(query)
                        .exactNameMatch(true)
                        .build();

                executorService.execute(() -> {

                    for (SearchDiscResult result : API.getDiscs(discName)) {
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
                break;
            case "Song":
                if (!trackResult.isEmpty()) {
                    trackResult.clear();
                }

                TrackQuery trackQuery = TrackQuery.builder()
                        .name(query)
                        .build();

                executorService.execute(() -> {

                for (SearchTrackResult result : API.getTracks(trackQuery)) {
                    trackResult.add(result);

                }
                    recyclerView.post(() -> {

                progressBar.setVisibility(View.GONE);
                searchTrackRecyclerViewAdapter = new SearchTrackRecyclerViewAdapter(trackResult, this);
                isTrackRecyclerView = true;
                recyclerView.setAdapter(searchTrackRecyclerViewAdapter);
                recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

                searchTrackRecyclerViewAdapter.notifyDataSetChanged();
                });
                });

                break;
//            case "Member":
//                if (!memberList.isEmpty()) {
//                    memberList.clear();
//                }
//                executorService.execute(() -> {
//                for (SearchMemberResult result : getMemberByName(query)) {
//                    for (PartialBand band : result.getBands()) {
//                        for (Band.PartialMember currentMember : band.load().getCurrentMembers()) {
//                            if (result.getId() == currentMember.getId()) {
//                                memberList.add(currentMember);
//                                Log.d("Search", "Member results: " + memberList);
//                            }
//                        }
//                    }
//
//
//                }
//
//                    recyclerView.post(() -> {
//                progressBar.setVisibility(View.GONE);
//                memberRecyclerViewAdapter = new MemberRecyclerViewAdapter(memberList, this, recyclerView);
//                isMemberRecyclerView = true;
//                recyclerView.setAdapter(memberRecyclerViewAdapter);
//                recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
//
//                memberRecyclerViewAdapter.notifyDataSetChanged();
//
//                });
//        });
//
//
//                break;
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