package com.example.domusmobile2.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.domusmobile2.R;
import com.example.domusmobile2.activities.CategoryActivity;
import com.example.domusmobile2.databinding.ItemCategoriesBinding;
import com.example.domusmobile2.model.CategoryModel;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    Context context;
    ArrayList<CategoryModel> categoryModels;

    public CategoryAdapter(Context context, ArrayList<CategoryModel> categoryModels){
        this.context = context;
        this.categoryModels = categoryModels;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.item_categories, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategoryModel categoryModel = categoryModels.get(position);
        holder.binding.label.setText(categoryModel.getName());
        Glide.with(context).load(categoryModel.getImage()).into(holder.binding.image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CategoryActivity.class);
                intent.putExtra("categoryId", categoryModel.getId());
                intent.putExtra("categoryName", categoryModel.getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryModels.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{
        ItemCategoriesBinding binding;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCategoriesBinding.bind(itemView);
        }
    }
}
