package com.example.eleplum.Utils;

import android.content.Context;
import android.widget.Toast;

import com.sinch.xms.ApiConnection;
import com.sinch.xms.SinchSMSApi;
import com.sinch.xms.api.MtBatchTextSmsCreate;
import com.sinch.xms.api.MtBatchTextSmsResult;

import java.text.DecimalFormat;
import java.util.HashMap;

public class Utils {

    public double getDistance(double lat1, double lon1, double lat2, double
            lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        DecimalFormat df=new DecimalFormat("#.##");
        df.format(dist);
        System.out.println(dist);
        return (dist); }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0); }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI); }


    public void sendSMS(String smsMessage, String to, Context context){
         final String SERVICE_PLAN_ID = "72241403a32044e192626bb7aa5dd542";
         final String TOKEN = "4299a3c6e03342f6ae49b841127253d3";
         ApiConnection conn;
         String SENDER = "+447520662390"; // This is the
         String[] RECIPIENTS = { "8595997992" }; //your mobile phone number
         conn = ApiConnection
                .builder()
                .servicePlanId(SERVICE_PLAN_ID)
                .token(TOKEN)
                .start();
        MtBatchTextSmsCreate message = SinchSMSApi
                .batchTextSms()
                .sender(SENDER)
                .addRecipient(RECIPIENTS)
                .body(smsMessage)
                .build();
        try {
            // if there is something wrong with the batch
            // it will be exposed in APIError
            MtBatchTextSmsResult batch = conn.createBatch(message);
            System.out.println(batch.id());
        } catch (Exception e) {
            Toast.makeText(context, "fail to send sms", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(context, "sms sent successfully", Toast.LENGTH_SHORT).show();
    }


    // method to get the fcm header

    public HashMap<String,String> getRemoteMessageHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put(Constants.REMOTE_MSG_AUTHORIZATION,
                "AAAAUaIu2gs:APA91bG8tybPkKjUFlFKHJp2bUqvHjBt64WilBYGKHH9yetpWm_PqW57B5i-nppOSZMRaVxC4y9a7xGvIyYGAQOripKhf13BhLCa2P1seIxsnY2UQOU4uuoGBkOfrDkO_Wr2b2kieR2w");
        headers.put(Constants.REMOTE_MSG_CONTENT_TYPE, "application/json");
        return headers;
    }

}
