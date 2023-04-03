package com.example.eleplum.Utils;

import android.content.Context;
import android.widget.Toast;

import com.sinch.xms.ApiConnection;
import com.sinch.xms.SinchSMSApi;
import com.sinch.xms.api.MtBatchTextSmsCreate;
import com.sinch.xms.api.MtBatchTextSmsResult;

import org.apache.http.protocol.HTTP;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

public class SMSSenderUtil {

    public void sendSMS(String number, String message, Context context){
        String apiKey="4yisekFwtP1XdGAnSv6jqTxNDmVHU5aCr9IcYWfbzLuoKJgMp7lAzTPQu5pc48Jnjvx1CwefLU6SNyWt";
        String senderId="FastSM";
        try {
            message= URLEncoder.encode(message,message);
            String language="english";
            String route="p";
            String smsUrl="https://www.fast2sms.com/dev/bulkV2?authorization="+apiKey+"&sender_id="+senderId+"&message="+message+"&route="+route+"&numbers="+number;
            URL url=new URL(smsUrl);

            HttpsURLConnection con= (HttpsURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent","Mozilla/5.0");
            con.setRequestProperty("cache-control","no-cache");
            int code=con.getResponseCode();
            System.out.println(code);
            Toast.makeText(context, ""+code, Toast.LENGTH_SHORT).show();
            if(code==200){
                System.out.println("sent the message");
            }


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
        }


    }

}
