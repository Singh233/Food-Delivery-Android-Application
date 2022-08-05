package com.example.shoppingapplication.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.shoppingapplication.R;
import com.example.shoppingapplication.User.HomeActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminAddNewProductsActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private CardView mobilePhones, electronics, appliances, furniture, fashion, addCategory;
    private GradientDrawable gradient1, gradient2, gradient3, gradient4;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_products);

        gradient2 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xff4776E6, 0xff8E54E9});
        gradient1 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xfff857a6, 0xffff5858});
        gradient3 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xff1CD8D2, 0xff93EDC7});
        gradient4 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xff1D976C, 0xff93F9B9});

        bottomNavigationView = findViewById(R.id.admin_add_product_menu);
        bottomNavigationView.setSelectedItemId(R.id.admin_add);

        BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.admin_home:
                        startActivity(new Intent(getApplicationContext(), AdminCategoryActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.admin_add:
                        return true;
                    case R.id.admin_edit:
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.putExtra("Admin", "Admin");
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.admin_orders:
                        startActivity(new Intent(getApplicationContext(), AdminNewOrdersActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                }
                return false;
            }
        };
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);


        mobilePhones = findViewById(R.id.mobile_card);
        electronics = findViewById(R.id.electronics_card);
        appliances = findViewById(R.id.appliances_card);
        furniture = findViewById(R.id.furniture_card);
        fashion = findViewById(R.id.fashion_card);
        addCategory = findViewById(R.id.add_card);




        mobilePhones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminAddNewProductsActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "Mobile Phones");
                startActivity(intent);
            }
        });

        electronics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminAddNewProductsActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "Electronics");
                startActivity(intent);
            }
        });

        appliances.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminAddNewProductsActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "Appliances");
                startActivity(intent);
            }
        });

        furniture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminAddNewProductsActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "furniture");
                startActivity(intent);
            }
        });

        fashion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminAddNewProductsActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "Fashion");
                startActivity(intent);
            }
        });

        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdminAddNewProductsActivity.this, "Under Construction", Toast.LENGTH_SHORT).show();
            }
        });


    }
}