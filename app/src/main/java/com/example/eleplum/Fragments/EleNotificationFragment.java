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

import com.example.eleplum.Adapters.EleNoteAdapter;
import com.example.eleplum.Models.CreatedTask;
import com.example.eleplum.R;
import com.example.eleplum.Utils.Constants;
import com.example.eleplum.Utils.PreferenceManager;
import com.example.eleplum.Utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class EleNotificationFragment extends Fragment {

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
                if(snapshot.exists()) {
                    for (DataSnapshot d : snapshot.getChildren()) {
                        CreatedTask task=d.getValue(CreatedTask.class);
                        double distance=
                                new Utils().getDistance(task.getLatitude(),task.getLongitude(),
                                        Double.valueOf(preferenceManager.getString(Constants.KEY_ELE_LATITUDE)),
                                        Double.valueOf(preferenceManager.getString(Constants.KEY_ELE_LONGITUDE))
                                );
                        if(distance<=11){
                            arrNote.add(task);
                        }
                    }
                    System.out.println(arrNote);
                    EleNoteAdapter adapter = new EleNoteAdapter(getContext(), arrNote);
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
}