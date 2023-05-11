package com.example.eleplum.Activity;

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

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IncomingCallActivity extends AppCompatActivity {
    String callerName,callerImageUrl,callerFcmToken;
    CircleImageView callAnswerImageView,callRejectImageView,callerImageView;
    TextView callerNameTextView;
    private MediaPlayer mediaPlayer;


    private Timer mTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_call);

        // set ringtone for calling
         setRingTone();

        // hide action bar
        getSupportActionBar().hide();

        // extract caller data from the intent
        getDataFromTheIntent();

        // initialize the widgets
        initialization();

        // set timer for 30 seconds if the callee does not receive or reject the call
        // it means callee didn't answer the call
        // method to count timer
        startATimer();

        // set the caller data on incoming activity
        setCallerData();

        // send response on click of accept imageview
        callAnswerImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendResponseToCaller(Constants.REMOTE_MSG_CALL_ACCEPTED);
            }
        });

        // send response on click of reject imageview
        callRejectImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendResponseToCaller(Constants.REMOTE_MSG_CALL_REJECTED);
            }
        });


    }

    // method to count the timer for call once the timer reached to the 30 seconds cut the call
    // and send the message that caller did not receive the call
    private void startATimer() {
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // kill the activity if 30 seconds complete
                sendResponseToCaller(Constants.REMOTE_MSG_NOT_ANSWERED);
                finish();
            }
        },30000);
    }

    // method to start the ringtone for outgoing call
    private void setRingTone() {
        // Initialize the MediaPlayer with the audio file
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.barsat_ki_dhun);

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

    // method to create the body of the remote message to send the caller
    private void sendResponseToCaller(String type){
        try{
            JSONArray tokens=new JSONArray();
            tokens.put(callerFcmToken);

            JSONObject body=new JSONObject();
            JSONObject data=new JSONObject();

            data.put(Constants.REMOTE_MSG_TYPE,Constants.REMOTE_MSG_CALL_RESPONSE);
            data.put(Constants.REMOTE_MSG_CALL_RESPONSE,type);
            body.put(Constants.REMOTE_MSG_DATA,data);
            body.put(Constants.REMOTE_MSG_REGISTRATION_IDS,tokens);


            sendRemoteMessage(body.toString(),type);

        }
        catch (Exception e){
            Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();
        }
    }


    // method to send the response of the call between caller and callee
    private void sendRemoteMessage(String remoteMessageBody,String type){
        HashMap<String,String> headers=new Utils().getRemoteMessageHeaders();
        System.out.println(headers);
        new RetrofitClient().getRetrofit().create(ApiService.class)
                .sendRemoteMessage(headers,remoteMessageBody)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.isSuccessful()){
                            if(type.equals(Constants.REMOTE_MSG_CALL_ACCEPTED)){
                                try{
                                    URL serverURL=new URL("https://meet.jit.si");
                                    JitsiMeetConferenceOptions conferenceOptions
                                            =new JitsiMeetConferenceOptions.Builder()
                                            .setServerURL(serverURL)
                                            .setRoom(getIntent().getStringExtra(Constants.REMOTE_MSG_CALL_ROOM))
                                            .setAudioOnly(true)
                                            .build();
                                    JitsiMeetActivity.launch(IncomingCallActivity.this,conferenceOptions);
                                    finish();
                                }
                                catch (Exception e){
                                    Toast.makeText(IncomingCallActivity.this, "Accepting Error :"+e, Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                                Toast.makeText(IncomingCallActivity.this, "Accepted", Toast.LENGTH_SHORT).show();
                            }
                            else if(type.equals(Constants.REMOTE_MSG_CALL_REJECTED)){
                                Toast.makeText(IncomingCallActivity.this, "Rejected", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else if(type.equals(Constants.REMOTE_MSG_NOT_ANSWERED)){
                                Toast.makeText(IncomingCallActivity.this, "Did not answer", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d("Throwable: ",t.toString());
                        Log.d("Throwable: ",call.toString());
                        Toast.makeText(IncomingCallActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // method to set the caller data on ui
    private void setCallerData() {
        callerNameTextView.setText(callerName);
    }

    // method to initialization of view
    private void initialization() {
       callAnswerImageView=findViewById(R.id.call_accept_imageviewIncoming);
       callRejectImageView=findViewById(R.id.call_reject_imageviewIncoming);
       callerImageView=findViewById(R.id.callerImageView);

       callerNameTextView=findViewById(R.id.callerNameTextView);
       mTimer=new Timer();

    }

    // method to get the data from the intent
    private void getDataFromTheIntent() {
        callerName=getIntent().getStringExtra(Constants.REMOTE_CALLER_NAME);
        callerImageUrl=getIntent().getStringExtra(Constants.REMOTE_CALLER_IMAGE_URL);
        callerFcmToken=getIntent().getStringExtra(Constants.REMOTE_CALLER_TOKEN);

    }
    // broadcast method to handle call response
    private BroadcastReceiver callResponseReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String type = intent.getStringExtra(Constants.REMOTE_MSG_CALL_RESPONSE);
            System.out.println(type);
            if (type.equals(Constants.REMOTE_MSG_CALL_CANCELLED)) {
                Toast.makeText(context, "Call cancelled", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mTimer.cancel();

    }
}