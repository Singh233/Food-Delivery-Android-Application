package com.example.shoppingapplication.Adapter.HomeAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapplication.R;

import java.util.ArrayList;

public class FeaturedAdapter extends RecyclerView.Adapter<FeaturedAdapter.FeaturedViewHolder> {

    ArrayList<FeaturedHelper> featuredLocations;

    public FeaturedAdapter(ArrayList<FeaturedHelper> featuredLocations) {
        this.featuredLocations = featuredLocations;
    }

    @NonNull
    @Override
    public FeaturedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.featured_card_design, parent, false);
        return new FeaturedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeaturedViewHolder holder, int position) {

        FeaturedHelper featuredHelper = featuredLocations.get(position);

        holder.image.setImageResource(featuredHelper.getImage());
        holder.title.setText(featuredHelper.getTitle());
        holder.description.setText(featuredHelper.getDescription());


    }

    @Override
    public int getItemCount() {

        return featuredLocations.size();
    }

    public static class FeaturedViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title, description;

        public FeaturedViewHolder(@NonNull View itemView) {
            super(itemView);

            //Hooks
            image = itemView.findViewById(R.id.featured_image);
            title = itemView.findViewById(R.id.featured_title);
            description = itemView.findViewById(R.id.featured_description);

        }
    }
}
