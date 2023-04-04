package com.example.eleplum.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.eleplum.Activity.ElectricianNearByActivity;
import com.example.eleplum.Models.Electrician;
import com.example.eleplum.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ElectricianNearByGoogleMapFragment extends Fragment implements OnMapReadyCallback {

    MapView mapView;
    View rootView;
    SupportMapFragment supportMapFragment;
    public static Double userLocLatitude,userLocLongitude;
    public static  ArrayList<Electrician>electricianList=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.fragment_nearelectricianmap, container, false);

        // get all electricians
        electricianList.clear();
        getAllElectricians();

        // initialize widget
        initialize();
        // getting location of user from intent
         userLocLatitude=getActivity().getIntent().getDoubleExtra("latitude",0);
         userLocLongitude=getActivity().getIntent().getDoubleExtra("longitude",0);

        Toast.makeText(getContext(), "Lat: "+userLocLatitude+" Long: "+userLocLongitude, Toast.LENGTH_SHORT).show();
        supportMapFragment= (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(userLocLatitude,userLocLongitude))
                        .title("Me"));

            }
        });



        return rootView;
    }
    private void getAllElectricians() {
        FirebaseDatabase.getInstance().getReference("ElePlum").child("Electrician").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println(snapshot);
                int i=0;
                for(DataSnapshot d:snapshot.getChildren()){
                    electricianList.add(d.getValue(Electrician.class));
                    Double eleLat=electricianList.get(i).getLatitude();
                    Double eleLong=electricianList.get(i).getLongitude();
                    String eleName=electricianList.get(i).getName();
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            googleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(eleLat, eleLong))
                                    .title(eleName));
                            System.out.println(eleName);
                        }
                    });
                    i++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Could not load data", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void initialize() {
       // mapView=rootview.findViewById(R.id.mapview);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
