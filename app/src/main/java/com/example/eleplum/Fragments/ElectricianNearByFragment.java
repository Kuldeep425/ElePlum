package com.example.eleplum.Fragments;

import static com.example.eleplum.Fragments.ElectricianNearByGoogleMapFragment.electricianList;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.eleplum.AdapterListener.ElectricianAdapterListener;
import com.example.eleplum.Adapters.ElectricianAdapter;
import com.example.eleplum.Models.Electrician;
import com.example.eleplum.R;


public class ElectricianNearByFragment extends Fragment implements ElectricianAdapterListener {

    View rootView;
    RecyclerView electricianRecyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_electrician_near_by, container, false);

        initialization();

        // listing electricians
        ElectricianAdapter electricianAdapter=new ElectricianAdapter(electricianList,getContext(),ElectricianNearByFragment.this);
        electricianRecyclerView.setAdapter(electricianAdapter);

        return rootView;
    }

    private void initialization() {

        electricianRecyclerView=rootView.findViewById(R.id.electricianRecyclerView);
        electricianRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    @Override
    public void onCallIconClick(Electrician electrician) {
        Toast.makeText(getContext(), ""+electrician.getElectricianId(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onChatIconClick(Electrician electrician) {

    }
}