package com.example.eleplum.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eleplum.Models.CreatedTask;
import com.example.eleplum.R;
import com.example.eleplum.Utils.PreferenceManager;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder> {

     Context context;
     ArrayList<CreatedTask>tasks;


     public TasksAdapter(ArrayList<CreatedTask>tasks,Context context){
          this.tasks=tasks;
          this.context=context;
     }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_user_task, parent, false);
        TasksAdapter.ViewHolder viewHolder = new TasksAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TasksAdapter.ViewHolder holder, int position) {
         CreatedTask tk=tasks.get(position);
         holder.desc.setText(tk.getDesc());
         holder.date.setText(tk.getDate());
         holder.time.setText(tk.getTime());
         holder.count.setText("12");
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

         TextView desc,date,time,count;
        public ViewHolder(View itemView) {
            super(itemView);
            desc=itemView.findViewById(R.id.userTaskDesc);
            date=itemView.findViewById(R.id.userTaskDate);
            time=itemView.findViewById(R.id.userTaskTime);
            count=itemView.findViewById(R.id.acceptElectricianCounts);
        }

    }
}
