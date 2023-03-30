package com.example.eleplum.Fragments;

import static com.example.eleplum.Fragments.ElectricianNearByGoogleMapFragment.electricianList;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eleplum.Adapters.ElectricianAdapter;
import com.example.eleplum.R;


public class ElectricianNearByFragment extends Fragment {

    View rootView;
    RecyclerView electricianRecyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_electrician_near_by, container, false);

        initialization();

        // listing electricians
        ElectricianAdapter electricianAdapter=new ElectricianAdapter(electricianList,getContext());
        electricianRecyclerView.setAdapter(electricianAdapter);







        return rootView;
    }

    private void initialization() {

        electricianRecyclerView=rootView.findViewById(R.id.electricianRecyclerView);
        electricianRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }
}