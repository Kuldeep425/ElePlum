package com.example.eleplum.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eleplum.Models.User;
import com.example.eleplum.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
     EditText phoneNumberEdtTxt,passwordEdtTxt;
     TextView gotoSignUp;
     String contactNumber,password;
     Button signInBtn;
     FirebaseAuth  mAuth;
     FirebaseDatabase database;
     DatabaseReference reference;
     public static String userId;
     @Override
     protected void onCreate(@Nullable Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_login);
          //Initialize all data fields
          initialization();
          // on clicking Sign Up move to SignUpActivity
          gotoSignUp.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                    Intent intent= new Intent(LoginActivity.this,SignUpActivity.class);
                    startActivity(intent);
                    finish();
               }
          });
          // on clicking Login
          signInBtn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                    loginUser();
               }
          });


     }

     private void loginUser() {
       // validate login info
          if (isFieldsValid()){
               // calling login to user function to login the user once the fields are valid
               loginToUser();
          }
     }

     private void loginToUser() {
          String input=passwordEdtTxt.getText().toString()+phoneNumberEdtTxt.getText().toString();
          System.out.println(input);
          reference=FirebaseDatabase.getInstance().getReference("ElePlum/users");
          Query checkUser=reference.orderByChild("phonePass").equalTo(input);
          checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                         Intent intent= new Intent(LoginActivity.this,MainActivityUser.class);
                         userId=snapshot.getValue(User.class).getUserId();
                         startActivity(intent);
                         finish();
                    }else {
                         Toast.makeText(LoginActivity.this, "failed", Toast.LENGTH_SHORT).show();
                    }
               }

               @Override
               public void onCancelled(@NonNull DatabaseError error) {

               }
          });

     }

     private void initialization() {
          signInBtn = findViewById(R.id.loginBtn);
          phoneNumberEdtTxt = findViewById(R.id.phoneTxt);
          passwordEdtTxt = findViewById(R.id.passwordTxt);
          gotoSignUp = findViewById(R.id.signUpTxt);
          mAuth = FirebaseAuth.getInstance();
          reference = FirebaseDatabase.getInstance().getReference("ElePum");
     }


     private boolean isFieldsValid() {
          contactNumber=phoneNumberEdtTxt.getText().toString().trim();
          password=passwordEdtTxt.getText().toString().trim();
          if(contactNumber.length()!=10){
               Toast.makeText(this, "Invalid Phone number", Toast.LENGTH_SHORT).show();
               return false;
          }
          if(password.length()<8){
               Toast.makeText(this, "Password should be minimum 8 characters", Toast.LENGTH_SHORT).show();
               return false;
          }
          return true;
     }


}
