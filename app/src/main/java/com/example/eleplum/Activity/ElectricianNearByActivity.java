package com.example.eleplum.Activity;

import static com.example.eleplum.Fragments.ElectricianNearByGoogleMapFragment.electricianList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.eleplum.Fragments.ElectricianNearByFragment;
import com.example.eleplum.Fragments.ElectricianNearByGoogleMapFragment;
import com.example.eleplum.Models.Electrician;
import com.example.eleplum.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ElectricianNearByActivity extends  AppCompatActivity {

    FrameLayout nearElectLayout;
    FloatingActionButton nearElectricianBtn;
    Boolean isMap=true;


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
        nearElectricianBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isMap) {
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.electricianNearBy_frameLayout, ElectricianNearByFragment.class, null)
                            .commit();
                    isMap=false;
                    // show the action bar
                    getSupportActionBar().show();
                }
                else{
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.electricianNearBy_frameLayout, ElectricianNearByGoogleMapFragment.class, null)
                            .commit();
                    isMap=true;
                    //hide the action bar
                    getSupportActionBar().hide();

                }

            }
        });

    }
        private void initialize(){
            nearElectLayout = findViewById(R.id.electricianNearBy_frameLayout);
            nearElectricianBtn=findViewById(R.id.nearElectricianButton);

        }



}