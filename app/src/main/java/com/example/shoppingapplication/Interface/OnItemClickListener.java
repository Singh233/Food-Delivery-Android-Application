package com.example.shoppingapplication.Interface;

import android.view.View;

import com.example.shoppingapplication.Adapter.HomeAdapter.CategoriesHelperClass;

import java.util.List;

public interface OnItemClickListener {
    public void onItemClick(View view, int position, List<CategoriesHelperClass> AllRestaurant);
}
