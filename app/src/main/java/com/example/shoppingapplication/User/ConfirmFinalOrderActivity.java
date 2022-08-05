package com.example.shoppingapplication.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.shoppingapplication.Prevalent.Prevalent;
import com.example.shoppingapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity {
    private EditText nameEditText, phoneEditText, addressEditText, cityEditText;
    private Button confirmOrderButton;
    private String totalAmount = "";
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        totalAmount = getIntent().getStringExtra("Total Price = ");

        confirmOrderButton = findViewById(R.id.confirm_final_order_button);
        nameEditText = findViewById(R.id.shipment_name_input);
        phoneEditText = findViewById(R.id.shipment_phone_number_input);
        addressEditText = findViewById(R.id.shipment_address_input);
        cityEditText = findViewById(R.id.shipment_city_input);

        confirmOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Check();
            }
        });

        backButton = findViewById(R.id.shipment_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfirmFinalOrderActivity.this, CartActivity.class);
                startActivity(intent);

            }
        });

    }

    private void Check() {
        if (TextUtils.isEmpty(nameEditText.getText().toString())) {
            Toast.makeText(this, "Please Provide your Full Name", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(phoneEditText.getText().toString())) {
            Toast.makeText(this, "Please Provide your Phone Number", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(addressEditText.getText().toString())) {
            Toast.makeText(this, "Please Provide your Address", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(cityEditText.getText().toString())) {
            Toast.makeText(this, "Please Enter your City Name", Toast.LENGTH_SHORT).show();
        } else {
            ConfirmOrder();
        }

    }

    private void ConfirmOrder() {
        final String saveCurrentTime, saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format((calForDate.getTime()));

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentDate.format((calForDate.getTime()));

        final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(Prevalent.currentOnlineUser.getPhone());


        HashMap<String, Object> ordersMap = new HashMap<>();
        ordersMap.put("totalAmount", totalAmount);
        ordersMap.put("name", nameEditText.getText().toString());
        ordersMap.put("phone", phoneEditText.getText().toString());
        ordersMap.put("address", addressEditText.getText().toString());
        ordersMap.put("city", cityEditText.getText().toString());
        ordersMap.put("date", saveCurrentDate);
        ordersMap.put("time", saveCurrentTime);
        ordersMap.put("state", "not shipped");


        ordersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {

                if (task.isSuccessful()) {
                    FirebaseDatabase.getInstance().getReference().child("Cart List")
                            .child("User view")
                            .child(Prevalent.currentOnlineUser.getPhone())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful()) {
                                     //   Toast.makeText(ConfirmFinalOrderActivity.this, "Your Order has been Placed Successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(ConfirmFinalOrderActivity.this, PaymentOptionActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }
            }
        });

    }
}