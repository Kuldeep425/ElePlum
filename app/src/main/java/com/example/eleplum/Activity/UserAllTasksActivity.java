package com.example.eleplum.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.eleplum.Adapters.TasksAdapter;
import com.example.eleplum.Models.CreatedTask;
import com.example.eleplum.R;
import com.example.eleplum.Utils.Constants;
import com.example.eleplum.Utils.PreferenceManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserAllTasksActivity extends AppCompatActivity {

    RecyclerView userAllTasksRecyclerView;
    private PreferenceManager preferenceManager;
    ArrayList<CreatedTask>userAllTasks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_all_tasks);

        preferenceManager=new PreferenceManager(getApplicationContext());

        // initialization
        initialization();

        Log.d("UserId : ",preferenceManager.getString(Constants.KEY_USER_ID));
        FirebaseDatabase.getInstance().getReference("ElePlum").child("Tasks")
                .orderByChild("userId").equalTo(preferenceManager.getString(Constants.KEY_USER_ID))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userAllTasks=new ArrayList<>();
                        System.out.println(snapshot);
                        for(DataSnapshot d : snapshot.getChildren()){
                            CreatedTask tk = d.getValue(CreatedTask.class);
                            userAllTasks.add(tk);
                        }
                        System.out.println(userAllTasks);
                        TasksAdapter adapter=new TasksAdapter(userAllTasks,UserAllTasksActivity.this);
                        userAllTasksRecyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    // method to initialize the widgets
    private void initialization() {
        userAllTasksRecyclerView=findViewById(R.id.userAllTask);
        userAllTasksRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }
}