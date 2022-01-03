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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class ShippingDetails extends AppCompatActivity {


    public static final String TAG = "TAG";
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    //Get Inputs
    private EditText shipDName, shipDMnumber, shipDHouseNo, shipDAdressL1, shipDAddressL2, shipDCity;
    private Spinner  shipDProvince;
    private Button shipDChekoutBt;
    private int lenght;
    private String shipDtName, shipDtMnumber, shipDtHouseNo, shipDtAdressL1, shipDtAddressL2, shipDtCity, shipDtProvince;
    public static String DocID , UID = "NdWegkj6ICWF7JVeFOo3TE7CUgB3";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_details);


        //Province Drop Down Array
        Spinner ShipD = (Spinner) findViewById(R.id.PaymenttypeDropDown);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(ShippingDetails.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Province));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ShipD.setAdapter(myAdapter);




        //Get Inputs
        shipDName = findViewById(R.id.ShipDNameField);
        shipDMnumber = findViewById(R.id.shipDMobileField);
        shipDHouseNo = findViewById(R.id.shipDHouseField);
        shipDAdressL1 = findViewById(R.id.shipDAddressL1Field);
        shipDAddressL2 = findViewById(R.id.shipDAddressL2Field);
        shipDCity = findViewById(R.id.shipDCityField);
        shipDProvince = findViewById(R.id.PaymenttypeDropDown);
        shipDChekoutBt = findViewById(R.id.ShipDChekoutBt);




        shipDChekoutBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                shipDtName = shipDName.getText().toString().trim();
                shipDtMnumber = shipDMnumber.getText().toString().trim();
                shipDtHouseNo = shipDHouseNo.getText().toString().trim();
                shipDtAdressL1 = shipDAdressL1.getText().toString().trim();
                shipDtAddressL2 = shipDAddressL2.getText().toString().trim();
                shipDtCity = shipDCity.getText().toString().trim();
                shipDtProvince = shipDProvince.getSelectedItem().toString().trim();
                lenght = shipDtMnumber.length();

                if (shipDtName.isEmpty() || shipDtMnumber.isEmpty() || shipDtHouseNo.isEmpty() || shipDtAdressL1.isEmpty())
                {
                    Toast.makeText(ShippingDetails.this,"Pleas fill the all field",Toast.LENGTH_SHORT).show();
                }
                else if ( lenght < 10 || lenght > 10 ){
                    Toast.makeText(ShippingDetails.this,"Mobile Number Must be in 10 digits",Toast.LENGTH_SHORT).show();

                }
                else {

                    insertShippingDetails();
                }

            }
        });
    }



    private void insertShippingDetails(){

        Map<String, Object> ShippingDetailsOb = new HashMap<>();
            ShippingDetailsOb.put("User ID",UID);
            ShippingDetailsOb.put("Cart ID","99999999999");
            ShippingDetailsOb.put("Name", shipDName.getText().toString().trim());
            ShippingDetailsOb.put("MNumber", shipDMnumber.getText().toString().trim());
            ShippingDetailsOb.put("HouseNo", shipDHouseNo.getText().toString().trim());
            ShippingDetailsOb.put("AdressL1", shipDAdressL1.getText().toString().trim());
            ShippingDetailsOb.put("AdressL2", shipDAddressL2.getText().toString().trim());
            ShippingDetailsOb.put("City", shipDCity.getText().toString().trim());
            ShippingDetailsOb.put("Province", shipDProvince.getSelectedItem().toString().trim());

        db.collection("ShippingDetails").add(ShippingDetailsOb)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(ShippingDetails.this, "Successfully Details Added", Toast.LENGTH_SHORT).show();
                        DocID = documentReference.getId();

                        startActivity(new Intent(getApplicationContext(),shipping_details_confirm.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ShippingDetails.this, "Error Adding Details", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void backBtnShippingDetails(View view) {
        Intent intent1 = new Intent(ShippingDetails.this, Home.class);
        startActivity(intent1);
        finish();
    }
}