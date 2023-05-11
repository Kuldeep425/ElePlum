package com.example.eleplum.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eleplum.R;
import com.example.eleplum.Retrofit.ApiService;
import com.example.eleplum.Retrofit.RetrofitClient;
import com.example.eleplum.Utils.Constants;
import com.example.eleplum.Utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OutgoingCallActivity extends AppCompatActivity {
    String callerId,calleeId,callerImageUrl,calleeImageUrl,callerName,calleeName,callerFcmToken,calleeFcmToken;
    CircleImageView calleeImageView,callCancelImageView;
    TextView calleeNameTextView;
    String callRoom;
    private MediaPlayer mediaPlayer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outgoing_call);

        // set ringtone on outgoing call
        setRingTone();

        // hide the action bar
        getSupportActionBar().hide();

        // extract data from the intent
        getDataFromTheIntent();

        // to initialization the view
        initialization();

        // set the callee 's data to the screen
        setTheCalleeData();

        // for getting fcm token
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(task.isSuccessful()){
                    // once the fcm token is retrieved save it to the db for notification purpose
                     callerFcmToken=task.getResult();
                    Toast.makeText(OutgoingCallActivity.this, "caller Fcm : "+callerFcmToken, Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(OutgoingCallActivity.this,"failed to create fcm token", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // initiate call
        initiateCall();

        callCancelImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCallCancelToCallee(Constants.REMOTE_MSG_CALL_CANCELLED);
            }
        });

    }

    private void setRingTone() {
        // Initialize the MediaPlayer with the audio file
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.rim_jhim_ye);

        // Start playing the audio file
        mediaPlayer.start();

        //listener to restart the audio file
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });
    }

    // method to create the body for the call cancelled and then send to the callee
    private void sendCallCancelToCallee(String type) {
        try{
            JSONArray tokens=new JSONArray();
            tokens.put(calleeFcmToken);

            JSONObject body=new JSONObject();
            JSONObject data=new JSONObject();

            data.put(Constants.REMOTE_MSG_TYPE,Constants.REMOTE_MSG_CALL_RESPONSE);
            data.put(Constants.REMOTE_MSG_CALL_RESPONSE,Constants.REMOTE_MSG_CALL_CANCELLED);
            
            body.put(Constants.REMOTE_MSG_DATA,data);
            body.put(Constants.REMOTE_MSG_REGISTRATION_IDS,tokens);
            
            sendRemoteMessage(body.toString(),type);
            
        }
        catch (Exception e){
            Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();
        }
    }

    // method to initiate call
    private void initiateCall()  {
        if(callerFcmToken==null || callerFcmToken.trim().length()==0){
            Toast.makeText(this, "could not found user fcm", Toast.LENGTH_SHORT).show();
            return;
        }
        if(calleeFcmToken==null || calleeFcmToken.trim().length()==0){
            Toast.makeText(this, "could not found callee fcm", Toast.LENGTH_SHORT).show();
            return;
        }

        // json array for call receivers' token
        JSONArray tokens=new JSONArray();
        tokens.put(calleeFcmToken);

        JSONObject body=new JSONObject();
        JSONObject data=new JSONObject();

        callRoom=callerId+"_"+ UUID.randomUUID().toString().substring(0,6);


        try {
            data.put(Constants.REMOTE_MSG_TYPE,Constants.REMOTE_MSG_CALL_INVITATION);
            data.put(Constants.REMOTE_CALLER_NAME,callerName);
            data.put(Constants.REMOTE_CALLER_IMAGE_URL,callerImageUrl);
            data.put(Constants.REMOTE_CALLER_TOKEN,callerFcmToken);
            data.put(Constants.REMOTE_MSG_CALL_ROOM,callRoom);

            body.put(Constants.REMOTE_MSG_DATA,data);
            body.put(Constants.REMOTE_MSG_REGISTRATION_IDS,tokens);

            System.out.println(body);

            // now send this remote message(call invitation) to the callee
            sendRemoteMessage(body.toString(),Constants.REMOTE_MSG_TYPE);

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Exception : ",e.toString());
            Toast.makeText(this, ""+e.toString(), Toast.LENGTH_SHORT).show();
            finish();
        }


    }

    // method to send the call invitation to the callee once the caller started the call
    // call cancel message to the callee 
    private void sendRemoteMessage(String remoteMessageBody,String type){
        HashMap<String,String>headers=new Utils().getRemoteMessageHeaders();
        System.out.println(headers);
        new RetrofitClient().getRetrofit().create(ApiService.class)
                .sendRemoteMessage(headers,remoteMessageBody)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.isSuccessful()){
                            if(type.equals(Constants.REMOTE_MSG_CALL_INVITATION)){
                                Toast.makeText(OutgoingCallActivity.this, "call invited", Toast.LENGTH_SHORT).show();
                            }
                            else if(type.equals(Constants.REMOTE_MSG_CALL_CANCELLED)){
                                Toast.makeText(OutgoingCallActivity.this, "Call cancelled", Toast.LENGTH_SHORT).show();
                                // stop the ringtone player
                                mediaPlayer.stop();
                                finish();
                            }
                            else{
                                Toast.makeText(OutgoingCallActivity.this, response.message(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                          Log.d("Throwable: ",t.toString());
                          Log.d("Throwable: ",call.toString());
                        Toast.makeText(OutgoingCallActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // broadcast method to handle call response
    private BroadcastReceiver callResponseReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String type = intent.getStringExtra(Constants.REMOTE_MSG_CALL_RESPONSE);
            System.out.println(type);
            if (type.equals(Constants.REMOTE_MSG_CALL_ACCEPTED)) {
                Toast.makeText(context, "Call Accepted", Toast.LENGTH_SHORT).show();
                // stop the player
                try{
                    URL serverURL=new URL("https://meet.jit.si");
                    JitsiMeetConferenceOptions conferenceOptions
                            =new JitsiMeetConferenceOptions.Builder()
                            .setServerURL(serverURL)
                            .setAudioOnly(true)
                            .setRoom(callRoom)
                            .build();
                    JitsiMeetActivity.launch(OutgoingCallActivity.this,conferenceOptions);
                    finish();
                }
                catch (Exception e){
                    Toast.makeText(OutgoingCallActivity.this, "Accepting Error :"+e, Toast.LENGTH_SHORT).show();
                }
            } else if(type.equals(Constants.REMOTE_MSG_CALL_REJECTED)){
                // stop the player
                Toast.makeText(context, "Call Rejected", Toast.LENGTH_SHORT).show();
                finish();
            }
            else if(type.equals(Constants.REMOTE_MSG_NOT_ANSWERED)){
                Toast.makeText(context, "Did not answer", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(
                callResponseReceiver,new IntentFilter(Constants.REMOTE_MSG_CALL_RESPONSE)
        );
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(
                callResponseReceiver
        );
    }

    // method to set the callee data on the screen
    private void setTheCalleeData() {
        Picasso.get().load(calleeImageUrl).into(calleeImageView);
        calleeNameTextView.setText(calleeName);
    }

    // method to initialize the widgets
    private void initialization() {
        calleeImageView=findViewById(R.id.calleeImageView);
        calleeNameTextView=findViewById(R.id.calleeNameTextView);
        callCancelImageView=findViewById(R.id.call_cancel_imageviewOutgoing);
    }

    // method to get data from the intent
    private void getDataFromTheIntent() {
        // for caller
        callerId=getIntent().getStringExtra("callerId");
        callerImageUrl=getIntent().getStringExtra("callerImageURL");
        callerName=getIntent().getStringExtra("callerName");
        callerFcmToken=getIntent().getStringExtra("callerFcmToken");

        // for callee
        calleeId=getIntent().getStringExtra("calleeId");
        calleeImageUrl=getIntent().getStringExtra("calleeImageURL");
        calleeName=getIntent().getStringExtra("calleeName");
        calleeFcmToken=getIntent().getStringExtra("calleeFcmToken");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
    }
}