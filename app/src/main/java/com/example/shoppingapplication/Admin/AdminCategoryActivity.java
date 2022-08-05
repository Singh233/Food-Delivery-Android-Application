package com.example.shoppingapplication.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.shoppingapplication.User.HomeActivity;
import com.example.shoppingapplication.MainActivity;
import com.example.shoppingapplication.R;
import com.example.shoppingapplication.User.ProductDetailsActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminCategoryActivity extends AppCompatActivity {

    Button AdminLogoutButton;
    BottomNavigationView bottomNavigationView;

    private final int ID_HOME = 1;
    private final int ID_MESSAGE = 2;
    private final int ID_NOTIFICATION = 3;
    private final int ID_ACCOUNT = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        bottomNavigationView = findViewById(R.id.admin_bottom_menu);
        bottomNavigationView.setSelectedItemId(R.id.admin_home);

        BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.admin_home:
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
                        startActivity(new Intent(getApplicationContext(), AdminNewOrdersActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        };

        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        AdminLogoutButton = findViewById(R.id.admin_logout_button);

        AdminLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                Toast.makeText(AdminCategoryActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
            }
        });

    }
}