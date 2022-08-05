package com.example.shoppingapplication.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.shoppingapplication.Adapter.HomeAdapter.OfferAdapter;
import com.example.shoppingapplication.Adapter.HomeAdapter.OffersHelperClass;
import com.example.shoppingapplication.Counter;
import com.example.shoppingapplication.Model.Products;
import com.example.shoppingapplication.Model.UserOrders;
import com.example.shoppingapplication.Prevalent.Prevalent;
import com.example.shoppingapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class ProductDetailsActivity extends AppCompatActivity {
    private Button addToCartButton;
    private ImageView productImage;
    private ElegantNumberButton numberButton;
    private TextView productPrice, productDescription, productName;
    private String productID = "", state = "Normal", categoryName = "", imageId = "";
    Counter counter;
    private TextView cartCounter;
    private RecyclerView offersRecycler;
    RecyclerView.Adapter adapter;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        offersRecycler = findViewById(R.id.offers_recycler);




        offersRecycler();

        productID = getIntent().getStringExtra("pid");
        imageId = getIntent().getStringExtra("image");
        categoryName = getIntent().getStringExtra("category");
        offersRecycler.setNestedScrollingEnabled(false);



        cartCounter = findViewById(R.id.cart_counter_number);

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference()
                .child("Cart List")
                .child("User view")
                .child(Prevalent.currentOnlineUser.getPhone())
                .child("Products");



        cartListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String quantity = snapshot.getValue(UserOrders.class).toString();
                    cartCounter.setText(quantity);
                } else {
                    cartCounter.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        addToCartButton = findViewById(R.id.pd_add_to_cart_button);
        counter = new Counter(findViewById(R.id.cart_counter_number));

        numberButton = findViewById(R.id.number_btn);
        productImage = findViewById(R.id.product_image_details);
        productPrice = findViewById(R.id.product_price_details);
        productDescription = findViewById(R.id.product_description_details);
        productName = findViewById(R.id.product_name_details);

        getProductDetails(productID);


        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state.equals("Order Placed") || state.equals("Order Shipped")) {
                    Toast.makeText(ProductDetailsActivity.this, "Please Wait", Toast.LENGTH_SHORT).show();
                } else {
                    addingToCartList();
                }
            }
        });
    }

    private void offersRecycler() {

        ArrayList<OffersHelperClass> OffersHelperClass = new ArrayList<>();
        OffersHelperClass.add(new OffersHelperClass(R.drawable.sbi));
        OffersHelperClass.add(new OffersHelperClass(R.drawable.phonepe));
        OffersHelperClass.add(new OffersHelperClass(R.drawable.paytm));
        OffersHelperClass.add(new OffersHelperClass(R.drawable.gpay));
        OffersHelperClass.add(new OffersHelperClass(R.drawable.fampay));


        offersRecycler.setHasFixedSize(true);
        adapter = new OfferAdapter(OffersHelperClass);
        offersRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        offersRecycler.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        CheckOrderState();
    }

    private void addingToCartList() {
        String saveCurrentTime, saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format((calForDate.getTime()));

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentDate.format((calForDate.getTime()));

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid", productID);
        cartMap.put("category", categoryName);
        cartMap.put("image", imageId);
        cartMap.put("pname", productName.getText().toString());
        cartMap.put("price", productPrice.getText().toString());
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);
        cartMap.put("quantity", numberButton.getNumber());
        cartMap.put("discount", "");

        cartListRef.child("User view")
                .child(Prevalent.currentOnlineUser.getPhone())
                .child("Products")
                .child(productID)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            cartListRef.child("Admin view").child(Prevalent.currentOnlineUser.getPhone())
                                    .child("Products").child(productID)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(ProductDetailsActivity.this, "Added to Cart", Toast.LENGTH_SHORT).show();

                                                Intent intent = new Intent(ProductDetailsActivity.this, HomeActivity.class);
                                                startActivity(intent);
                                            }
                                        }
                                    });
                        }
                    }
                });


    }

    private void getProductDetails(String productID) {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Products");
        productRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Products products = dataSnapshot.getValue(Products.class);

                    productName.setText(products.getPname());
                    productPrice.setText(products.getPrice());
                    productDescription.setText(products.getDescription());
                    Picasso.get().load(products.getImage()).into(productImage);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void CheckOrderState() {
        DatabaseReference ordersRef;
        ordersRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(Prevalent.currentOnlineUser.getPhone());

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String shippingState = snapshot.child("state").getValue().toString();

                    if (shippingState.equals("shipped")) {
                        state = "Order Shipped";

                    } else if (shippingState.equals(("not shipped"))) {
                        state = "Order Shipped";

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);

        menu.findItem(R.id.cart_fab).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(ProductDetailsActivity.this, CartActivity.class);
                startActivity(intent);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

//        if (id == R.id.action_settings)
//      {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

}