package com.example.domusmobile2.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.domusmobile2.R;
import com.example.domusmobile2.databinding.ActivityProductDetailsBinding;
import com.example.domusmobile2.model.ProductModel;
import com.example.domusmobile2.utils.Api;
import com.google.android.material.internal.ManufacturerUtils;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.util.TinyCartHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProductDetailsActivity extends AppCompatActivity {

    ActivityProductDetailsBinding binding;
    ProductModel currentProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String name = getIntent().getStringExtra("name");
        String image = getIntent().getStringExtra("image");
        int id = getIntent().getIntExtra("id",0);
        double price = getIntent().getDoubleExtra("price",0);

        Glide.with(this).load(image).into(binding.productImage);

        getProductDetails(id);

        getSupportActionBar().setTitle(name);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Cart cart = TinyCartHelper.getCart();

        binding.addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cart.addItem(currentProduct, 1);
                binding.addToCartBtn.setEnabled(false);
                binding.addToCartBtn.setText("Added to cart");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.cart){
            startActivity(new Intent(this, CartActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    void getProductDetails(int id){
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = Api.GET_PRODUCT_DETAILS + id;

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    JSONArray productDetailsArray = object.getJSONArray("products");
                    for(int i=0; i<productDetailsArray.length(); i++){
                        JSONObject product = productDetailsArray.getJSONObject(i);
                        String description = product.getString("small_description");
                        String price = product.getString("selling_price");
                        binding.productDes.setText(description);
                        binding.productPrice.setText("€ " + price);

                        currentProduct = new ProductModel(
                                product.getString("name"),
                                Api.GET_UPLOADS + product.getString("image"),
                                product.getDouble("selling_price"),
                                product.getInt("quantity"),
                                product.getInt("id")
                        );
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(request);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}