package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailed extends AppCompatActivity {

    TextView name,price,description;
    Button addToWishList;
    Button buyBtn;
    public static String Pname,Pprice,Pdescription;

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    product product = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detailed);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        final Object object = getIntent().getSerializableExtra("detail");
        if(object instanceof product){
            product = (product) object;
        }

        name = findViewById(R.id.pd_proName);
        price = findViewById(R.id.pd_proPrice);
        description = findViewById(R.id.pd_proDescription);
        buyBtn = findViewById(R.id.BuyBt);

        //product product = new product();
        if (product != null){
            name.setText(product.getProductName());
            price.setText(product.getProductPrice());
            description.setText(product.getProductDescription());
        }

        addToWishList = findViewById(R.id.pd_addWishListBtn);
        addToWishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addedToWish();
            }
        });

        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pname = product.getProductName();
                Pprice = product.getProductPrice();
                Pdescription = product.getProductDescription();

                startActivity(new Intent(getApplicationContext(),  com.example.myapp.ShippingDetails.class));
                finish();
            }
        });
    }

    private void addedToWish() {
        String saveCurrentDate, saveCurrentTime;
        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final HashMap<String,Object> wishListMap = new HashMap<>();

        wishListMap.put("Name",product.getProductName());
        wishListMap.put("Price",product.getProductPrice());
        wishListMap.put("Description",product.getProductDescription());
        wishListMap.put("currentDate",saveCurrentDate);
        wishListMap.put("currentTime",saveCurrentTime);

        fStore.collection("WishList").document(fAuth.getCurrentUser().getUid())
                .collection("CurrentUser").add(wishListMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentReference> task) {
                Toast.makeText(ProductDetailed.this, "Added To A WishList!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(ProductDetailed.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    public void backward(View view) {
        Intent intent1 = new Intent(ProductDetailed.this, Home.class);
        startActivity(intent1);
        finish();
    }
}