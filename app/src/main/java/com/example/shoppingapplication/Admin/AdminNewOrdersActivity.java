package com.example.shoppingapplication.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.shoppingapplication.Model.AdminOrders;
import com.example.shoppingapplication.R;
import com.example.shoppingapplication.User.HomeActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminNewOrdersActivity extends AppCompatActivity {
    private RecyclerView ordersList;
    private DatabaseReference ordersRef;
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);

        bottomNavigationView = findViewById(R.id.admin_check_order_menu);
        bottomNavigationView.setSelectedItemId(R.id.admin_orders);

        BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.admin_home:
                        startActivity(new Intent(getApplicationContext(), AdminCategoryActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.admin_add:
                        startActivity(new Intent(getApplicationContext(), AdminAddNewProductsActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.admin_edit:
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.putExtra("Admin", "Admin");
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.admin_orders:

                        return true;

                }
                return false;
            }
        };

        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        ordersRef = FirebaseDatabase.getInstance()
                .getReference()
                .child("Orders");

        ordersList = findViewById(R.id.orders_list);
        ordersList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<AdminOrders> options = new FirebaseRecyclerOptions.Builder<AdminOrders>()
                .setQuery(ordersRef, AdminOrders.class)
                .build();
        FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder> adapter = new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder>(options) {
            @NonNull
            @Override
            public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout, parent, false);
                return new AdminOrdersViewHolder((view));
            }

            @Override
            protected void onBindViewHolder(@NonNull AdminOrdersViewHolder holder, int position, @NonNull AdminOrders model) {

                holder.userName.setText("Name: " + model.getName());
                holder.userPhoneNumber.setText("Phone: " + model.getPhone());
                holder.userTotalPrice.setText("Total Amount: " + model.getTotalAmount());
                holder.userDateTime.setText("Order at: " + model.getDate() + " " + model.getTime());
                holder.userShippingAddress.setText("Address: " + model.getAddress());
                holder.userCity.setText("City: " + model.getCity());

                holder.ShowOrderButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String uID = getRef(position).getKey();

                        Intent intent = new Intent(AdminNewOrdersActivity.this, AdminUserProductsActivity.class);
                        intent.putExtra("uID", uID);
                        startActivity(intent);
                    }
                });

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        CharSequence[] options = new CharSequence[] {
                                "Yes",
                                "No"
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminNewOrdersActivity.this);
                        builder.setTitle("Have you shipped this order products ?");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                if (i == 0) {
                                    String uID = getRef(position).getKey();
                                    RemoveOrder(uID);

                                } else {
                                    finish();
                                }
                            }
                        });
                        builder.show();
                    }
                });


            }
        };
        ordersList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class AdminOrdersViewHolder extends RecyclerView.ViewHolder {
        public TextView userName, userPhoneNumber, userTotalPrice, userDateTime, userShippingAddress, userCity;
        public Button ShowOrderButton;

        public AdminOrdersViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.order_user_name);
            userPhoneNumber = itemView.findViewById(R.id.order_phone_number);
            userTotalPrice = itemView.findViewById(R.id.order_total_price);
            userDateTime = itemView.findViewById(R.id.order_date_time);
            userShippingAddress = itemView.findViewById(R.id.order_address);
            userCity = itemView.findViewById(R.id.order_city);
            ShowOrderButton = itemView.findViewById(R.id.show_all_products);


        }
    }

    private void RemoveOrder(String uID) {
        ordersRef.child(uID).removeValue();
    }
}