package com.example.eleplum.Utils;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.eleplum.Activity.IncomingCallActivity;
import com.example.eleplum.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d("FCM",token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        Log.d("message: ",message.getData()+"");
        String type=message.getData().get(Constants.REMOTE_MSG_TYPE);
        if(type!=null) {
            if(type.equals(Constants.REMOTE_MSG_CALL_INVITATION)) {
                Intent intent = new Intent(getApplicationContext(), IncomingCallActivity.class);
                intent.putExtra(Constants.REMOTE_CALLER_NAME, message.getData().get(Constants.REMOTE_CALLER_NAME));
                intent.putExtra(Constants.REMOTE_CALLER_IMAGE_URL, message.getData().get(Constants.REMOTE_CALLER_IMAGE_URL));
                intent.putExtra(Constants.REMOTE_CALLER_TOKEN, message.getData().get(Constants.REMOTE_CALLER_TOKEN));
                intent.putExtra(Constants.REMOTE_MSG_CALL_ROOM,message.getData().get(Constants.REMOTE_MSG_CALL_ROOM));
                intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            else if(type.equals(Constants.REMOTE_MSG_CALL_RESPONSE)){
                Intent intent=new Intent(Constants.REMOTE_MSG_CALL_RESPONSE);
                String type1=message.getData().get(Constants.REMOTE_MSG_CALL_RESPONSE);
                System.out.println(type1);
                intent.putExtra(Constants.REMOTE_MSG_CALL_RESPONSE,
                        message.getData().get(Constants.REMOTE_MSG_CALL_RESPONSE));
                LocalBroadcastManager
                        .getInstance(getApplicationContext())
                        .sendBroadcast(intent);
            }
            else if (type.equals(Constants.REMOTE_MSG_NOTIFICATION)) {
                createNotification(message);
            }
        }
        else{
           Log.d("Notification: ","Data not found");
        }
    }
    private void createNotification(RemoteMessage message){
        System.out.println("Got a Notification");
        // Create and show a notification to the user
        NotificationManager notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notification = new Notification.Builder(this)
                    .setSmallIcon(R.drawable.icon_notification)
                    .setContentTitle(message.getData().get(Constants.REMOTE_MSG_TITLE))
                    .setContentText(message.getData().get(Constants.REMOTE_MSG_MESSAGE))
                    .setChannelId(Constants.NOTIFICATION_CHANNEL)
                    .build();
            notificationManager.createNotificationChannel(new NotificationChannel(
                    Constants.NOTIFICATION_CHANNEL,"Work Notification",NotificationManager.IMPORTANCE_HIGH
            ));
        }
        else{
            notification = new Notification.Builder(this)
                    .setSmallIcon(R.drawable.icon_notification)
                    .setContentTitle(message.getData().get(Constants.REMOTE_MSG_TITLE))
                    .setContentText(message.getData().get(Constants.REMOTE_MSG_MESSAGE))
                    .build();
        }

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)== PackageManager.PERMISSION_GRANTED){
                notificationManager.notify(Constants.NOTIFICATION_ID,notification);
            }
            else{
                Toast.makeText(this, "Notification permission not granted", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            notificationManager.notify(Constants.NOTIFICATION_ID,notification);
        }
    }
}
