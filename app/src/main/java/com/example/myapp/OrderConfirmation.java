package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class OrderConfirmation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);
    }

    public void orderplacedreturn(View view) {
        Intent intent1 = new Intent(OrderConfirmation.this,Home.class);
        startActivity(intent1);
        finish();
    }
}