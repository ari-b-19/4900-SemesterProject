package com.metalexplorer;

import static com.github.loki.afro.metallum.search.API.getBandById;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.loki.afro.metallum.entity.Band;
import com.github.loki.afro.metallum.enums.DiscType;
import com.github.loki.afro.metallum.search.API;
import com.github.loki.afro.metallum.search.query.entity.BandQuery;
import com.github.loki.afro.metallum.search.query.entity.DiscQuery;
import com.github.loki.afro.metallum.search.query.entity.SearchBandResult;
import com.github.loki.afro.metallum.search.query.entity.SearchDiscResult;
import com.metalexplorer.databinding.FragmentSavedBinding;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SavedFragment extends Fragment implements RecyclerViewInterface {

    private FragmentSavedBinding binding;

    private View view;

    private RecyclerView recyclerView;

    private RecyclerViewAdapter itemAdapter;

    private ArrayList<Band> itemList = new ArrayList<>();



    //   private List<String> itemList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_saved, container, false);

        List<Long> retrievedLongList = UserBookmarks.getLongList(requireContext(), "user123");

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));



        for (long number : retrievedLongList) {
            Band band = API.getBandById(number);
            if (!itemList.contains(band)) {
                itemList.add(band);
            }
        }

        itemAdapter = new RecyclerViewAdapter(itemList, this, recyclerView, true);
        recyclerView.setAdapter(itemAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        itemAdapter.notifyDataSetChanged();
        return view;

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void OnItemClick(int position, RecyclerView recyclerView) {
        BandProfileFragment bandProfileFragment = new BandProfileFragment();
        Bundle bundle = new Bundle();
        List<SearchDiscResult> discList1 = new ArrayList<>();
        int beginDate = itemList.get(position).getYearFormedIn();
        int currentYear = Year.now().getValue();

        DiscQuery query = DiscQuery.builder()
                .discType(DiscType.FULL_LENGTH)
                .bandName(itemList.get(position).getName())
                .fromYear(beginDate)
                .toYear(currentYear)
                .build();

        for (SearchDiscResult discResult : API.getDiscs(query)) {
            if (discResult.getBandId() == itemList.get(position).getId()) {
                discList1.add(discResult);
            }
        }

        ParcelableDisc parcelableDisc = new ParcelableDisc(discList1);

        bundle.putParcelable("DISCOGRAPHY", parcelableDisc);

        ParcelableLineup parcelableLineup = new ParcelableLineup(itemList.get(position).getCurrentMembers());

        bundle.putParcelable("LINEUP", parcelableLineup);

        ParcelableLink parcelableLinks = new ParcelableLink(itemList.get(position).getLinks());

        bundle.putParcelable("LINKS", parcelableLinks);

        bundle.putString("FORMED", String.valueOf(itemList.get(position).getYearFormedIn()));

        bundle.putString("COUNTRY", itemList.get(position).getCountry().getFullName());

        bundle.putString("GENRE", itemList.get(position).getGenre());

        bundle.putLong("ID", itemList.get(position).getId());



        Optional<byte[]> optionalPhoto = itemList.get(position).getLogo();

        if (optionalPhoto.isPresent()) {
            byte[] photoBytes = optionalPhoto.get();
            bundle.putByteArray("IMAGE", photoBytes);

        } else {
            bundle.putByteArray("IMAGE", (byte[]) null);
        }

        Optional<byte[]> optionalPhoto2 = itemList.get(position).getPhoto();

        if (optionalPhoto2.isPresent()) {
            byte[] photoBytes2 = optionalPhoto2.get();
            bundle.putByteArray("BAND_PHOTO", photoBytes2);

        } else {
            bundle.putByteArray("BAND_PHOTO", (byte[]) null);
        }

        bandProfileFragment.setArguments(bundle);

        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, bandProfileFragment);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
}