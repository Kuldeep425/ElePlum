package com.example.eleplum.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eleplum.AdapterListener.PendingElectricianListener;
import com.example.eleplum.Models.Electrician;
import com.example.eleplum.R;

import java.util.ArrayList;

public class PendingElectricianAdapter extends RecyclerView.Adapter<PendingElectricianAdapter.ViewHolder> {

    ArrayList<Electrician>electricians;
    Context context;
    private PendingElectricianListener listner;
    public PendingElectricianAdapter(ArrayList<Electrician>electricians, Context context, PendingElectricianListener listner){
          this.electricians=electricians;
          this.context=context;
          this.listner=listner;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.view_adminelectrician, parent, false);
        ViewHolder viewHolder = new PendingElectricianAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PendingElectricianAdapter.ViewHolder holder, int position) {
       Electrician electrician=electricians.get(position);
       holder.eleName.setText(electrician.getName());
       holder.elePhone.setText(electrician.getPhone());
       holder.rightCheck.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
                listner.onRightImageClicked(electricians.get(position));
           }
       });
    }

    @Override
    public int getItemCount() {
        return electricians.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{
        public TextView eleName;
        public TextView elePhone;
        public ImageView rightCheck;
        public  ImageView crossCheck;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eleName=itemView.findViewById(R.id.eleNameTxt);
            elePhone=itemView.findViewById(R.id.elePhoneTxt);
            rightCheck=itemView.findViewById(R.id.rightCheckImg);
            crossCheck=itemView.findViewById(R.id.crossCheckImg);

        }
    }
}
