package com.example.domusmobile2.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.domusmobile2.R;
import com.example.domusmobile2.adapters.CartAdapter;
import com.example.domusmobile2.databinding.ActivityCartBinding;
import com.example.domusmobile2.model.ProductModel;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.model.Item;
import com.hishd.tinycart.util.TinyCartHelper;

import java.util.ArrayList;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    ActivityCartBinding binding;
    CartAdapter adapter;
    ArrayList<ProductModel> productModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        productModels = new ArrayList<>();

        Cart cart = TinyCartHelper.getCart();

        for(Map.Entry<Item,Integer> item : cart.getAllItemsWithQty().entrySet()){
            ProductModel productModel = (ProductModel) item.getKey();
            int quantity = item.getValue();
            productModel.setQuantity(quantity);

            productModels.add(productModel);
        }

        adapter = new CartAdapter(this, productModels, new CartAdapter.CartListener() {
            @Override
            public void onQuantityChanged() {
                binding.subtotal.setText(String.format("€ %.2f", cart.getTotalPrice()));
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        binding.cartList.setLayoutManager(layoutManager);
        binding.cartList.addItemDecoration(itemDecoration);
        binding.cartList.setAdapter(adapter);

        binding.subtotal.setText(String.format("€ %.2f", cart.getTotalPrice()));

        binding.orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CartActivity.this, CheckoutActivity.class));
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}