package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class WishList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);

        getSupportFragmentManager().beginTransaction().add(R.id.frameLayoutWish,new WishListFragment()).commit();
    }

    public void wishBackBtn(View view) {
        Intent intent1 = new Intent(WishList.this, UserProfile.class);
        startActivity(intent1);
        finish();
    }
}