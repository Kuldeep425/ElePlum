package com.example.eleplum.Activity;

import static com.google.common.reflect.Reflection.initialize;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.eleplum.Models.Electrician;
import com.example.eleplum.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class EleProfileUpdate extends AppCompatActivity {
    Uri imageUri;
    private Electrician electrician;
    private double eleLatitude=0,eleLongitude=0;
    private static final int IMAGE_REQUEST_CODE = 123;
    TextInputEditText nameTxt,addressTxt,pinCodeTxt,bioTxt;
    String name,city,pinCode,bio;
    CircleImageView elePic;
    Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ele_profile_update);

        initialization();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            electrician=(Electrician) getIntent().getSerializableExtra("electrician");
        }
        if(electrician==null){
            Toast.makeText(this, "electrician not found", Toast.LENGTH_SHORT).show();
        }
        System.out.println(electrician.getElectricianId());
        elePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // pic the image from gallery and set on the circle image view
               picImageFormTheGallery();
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getLocationFromLocationName()){
                    Toast.makeText(EleProfileUpdate.this, "Lat: "+eleLatitude+" Lon: "+eleLongitude, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(imageUri==null){
                    Toast.makeText(EleProfileUpdate.this, "pic an image", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(validateInputs()){
                    uploadImageToFirebase(imageUri);
                }
            }
        });

    }

    private boolean getLocationFromLocationName() {

        Geocoder geocoder = new Geocoder(this);
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocationName(city, 1);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        if (addresses != null && !addresses.isEmpty()) {
            eleLatitude = addresses.get(0).getLatitude();
            eleLongitude = addresses.get(0).getLongitude();
            Log.d("TAG", "Latitude: " + eleLatitude + ", Longitude: " + eleLongitude);
        } else {
            Log.d("TAG", "Unable to find location.");
            return false;
        }
        return true;
    }

    private boolean validateInputs() {
        name=nameTxt.getText().toString().trim();
        bio=bioTxt.getText().toString().trim();
        city=addressTxt.getText().toString().trim();
        pinCode=pinCodeTxt.getText().toString().trim();
        if(name.length()==0){
            Toast.makeText(this, "Enter name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(bio.length()==0){
            Toast.makeText(this, "Enter bio", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(city.length()==0){
            Toast.makeText(this, "Enter city ", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(pinCode.length()!=6){
            Toast.makeText(this, "please correct pin code", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void picImageFormTheGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_REQUEST_CODE) {
             imageUri = data.getData();
             elePic.setImageURI(imageUri);

        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imagesRef = storageRef.child("images/" + UUID.randomUUID().toString());
        ProgressDialog dialog=new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setTitle("Uploading image...");
        dialog.show();
        imagesRef.putFile(imageUri)
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        float percent=(100*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                        dialog.setMessage("uploaded: "+(int)percent+"%");
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Image uploaded successfully
                        imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageUrl = uri.toString();
                                dialog.dismiss();
                                System.out.println(electrician.getElectricianId());
                                Electrician nElectrician=new Electrician(electrician.getElectricianId(),name,
                                        electrician.getPassword(),eleLatitude,eleLongitude,imageUrl,
                                        electrician.getPhone(),electrician.getPhonePass(),true);
                                updateElectricianProfile(nElectrician);
                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Image upload failed
                    }
                });
    }

    // method to update electrician profile
    private void updateElectricianProfile(Electrician electrician) {
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference dbReference=database.getReference("ElePlum");
        dbReference.child("Electrician").child(electrician.getElectricianId()).setValue(electrician).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
             if(task.isSuccessful()){
                 Toast.makeText(EleProfileUpdate.this,"Profile updated successfully",Toast.LENGTH_SHORT).show();
                 Intent intent=new Intent(EleProfileUpdate.this,EleMainActivity.class);
                 startActivity(intent);
                 finish();
             }
            }
        });
    }


    private void initialization(){
        nameTxt=findViewById(R.id.ele_name);
        addressTxt=findViewById(R.id.ele_city);
        pinCodeTxt=findViewById(R.id.ele_pinCode);
        bioTxt=findViewById(R.id.ele_bio);
        elePic=findViewById(R.id.ele_image);
        saveBtn=findViewById(R.id.saveProfile_Btn);



    }
}