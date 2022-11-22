package com.example.domusmobile2.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.domusmobile2.R;
import com.example.domusmobile2.adapters.CartAdapter;
import com.example.domusmobile2.databinding.ActivityCheckoutBinding;
import com.example.domusmobile2.model.ProductModel;
import com.example.domusmobile2.utils.Api;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.model.Item;
import com.hishd.tinycart.util.TinyCartHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity {

    ActivityCheckoutBinding binding;
    CartAdapter adapter;
    ArrayList<ProductModel> productModels;
    double totalPrice = 0;
    final int tax = 11;
    ProgressDialog progressDialog;
    Cart cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Processing...");

        productModels = new ArrayList<>();

        cart = TinyCartHelper.getCart();

        for (Map.Entry<Item, Integer> item : cart.getAllItemsWithQty().entrySet()) {
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

        totalPrice = (cart.getTotalPrice().doubleValue() * tax / 100) + cart.getTotalPrice().doubleValue();
        binding.total.setText("€ " + totalPrice);

        binding.checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmOrder();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    void confirmOrder() {
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(this);

        JSONObject productOrder = new JSONObject();
        JSONObject dataObject = new JSONObject();
        try {
            productOrder.put("name", binding.nameBox.getText().toString());
            productOrder.put("email", binding.emailBox.getText().toString());
            productOrder.put("phone", binding.phoneBox.getText().toString());
            productOrder.put("address", binding.addressBox.getText().toString());
            productOrder.put("date", binding.dateBox.getText().toString());
            productOrder.put("comment", binding.commentBox.getText().toString());
            productOrder.put("tax", tax);
            productOrder.put("total_price", totalPrice);

            JSONArray mobile_order_items = new JSONArray();
            for (Map.Entry<Item, Integer> item : cart.getAllItemsWithQty().entrySet()) {
                ProductModel productModel = (ProductModel) item.getKey();
                int quantity = item.getValue();
                productModel.setQuantity(quantity);

                JSONObject productObj = new JSONObject();
                productObj.put("amount", quantity);
                productObj.put("price_item", productModel.getPrice());
                productObj.put("product_id", productModel.getId());
                productObj.put("product_name", productModel.getName());
                mobile_order_items.put(productObj);
            }

            dataObject.put("mobile_order", productOrder);
            dataObject.put("mobile_order_items", mobile_order_items);

            //Log.e("err", dataObject.toString());

        } catch (JSONException e) {
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Api.POST_ORDER, dataObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(CheckoutActivity.this,"Order placed", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CheckoutActivity.this,"Order placed", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
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

//{"mobile_order":{"name":"Lukas","email":"abc@luk.as","phone":"123456789","address":"Petrina 17","date":"7\/7\/2022","comment":"No comment","tax":11,"total_price":832.5},"mobile_order_items":[{"amount":1,"price_item":750,"product_id":5,"product_name":"Kids Bed"}]}