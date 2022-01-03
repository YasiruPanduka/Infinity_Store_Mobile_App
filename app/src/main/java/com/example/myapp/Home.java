package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class Home extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<product> productArrayList;
    productViewAdapter productAdapter;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    FirebaseUser user;
    String userMail;
    String userId;
    ProgressDialog progressDialog;
    Button addProduct, wishlistBtn;
    product product;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        addProduct = findViewById(R.id.Buy_page);
        wishlistBtn = findViewById(R.id.home_Buy);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Dta...");
        progressDialog.show();


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        FirebaseUser mFirebaseUser = fAuth.getCurrentUser();
        if(mFirebaseUser != null) {
            userId = mFirebaseUser.getUid();
        }

        user = fAuth.getCurrentUser();
        if(user != null) {
            userMail = user.getEmail();
        }

        if(userMail != null) {
            if(userMail.equals("22panduka@gmail.com")){
                addProduct.setVisibility(View.VISIBLE);

                addProduct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(Home.this, Add_Products.class));
                    }
                });
            }else{
                addProduct.setVisibility(View.GONE);
            }
        }


        productArrayList = new ArrayList<product>();
        productAdapter = new productViewAdapter(Home.this, productArrayList);

        recyclerView.setAdapter(productAdapter);

        EventChangeListener();


    }


    private void EventChangeListener() {

        fStore.collection("products").orderBy("productName", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {

                if(error != null){

                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }

                    Log.e("DataBase Error!", error.getMessage());
                    return;
                }

                for(DocumentChange dc : value.getDocumentChanges()) {
                    if(dc.getType() == DocumentChange.Type.ADDED){

                        productArrayList.add(dc.getDocument().toObject(product.class));

                    }

                    productAdapter.notifyDataSetChanged();

                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                }

            }
        });
    }






    public void profile(View view) {
        Intent intent1 = new Intent(Home.this, UserProfile.class);
        startActivity(intent1);
    }

   /* public void addProduct(View view) {
        Intent intent2 = new Intent(Home.this, Add_Products.class);
        startActivity(intent2);
    }*/
}