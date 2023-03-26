package com.example.eleplum.Activity;

import android.os.Bundle;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

public class LoginActivity extends AppCompatActivity {
     TextView phoneNumberTxtV,passwordTxtV;
     String contactNumber,password;
     Button signInBtn;
     FirebaseAuth  mAuth;
     FirebaseDatabase database;
     @Override
     protected void onCreate(@Nullable Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_login);

          // method to initialize the widgets
          initialize();
          mAuth= FirebaseAuth.getInstance();
          database=FirebaseDatabase.getInstance();

          // click on signInBtn
          signInBtn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                    // check valid arguments
                     boolean check=isFieldsValid();
                    Query query=database.getReference("users");//orderByChild("phonePassword").equalTo(contactNumber+password);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                         @Override
                         public void onDataChange(@NonNull DataSnapshot snapshot) {
                              if(snapshot.exists())
                              Toast.makeText(LoginActivity.this, "login", Toast.LENGTH_SHORT).show();
                         }
                         @Override
                         public void onCancelled(@NonNull DatabaseError error) {
                              Toast.makeText(LoginActivity.this, "not found", Toast.LENGTH_SHORT).show();
                         }
                    });

               }
          });
     }

     private boolean isFieldsValid() {
          contactNumber=phoneNumberTxtV.getText().toString().trim();
          password=passwordTxtV.getText().toString().trim();
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

     private void initialize() {
          phoneNumberTxtV=findViewById(R.id.contactNumber);
          passwordTxtV=findViewById(R.id.password);
          signInBtn=findViewById(R.id.loginBtn);
     }
}
