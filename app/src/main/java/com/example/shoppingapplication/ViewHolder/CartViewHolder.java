package com.example.shoppingapplication.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapplication.Interface.ItemClickListener;
import com.example.shoppingapplication.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtProductName, txtProductPrice, txtProductQuantity;
    public ImageView imageView;
    public ItemClickListener listener;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        txtProductName = itemView.findViewById(R.id.cart_product_name);
        txtProductPrice = itemView.findViewById(R.id.cart_product_price);
        txtProductQuantity = itemView.findViewById(R.id.cart_product_quantity);
        imageView = itemView.findViewById(R.id.cart_product_image);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.listener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        listener.onClick(view, getAdapterPosition(), false);
    }



}

