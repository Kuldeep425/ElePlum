package com.example.eleplum.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eleplum.Activity.LoginActivity;
import com.example.eleplum.Fragments.EleNotificationFragment;
import com.example.eleplum.Models.CreatedTask;
import com.example.eleplum.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class EleNoteAdapter extends RecyclerView.Adapter<EleNoteAdapter.ViewHolder> {
    Context context;
 ArrayList<CreatedTask> arrNote;
 public EleNoteAdapter(Context context, ArrayList<CreatedTask> arrNote){
     this.context=context;
     this.arrNote=arrNote;
 }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view=  LayoutInflater.from(context).inflate(R.layout.ele_note_row,parent,false);
      ViewHolder viewHolder= new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
      CreatedTask createdTask=arrNote.get(position);
     double taskLongitude=createdTask.getLongitude();
     double taskLatitude=createdTask.getLatitude();
     double distance=getDistance(taskLatitude,taskLongitude, LoginActivity.eleLatitude,LoginActivity.eleLongitude);

         holder.txtId.setText(createdTask.getTaskId());
         holder.txtDate.setText(createdTask.getDate());
         holder.txtDistance.setText(distance+"");
         holder.txtTime.setText(createdTask.getTime());
    }

    @Override
    public int getItemCount() {
        return arrNote.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtId,txtDate,txtTime,txtDistance;

        public ViewHolder(View itemView){
            super(itemView);
            txtId=itemView.findViewById(R.id.textId);
            txtDate=itemView.findViewById(R.id.textDate);
            txtTime=itemView.findViewById(R.id.textTime);
            txtDistance=itemView.findViewById(R.id.textDistance);
        }

    }
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
}
