package com.example.eleplum.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.eleplum.R;
import com.example.eleplum.Utils.Constants;
import com.example.eleplum.Utils.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;


public class EleHomeFragment extends Fragment {
   private PreferenceManager preferenceManager;


    public EleHomeFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        preferenceManager=new PreferenceManager(getContext());

        // for getting fcm token
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(task.isSuccessful()){
                    // once the fcm token is retrieved save it to the db for notification purpose
                    saveFcmToDatabase(task.getResult());
                }
                else{
                    Toast.makeText(getContext(), "failed to create fcm token", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ele_home, container, false);
    }
    // method to save electrician 's fcm token  in db
    private void saveFcmToDatabase(String token) {
        DatabaseReference db= FirebaseDatabase.getInstance().getReference("ElePlum");
        String loggedInEleId=preferenceManager.getString(Constants.KEY_ELE_ID);
        if(loggedInEleId==null || loggedInEleId.length()==0){
            Log.d("Ele","Ele not found");
            Log.d("FCM Fail:", "Could not save the fcm");
            return;
        }
        System.out.println(token);
        DatabaseReference reference=db.child("Electrician").child(loggedInEleId).child("fcmToken");
        reference.setValue(token).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d("FCM Success : ","Successfully saved");
                }
                else{
                    Log.d("FCM Fail:", "Could not save the fcm");
                }
            }
        });
    }
}