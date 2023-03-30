package com.example.eleplum.Activity;

import static com.example.eleplum.Activity.LoginActivity.userId;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.eleplum.Fragments.HomeUserFragment;
import com.example.eleplum.Models.CreatedTask;
import com.example.eleplum.R;
import com.example.eleplum.Utils.NotificationSender;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

public class CreateTaskActivity extends AppCompatActivity {

    TextInputEditText taskTxt,timeTxt,dateTxt,addressTxt;
    Geocoder geocoder;
    Double latitude,longitude;
    Button createBtn;
    String taskDesc,time,date,address;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        initialize();

        // subscribe the topic to get the notification from that topic
        subscribeTopicToGetNotification();


        // get latitude and longitude
        latitude=getIntent().getDoubleExtra("latitude",0);
        longitude=getIntent().getDoubleExtra("longitude",0);

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
                taskDesc=taskTxt.getText().toString().trim();
                time=timeTxt.getText().toString().trim();
                date=dateTxt.getText().toString().trim();
                address=addressTxt.getText().toString().trim();
                if(validateInputFields()){
                    // add task to database
                    addCreatedTaskToDatabase();
                }
            }
        });

    }

    // method to add created task to add database
    private void addCreatedTaskToDatabase() {
          databaseReference= FirebaseDatabase.getInstance().getReference("ElePlum");
          String taskId=databaseReference.child("Tasks").push().getKey();
          CreatedTask createdTask=new CreatedTask(taskId,userId,latitude,longitude,time,date,taskDesc,address);
          databaseReference.child("Tasks").child(taskId).setValue(createdTask).addOnCompleteListener(new OnCompleteListener<Void>() {
              @Override
              public void onComplete(@NonNull Task<Void> task) {
                  if(task.isSuccessful()){
                      // if the created task successfully stored into database  sends notification to nearby electricians

                      //creating notification message
                      NotificationSender notificationSender=new NotificationSender("/topics/notification",date,address,getApplicationContext(),CreateTaskActivity.this);
                      //sending notification
                      notificationSender.sendNotification();
                  }
              }
          });

    }

    // subscribe topic to get notification
    private void subscribeTopicToGetNotification() {
        FirebaseMessaging.getInstance().subscribeToTopic("notification")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(CreateTaskActivity.this, "Subscribed", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CreateTaskActivity.this, "Some Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean validateInputFields() {
        if(taskDesc.length()==0){
            Toast.makeText(this, "Please add task description", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(taskDesc.length()>200){
            Toast.makeText(this, "maximum limit of 200 characters for task description", Toast.LENGTH_LONG).show();
            return false;
        }
        if(time.length()==0){
            Toast.makeText(this, "time add a time", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(date.length()==0){
            Toast.makeText(this, "Please add a date", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(address.length()==0){
            Toast.makeText(this, "Address not found", Toast.LENGTH_SHORT).show();
            return false;
        }
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