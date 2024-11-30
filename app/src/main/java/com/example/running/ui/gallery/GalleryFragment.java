package com.example.running.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.running.BuildConfig;
import com.example.running.databinding.FragmentGalleryBinding;
import com.example.running.ui.home.HomeViewModel;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;
    String MAP_API_KEY= BuildConfig.MAPS_API_KEY;
    private MapView mapView;
    private MapboxMap mapboxMap;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        Mapbox.getInstance(requireContext());
        Mapbox.setApiKey(MAP_API_KEY);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mapView = binding.mapView;

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(map -> {
            mapboxMap = map;
            String styleUrl = "https://api.maptiler.com/maps/streets-v2/style.json?key=" +
                    MAP_API_KEY;
            mapboxMap.setStyle(styleUrl);

            // Set initial camera position
            mapboxMap.setCameraPosition(new CameraPosition.Builder()
                    .target(new LatLng(0.0, 0.0)) // Default to center of the world
                    .zoom(1.0)
                    .build());
        });

        final TextView textView = binding.textGallery;

        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}