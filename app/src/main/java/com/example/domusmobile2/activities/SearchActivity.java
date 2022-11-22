package com.example.domusmobile2.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.domusmobile2.adapters.ProductAdapter;
import com.example.domusmobile2.databinding.ActivitySearchBinding;
import com.example.domusmobile2.model.ProductModel;
import com.example.domusmobile2.utils.Api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    ActivitySearchBinding binding;
    ProductAdapter productAdapter;
    ArrayList<ProductModel> productModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        productModels = new ArrayList<>();
        productAdapter = new ProductAdapter(this, productModels);


        String query = getIntent().getStringExtra("query");

        getSupportActionBar().setTitle(query);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getProducts(query);

        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        binding.productsArray.setLayoutManager(layoutManager);
        binding.productsArray.setAdapter(productAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    void getProducts(String query) {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = Api.SEARCH_PRODUCTS + query;

        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject mainObj = new JSONObject(response);

                JSONArray productsArray = mainObj.getJSONArray("products");
                for(int i=0; i<productsArray.length(); i++){
                    JSONObject childObj = productsArray.getJSONObject(i);
                    ProductModel productModel = new ProductModel(
                            childObj.getString("name"),
                            Api.GET_UPLOADS + childObj.getString("image"),
                            childObj.getDouble("selling_price"),
                            childObj.getInt("quantity"),
                            childObj.getInt("id")
                    );
                    productModels.add(productModel);
                }
                productAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {

        });

        queue.add(request);
    }
}