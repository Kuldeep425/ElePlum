package com.example.eleplum.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
     // Login page data fields
     Button logBtn;
     EditText phoneNo;
     EditText password;
     TextView signUp;
     FirebaseAuth auth;
     DatabaseReference reference;


     @Override
     protected void onCreate(@Nullable Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_login);
          //Initialize all data fields
          initialization();
          // on clicking Sign Up move to SignUpActivity
          signUp.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                    Intent intent= new Intent(LoginActivity.this,SignUpActivity.class);
                    startActivity(intent);
               }
          });
          // on clicking Login
          logBtn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                         loginUser();
               }
          });


     }

     private void loginUser() {
       // validate login info
          if (!validateUser() || !validatePassword()){
               return;
          }else{
               isUser();
          }
               
          
     }

     private void isUser() {
          String input=password.getText().toString()+phoneNo.getText().toString();
          System.out.println(input);
          reference=FirebaseDatabase.getInstance().getReference("ElePlum/users");
          Query checkUser=reference.orderByChild("phonePass").equalTo(input);
          checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                         Intent intent= new Intent(LoginActivity.this,MainActivityUser.class);
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

     private boolean validatePassword() {
          String val=password.getText().toString();

          if(val.isEmpty()){
//               password.setError("Field cannot be empty");
               return false;
          }else{
//               password.setError(null);
//               password.setErrorEnabled(false);
               return true;
          }
     }

     private boolean validateUser() {
          String val=phoneNo.getText().toString();
          if(val.isEmpty()){
//               phoneNo.setError("Field cannot be empty");
               return false;
          }else{
//               phoneNo.setError(null);
//               phoneNo.setErrorEnabled(false);
               return true;
          }
     }


     private void initialization() {
          logBtn=findViewById(R.id.loginBtn);
          phoneNo=findViewById(R.id.phoneTxt);
          password=findViewById(R.id.passwordTxt);
          signUp=findViewById(R.id.signUpTxt);
          auth=FirebaseAuth.getInstance();
          reference= FirebaseDatabase.getInstance().getReference("ElePum");
     }
}
