package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class WishListFragment extends Fragment {

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;

    RecyclerView recyclerView;
    WishListViewAdapter wishListViewAdapter;
    ArrayList<WishListModel> wishModelList;

    public WishListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_wish_list, container, false);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        recyclerView = root.findViewById(R.id.recyclerView1);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        wishModelList = new ArrayList<>();
        wishListViewAdapter = new WishListViewAdapter(getActivity(),wishModelList);
        recyclerView.setAdapter(wishListViewAdapter);

        fStore.collection("WishList").document(fAuth.getCurrentUser().getUid()).collection("CurrentUser").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){

                        String documentId = documentSnapshot.getId();
                        WishListModel wishModel = documentSnapshot.toObject(WishListModel.class);

                        wishModel.setDocumentId(documentId);

                        wishModelList.add(wishModel);
                        wishListViewAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        return root;
    }
}