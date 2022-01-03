package com.example.myapp;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class shipping_details_confirm extends AppCompatActivity {


    private TextView shipDConName, shipDConMnumber, shipDConHouseNo, shipDConAdressL1, shipDConAddressL2, shipDConCity, shipDConProvince, rsTag;
    private Button editBt, confirmBt;
    private  String Province;
    public static double shipCost, shipCost2 ;

    public static final String TAG = "TAG";
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_details_confirm);

        shipDConName = findViewById(R.id.ShipDConNameView);
        shipDConMnumber = findViewById(R.id.shipDConMobileView);
        shipDConHouseNo = findViewById(R.id.shipDConHouseNoView);
        shipDConAdressL1 = findViewById(R.id.shipDConLine1View);
        shipDConAddressL2 = findViewById(R.id.shipDConLine2View);
        shipDConCity = findViewById(R.id.shipDConCityView);
        shipDConProvince = findViewById(R.id.shipDConProvinceView);
        rsTag = findViewById(R.id.shipDConcostView);
        editBt = findViewById(R.id.shipDEditBut);
        confirmBt = findViewById(R.id.ShipDEditConfirmBut);

        loadShippingDetails();

        editBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),  com.example.myapp.ShipDEdit.class));
                finish();
            }
        });

        confirmBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Bill.class));
                finish();
            }
        });

    }

    public void backward(View view) {
        Intent backwardIntent = new Intent(shipping_details_confirm.this,  com.example.myapp.ShippingDetails.class);
        startActivity(backwardIntent);
        finish();
    }


    public void loadShippingDetails() {

        DocumentReference documentReference = db.collection("ShippingDetails").document( com.example.myapp.ShippingDetails.DocID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                if (documentSnapshot != null) {
                    shipDConName.setText(documentSnapshot.getString("Name"));
                    shipDConMnumber.setText(documentSnapshot.getString("MNumber"));
                    shipDConHouseNo.setText(documentSnapshot.getString("HouseNo"));
                    shipDConAdressL1.setText(documentSnapshot.getString("AdressL1"));
                    shipDConAddressL2.setText(documentSnapshot.getString("AdressL2"));
                    shipDConCity.setText(documentSnapshot.getString("City"));
                    shipDConProvince.setText(documentSnapshot.getString("Province"));

                    Province = shipDConProvince.getText().toString();
                    shipCost2 = ShipCostCal();
                    Log.d(TAG, "Province - :" + Province);
                    String rsTag2 = String.valueOf(shipCost2);
                    rsTag.setText("Rs."+ rsTag2 + "0");

                }

            }
        });
    }


    private double ShipCostCal() {

        if(Province == "Central Province"){
            shipCost = 300.00;
        }
        else if(Province == "Eastern Province"){
            shipCost = 300.00;
        }
        else if(Province == "North Central Province"){
            shipCost = 330.00;
        }
        else if(Province == "Northern Province"){
            shipCost = 350.00;
        }
        else if(Province == "North Western Province"){
            shipCost = 290.00;
        }
        else if(Province == "Sabaragamuwa Provincee"){
            shipCost = 245.00;
        }
        else if(Province == "Sothern Province"){
            shipCost = 220.00;
        }
        else if(Province == "Uva Province"){
            shipCost = 300.00;
        }
        else if(Province == "Western Province"){
            shipCost = 250.00;
        }
        else {
            shipCost = 358.00;
        }

        return shipCost;
    }
}