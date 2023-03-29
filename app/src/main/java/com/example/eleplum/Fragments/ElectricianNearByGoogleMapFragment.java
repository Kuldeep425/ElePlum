package com.example.eleplum.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.eleplum.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ElectricianNearByGoogleMapFragment extends Fragment implements OnMapReadyCallback {

    MapView mapView;
    View rootview;
    Double userLocLatitude,userLocLongitude;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview=inflater.inflate(R.layout.fragment_nearelectricianmap, container, false);

        // initialize widget
        initialize();
        // getting location of user from intent
         userLocLatitude=getActivity().getIntent().getDoubleExtra("latitude",0);
         userLocLongitude=getActivity().getIntent().getDoubleExtra("longitude",0);

        Toast.makeText(getContext(), "Lat: "+userLocLatitude+" Long: "+userLocLongitude, Toast.LENGTH_SHORT).show();
        SupportMapFragment supportMapFragment= (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(userLocLatitude,userLocLongitude))
                        .title("Me"));
            }
        });



        return rootview;
    }

    private void initialize() {
       // mapView=rootview.findViewById(R.id.mapview);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
