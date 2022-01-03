package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ShipDEdit extends AppCompatActivity {

    private EditText ShipDEditName, ShipDEditMobile, shipDEditHouseNo, shipDEditAddressL1, shipDEditAddressL2, shipDEditCity;
    private Spinner ShipDEditProvince;
    private Button UpdateBt;
    private String ShipDtEditName, ShipDtEditMobile, shipDtEditHouseNo, shipDtEditAddressL1, shipDtEditAddressL2, shipDtEditCity, ShipDtEditProvince;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ship_dedit);

        Spinner ShipDEdit = (Spinner) findViewById(R.id.PaymenttypeDropDown);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(ShipDEdit.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Province));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ShipDEdit.setAdapter(myAdapter);


        ShipDEditName = findViewById(R.id.ShipDEditNameField);
        ShipDEditMobile = findViewById(R.id.ShipDEditMobileField);
        shipDEditHouseNo = findViewById(R.id.shipDEditHouseNoField);
        shipDEditAddressL1 = findViewById(R.id.shipDEditAddressL1Field);
        shipDEditAddressL2 = findViewById(R.id.shipDEditAddressL2Field);
        shipDEditCity = findViewById(R.id.shipDEditCityField);
        ShipDEditProvince = findViewById(R.id.PaymenttypeDropDown);
        UpdateBt = findViewById(R.id.ShipDEditConfirmBut);

        loadShippingDetails();

        UpdateBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ShipDtEditName = ShipDEditName.getText().toString().trim();
                ShipDtEditMobile = ShipDEditMobile.getText().toString().trim();
                shipDtEditHouseNo = shipDEditHouseNo.getText().toString().trim();
                shipDtEditAddressL1 = shipDEditAddressL1.getText().toString().trim();
                shipDtEditAddressL2 = shipDEditAddressL2.getText().toString().trim();
                shipDtEditCity = shipDEditCity.getText().toString().trim();
                ShipDtEditProvince = ShipDEditProvince.getSelectedItem().toString().trim();

                startActivity(new Intent(getApplicationContext(),shipping_details_confirm.class));
                finish();

                updateShipDetails();


            }
        });


    }

    public void backward(View view) {
        Intent backwardIntent = new Intent(ShipDEdit.this, shipping_details_confirm.class);
        startActivity(backwardIntent);
        finish();
    }

    private void updateShipDetails(){
        DocumentReference docRef = db
                .collection("ShippingDetails")
                .document(ShippingDetails.DocID);

        Map<String, Object> ShipUpdate = new HashMap<>();
            ShipUpdate.put("Name",ShipDtEditName);
            ShipUpdate.put("MNumber", ShipDtEditMobile);
            ShipUpdate.put("HouseNo", shipDtEditHouseNo);
            ShipUpdate.put("AdressL1", shipDtEditAddressL1);
            ShipUpdate.put("AdressL2", shipDtEditAddressL2);
            ShipUpdate.put("City", shipDtEditCity);
            ShipUpdate.put("Province", ShipDtEditProvince);

        docRef.update(ShipUpdate)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ShipDEdit.this, "Details Successfully Updated ", Toast.LENGTH_SHORT).show();
                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ShipDEdit.this, "Details Uppdate Failed Try Again", Toast.LENGTH_SHORT).show();

                    }
                });

    }


    private void loadShippingDetails() {

        DocumentReference documentReference = db.collection("ShippingDetails").document(ShippingDetails.DocID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                if (documentSnapshot != null) {
                    ShipDEditName.setText(documentSnapshot.getString("Name"));
                    ShipDEditMobile.setText(documentSnapshot.getString("MNumber"));
                    shipDEditHouseNo.setText(documentSnapshot.getString("HouseNo"));
                    shipDEditAddressL1.setText(documentSnapshot.getString("AdressL1"));
                    shipDEditAddressL2.setText(documentSnapshot.getString("AdressL2"));
                    shipDEditCity.setText(documentSnapshot.getString("City"));

                }

            }
        });

    }

}