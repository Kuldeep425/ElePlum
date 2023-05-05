package com.example.eleplum.Fragments;

import static com.example.eleplum.Activity.LoginActivity.userId;
import static com.example.eleplum.Fragments.ElectricianNearByGoogleMapFragment.electricianList;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.eleplum.Activity.OutgoingCallActivity;
import com.example.eleplum.AdapterListener.ElectricianAdapterListener;
import com.example.eleplum.Adapters.ElectricianAdapter;
import com.example.eleplum.Models.Electrician;
import com.example.eleplum.R;
import com.example.eleplum.Utils.Constants;
import com.example.eleplum.Utils.PreferenceManager;


public class ElectricianNearByFragment extends Fragment implements ElectricianAdapterListener {

    View rootView;
    RecyclerView electricianRecyclerView;
    Electrician electrician=null;
    int CALL_REQ_CODE=11;
    private PreferenceManager preferenceManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_electrician_near_by, container, false);

        initialization();

        preferenceManager=new PreferenceManager(getContext());

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
    public void onCallIconClick(Electrician electrician1) {
        electrician=electrician1;
        if(electrician==null){
            Toast.makeText(getContext(), "electrician not found..could not make a call", Toast.LENGTH_LONG).show();
            return;
        }
//        if(electrician.getFcmToken()!=null){
//            Toast.makeText(getContext(), "Fcm token not found", Toast.LENGTH_SHORT).show();
//             return;
//        }
        Toast.makeText(getContext(), ""+electrician.getElectricianId(), Toast.LENGTH_SHORT).show();
        // function to check request permission
        checkPermissionAndMakeCall();
    }


    // method to check permission whether the necessary permission for the audio call is granted or not
    private void checkPermissionAndMakeCall() {
        if(ActivityCompat.checkSelfPermission(getContext(),"Manifest.permission.RECORD_AUDIO")== PackageManager.PERMISSION_GRANTED){
            // go to Call activity to make
            Toast.makeText(getContext(), "Permission already granted", Toast.LENGTH_SHORT).show();
            goToOutgoingCallActivity();
        }
        else{
            requestPermissions(new String[]{"android.permission.RECORD_AUDIO"},CALL_REQ_CODE);
        }
    }

    // check for result


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==CALL_REQ_CODE){
            if(grantResults.length >0 && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                goToOutgoingCallActivity();
            }
        }
    }
    private void goToOutgoingCallActivity() {
        Intent intent=new Intent(getContext(), OutgoingCallActivity.class);
        intent.putExtra("callerId",preferenceManager.getString(Constants.KEY_USER_ID));
        intent.putExtra("callerName",preferenceManager.getString(Constants.KEY_NAME));
        intent.putExtra("callerImageURL",preferenceManager.getString(Constants.KEY_PROFILE_IMAGE_URL));
        intent.putExtra("callerFcmToken",preferenceManager.getString(Constants.KEY_FCM_TOKEN));

        intent.putExtra("calleeId",electrician.getElectricianId());
        intent.putExtra("calleeName",electrician.getName());
        intent.putExtra("calleeImageURL",electrician.getImageURL());
        intent.putExtra("calleeFcmToken",electrician.getFcmToken());

        startActivity(intent);
    }

    @Override
    public void onChatIconClick(Electrician electrician) {

    }
}