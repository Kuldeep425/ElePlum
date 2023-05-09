package com.example.eleplum.Adapters;

import static java.security.AccessController.getContext;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eleplum.Activity.ChatActivity;
import com.example.eleplum.Models.msgModelClass;
import com.example.eleplum.R;
import com.example.eleplum.Utils.Constants;
import com.example.eleplum.Utils.PreferenceManager;

import java.util.ArrayList;

public class messagesAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<msgModelClass> messagesAdapterArrayList;
    int ITEM_SEND=1;
    int ITEM_RECIVE=2;
    private PreferenceManager preferenceManager;
    String senderId;


    public messagesAdapter(Context context, ArrayList<msgModelClass> messagesAdapterArrayList) {
        this.context = context;
        this.messagesAdapterArrayList = messagesAdapterArrayList;
        preferenceManager=new PreferenceManager((ChatActivity) context);
        if(preferenceManager.getBoolean(Constants.KEY_IS_USER)){
            senderId=preferenceManager.getString(Constants.KEY_USER_ID);
        }
        else{
            senderId=preferenceManager.getString(Constants.KEY_ELE_ID);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

      //  senderId=preferenceManager.getString(Constants.KEY_USER_ID);
        System.out.println(senderId);
        if (viewType == ITEM_SEND){
            View view = LayoutInflater.from(context).inflate(R.layout.sender_layout, parent, false);
            return new senderVierwHolder(view);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.reciver_layout, parent, false);
            return new reciverViewHolder(view);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        msgModelClass messages = messagesAdapterArrayList.get(position);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(context).setTitle("Delete")
                        .setMessage("Are you sure you want to delete this message?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();

                return false;
            }
        });
        if (holder.getClass()==senderVierwHolder.class){
            senderVierwHolder viewHolder = (senderVierwHolder) holder;
            viewHolder.msgtxt.setText(messages.getMessage());

        }else {
            reciverViewHolder viewHolder = (reciverViewHolder) holder;
            viewHolder.msgtxt.setText(messages.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messagesAdapterArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
            msgModelClass messages = messagesAdapterArrayList.get(position);
            if (senderId.equals(messages.getSenderid())) {
                return ITEM_SEND;
            } else {
                return ITEM_RECIVE;
            }
    }

    class  senderVierwHolder extends RecyclerView.ViewHolder {

        TextView msgtxt;
        public senderVierwHolder(@NonNull View itemView) {
            super(itemView);

            msgtxt = itemView.findViewById(R.id.msgsendertyp);

        }
    }
    class reciverViewHolder extends RecyclerView.ViewHolder {

        TextView msgtxt;
        public reciverViewHolder(@NonNull View itemView) {
            super(itemView);

            msgtxt = itemView.findViewById(R.id.recivertextset);
        }
    }
}
