package com.example.shoppingapplication.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.shoppingapplication.R;
import com.example.shoppingapplication.User.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;


public class AdminMaintainProducts extends AppCompatActivity {
    private Button applyChangesButton, deleteButton;
    private EditText name, price, description;
    private ImageView imageView;
    private String productID = "";
    private DatabaseReference ProductsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_products);



        productID = getIntent().getStringExtra("pid");
        ProductsRef = FirebaseDatabase.getInstance().getReference()
                .child("Products")
                .child(productID);



        applyChangesButton = findViewById(R.id.apply_changes_button);
        name = findViewById(R.id.product_name_maintain_input);
        price = findViewById(R.id.product_price_maintain_input);
        description = findViewById(R.id.product_description_maintain_input);
        imageView = findViewById(R.id.product_image_maintain);
        deleteButton = findViewById(R.id.delete_product_button);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProduct();
            }
        });


        applyChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyChanges();
            }
        });

        displaySpecificProductInfo();

    }



    private void deleteProduct() {
        ProductsRef.removeValue().addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                Intent intent = new Intent(AdminMaintainProducts.this, AdminCategoryActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(AdminMaintainProducts.this, "The Product is Removed Successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void applyChanges() {
        String pName = name.getText().toString();
        String pPrice = price.getText().toString();
        String pDescription = description.getText().toString();

        if (pName.equals("")) {
            Toast.makeText(this, "Write down Product Name.", Toast.LENGTH_SHORT).show();
        } else if (pPrice.equals("")) {
            Toast.makeText(this, "Write down Product Price.", Toast.LENGTH_SHORT).show();
        } else if (pDescription.equals("")) {
            Toast.makeText(this, "Write down Product Description", Toast.LENGTH_SHORT).show();
        } else {
            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put("pid", productID);
            productMap.put("description", pDescription);
            productMap.put("price", pPrice);
            productMap.put("pname", pName);

            ProductsRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                     if (task.isSuccessful()) {
                         Toast.makeText(AdminMaintainProducts.this, "Changes Applied Successfully", Toast.LENGTH_SHORT).show();
                         Intent intent = new Intent(AdminMaintainProducts.this, AdminCategoryActivity.class);
                         startActivity(intent);
                         finish();
                     }
                }
            });
        }
    }

    private void displaySpecificProductInfo() {
        ProductsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String pName = snapshot.child("pname").getValue().toString();
                    String pPrice = snapshot.child("price").getValue().toString();
                    String pDescription = snapshot.child("description").getValue().toString();
                    String pImage = snapshot.child("image").getValue().toString();

                    name.setText(pName);
                    price.setText(pPrice);
                    description.setText(pDescription);
                    Picasso.get().load(pImage).into(imageView);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}