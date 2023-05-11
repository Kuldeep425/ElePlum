package com.example.eleplum.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eleplum.Activity.LoginActivity;
import com.example.eleplum.Models.CreatedTask;
import com.example.eleplum.R;
import com.example.eleplum.Utils.Constants;
import com.example.eleplum.Utils.PreferenceManager;
import com.example.eleplum.Utils.Utils;

import java.lang.ref.PhantomReference;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class EleNoteAdapter extends RecyclerView.Adapter<EleNoteAdapter.ViewHolder> {
    Context context;
    ArrayList<CreatedTask> arrNote;
    private PreferenceManager preferenceManager;

    public EleNoteAdapter(Context context, ArrayList<CreatedTask> arrNote) {
        this.context = context;
        this.arrNote = arrNote;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ele_note_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        preferenceManager = new PreferenceManager(context);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CreatedTask createdTask = arrNote.get(position);
        double taskLongitude = createdTask.getLongitude();
        double taskLatitude = createdTask.getLatitude();
        double distance = new Utils().getDistance(taskLatitude, taskLongitude,
                Double.valueOf(preferenceManager.getString(Constants.KEY_ELE_LATITUDE)),
                Double.valueOf(preferenceManager.getString(Constants.KEY_ELE_LONGITUDE))
        );

        holder.txtId.setText("Desc: " + createdTask.getDesc());
        holder.txtDate.setText("Date: " + createdTask.getDate());
        holder.txtDistance.setText("Distance : " + String.format("%.2f", distance) + "km");
        holder.txtTime.setText("Time: " + createdTask.getTime());

    }

    @Override
    public int getItemCount() {
        return arrNote.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtId, txtDate, txtTime, txtDistance;

        public ViewHolder(View itemView) {
            super(itemView);
            txtId = itemView.findViewById(R.id.textId);
            txtDate = itemView.findViewById(R.id.textDate);
            txtTime = itemView.findViewById(R.id.textTime);
            txtDistance = itemView.findViewById(R.id.textDistance);
        }

    }
}