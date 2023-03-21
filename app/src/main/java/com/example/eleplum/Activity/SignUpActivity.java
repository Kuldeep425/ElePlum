package com.example.eleplum.Activity;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eleplum.Models.User;
import com.example.eleplum.R;


public class SignUpActivity extends AppCompatActivity {
    CheckBox signUpEleBox;
    boolean isCheckBox=false,isShowPass=false;
    EditText nameTxt,phoneNumberTxt,passwordTxt;
    Button signUpBtn;
    String name,phoneNumber,password;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // calling method to initializing the components
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
                    registerAnElectrician();
                }

            }
        });




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
            User user=new User(name,phoneNumber,password);


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
