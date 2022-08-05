package com.example.shoppingapplication.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapplication.Interface.ItemClickListener;
import com.example.shoppingapplication.R;

public class OrdersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView orderProductName, orderProductPrice, orderProductQuantity;
    public ItemClickListener listener;

    public OrdersViewHolder(@NonNull View itemView) {
        super(itemView);
        orderProductName = itemView.findViewById(R.id.order_product_name);
        orderProductPrice = itemView.findViewById(R.id.order_product_price);
        orderProductQuantity = itemView.findViewById(R.id.order_product_quantity);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.listener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        listener.onClick(view, getAdapterPosition(), false);
    }
}
