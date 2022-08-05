package com.example.shoppingapplication.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppingapplication.Model.Cart;
import com.example.shoppingapplication.Prevalent.Prevalent;
import com.example.shoppingapplication.R;
import com.example.shoppingapplication.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

public class CartActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    RecyclerView.LayoutManager layoutManager;
    private Button NextProcessButton;
    private TextView textTotalAmount, textMsg1;
    private String TotalPrice = "";
    private ImageView backButton;
    private ImageView productImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Paper.init(this);
        productImage = findViewById(R.id.cart_product_image);

        recyclerView = findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        NextProcessButton = findViewById(R.id.next_process_button);
        textTotalAmount = findViewById(R.id.total_price);
        textMsg1 = findViewById(R.id.msg1);


        NextProcessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textTotalAmount.setText("Total Price = " + String.valueOf(TotalPrice));

                Intent intent = new Intent(CartActivity.this, ConfirmFinalOrderActivity.class);
                intent.putExtra("Total Price", String.valueOf(TotalPrice));
                startActivity(intent);
                finish();
            }
        });

        backButton = findViewById(R.id.cart_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                startActivity(intent);

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference userCartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(userCartListRef.child("User view")
                                .child(Prevalent.currentOnlineUser.getPhone())
                                .child("Products"), Cart.class)
                .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {
                int oneTypeProductTPrice = ((Integer.parseInt(model.getPrice()))) * Integer.parseInt(model.getQuantity());

                holder.txtProductQuantity.setText("Quantity " + model.getQuantity());
                holder.txtProductPrice.setText("₹" + oneTypeProductTPrice);
                holder.txtProductName.setText(model.getPname());
                Picasso.get().load(model.getImage()).into(holder.imageView);

                TotalPrice = TotalPrice + oneTypeProductTPrice;

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CharSequence[] options = new CharSequence[] {
                            "Edit", "Remove"
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Cart Options:");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i == 0) {
                                    Intent intent = new Intent(CartActivity.this, ProductDetailsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                }
                                if (i == 1)  {
                                    userCartListRef.child("User view")
                                            .child(Prevalent.currentOnlineUser.getPhone())
                                            .child("Products")
                                            .child(model.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener() {
                                                @Override
                                                public void onComplete(@NonNull Task task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(CartActivity.this, "Item removed successfully.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                    userCartListRef.child("Admin view")
                                            .child(Prevalent.currentOnlineUser.getPhone())
                                            .child("Products")
                                            .child(model.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener() {
                                                @Override
                                                public void onComplete(@NonNull Task task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(CartActivity.this, "Item removed successfully.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                            }
                        });
                        builder.show();

                    }
                });

            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void CheckOrderState() {
        DatabaseReference ordersRef;
        ordersRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child("Orders")
                .child(Prevalent.currentOnlineUser.getPhone());

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String shippingState = snapshot.child("state").getValue().toString();
                    String userName = snapshot.child("name").getValue().toString();

                    if (shippingState.equals("shipped")) {
                        textTotalAmount.setText(("Dear " + userName + "\n Order is Shipped Successfully"));
                        recyclerView.setVisibility(View.GONE);
                        textMsg1.setVisibility(View.VISIBLE);
                        textMsg1.setText("Congratulations, your final order has been Shipped successfully.");
                        NextProcessButton.setVisibility(View.GONE);
                        Toast.makeText(CartActivity.this, "You can purchase more products, once your previous order is confirmed by Seller", Toast.LENGTH_SHORT).show();

                    } else if (shippingState.equals(("not shipped"))) {
                        textTotalAmount.setText(("Your package is not yet Shipped"));
                        recyclerView.setVisibility(View.GONE);
                        textMsg1.setVisibility(View.VISIBLE);
                        NextProcessButton.setVisibility(View.GONE);
                        Toast.makeText(CartActivity.this, "You can purchase more products, once your previous order is confirmed by Seller", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}