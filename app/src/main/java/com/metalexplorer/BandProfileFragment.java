package com.metalexplorer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.metalexplorer.databinding.FragmentBandProfileBinding;

public class BandProfileFragment extends Fragment {
    private FragmentBandProfileBinding binding;

    private ImageView bandImageView;
    private TextView discography;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        binding = FragmentBandProfileBinding.inflate(inflater, container, false);
        View view = inflater.inflate(R.layout.fragment_band_profile, container, false);

        bandImageView = view.findViewById(R.id.imageView);
//        discography = view.findViewById(R.id.Albums);

        byte[] photoBytes = getArguments().getByteArray("IMAGE");

        if (photoBytes != null) {
            // Convert the byte array to a Bitmap
            Bitmap bitmap = BitmapFactory.decodeByteArray(photoBytes, 0, photoBytes.length);

            // Set the Bitmap to the ImageView
            bandImageView.setImageBitmap(bitmap);
        } else {
            // Handle the case where the photo is not present, maybe by setting a default image
            bandImageView.setImageResource(R.drawable.default_band_image);
        }




        return view;


    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("BandProfileFragment", "onCreate() called");

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
