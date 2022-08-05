package com.example.shoppingapplication.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.shoppingapplication.Model.Products;
import com.example.shoppingapplication.R;
import com.example.shoppingapplication.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class SearchProductActivity extends AppCompatActivity {
    private Button SearchButton;
    private TextInputEditText InputText;
    private RecyclerView SearchList;
    private String SearchInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);

        InputText = findViewById(R.id.search_product_name_input);
        SearchButton = findViewById(R.id.search_product_button);
        SearchList = findViewById(R.id.search_list);
        SearchList.setLayoutManager(new LinearLayoutManager(SearchProductActivity.this));

        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchInput = Objects.requireNonNull(InputText.getText()).toString();
                onStart();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Products");

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(reference.orderByChild("pname").startAt(SearchInput), Products.class)
                .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;
            }

            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model) {
                holder.txtProductName.setText(model.getPname());
                holder.txtProductDescription.setText(model.getDescription());
                holder.txtProductPrice.setText("₹" + model.getPrice());
                Picasso.get().load(model.getImage()).into(holder.imageView);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SearchProductActivity.this, ProductDetailsActivity.class);
                        intent.putExtra("pid", model.getPid());
                        startActivity(intent);
                    }
                });

            }
        };
        SearchList.setAdapter(adapter);
        adapter.startListening();


    }
}