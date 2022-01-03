package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class UserProfile extends AppCompatActivity {

    TextView fullName1,fullName2,email,birth,phone,verifyMsg;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    Button resendCode,editBtn;
    Button resetPassLocal;
    FirebaseUser user;
    ImageView profileImageView;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        phone = findViewById(R.id.profilePhone);
        fullName1 = findViewById(R.id.profileName);
        fullName2 = findViewById(R.id.name_id);
        email = findViewById(R.id.profileEmail);
        birth = findViewById(R.id.profileBirth);
        resetPassLocal = findViewById(R.id.resetPasswordLocal);
        editBtn = findViewById(R.id.edit_btn1);
        profileImageView = findViewById(R.id.pro_img1);
        resendCode = findViewById(R.id.resendCode);
        verifyMsg = findViewById(R.id.verifyMsg);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        FirebaseUser mFirebaseUser = fAuth.getCurrentUser();
        if(mFirebaseUser != null) {
            userId = mFirebaseUser.getUid();
        }

        user = fAuth.getCurrentUser();


        StorageReference profileRef = storageReference.child("users/"+userId+"profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImageView);
            }
        });

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(UserProfile.this, "Profile Image Clicked!", Toast.LENGTH_SHORT).show();
            }
        });



        if(user != null){
            if(!user.isEmailVerified()) {
                verifyMsg.setVisibility(View.VISIBLE);
                resendCode.setVisibility(View.VISIBLE);

                resendCode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(view.getContext(), "Verification Email Has been Sent!", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("TAG", "onFailure: Email not sent! " + e.getMessage());
                            }
                        });
                    }
                });
            }
        }


        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot != null) {
                    phone.setText(documentSnapshot.getString("phone"));
                    fullName1.setText(documentSnapshot.getString("fName"));
                    fullName2.setText(documentSnapshot.getString("fName"));
                    email.setText(documentSnapshot.getString("email"));
                    birth.setText(documentSnapshot.getString("birth"));
                }

            }
        });
        
        resetPassLocal.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText resetPassword = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle(("Reset Password?"));
                passwordResetDialog.setMessage("Enter New Password. Minimum 6 characters needed.");
                passwordResetDialog.setView(resetPassword);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //extract the email and send reset link
                        String newPassword = resetPassword.getText().toString();
                        user.updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(UserProfile.this, "Password Reset SuccessFully.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Toast.makeText(UserProfile.this, "Password Reset Failed.", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Close the Dialog
                    }
                });

                passwordResetDialog.create().show();
            }
        });

        //send data to edit profile page using edit button onClick
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), UserProfileUpdate.class);
                i.putExtra("fullName",fullName1.getText().toString());
                i.putExtra("email",email.getText().toString());
                i.putExtra("birth",birth.getText().toString());
                i.putExtra("phone",phone.getText().toString());
                startActivity(i);
            }
        });

    }

    public void logout1(View view) {
        FirebaseAuth.getInstance().signOut(); //This is the method for sign-out which is called from FirebaseAuth
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }

    public void backBtn2(View view) {
        Intent intent2 = new Intent(UserProfile.this, Home.class);
        startActivity(intent2);
        finish();
    }

    public void wishListPr(View view){
        Intent intent3 = new Intent(UserProfile.this, WishList.class);
        startActivity(intent3);
    }

    public void AboutUs(View view) {
        Intent intent4 = new Intent(UserProfile.this, AboutUS.class);
        startActivity(intent4);
    }
}