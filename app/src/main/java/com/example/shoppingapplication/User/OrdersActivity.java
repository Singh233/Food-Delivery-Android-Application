package com.example.shoppingapplication.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppingapplication.Admin.AdminNewOrdersActivity;
import com.example.shoppingapplication.Admin.AdminUserProductsActivity;
import com.example.shoppingapplication.Interface.ItemClickListener;
import com.example.shoppingapplication.Model.AdminOrders;
import com.example.shoppingapplication.Model.Cart;
import com.example.shoppingapplication.Model.UserOrders;
import com.example.shoppingapplication.Model.Users;
import com.example.shoppingapplication.Prevalent.Prevalent;
import com.example.shoppingapplication.R;
import com.example.shoppingapplication.ViewHolder.CartViewHolder;
import com.example.shoppingapplication.ViewHolder.OrdersViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrdersActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private String TotalPrice = "";
    RecyclerView.LayoutManager layoutManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);


        recyclerView = findViewById(R.id.order_cart_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);



    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference()
                .child("Cart List");

        FirebaseRecyclerOptions<UserOrders> options = new FirebaseRecyclerOptions.Builder<UserOrders>()
                .setQuery(ordersRef.child("Admin view")
                        .child(Prevalent.currentOnlineUser.getPhone())
                        .child("Products"), UserOrders.class)
                .build();

        FirebaseRecyclerAdapter<UserOrders, OrdersViewHolder> adapter = new FirebaseRecyclerAdapter<UserOrders, OrdersViewHolder>(options) {
            @NonNull
            @Override
            public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_items_layout, parent, false);
                OrdersViewHolder holder = new OrdersViewHolder(view);
                return holder;
            }

            @Override
            protected void onBindViewHolder(@NonNull OrdersViewHolder holder, int position, @NonNull UserOrders model) {

                holder.orderProductName.setText("Quantity = " + model.getQuantity());
                holder.orderProductPrice.setText("Price = " + model.getPrice());
                holder.orderProductQuantity.setText(model.getPname());
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();;

    }
}

