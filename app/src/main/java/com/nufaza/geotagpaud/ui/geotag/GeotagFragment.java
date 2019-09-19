package com.nufaza.geotagpaud.ui.geotag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nufaza.geotagpaud.R;

public class GeotagFragment extends Fragment {

    private GeotagViewModel geotagViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        geotagViewModel =
                ViewModelProviders.of(this).get(GeotagViewModel.class);
        View root = inflater.inflate(R.layout.fragment_geotag, container, false);

//        final TextView textView = root.findViewById(R.id.text_share);
//        geotagViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frg);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {

                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                mMap.clear();

                CameraPosition bandung = CameraPosition.builder()
                        .target(new LatLng(-6.225657, 106.801943))
                        .zoom(18)
                        .bearing(0)
                        .tilt(0)
                        .build();

                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(bandung));

            }
        });
        return root;
    }
}