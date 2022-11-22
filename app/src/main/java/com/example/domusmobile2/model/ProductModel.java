package com.example.domusmobile2.model;

import com.hishd.tinycart.model.Item;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProductModel implements Item, Serializable {
    private String name, image;
    private double price;
    private int stock, id;
    private int quantity;

    public ProductModel(String name, String image, double price, int stock, int id) {
        this.name = name;
        this.image = image;
        this.price = price;
        this.stock = stock;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public BigDecimal getItemPrice() {
        return new BigDecimal(price);
    }

    @Override
    public String getItemName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
