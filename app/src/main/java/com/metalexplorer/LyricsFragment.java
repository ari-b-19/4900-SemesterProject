package com.metalexplorer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.metalexplorer.databinding.FragmentHomeBinding;

public class LyricsFragment extends Fragment {
    private FragmentHomeBinding binding;

    private View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_lyrics, container, false);

        TextView lyrics = view.findViewById(R.id.lyrics);

        TextView trackName = view.findViewById(R.id.trackName);

        TextView byline = view.findViewById(R.id.byline);

        assert getArguments() != null;
        trackName.setText(getArguments().getString("TRACK_NAME"));
        byline.setText(getArguments().getString("BAND_NAME"));
        lyrics.setText(getArguments().getString("LYRICS"));



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

}
