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
import com.example.domusmobile2.activities.ProductDetailsActivity;
import com.example.domusmobile2.databinding.ItemProductsBinding;
import com.example.domusmobile2.model.ProductModel;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    Context context;
    ArrayList<ProductModel> productModels;

    public ProductAdapter(Context context, ArrayList<ProductModel> productModels){
        this.context = context;
        this.productModels = productModels;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductViewHolder(LayoutInflater.from(context).inflate(R.layout.item_products, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductModel productModel = productModels.get(position);
        Glide.with(context).load(productModel.getImage()).into(holder.binding.image);
        holder.binding.label.setText(productModel.getName());
        holder.binding.price.setText("€ " + productModel.getPrice());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductDetailsActivity.class);
                intent.putExtra("name", productModel.getName());
                intent.putExtra("image", productModel.getImage());
                intent.putExtra("id", productModel.getId());
                intent.putExtra("price", productModel.getPrice());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productModels.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder{

        ItemProductsBinding binding;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemProductsBinding.bind(itemView);
        }
    }
}
