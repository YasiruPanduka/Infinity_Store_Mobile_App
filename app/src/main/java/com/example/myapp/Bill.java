package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Bill extends AppCompatActivity {

    public static double total, subTotal, shipCost = 350.00; //shipping_details_confirm.shipCost;
    public static String PID = "5qZWcm2sN79XimRg6Du3";
    private String qt, pPrice1, subT;
    private int Pqt = 2, price ;
    private double pPrice ;
    private Button cancelBt, chekoutBt;
    private TextView SubTagV, ShipCostTagV,TotalTagV, prodTitle, priceTag, ptotal;
    public static final String TAG = "TAG";

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        SubTagV = findViewById(R.id.SubTag);
        ShipCostTagV = findViewById(R.id.ShipCostTag);
        TotalTagV = findViewById(R.id.TotalTag);
        cancelBt = findViewById(R.id.BillCancelBt);
        chekoutBt = findViewById(R.id.BillChekoutBt);
        prodTitle = findViewById(R.id.productTitle);
        priceTag = findViewById(R.id.qutText);
        ptotal = findViewById(R.id.rsTag);

        loadProductDetails();

        chekoutBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), com.example.myapp.PaymentMethods.class));
                finish();
            }
        });

        cancelBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CancelOrder();

            }
        });




    }


    public  void CalBill(){
        price = Integer.parseInt(pPrice1);
        subTotal = price * Pqt ;
        total = shipCost + subTotal;
        subT = Double.toString(subTotal);

        SubTagV.setText("Rs." + String.valueOf(Double.toString(subTotal)));
        ShipCostTagV.setText("Rs." + String.valueOf(shipCost));
        TotalTagV.setText("Rs." + String.valueOf(total));

    }


    public void loadProductDetails() {

        prodTitle.setText(ProductDetailed.Pname);
        pPrice1 = ProductDetailed.Pprice;

                    qt = Integer.toString(Pqt);
                    Log.d(TAG, "Qt - :" + qt);

                    priceTag.setText("Rs." + pPrice1 + ".00" + " X " + qt );
                    CalBill();
                    ptotal.setText("Rs." + subT);
                    Log.d(TAG, "Qt - :" + qt + priceTag);
    }


    public void CancelOrder(){

            FirebaseFirestore.getInstance().collection("ShippingDetails")
                    .document( com.example.myapp.ShippingDetails.DocID)
                    .delete()

                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(Bill.this, "Order Cancelled", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),  com.example.myapp.ShippingDetails.class));
                            finish();

                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Bill.this, "Something went wrong Try Again", Toast.LENGTH_SHORT).show();
                        }
                    });
    }

}