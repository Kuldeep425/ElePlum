package com.example.eleplum.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.eleplum.AdapterListener.NotificationListener;
import com.example.eleplum.Adapters.EleNoteAdapter;
import com.example.eleplum.Models.CreatedTask;
import com.example.eleplum.Models.InterestedEle;
import com.example.eleplum.R;
import com.example.eleplum.Utils.Constants;
import com.example.eleplum.Utils.PreferenceManager;
import com.example.eleplum.Utils.Utils;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class EleNotificationFragment extends Fragment implements NotificationListener {

    RecyclerView recyclerView;
    View view;
    public ArrayList<CreatedTask> arrNote= new ArrayList<>();
    private PreferenceManager preferenceManager;

    public EleNotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_ele_notification, container, false);
        preferenceManager=new PreferenceManager(getContext());
        initialization();

        return view;

    }

    private void initialization() {
            recyclerView=view.findViewById(R.id.recyclerNote);
            // setting how will be the layout of recyclerview
            recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
            arrNote.clear();
            getAllNote();


    }
    private void getAllNote() {
        FirebaseDatabase.getInstance().getReference("ElePlum").child("Tasks").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println(snapshot);
                arrNote = new ArrayList<>();
                if(snapshot.exists()) {
                    for (DataSnapshot d : snapshot.getChildren()) {
                        CreatedTask task=d.getValue(CreatedTask.class);

                        boolean isActionTakenAlready=false;
                        if(task.getListOfActEle()!=null) {
                            for (int i = 0; i < task.getListOfActEle().size(); i++) {
                                if (task.getListOfActEle().get(i).equals(preferenceManager.getString(Constants.KEY_ELE_ID))) {
                                    isActionTakenAlready = true;
                                    break;
                                }
                            }
                        }
                        if(isActionTakenAlready) continue;
                        System.out.println(d);
                        double distance=
                                new Utils().getDistance(task.getLatitude(),task.getLongitude(),
                                        Double.valueOf(preferenceManager.getString(Constants.KEY_ELE_LATITUDE)),
                                        Double.valueOf(preferenceManager.getString(Constants.KEY_ELE_LONGITUDE))
                                );
                        if(distance<=11){
                            arrNote.add(task);
                        }
                    }
                    EleNoteAdapter adapter = new EleNoteAdapter(getContext(), arrNote,EleNotificationFragment.this);
                    recyclerView.setAdapter(adapter);
                }
                Toast.makeText(getContext(), ""+arrNote, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Could not load data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // this function will execute when accept button is clicked
    @Override
    public void onAcceptClick(CreatedTask task) {
        InterestedEle ele = new InterestedEle(preferenceManager.getString(Constants.KEY_ELE_ID),
                preferenceManager.getString(Constants.KEY_NAME),
                preferenceManager.getString(Constants.KEY_PROFILE_IMAGE_URL));

        FirebaseDatabase.getInstance().getReference("ElePlum")
                .child("Tasks")
                .child(task.getTaskId())
                .child("InterestedEle")
                .child(preferenceManager.getString(Constants.KEY_ELE_ID))
                .setValue(ele)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        FirebaseDatabase.getInstance().getReference("ElePlum")
                                .child("Tasks")
                                .child(task.getTaskId())
                                .child("listOfAcceptedEle")
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        List<String> eleId = new ArrayList<>();
                                        for(DataSnapshot d:snapshot.getChildren()){
                                            eleId.add(d.getValue(String.class));
                                        }
                                        eleId.add(ele.getEleId());
                                        FirebaseDatabase.getInstance().getReference("ElePlum")
                                                .child("Tasks")
                                                .child(task.getTaskId())
                                                .child("listOfAcceptedEle")
                                                .setValue(eleId)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                      // Electrician takes action
                                                        onElectricianTakenAction(preferenceManager.getString(
                                                           Constants.KEY_ELE_ID
                                                        ),task.getTaskId());
                                                    }
                                                });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                    }
                });
    }

    // once the electrician took the action
    private void onElectricianTakenAction(String eleId,String taskId) {
        FirebaseDatabase.getInstance().getReference("ElePlum")
                .child("Tasks")
                .child(taskId)
                .child("listOfActEle")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<String>Ids = new ArrayList<>();
                        for(DataSnapshot d:snapshot.getChildren()){
                            Ids.add(d.getValue(String.class));
                        }
                        Ids.add(eleId);
                        FirebaseDatabase.getInstance().getReference("ElePlum")
                                .child("Tasks")
                                .child(taskId)
                                .child("listOfActEle")
                                .setValue(Ids)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
                                       }
                                    }
                                });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    // this function will execute when reject button in clicked
    @Override
    public void onRejectClick(CreatedTask task) {
        onElectricianTakenAction(preferenceManager.getString(Constants.KEY_ELE_ID),task.getTaskId());
    }
}