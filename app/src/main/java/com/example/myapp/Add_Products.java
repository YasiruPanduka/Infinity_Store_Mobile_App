package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class Add_Products extends AppCompatActivity {

    EditText prodName,prodPrice,prodDescription;
    Button addBtn;
    FirebaseFirestore fStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_products);

        prodName = findViewById(R.id.pName);
        prodPrice = findViewById(R.id.pPrice);
        prodDescription = findViewById(R.id.pDescription);
        addBtn = findViewById(R.id.addBtn);

        fStore = FirebaseFirestore.getInstance();


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference docRef = fStore.collection("products").document("infinity");

                String pName = prodName.getText().toString().trim();
                String pPrice = prodPrice.getText().toString().trim();
                String pDescription = prodDescription.getText().toString().trim();

                if(TextUtils.isEmpty(pName)){
                    prodName.setError("Product Name is Required!");
                    return;
                }
                if(TextUtils.isEmpty(pPrice)){
                    prodPrice.setError("Product Price is Required!");
                    return;
                }
                if(TextUtils.isEmpty(pDescription)){
                    prodDescription.setError("Product Description is Required!");
                    return;
                }

                Map<String,Object> prod = new HashMap<>();
                prod.put("productName",prodName.getText().toString());
                prod.put("productPrice",prodPrice.getText().toString());
                prod.put("productDescription",prodDescription.getText().toString());
                fStore.collection("products").add(prod).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(Add_Products.this, "Product Added!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),Add_Products.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(Add_Products.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });
    }

    public void backward(View view) {
        Intent intent1 = new Intent(Add_Products.this, Home.class);
        startActivity(intent1);
        finish();
    }
}