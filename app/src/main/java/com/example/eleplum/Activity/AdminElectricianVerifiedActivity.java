package com.example.eleplum.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eleplum.AdapterListener.PendingElectricianListener;
import com.example.eleplum.Adapters.PendingElectricianAdapter;
import com.example.eleplum.Models.Electrician;
import com.example.eleplum.R;
import com.example.eleplum.Utils.SMSSenderUtil;
import com.example.eleplum.Utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminElectricianVerifiedActivity extends AppCompatActivity implements PendingElectricianListener {

    RecyclerView pendingElectricianView;
    DatabaseReference databaseReference;
    ArrayList<Electrician>electricians;
    Button dialogSendBtn;
    TextView dialogPasswordTxt;
    String dialogPassword;
    final int SMS_REQ_CODE=10;
    boolean smsPermission=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_electrician_verified);

        // initialize
        initialization();

        // get all pending electricians
        getAllPendingElectricians();









    }

    // method to check and give permission
    private void smsSendingPermission(Electrician electrician,int isVerified) {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)== PackageManager.PERMISSION_GRANTED){
             sendSMSToElectrician(electrician,isVerified);
        }
        else{
            // method to  request sms permission
            requestPermissionToSMS(electrician,isVerified);
        }
    }
    // method to request sms permission
    private void requestPermissionToSMS(Electrician electrician,int isVerified) {
        String [] per={"android.permission.SEND_SMS"};
        requestPermissions(per,SMS_REQ_CODE);
        if(smsPermission){
            sendSMSToElectrician(electrician,isVerified);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==SMS_REQ_CODE){
            smsPermission=true;
            Toast.makeText(this, "Sms permission granted", Toast.LENGTH_SHORT).show();
        }
        else{
            smsPermission=false;
        }
    }

    private void getAllPendingElectricians() {
        databaseReference.child("pendingElectrician").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println(snapshot);
                for(DataSnapshot d:snapshot.getChildren()) {
                    electricians.add(d.getValue(Electrician.class));
                }

                // list all pending electricians
                PendingElectricianAdapter adapter=new PendingElectricianAdapter(electricians,AdminElectricianVerifiedActivity.this,AdminElectricianVerifiedActivity.this);
                pendingElectricianView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initialization() {
        pendingElectricianView=findViewById(R.id.pendingViewRecyclerView);
        pendingElectricianView.setLayoutManager(new LinearLayoutManager(AdminElectricianVerifiedActivity.this));
        electricians=new ArrayList<>();
        //database reference
        databaseReference=FirebaseDatabase.getInstance().getReference("ElePlum");
    }

    @Override
    public void onRightImageClicked(Electrician electrician) {
        Toast.makeText(this, electrician.getName(), Toast.LENGTH_SHORT).show();
        Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.alert_verifyelectrician);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().windowAnimations=R.style.animation;
        dialog.show();
        dialogSendBtn=dialog.findViewById(R.id.dialogSendBtn);
        dialogPasswordTxt=dialog.findViewById(R.id.dialogPasswordTxt);
        dialogSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValid()){
                    savePasswordToDatabase(electrician);
                  //  new SMSSenderUtil().sendSMS(electrician.getPhone(),dialogPassword+"is your password",AdminElectricianVerifiedActivity.this);
                    dialog.dismiss();
                }
            }
        });

    }


    // method by admin to save the electrician password
    private void savePasswordToDatabase(Electrician electrician) {
        boolean isPasswordSaved=false;
        databaseReference.child("pendingElectrician").child(electrician.getElectricianId()).child("phonePass").setValue(dialogPassword+electrician.getPhone()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    // set password
                    electrician.setPassword(dialogPassword);
                    electrician.setPhonePass(dialogPassword+electrician.getPhone());
                    // once the admin save the password for electrician means simply verified
                    // move pendingElectrician to Electrician
                    Toast.makeText(AdminElectricianVerifiedActivity.this, "password saved", Toast.LENGTH_SHORT).show();
                    smsSendingPermission(electrician,1);
                }
            }
        });

    }


   // delete the electrician from the pending Electrician
    private void deleteTheElectrician(Electrician electrician,int isVerified) {
        databaseReference.child("pendingElectrician").child(electrician.getElectricianId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                   if(task.isSuccessful() && isVerified==1){
                       saveElectrician(electrician);
                   }
            }
        });
    }

    // save the electrician in Electrician if the admin verifies electrician
    private void saveElectrician(Electrician electrician) {
        databaseReference.child("Electrician").child(electrician.getElectricianId()).setValue(electrician).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    // if the task successfully send message to electrician
                    Toast.makeText(AdminElectricianVerifiedActivity.this, "Electrician has been verified", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // send sms to electrician
    private void sendSMSToElectrician(Electrician electrician,int isVerified) {
        String message="";
        if(isVerified==1){
            message+="Hi, "+electrician.getName()+"\n";
            message+="This is the password of your Electrician Profile: " +electrician.getPassword()+
                    "\n Don't share to anybody. \n Team - ElePlum";
        }
        SmsManager smsManager=SmsManager.getDefault();
        smsManager.sendTextMessage(electrician.getPhone(),null,message,null,null);
        deleteTheElectrician(electrician,isVerified);
        Toast.makeText(this, "sent the sms", Toast.LENGTH_SHORT).show();


    }

    // check if the input fields are valid
    private boolean isValid() {
        dialogPassword=dialogPasswordTxt.getText().toString().trim();
        if(dialogPassword.length()<8){
            Toast.makeText(this, "minimum 8 character", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}