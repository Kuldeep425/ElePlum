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
import com.example.eleplum.Models.Electrician;
import com.example.eleplum.R;
import com.example.eleplum.Retrofit.ApiService;
import com.example.eleplum.Retrofit.RetrofitClient;
import com.example.eleplum.Utils.Constants;
import com.example.eleplum.Utils.NotificationSender;
import com.example.eleplum.Utils.PreferenceManager;
import com.example.eleplum.Utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateTaskActivity extends AppCompatActivity {

    TextInputEditText taskTxt,timeTxt,dateTxt,addressTxt;
    Geocoder geocoder;
    Double latitude,longitude;
    Button createBtn;
    String taskDesc,time,date,address;
    DatabaseReference databaseReference;
    JSONArray fcmOfNearByElectricians;
    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        preferenceManager=new PreferenceManager(CreateTaskActivity.this);

      //  Log.d("UserId",userId);
        initialize();

        // get fcm of electricians
        getFcmTokenOfAllNearByElectrician();


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

    // this is the method to find the fcm tokens of all near by electrician to
    // send the notification once the user creates task
    private void getFcmTokenOfAllNearByElectrician() {

        FirebaseDatabase.getInstance().getReference("ElePlum")
                .child("Electrician")
                .orderByChild(Constants.KEY_IS_INTERESTED)
                .equalTo(true)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        System.out.println(snapshot);
                        fcmOfNearByElectricians=new JSONArray();
                        fcmOfNearByElectricians.put(preferenceManager.getString(Constants.KEY_FCM_TOKEN));
                        for(DataSnapshot d:snapshot.getChildren()){
                            Electrician electrician=d.getValue(Electrician.class);

                            // calling method to find the distance between the electrician and user
                            // if the dis is greater the 10km we would not send notification to them

                            Double dis=new Utils()
                                    .getDistance(latitude,longitude,electrician.getLatitude(),electrician.getLongitude());
                            if(dis<=11){
                                // collect the fcm token of that electrician
                                fcmOfNearByElectricians.put(electrician.getFcmToken());
                            }
                        }
                        System.out.println(fcmOfNearByElectricians);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    // method to add created task to add database
    private void addCreatedTaskToDatabase() {
          databaseReference= FirebaseDatabase.getInstance().getReference("ElePlum");
          String taskId=databaseReference.child("Tasks").push().getKey();
          CreatedTask createdTask=new CreatedTask(taskId,preferenceManager.getString(Constants.KEY_USER_ID),latitude,longitude,time,date,taskDesc,address);
          databaseReference.child("Tasks").child(taskId).setValue(createdTask).addOnCompleteListener(new OnCompleteListener<Void>() {
              @Override
              public void onComplete(@NonNull Task<Void> task) {
                  if(task.isSuccessful()){
                      // now send the notification of created task to near by electricians
                      Toast.makeText(CreateTaskActivity.this, "created", Toast.LENGTH_SHORT).show();
                      sendNotificationToNearByElectricians(createdTask);
                  }
              }
          });

    }
    // method to build the content of the notification and then send the notifications
    private void sendNotificationToNearByElectricians(CreatedTask task) {
        JSONObject body=new JSONObject();
        JSONObject data=new JSONObject();
        try{
            data.put(Constants.REMOTE_MSG_TYPE,Constants.REMOTE_MSG_NOTIFICATION);
            data.put(Constants.REMOTE_MSG_MESSAGE,task.getDesc());
            data.put(Constants.REMOTE_MSG_TITLE,"Work on "+task.getDate()+" at "+task.getTime());

            body.put(Constants.REMOTE_MSG_DATA,data);
            body.put(Constants.REMOTE_MSG_REGISTRATION_IDS,fcmOfNearByElectricians);
            System.out.println(body);

            // call the method to send the remote message
            sendRemoteMessage(body.toString(),Constants.REMOTE_MSG_NOTIFICATION);

        }
        catch (Exception e){
            System.out.println(e+"");
        }

    }

    // method to send the the remote message
    private void sendRemoteMessage(String remoteMessageBody,String type){
        HashMap<String,String>headers=new Utils().getRemoteMessageHeaders();
        System.out.println(headers);
        new RetrofitClient().getRetrofit().create(ApiService.class)
                .sendRemoteMessage(headers,remoteMessageBody)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.isSuccessful()){
                           if(type.equals(Constants.REMOTE_MSG_NOTIFICATION)){
                               Toast.makeText(CreateTaskActivity.this, "notified near by electricians", Toast.LENGTH_SHORT).show();
                           }
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d("Throwable: ",t.toString());
                        Log.d("Throwable: ",call.toString());
                        Toast.makeText(CreateTaskActivity.this, "Error", Toast.LENGTH_SHORT).show();
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