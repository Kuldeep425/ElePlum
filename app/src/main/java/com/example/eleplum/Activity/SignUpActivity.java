package com.example.eleplum.Activity;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eleplum.Models.User;
import com.example.eleplum.R;
import com.google.firebase.FirebaseAppLifecycleListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class SignUpActivity extends AppCompatActivity {
    CheckBox signUpEleBox;
    boolean isCheckBox=false,isShowPass=false;
    EditText nameTxt,phoneNumberTxt,passwordTxt;
    Button signUpBtn;
    String name,phoneNumber,password;

    FirebaseAuth authInstance;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    FirebaseDatabase firebaseDatabase;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    User user;
    boolean isUser=true;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // calling method to initializing the components
        authInstance = FirebaseAuth.getInstance();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);

                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }

                // Show a message and update the UI
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.

                Log.d(TAG, "onCodeSent:" + verificationId);
                // clear all fields
                 makeEmptyAttributes();
                 // change activity to otp verification

                Intent intOtp=new Intent(SignUpActivity.this,OTPVerificationActivity.class);
                intOtp.putExtra("signUpData",user);
                intOtp.putExtra("verId",verificationId);
               // intOtp.putExtra("ifUser",isUser);
                startActivity(intOtp);

                // Save verification ID and resending token so we can use them later
//                mVerificationId = verificationId;
//                mResendToken = token;
            }
        };
        initializeTheComponents();


//        if(isShowPass){
//            phoneNumberTxt.setInputType(InputType.TYPE_CLASS_NUMBER);
//            isShowPass=true;
//        }


        signUpEleBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // register user as electrician
                if(isCheckBox==false){
                    passwordTxt.setVisibility(View.GONE);
                    isCheckBox=true;
                }
                else{
                    passwordTxt.setVisibility(View.VISIBLE);
                    isCheckBox=false;
                }

            }
        });
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name=nameTxt.getText().toString().trim();
                phoneNumber=phoneNumberTxt.getText().toString().trim();
                password=passwordTxt.getText().toString().trim();

                if(isCheckBox==false){
                    // calling method to register a user

                    registerAUser();
                }
                else{
                    // calling method to register an electrician
                    isUser=false;
                    registerAnElectrician();
                }

            }
        });




    }

    private void makeEmptyAttributes() {
        nameTxt.getText().clear();
        phoneNumberTxt.getText().clear();
        passwordTxt.getText().clear();

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
    }

    // method to verify the data provided by the user

    boolean isValidData(int isUser){
        if(name.length()==0){
            Toast.makeText(this, "Blank user name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(phoneNumber.length()!=10){
            Toast.makeText(this, "Invalid phone number", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(isUser==1 && password.length()<8){
            Toast.makeText(this, "Password should be minimum 8 characters", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void registerAnElectrician() {
    }

    private void registerAUser() {
        if(isValidData(1)){
             user=new User(name,phoneNumber,password);

            PhoneAuthOptions options =
                    PhoneAuthOptions.newBuilder(authInstance)
                            .setPhoneNumber("+91"+phoneNumber)       // Phone number to verify
                            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                            .setActivity(this)                 // Activity (for callback binding)
                            .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                            .build();
            PhoneAuthProvider.verifyPhoneNumber(options);
        }
    }

    private void initializeTheComponents() {
        nameTxt=findViewById(R.id.nameTxt);
        phoneNumberTxt=findViewById(R.id.phoneNumberTxt);
        passwordTxt=findViewById(R.id.passwordTxt);
        signUpEleBox=findViewById(R.id.checkBox);
        signUpBtn=findViewById(R.id.signUpBtn);

    }
}
