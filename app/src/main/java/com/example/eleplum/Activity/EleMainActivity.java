package com.example.eleplum.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.AdapterView;

import com.example.eleplum.Fragments.EleCallFragment;
import com.example.eleplum.Fragments.EleChatFragment;
import com.example.eleplum.Fragments.EleHomeFragment;
import com.example.eleplum.Fragments.EleNotificationFragment;
import com.example.eleplum.Fragments.ElePostFragment;
import com.example.eleplum.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class EleMainActivity extends AppCompatActivity {
   BottomNavigationView bnView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ele_main);
        initialization();
        bnView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id= item.getItemId();
                if(id==R.id.nav_home){
                    loadFragment(new EleHomeFragment());
                }else if(id==R.id.nav_post){
                    loadFragment(new ElePostFragment());
                } else if(id==R.id.nav_notification) {
                    loadFragment(new EleNotificationFragment());
                }else if (id==R.id.nav_chat) {
                        loadFragment(new EleChatFragment());
                    }else{
                        loadFragment(new EleCallFragment());
                    }

                return true;
            }
        });
        bnView.setSelectedItemId(R.id.nav_home);
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction().setReorderingAllowed(true);

            ft.add(R.id.frameLayout, fragment);

            ft.replace(R.id.frameLayout,fragment);

        ft.commit();
    }

    private void initialization() {
        bnView=findViewById(R.id.bnView);
    }

}