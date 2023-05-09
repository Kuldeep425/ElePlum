package com.example.eleplum.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eleplum.Adapters.ChatFriendsAdapter;
import com.example.eleplum.Models.Friends;
import com.example.eleplum.R;
import com.example.eleplum.Utils.Constants;
import com.example.eleplum.Utils.PreferenceManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;


public class ChatFragment extends Fragment {
     private PreferenceManager preferenceManager;
     View view;
    RecyclerView chatFriendRecyclerView;
    ArrayList<Friends> friends;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_chat, container, false);

        preferenceManager=new PreferenceManager(getContext());
        // initialize
        initialization();

        String userId=preferenceManager.getString(Constants.KEY_USER_ID);
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("ElePlum");
        reference
                .child("users")
                .child(userId)
                .child("Friends")
                .orderByChild("timeStamp")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                       friends.clear();
                        System.out.println(snapshot);
                       for(DataSnapshot d: snapshot.getChildren()){
                           Friends ch=d.getValue(Friends.class);
                           friends.add(ch);
                       }

                        Collections.reverse(friends);
                        ChatFriendsAdapter chatFriendsAdapter=new ChatFriendsAdapter(friends,getContext());
                        chatFriendRecyclerView.setAdapter(chatFriendsAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        return view;
    }

    private void initialization() {
        friends=new ArrayList<>();
        chatFriendRecyclerView=view.findViewById(R.id.chatFriendUserRecyclerView);
        chatFriendRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}