package com.example.myapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class productViewAdapter extends RecyclerView.Adapter<productViewAdapter.MyViewHolder>{

    Context context;
    ArrayList<product> productArrayList;

    public productViewAdapter(Context context, ArrayList<product> productArrayList) {
        this.context = context;
        this.productArrayList = productArrayList;
    }


    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {

        product product = productArrayList.get(position);

        /*holder.prName.setText(product.productName);
        holder.prPrice.setText(product.productPrice);
        holder.prDescription.setText(product.productPrice);*/

        holder.prName.setText(productArrayList.get(position).getProductName());
        holder.prPrice.setText(productArrayList.get(position).getProductPrice());
        holder.prDescription.setText(productArrayList.get(position).getProductDescription());

        //productdetails
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ProductDetailed.class);
                intent.putExtra("detail", productArrayList.get(position));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {



        TextView prName,prPrice,prDescription;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            prName = itemView.findViewById(R.id.tvName);
            prPrice = itemView.findViewById(R.id.tvPrice);
            prDescription = itemView.findViewById(R.id.tvDescription);


        }

    }

}
