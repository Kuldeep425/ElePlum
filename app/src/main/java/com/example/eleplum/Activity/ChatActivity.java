package com.example.eleplum.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eleplum.Adapters.messagesAdapter;
import com.example.eleplum.Models.msgModelClass;
import com.example.eleplum.R;
import com.example.eleplum.Utils.Constants;
import com.example.eleplum.Utils.PreferenceManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {
String senderName,receiverName,receiverId,senderId;
    FirebaseDatabase database;

    public  static String senderImg;
    public  static String reciverImg;
    CardView sendButton;
    EditText textmsg;


    RecyclerView messageRecyclerView;
    ArrayList<msgModelClass> messagesArrayList;
    messagesAdapter msgAdpter;
    private PreferenceManager preferenceManager;
    String senderType,receiverType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        database = FirebaseDatabase.getInstance();

        preferenceManager=new PreferenceManager(ChatActivity.this);
//        reciverName = getIntent().getStringExtra("nameeee");
//        reciverimg = getIntent().getStringExtra("reciverImg");
        if(preferenceManager.getBoolean(Constants.KEY_IS_USER)) {
            senderId = preferenceManager.getString(Constants.KEY_USER_ID);
            senderType="users";
            receiverType="Electrician";
        }
        else {
            senderId = preferenceManager.getString(Constants.KEY_ELE_ID);
            senderType="Electrician";
            receiverType="users";
        }

        // calling method to get the extra from the intent
        getIntentExtra();

        messagesArrayList = new ArrayList<>();

        sendButton = findViewById(R.id.sendcardview);
       textmsg = findViewById(R.id.writemsg);
//      reciverNName = findViewById(R.id.recivername);
//      profile = findViewById(R.id.profileimgg);
        messageRecyclerView = findViewById(R.id.recyclerChat);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        messageRecyclerView.setLayoutManager(linearLayoutManager);

        if(messagesArrayList.size()!=0){
        msgAdpter = new messagesAdapter(ChatActivity.this,messagesArrayList);
        messageRecyclerView.setAdapter(msgAdpter);
        }


//        Picasso.get().load(reciverimg).into(profile);
//        reciverNName.setText(""+reciverName);




        DatabaseReference reference = database.getReference("ElePlum").child("users").child(senderId);
        DatabaseReference  chatreference = database.getReference("ElePlum").child(senderType).child(senderId).child("Friends").child(receiverId).child("messages");


        chatreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesArrayList.clear();
                System.out.println(snapshot);
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){

                    msgModelClass messages = dataSnapshot.getValue(msgModelClass.class);
                    messagesArrayList.add(messages);
                }
                msgAdpter = new messagesAdapter(ChatActivity.this,messagesArrayList);
//                msgAdpter.notifyDataSetChanged();
                messageRecyclerView.setAdapter(msgAdpter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                senderImg= snapshot.child("profilepic").getValue().toString();
//                reciverIImg=reciverimg;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = textmsg.getText().toString();
                if (message.isEmpty()){
                    Toast.makeText(ChatActivity.this, "Enter The Message First", Toast.LENGTH_SHORT).show();
                    return;
                }
                textmsg.setText("");
                Date date = new Date();
                msgModelClass messagess = new msgModelClass(message,senderId,date.getTime());
                database=FirebaseDatabase.getInstance();
                // save the data in sender side
                database.getReference("ElePlum").child(senderType)
                        .child(senderId)
                        .child("Friends")
                        .child(receiverId)
                        .child("messages")
                        .push().setValue(messagess).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                // save the data in receiver side
                                database.getReference("ElePlum").child(receiverType)
                                        .child(receiverId)
                                        .child("Friends")
                                        .child(senderId)
                                        .child("messages")
                                        .push()
                                        .setValue(messagess).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused){
                                                // method to up the last message and time
                                                Toast.makeText(ChatActivity.this, "added", Toast.LENGTH_SHORT).show();
                                              updateLastMessageAndTime(messagess);
                                            }
                                        });
                            }
                        });
            }
        });
    }

    // method to get the value that intent has
    private void getIntentExtra() {
        receiverId=getIntent().getStringExtra(Constants.CHAT_RECEIVER_ID);
        receiverName=getIntent().getStringExtra(Constants.CHAT_RECEIVER_NAME);
        senderName=preferenceManager.getString(Constants.KEY_NAME);
        System.out.println(receiverId);
        System.out.println(receiverName);
        System.out.println(senderName);
    }

    // it is the helper method to update the last message and last time name and

    void helperToUpdateSenderSide(String key,String val,long timeStamp,int a){
        if(a==1){
            database.getReference("ElePlum")
                    .child(senderType)
                    .child(senderId)
                    .child("Friends")
                    .child(receiverId)
                    .child(key)
                    .setValue(val)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    });

        }
        else{
            FirebaseDatabase.getInstance().getReference("ElePlum")
                    .child(senderType)
                    .child(senderId)
                    .child("Friends")
                    .child(receiverId)
                    .child(key)
                    .setValue(timeStamp)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    });
        }



    }

    // method to update from the receiver side
    void helperToUpdateReceiverSide(String key,String val,long timeStamp,int a){
        if(a==1){
            FirebaseDatabase.getInstance().getReference("ElePlum")
                    .child(receiverType)
                    .child(receiverId)
                    .child("Friends")
                    .child(senderId)
                    .child(key)
                    .setValue(val)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    });
        }
        else{
            FirebaseDatabase.getInstance().getReference("ElePlum")
                    .child(receiverType)
                    .child(receiverId)
                    .child("Friends")
                    .child(senderId)
                    .child(key)
                    .setValue(timeStamp)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    });
        }



    }
    private void updateLastMessageAndTime(msgModelClass messagess) {
            // update sender side
             helperToUpdateSenderSide("lastMessage",messagess.getMessage(),1,1);
             helperToUpdateSenderSide("timeStamp","",messagess.getTimeStamp(),0);
             helperToUpdateSenderSide("name",receiverName,1,1);
             helperToUpdateSenderSide("friendId",receiverId,1,1);

             // update receiver side
             helperToUpdateReceiverSide("lastMessage",messagess.getMessage(),1,1);
             helperToUpdateReceiverSide("timeStamp","",messagess.getTimeStamp(),0);
             helperToUpdateReceiverSide("name",senderName,1,1);
             helperToUpdateReceiverSide("friendId",senderId,1,1);
    }
}