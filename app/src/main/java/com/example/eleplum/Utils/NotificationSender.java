package com.example.eleplum.Utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NotificationSender {

    String userToken;
    String title;
    String body;
    Context context;
    Activity activity;

    private RequestQueue requestQueue;
    private final String postUrl="https://fcm.googleapis.com/fcm/send";
    private final String fcmServerKey="AAAAUaIu2gs:APA91bG8tybPkKjUFlFKHJp2bUqvHjBt64WilBYGKHH9yetpWm_PqW57B5i-nppOSZMRaVxC4y9a7xGvIyYGAQOripKhf13BhLCa2P1seIxsnY2UQOU4uuoGBkOfrDkO_Wr2b2kieR2w";

    public NotificationSender(String userToken, String title, String body, Context context, Activity activity) {
        this.userToken = userToken;
        this.title = title;
        this.body = body;
        this.context = context;
        this.activity = activity;
    }

    // method to send the notification
    public void sendNotification(){
         requestQueue= Volley.newRequestQueue(activity);
        JSONObject mainObject=new JSONObject();
        try {
            mainObject.put("to",userToken);

            JSONObject notification=new JSONObject();
            notification.put("title",title);
            notification.put("body",body);

            mainObject.put("notification",notification);

            JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, postUrl, mainObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("Result","Sent");
                    Log.d("Result",response.toString());

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Result","Error in sending");
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {


                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=" + fcmServerKey);
                    return header;

                }
            };
            requestQueue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
