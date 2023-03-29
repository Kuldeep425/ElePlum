package com.example.eleplum.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
import com.example.eleplum.Models.User;
import com.example.eleplum.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

public class OTPVerificationActivity extends AppCompatActivity {
    TextView phoneTextView;
    User signUpUser;
    Button verifyOtpBtn;
    PinView pinView;
    String otp,verificationId;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference dbReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);

        // hide app bar in this activity
        getSupportActionBar().hide();
        //To retrieve signUpData in OTPVerification Activity
        signUpUser=(User)getIntent().getSerializableExtra("signUpData");
        // calling
        phoneTextView=findViewById(R.id.otpTextView);
        String message=phoneTextView.getText().toString()+signUpUser.getPhoneNumber();
        phoneTextView.setText(message);
        //
        verifyOtpBtn=findViewById(R.id.verifyOtpBtn);
        //
        mAuth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        dbReference=database.getReference("ElePlum");
        //
        pinView=findViewById(R.id.otppin);
        pinView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                   otp=charSequence.toString();
                   if(otp.length()==6){
                       Toast.makeText(OTPVerificationActivity.this, "otp completed", Toast.LENGTH_SHORT).show();

                   }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        verificationId=(String)getIntent().getStringExtra("verId");
        verifyOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    verifyOtp(otp);


            }
        });



    }
//verifyOtp is used to verify code from firebase
    private void verifyOtp(String code) {
        // getting credential for verification id and code
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);

    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        // inside this method we are checking if
        // the code entered is correct or not.
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // if the code is correct and the task is successful
                            // we are sending our user to new activity.
                            Toast.makeText(OTPVerificationActivity.this, "Verified", Toast.LENGTH_LONG).show();
                            // method to call to save the user data in database
                            saveUserDataInDatabase(signUpUser);
                            startActivity(new Intent(OTPVerificationActivity.this,MainActivityUser.class));
                            finish();
                        } else {
                            // if the code is not correct then we are
                            // displaying an error message to the user.
                            Toast.makeText(OTPVerificationActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
   String userId=dbReference.child("users").push().getKey();
    private void saveUserDataInDatabase(User signUpUser) {
        dbReference.child("users").child(userId).setValue(signUpUser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
               if(task.isSuccessful()){
                   String uniqueId=dbReference.child("users").getKey();
                   Log.d("Unique Id: ",uniqueId);
                   Toast.makeText(OTPVerificationActivity.this, "added to database", Toast.LENGTH_SHORT).show();
               }
               else{
                   Toast.makeText(OTPVerificationActivity.this, "unable to add in database", Toast.LENGTH_SHORT).show();
               }
            }
        });
    }
}
