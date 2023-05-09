package com.example.eleplum.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eleplum.R;
import com.example.eleplum.Utils.Constants;
import com.example.eleplum.Utils.PreferenceManager;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class EleHomeFragment extends Fragment {
   private PreferenceManager preferenceManager;
   View view;
   CircleImageView electricianPic;
   TextView electricianName;
   Button interestedBtn;
   boolean interest;
   DatabaseReference reference;

    public EleHomeFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // initialize view
        view=inflater.inflate(R.layout.fragment_ele_home, container, false);

        preferenceManager=new PreferenceManager(getContext());

        // initialization
        initialization();




        // method to set the electrician profile on the screen
        setElectricianData();

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

        // on touching the interested btn
        interestedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              // update the value
                updateInterestedStatus();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    // method to update interest status
    private void updateInterestedStatus(){
        FirebaseDatabase.getInstance().getReference("ElePlum")
                .child("Electrician")
                .child(preferenceManager.getString(Constants.KEY_ELE_ID))
                .child("isInterested")
                .setValue(!interest)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                      interest=!interest;
                      // set the interest status
                        if(interest) interestedBtn.setText("Yes");
                        else interestedBtn.setText("No");
                    }
                });
    }

    // method to set the electrician data on the screen
    private void setElectricianData() {
        Picasso.get().load(preferenceManager.getString(Constants.KEY_PROFILE_IMAGE_URL)).into(electricianPic);
        electricianName.setText(preferenceManager.getString(Constants.KEY_NAME));
        if(interest) interestedBtn.setText("Yes");
        else interestedBtn.setText("No");


    }

    // method to initialize the element
    private void initialization() {
        electricianPic=view.findViewById(R.id.ele_pic_image);
        electricianName=view.findViewById(R.id.ele_name_textview);
        interestedBtn=view.findViewById(R.id.interestedBtn);

        // interest boolean
        interest=preferenceManager.getBoolean(Constants.KEY_IS_INTERESTED);



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