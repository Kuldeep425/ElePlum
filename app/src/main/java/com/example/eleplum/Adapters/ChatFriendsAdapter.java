package com.example.eleplum.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eleplum.Activity.ChatActivity;
import com.example.eleplum.Models.Friends;
import com.example.eleplum.Models.User;
import com.example.eleplum.R;
import com.example.eleplum.Utils.Constants;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatFriendsAdapter extends RecyclerView.Adapter<ChatFriendsAdapter.ViewHolder> {

    ArrayList<Friends> chatFriends;
    Context context;

    public ChatFriendsAdapter(ArrayList<Friends>chatFriends,Context context) {
        this.chatFriends=chatFriends;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.view_chat_friends, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Friends friend=chatFriends.get(position);
        holder.chatFriendName.setText(friend.getName());
        holder.lastMessage.setText(friend.getLastMessage());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, ChatActivity.class);
                System.out.println(friend.getFriendId());
                intent.putExtra(Constants.CHAT_RECEIVER_ID,friend.getFriendId());
                intent.putExtra(Constants.CHAT_RECEIVER_NAME,friend.getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatFriends.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{

        public CircleImageView chatFriendImage;
        public TextView chatFriendName,lastMessage;
        LinearLayout linearLayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
           // chatFriendName=itemView.findViewById(R.id.chatFriendPic);
            chatFriendName=itemView.findViewById(R.id.chatFriendName);
            lastMessage=itemView.findViewById(R.id.lastMessage);
            linearLayout=itemView.findViewById(R.id.goToChatActivity);
        }
    }
}

