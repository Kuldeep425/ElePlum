package com.example.eleplum.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.eleplum.Fragments.HomeUserFragment;
import com.example.eleplum.R;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CreateTaskActivity extends AppCompatActivity {

    TextInputEditText taskTxt,timeTxt,dateTxt,addressTxt;
    Geocoder geocoder;
    Double latitude,longitude;
    Button createBtn;
    String taskDesc,time,date,address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        initialize();


        // get latitude and longitude
        latitude=getIntent().getDoubleExtra("latitude",0);
        longitude=getIntent().getDoubleExtra("longitude",0);

        System.out.println(latitude + " " +longitude );

        // initialize geocoder
        geocoder=new Geocoder(this, Locale.getDefault());

        // get user location
        runOnUiThread(()->{
            try {
            getCurrentLocation();
        } catch (IOException e) {
            e.printStackTrace();
        }
        });

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 validateInputFields();
            }
        });

    }

    private boolean validateInputFields() {

        return true;
    }

    private void getCurrentLocation() throws IOException {
        List<Address> addresses=geocoder.getFromLocation(latitude,longitude,1);
        System.out.println(addresses);
        if(addresses.size()==0) {
            Toast.makeText(this, "List has no item", Toast.LENGTH_SHORT).show();
            return;
        }
        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String subLocality=addresses.get(0).getSubLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();
        Log.d("Address",address);
        Log.d("City",city);
        Log.d("state",state);
        Log.d("country",country);
        Log.d("postalCode",postalCode);
        Log.d("knownName",knownName);
        Log.d("subLocality",subLocality);

        addressTxt.setText(subLocality+","+postalCode);
        addressTxt.setEnabled(false);
    }

    private void initialize() {
        taskTxt=findViewById(R.id.task);
        timeTxt=findViewById(R.id.time);
        dateTxt=findViewById(R.id.date);
        addressTxt=findViewById(R.id.address);
        createBtn=findViewById(R.id.create_taskBtn);
    }
}