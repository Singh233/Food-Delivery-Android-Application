package com.example.shoppingapplication.Adapter.HomeAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapplication.R;

import java.util.ArrayList;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.AdapterAllCategoriesViewHolder> {

    ArrayList<OffersHelperClass> offers;

    public OfferAdapter(ArrayList<OffersHelperClass> offers) {
        this.offers = offers;
    }

    @NonNull
    @Override
    public OfferAdapter.AdapterAllCategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offers_card_design, parent, false);
        OfferAdapter.AdapterAllCategoriesViewHolder lvh = new OfferAdapter.AdapterAllCategoriesViewHolder(view);
        return lvh;
    }

    @Override
    public void onBindViewHolder(@NonNull OfferAdapter.AdapterAllCategoriesViewHolder holder, int position) {

        OffersHelperClass helperClass = offers.get(position);
        holder.imageView.setImageResource(helperClass.getImage());


    }

    @Override
    public int getItemCount() {
        return offers.size();
    }

    public static class AdapterAllCategoriesViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout relativeLayout;
        ImageView imageView;

        public AdapterAllCategoriesViewHolder(@NonNull View itemView) {
            super(itemView);

            relativeLayout = itemView.findViewById(R.id.offers_gradient);
            imageView = itemView.findViewById(R.id.offers_image);
        }
    }
}