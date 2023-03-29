package com.example.eleplum.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.eleplum.Fragments.ElectricianNearByGoogleMapFragment;
import com.example.eleplum.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ElectricianNearByActivity extends  AppCompatActivity {

    FrameLayout nearElectLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electrician_near_by);

        // hide the action bar
        getSupportActionBar().hide();
        // initialize the widget etc.
        initialize();
        //set the google map fragement by default
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.electricianNearBy_frameLayout, ElectricianNearByGoogleMapFragment.class, null)
                .commit();

    }
        private void initialize(){
            nearElectLayout = findViewById(R.id.electricianNearBy_frameLayout);
        }


}