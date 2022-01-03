package com.example.myapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

@RequiresApi(api = Build.VERSION_CODES.O)
public class PaymentMethods extends AppCompatActivity {

    private Button cardPayment, cashPayment;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    LocalDateTime now = LocalDateTime.now();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_methods);


        cardPayment = findViewById(R.id.PayDCardbut);
        cashPayment = findViewById(R.id.PayDCashBut);



        cardPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),  com.example.myapp.CardPayment.class));
                finish();
            }
        });

        cashPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CashPaymentAdd();
            }
        });



    }

    public void backward(View view) {
        Intent backwardIntent = new Intent(PaymentMethods.this, Bill.class);
        startActivity(backwardIntent);
        finish();
    }

    public void CashPaymentAdd (){

        Map<String, Object> cashpayOb = new HashMap<>();
        cashpayOb.put("Date",dtf.format(now));
        cashpayOb.put("Type","Cash");
        cashpayOb.put("Amount", Bill.total);
        cashpayOb.put("UID",  com.example.myapp.ShippingDetails.UID);
        cashpayOb.put("PID", Bill.PID);


        db.collection("Payments").add(cashpayOb)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(PaymentMethods.this, "Cash on Delivery Selected", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),  com.example.myapp.OrderConfirmation.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PaymentMethods.this, "Payment Methods has Error ", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
