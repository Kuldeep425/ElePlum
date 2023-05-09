package com.example.eleplum.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eleplum.Activity.ChatActivity;
import com.example.eleplum.Adapters.ChatFriendsAdapter;
import com.example.eleplum.Adapters.messagesAdapter;
import com.example.eleplum.Models.Friends;
import com.example.eleplum.Models.msgModelClass;
import com.example.eleplum.R;
import com.example.eleplum.Utils.Constants;
import com.example.eleplum.Utils.PreferenceManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Predicate;


public class EleChatFragment extends Fragment {
     RecyclerView chatFriendRecyclerView;
     View view;

     ArrayList<Friends>friends;



     private PreferenceManager preferenceManager;

    public EleChatFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        view=inflater.inflate(R.layout.fragment_ele_chat, container, false);

        preferenceManager=new PreferenceManager(getContext());

        // method to initialize
        initialization();

        String electricianId=preferenceManager.getString(Constants.KEY_ELE_ID);
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("ElePlum");
        reference
                .child("Electrician")
                .child(electricianId)
                .child("Friends")
                .orderByChild("timeStamp")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        friends.clear();
                        for(DataSnapshot d:snapshot.getChildren()){
                            friends.add(d.getValue(Friends.class));
                        }
                        Collections.reverse(friends);
                        ChatFriendsAdapter adapter=new ChatFriendsAdapter(friends,getContext());
                        chatFriendRecyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




        // Inflate the layout for this fragment

        return view;
    }

    private void initialization() {
        friends=new ArrayList<>();
        chatFriendRecyclerView=view.findViewById(R.id.chatFriendRecyclerView);
        chatFriendRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}