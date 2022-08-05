package com.example.shoppingapplication;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.shoppingapplication.Model.Cart;
import com.example.shoppingapplication.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Counter {
    private TextView cartCounter;
    private final int MAX_NUMBER = 99;
    private String quantity = "";
    private String pid = "";



    final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference()
            .child("Cart List")
            .child("User view")
            .child(Prevalent.currentOnlineUser.getPhone())
            .child("Products")
            .child(pid);

    public Counter(View view) {

        cartCounter = view.findViewById(R.id.cart_counter_number);

    }

   /* public void increaseNumber() {

        cartListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    quantity = snapshot.getValue(String.class).toString();
                    if (quantity.equals("")) {
                        cartCounter.setText(quantity);
                    } else {
                        cartCounter.setText("0");

                    }
                } else {
                    cartCounter.setText("0");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    */




}

