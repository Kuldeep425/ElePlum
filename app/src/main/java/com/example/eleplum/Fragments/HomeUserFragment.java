package com.example.eleplum.Fragments;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.eleplum.Activity.CreateTaskActivity;
import com.example.eleplum.Activity.ElectricianNearByActivity;
import com.example.eleplum.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.List;
import java.util.jar.Pack200;


public class HomeUserFragment extends Fragment {
    CardView searchNearCardview,createTaskview;
    View rootview;
    public final static int REQ_CODE = 30;
    boolean isLocationPer;
    FusedLocationProviderClient fusedLocationProviderClient;
    Location userCurrentLoc;
    boolean locationRes=false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_home_user, container, false);
        // initialize the widget
        initialize();

        // initialize the fusedLocationProvider
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());


        searchNearCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPer()) {
                    // get user current location and change the activity there if the location permission is provided
                    getUserCurrentLocationMethod();
                } else {
                    requestPermissions(new String[]{"android.permission.ACCESS_FINE_LOCATION","android.permission.ACCESS_COARSE_LOCATION"}, REQ_CODE);
                }
                if(locationRes){
                    Intent intent=new Intent(getContext(),ElectricianNearByActivity.class);
                    intent.putExtra("latitude", userCurrentLoc.getLatitude());
                    intent.putExtra("longitude", userCurrentLoc.getLongitude());
                    startActivity(intent);
                }
            }
        });

        createTaskview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkPer()){
                    // get user current location and change the activity there if the location permission is provided
                    getUserCurrentLocationMethod();
                }
                else {
                    requestPermissions(new String[]{"android.permission.ACCESS_FINE_LOCATION","android.permission.ACCESS_COARSE_LOCATION"}, REQ_CODE);
                }
                if(locationRes){
                    Intent intent=new Intent(getContext(),CreateTaskActivity.class);
                    intent.putExtra("latitude", userCurrentLoc.getLatitude());
                    intent.putExtra("longitude", userCurrentLoc.getLongitude());
                    startActivity(intent);
                }
            }
        });


        return rootview;
    }


    private boolean checkPer() {
        int fineLoc = ActivityCompat.checkSelfPermission(getContext(), ACCESS_FINE_LOCATION);
        int coarseLoc=ActivityCompat.checkSelfPermission(getContext(),ACCESS_COARSE_LOCATION);
        return (fineLoc == PackageManager.PERMISSION_GRANTED );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length != 0) {
            int fineLoc = grantResults[0];
            int coarseLoc= grantResults[1];
            isLocationPer = (fineLoc == PackageManager.PERMISSION_GRANTED && coarseLoc==PackageManager.PERMISSION_GRANTED);
            if (isLocationPer) {
                // get user current location and change the activity there if the location permission is provided
                getUserCurrentLocationMethod();
            }
        }
    }

    // method to initialize all components
    private void initialize() {
        searchNearCardview = rootview.findViewById(R.id.seach_near_cardview);
        createTaskview=rootview.findViewById(R.id.create_task_view);
    }

    // method to get user current location
    private void getUserCurrentLocationMethod() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            Task task = fusedLocationProviderClient.getLastLocation();

            task.addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    if(o!=null){
                        userCurrentLoc= (Location) o;
                        Toast.makeText(getContext(), "Lat: "+userCurrentLoc.getLatitude()+" Long: "+userCurrentLoc.getLongitude(), Toast.LENGTH_SHORT).show();
                        locationRes=true;
                    }
                    else{
                        Toast.makeText(getContext(), "Location not found", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else Toast.makeText(getContext(), "Location Access is not permitted", Toast.LENGTH_SHORT).show();;
    }


}