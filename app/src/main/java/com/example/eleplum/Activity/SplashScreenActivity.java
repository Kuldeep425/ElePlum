package com.example.eleplum.Activity;


import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eleplum.R;


public class SplashScreenActivity extends AppCompatActivity {
   TextView appNameTxtView;
   ImageView appIconImage;

    // method to initialize widgets
    void initialize(){
        appNameTxtView=findViewById(R.id.appnameTxt);
        appIconImage=findViewById(R.id.appIconImageView);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        // hide action app bar
        getSupportActionBar().hide();

        // call method to initialize components
         initialize();

         // method to start animation
         setAnimationOnWidgets();
    }

    // method to apply animation on different components
    private void setAnimationOnWidgets() {
        appNameTxtView.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotateanim));
        appIconImage.startAnimation(AnimationUtils.loadAnimation(this,R.anim.scalelarge));
    }
}
