package com.example.eleplum.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eleplum.R;
import com.example.eleplum.Retrofit.ApiService;
import com.example.eleplum.Retrofit.RetrofitClient;
import com.example.eleplum.Utils.Constants;
import com.example.eleplum.Utils.Utils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OutgoingCallActivity extends AppCompatActivity {
    String callerId,calleeId,callerImageUrl,calleeImageUrl,callerName,calleeName,callerFcmToken,calleeFcmToken;
    CircleImageView calleeImageView;
    TextView calleeNameTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outgoing_call);

        // hide the action bar
        getSupportActionBar().hide();

        // extract data from the intent
        getDataFromTheIntent();

        // to initialization the view
        initialization();

        // set the callee 's data to the screen
        setTheCalleeData();

        // initiate call
        initiateCall();

    }

    // method to initiate call
    private void initiateCall()  {
        if(callerFcmToken!=null || callerFcmToken.trim().length()==0){
            Toast.makeText(this, "could not found user fcm", Toast.LENGTH_SHORT).show();
            return;
        }
        if(calleeFcmToken!=null || calleeFcmToken.trim().length()==0){
            Toast.makeText(this, "could not found user fcm", Toast.LENGTH_SHORT).show();
            return;
        }

        // json array for call receivers' token
        JSONArray tokens=new JSONArray();
        tokens.put(calleeFcmToken);

        JSONObject body=new JSONObject();
        JSONObject data=new JSONObject();

        try {
            data.put(Constants.REMOTE_MSG_TYPE,Constants.REMOTE_MSG_CALL_INVITATION);
            data.put(Constants.REMOTE_CALLER_NAME,callerName);
            data.put(Constants.REMOTE_CALLER_IMAGE_URL,calleeImageUrl);
            data.put(Constants.REMOTE_CALLER_TOKEN,calleeFcmToken);

            body.put(Constants.REMOTE_MSG_DATA,data);
            body.put(Constants.REMOTE_MSG_REGISTRATION_IDS,tokens);

            // now send this remote message(call invitation) to the callee
            sendCallInvitationToCallee(body.toString(),Constants.REMOTE_MSG_TYPE);

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Exception : ",e.toString());
            Toast.makeText(this, ""+e.toString(), Toast.LENGTH_SHORT).show();
            finish();
        }


    }

    // method to send the call invitation to the callee once the caller started the call
    private void sendCallInvitationToCallee(String remoteMessageBody,String type){
        new RetrofitClient().getRetrofit().create(ApiService.class)
                .sendRemoteMessage(new Utils().getRemoteMessageHeaders(),remoteMessageBody)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.isSuccessful()){

                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
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


}