package com.example.eleplum.Activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.eleplum.Fragments.ChatFragment;
import com.example.eleplum.Fragments.HomeUserFragment;
import com.example.eleplum.R;

public class MainActivityUser extends AppCompatActivity {
    LinearLayout homeLayout,chatLayout,notificationLayout,callLayout;
    ImageView homeImageview,chatImageview,notificationImageview,callImageview;
    TextView homeTextview,notificationTextview,chatTextview,callTextview;
    Double latitude,longitude;
    private int selectedTab=1; // we have 4 tab (home,notification ,chat and call) but by default the home would be selected
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);

        // to hide the action bar
        getSupportActionBar().hide();

        // calling method to initialize the components
         initialiazeTheComponents();

         // get notification permission
        getNotificationPerm();

         // set home fragment by default
          getSupportFragmentManager().beginTransaction()
                  .setReorderingAllowed(true)
                  .replace(R.id.fragmentContainer, HomeUserFragment.class,null)
                  .commit();


         // when user selects the home layout
          homeLayout.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                   // if the previous selected tab is not 1 means home tab
                   if(selectedTab!=1){
                       // set the home fragment
                       getSupportFragmentManager().beginTransaction()
                               .setReorderingAllowed(true)
                               .replace(R.id.fragmentContainer, HomeUserFragment.class,null)
                               .commit();

                       // set the background color of the other tab is transparent
                       notificationLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                       chatLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                       callLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                       // make invisible of textviews of other tabs
                       notificationTextview.setVisibility(View.GONE);
                       chatTextview.setVisibility(View.GONE);
                       callTextview.setVisibility(View.GONE);

                       // make visible to the home textview and the round color background
                       homeLayout.setBackgroundResource(R.drawable.round_background_home_icon);
                       homeTextview.setVisibility(View.VISIBLE);
                       homeImageview.setImageResource(R.drawable.icon_home);

                       // create animation between tab changing
                       ScaleAnimation scaleAnimation=new ScaleAnimation(0.8f,1.0f,1f,1f, Animation.RELATIVE_TO_SELF,0.0f,Animation.RELATIVE_TO_SELF,0.0f);
                       scaleAnimation.setDuration(500);
                       scaleAnimation.setFillAfter(true);
                       homeLayout.startAnimation(scaleAnimation);

                       // make selected tab 1 as home is selected
                        selectedTab=1;
                   }
              }
          });

          // when user clicks chat tab
          chatLayout.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  // if the previous selected tab is not 2 means chat tab
                  if (selectedTab != 2) {

                      // set the chat fragment
                      getSupportFragmentManager().beginTransaction()
                              .setReorderingAllowed(true)
                              .replace(R.id.fragmentContainer, ChatFragment.class,null)
                              .commit();

                      // set the background color of the other tab is transparent
                      notificationLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                      homeLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                      callLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                      // make invisible of textviews of other tabs
                      notificationTextview.setVisibility(View.GONE);
                      homeTextview.setVisibility(View.GONE);
                      callTextview.setVisibility(View.GONE);

                      // make visible to the chatLayout textview and the round color background
                      chatLayout.setBackgroundResource(R.drawable.round_background_home_icon);
                      chatTextview.setVisibility(View.VISIBLE);
                      chatImageview.setImageResource(R.drawable.icon_chat);

                      // create animation between tab changing
                      ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 1f, 1f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                      scaleAnimation.setDuration(500);
                      scaleAnimation.setFillAfter(true);
                      chatLayout.startAnimation(scaleAnimation);

                      // make selected tab 2 as chat is selected
                      selectedTab = 2;
                  }
              }
          });

          // when user clicks notification tab
          notificationLayout.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  // if the previous selected tab is not 3 means notification tab
                  if (selectedTab != 3) {
                      // reset the images
                      homeImageview.setImageResource(R.drawable.icon_home);
                      chatImageview.setImageResource(R.drawable.icon_chat);
                      callImageview.setImageResource(R.drawable.icon_call);

                      // set the background color of the other tab is transparent
                      homeLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                      chatLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                      callLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                      // make invisible of textviews of other tabs
                      homeTextview.setVisibility(View.GONE);
                      chatTextview.setVisibility(View.GONE);
                      callTextview.setVisibility(View.GONE);

                      // make visible to the home textview and the round color background
                      notificationLayout.setBackgroundResource(R.drawable.round_background_home_icon);
                      notificationTextview.setVisibility(View.VISIBLE);
                      notificationImageview.setImageResource(R.drawable.icon_notification);

                      // create animation between tab changing
                      ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 1f, 1f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                      scaleAnimation.setDuration(500);
                      scaleAnimation.setFillAfter(true);
                      notificationLayout.startAnimation(scaleAnimation);

                      // make selected tab 1 as home is selected
                      selectedTab = 3;
                  }
              }
          });

          // when user clicks call tab
          callLayout.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  // if the previous selected tab is not 4 means call tab
                  if (selectedTab != 4) {

                      // set the background color of the other tab is transparent
                      notificationLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                      chatLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                      homeLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                      // make invisible of textviews of other tabs
                      notificationTextview.setVisibility(View.GONE);
                      chatTextview.setVisibility(View.GONE);
                      homeTextview.setVisibility(View.GONE);

                      // make visible to the home textview and the round color background
                      callLayout.setBackgroundResource(R.drawable.round_background_home_icon);
                      callTextview.setVisibility(View.VISIBLE);
                      callImageview.setImageResource(R.drawable.icon_call);

                      // create animation between tab changing
                      ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 1f, 1f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                      scaleAnimation.setDuration(500);
                      scaleAnimation.setFillAfter(true);
                      callLayout.startAnimation(scaleAnimation);

                      // make selected tab 1 as home is selected
                      selectedTab = 4;
                  }
              }
          });



    }

    // get notification permission
    private void getNotificationPerm() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU){
            int notifPer= ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS);
            if(notifPer!= PackageManager.PERMISSION_GRANTED){
                String [] perm={Manifest.permission.POST_NOTIFICATIONS};
                ActivityCompat.requestPermissions(this,perm,100);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100){
            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        }
    }

    // method to initializing the widgets,components,layout etc.
    private void initialiazeTheComponents() {

        // initializing the linear layouts those are inside the bottom linear layout
        homeLayout=findViewById(R.id.homeLayout);
        chatLayout=findViewById(R.id.chatLayout);
        notificationLayout=findViewById(R.id.notificationLayout);
        callLayout=findViewById(R.id.callLayout);

        // initializing the image views (home,notification,call,chat)
        homeImageview=findViewById(R.id.home_iconImage);
        chatImageview=findViewById(R.id.chat_iconImage);
        notificationImageview=findViewById(R.id.notification_iconImage);
        callImageview=findViewById(R.id.call_iconImage);

        // initializing the textviews those are hidden to the icons of bottom linear layout
        homeTextview=findViewById(R.id.homeTextview);
        chatTextview=findViewById(R.id.chatTextview);
        notificationTextview=findViewById(R.id.notificationTextview);
        callTextview=findViewById(R.id.callTextview);

    }
}
