package com.example.myapp;

import android.content.Intent;
import android.os.Build;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

@RequiresApi(api = Build.VERSION_CODES.O)
public class CardPayment extends AppCompatActivity {

    private EditText PayCardName, payCardNumber, payCardCVV, cardDate;
    private Spinner PaymenttypeDropDown;
    private Button confirmBt ;
    private String CardName, CardNumber, cardCvv, cardDate1, cardType;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    LocalDateTime now = LocalDateTime.now();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_payment);

        Spinner CardType = (Spinner) findViewById(R.id.PaymenttypeDropDown);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(CardPayment.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.CardType));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CardType.setAdapter(myAdapter);


        PayCardName = findViewById(R.id.PayCardNameField);
        payCardNumber = findViewById(R.id.payCardNumberField);
        payCardCVV = findViewById(R.id.payCardCVVField);
        cardDate = findViewById(R.id.cardDate);
        PaymenttypeDropDown = findViewById(R.id.PaymenttypeDropDown);
        confirmBt = findViewById(R.id.CardPayConfirmBut);

        confirmBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CardName = PayCardName.getText().toString().trim();
                CardNumber = PayCardName.getText().toString().trim();
                cardCvv = PayCardName.getText().toString().trim();
                cardDate1 = cardDate.getText().toString().trim();
                cardType = PaymenttypeDropDown.getSelectedItem().toString();

                if (CardName.isEmpty() || CardNumber.isEmpty() || cardCvv.isEmpty() || cardDate1.isEmpty())
                {
                    Toast.makeText(CardPayment.this,"Pleas fill the all field",Toast.LENGTH_SHORT).show();
                }
                else {
                    CardPaymentAdd();
                }
            }
        });


    }

    public void backward(View view) {
        Intent backwardIntent = new Intent(CardPayment.this, PaymentMethods.class);
        startActivity(backwardIntent);
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)

    public void CardPaymentAdd (){

        Map<String, Object> cardpayOb = new HashMap<>();
            cardpayOb.put("Date",dtf.format(now));
            cardpayOb.put("Type","Card");
            cardpayOb.put("Amount", Bill.total);
            cardpayOb.put("UID", ShippingDetails.UID);
            cardpayOb.put("PID", Bill.PID);
            cardpayOb.put("Card No", CardNumber);
            cardpayOb.put("Owner Name", CardName);
            cardpayOb.put("Card Type", cardType);





        db.collection("Payments").add(cardpayOb)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(CardPayment.this, "Payment Successfully  Added", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),  com.example.myapp.OrderConfirmation.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CardPayment.this, "Payment has error", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}