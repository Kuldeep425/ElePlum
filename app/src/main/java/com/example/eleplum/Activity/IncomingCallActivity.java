package com.example.eleplum.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.eleplum.R;

public class IncomingCallActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_call);

        // hide action bar
        getSupportActionBar().hide();
    }
}