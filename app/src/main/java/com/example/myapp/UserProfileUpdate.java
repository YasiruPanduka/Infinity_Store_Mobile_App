package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class UserProfileUpdate extends AppCompatActivity {
    public static final String TAG = "TAG";
    ImageView profileImage;
    Button changeProfileImage,saveBtn;
    String userId;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    StorageReference storageReference;
    EditText profileName,profileEmail,profilePhone,profileBirth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_update);

        //get Data from profile page to this edit profile page
        Intent data = getIntent();
        String fullName = data.getStringExtra("fullName");
        String email = data.getStringExtra("email");
        String birth = data.getStringExtra("birth");
        String phone = data.getStringExtra("phone");

        Log.d(TAG, "onCreate: " + fullName + " " + email + " " + phone + " " + birth);

        profileImage = findViewById(R.id.profileImage);
        changeProfileImage = findViewById(R.id.changeProfile);
        profileName = findViewById(R.id.profileName1);
        profileEmail = findViewById(R.id.profileEmail1);
        profilePhone = findViewById(R.id.profilePhone1);
        profileBirth = findViewById(R.id.profileBirth1);
        saveBtn = findViewById(R.id.save_btn1);

        profileEmail.setText(email);
        profileName.setText(fullName);
        profileBirth.setText(birth);
        profilePhone.setText(phone);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        //edit
        FirebaseUser mFirebaseUser = fAuth.getCurrentUser();
        if(mFirebaseUser != null) {
            userId = mFirebaseUser.getUid();
        }

        user = fAuth.getCurrentUser();

        storageReference = FirebaseStorage.getInstance().getReference();

        StorageReference profileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
            }
        });

        changeProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open gallery
               Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
               startActivityForResult(openGalleryIntent, 1000);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(profileName.getText().toString().isEmpty() || profileEmail.getText().toString().isEmpty() || profilePhone.getText().toString().isEmpty() || profileBirth.getText().toString().isEmpty()){
                    Toast.makeText(UserProfileUpdate.this, "Empty Field(s) Detected!", Toast.LENGTH_SHORT).show();
                    return;
                }

                String email = profileEmail.getText().toString();
                user.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        DocumentReference docRef = fStore.collection("users").document(userId);

                            Map<String,Object> edited = new HashMap<>();
                            edited.put("email",email);
                            edited.put("fName",profileName.getText().toString());
                            edited.put("phone",profilePhone.getText().toString());
                            edited.put("birth",profileBirth.getText().toString());
                            docRef.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(UserProfileUpdate.this, "Profile Updated!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), UserProfile.class));
                                    finish();
                                }
                            });
                            Toast.makeText(UserProfileUpdate.this, "Profile Updated!", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UserProfileUpdate.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000) {
            if(resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();
                //profileImage.setImageURI(imageUri);
                
                uploadImageToFirebase(imageUri);
            }
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        //upload image to firebase storage
        StorageReference fileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profileImage);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(UserProfileUpdate.this, "Failed to Upload Image!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void discardProfile(View view) {
        Intent intent2 = new Intent(UserProfileUpdate.this, UserProfile.class);
        startActivity(intent2);
        finish();
    }

    public void backBtn3(View view) {
        Intent intent1 = new Intent(UserProfileUpdate.this, UserProfile.class);
        startActivity(intent1);
        finish();
    }
}