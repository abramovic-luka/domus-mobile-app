package com.example.domusmobile2.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.domusmobile2.R;
import com.example.domusmobile2.databinding.ItemCartBinding;
import com.example.domusmobile2.databinding.QuantityWindowBinding;
import com.example.domusmobile2.model.ProductModel;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.util.TinyCartHelper;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder>{

    Context context;
    ArrayList<ProductModel> productModels;
    CartListener cartListener;
    Cart cart;

    public interface CartListener{
        public void onQuantityChanged();
    }

    public CartAdapter(Context context, ArrayList<ProductModel> productModels, CartListener cartListener){
        this.context = context;
        this.productModels = productModels;
        this.cartListener = cartListener;
        cart = TinyCartHelper.getCart();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartViewHolder(LayoutInflater.from(context).inflate(R.layout.item_cart, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        ProductModel productModel = productModels.get(position);
        Glide.with(context).load(productModel.getImage()).into(holder.binding.image);
        holder.binding.name.setText(productModel.getName());
        holder.binding.price.setText("€ " + productModel.getPrice());
        holder.binding.quantity.setText(productModel.getQuantity() + " item(s)");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                QuantityWindowBinding quantityWindowBinding = QuantityWindowBinding.inflate(LayoutInflater.from(context));
                AlertDialog dialog = new AlertDialog.Builder(context).setView(quantityWindowBinding.getRoot()).create();

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));

                quantityWindowBinding.productName.setText(productModel.getName());
                quantityWindowBinding.productQuantity.setText("In stock: " + productModel.getStock());
                quantityWindowBinding.quantity.setText(String.valueOf(productModel.getQuantity()));
                int stock = productModel.getStock();

                quantityWindowBinding.increaseBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int quantity = productModel.getQuantity();
                        quantity++;

                        if(quantity>productModel.getStock()){
                            Toast.makeText(context, "No more available", Toast.LENGTH_SHORT).show();
                        }else{
                            productModel.setQuantity(quantity);
                            quantityWindowBinding.quantity.setText(String.valueOf(quantity));
                        }

                        notifyDataSetChanged();
                        cart.updateItem(productModel, productModel.getQuantity());
                        cartListener.onQuantityChanged();
                    }
                });

                quantityWindowBinding.decreaseBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int quantity = productModel.getQuantity();
                        if(quantity > 1){
                            quantity--;
                        }
                        productModel.setQuantity(quantity);
                        quantityWindowBinding.quantity.setText(String.valueOf(quantity));

                        notifyDataSetChanged();
                        cart.updateItem(productModel, productModel.getQuantity());
                        cartListener.onQuantityChanged();
                    }
                });

                quantityWindowBinding.confirmQuantity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();

                        notifyDataSetChanged();
                        cart.updateItem(productModel, productModel.getQuantity());
                        cartListener.onQuantityChanged();
                    }
                });

                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return productModels.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder{

        ItemCartBinding binding;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCartBinding.bind(itemView);
        }
    }
}
