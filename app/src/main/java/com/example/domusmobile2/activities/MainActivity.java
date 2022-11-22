package com.example.domusmobile2.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.domusmobile2.R;
import com.example.domusmobile2.adapters.CategoryAdapter;
import com.example.domusmobile2.adapters.ProductAdapter;
import com.example.domusmobile2.databinding.ActivityMainBinding;
import com.example.domusmobile2.model.CategoryModel;
import com.example.domusmobile2.model.ProductModel;
import com.example.domusmobile2.utils.Api;
import com.mancj.materialsearchbar.MaterialSearchBar;

import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    CategoryAdapter categoryAdapter;
    ArrayList<CategoryModel> categoryModels;

    ProductAdapter productAdapter;
    ArrayList<ProductModel> productModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("query", text.toString());
                startActivity(intent);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

        initCategories();
        initProducts();
        initBanners();
    }

    void initBanners() {
        getBanners();
    }

    void getBanners(){
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.GET, Api.GET_BANNERS, response -> {
            try {
                JSONObject object = new JSONObject(response);

                JSONArray bannersArray = object.getJSONArray("banners");
                for(int i=0; i<bannersArray.length(); i++){
                    JSONObject childObj = bannersArray.getJSONObject(i);
                    binding.carousel.addData(
                            new CarouselItem(
                                    Api.GET_BANNER_IMG + childObj.getString("image")
                            )
                    );
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {});

        queue.add(request);
    }

    void initCategories(){
        categoryModels = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(this, categoryModels);

        getCategories();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        binding.categoriesArray.setLayoutManager(layoutManager);
        binding.categoriesArray.addItemDecoration(itemDecoration);
        binding.categoriesArray.setAdapter(categoryAdapter);
    }

    void getCategories(){
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.GET, Api.GET_CATEGORIES, response -> {
            try {
                JSONObject mainObj = new JSONObject(response);

                JSONArray categoriesArray = mainObj.getJSONArray("categories");
                for(int i=0; i<categoriesArray.length(); i++){
                    JSONObject object = categoriesArray.getJSONObject(i);
                    CategoryModel categoryModel = new CategoryModel(
                            object.getString("name"),
                            Api.GET_UPLOADS + object.getString("image"),
                            object.getInt("id")
                    );
                    categoryModels.add(categoryModel);
                }
                categoryAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {

        });

        queue.add(request);
    }

    void initProducts(){
        productModels = new ArrayList<>();
        productAdapter = new ProductAdapter(this, productModels);

        getTrendingProducts();

        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        binding.productsArray.setLayoutManager(layoutManager);
        binding.productsArray.setAdapter(productAdapter);
    }

    void getTrendingProducts() {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.GET, Api.GET_TRENDING_PRODUCTS, response -> {
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