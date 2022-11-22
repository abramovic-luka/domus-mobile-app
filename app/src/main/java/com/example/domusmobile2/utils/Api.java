package com.example.domusmobile2.utils;

public class Api {
    public static String API_BASE_URL = "https://abramovicluka-diplomski.com/Domus/mobile";
    public static String GET_CATEGORIES = API_BASE_URL + "/api/categories.php";
    public static String GET_TRENDING_PRODUCTS = API_BASE_URL + "/api/trending_products.php";
    public static String GET_PRODUCTS = API_BASE_URL + "/api/products.php?category_id=";
    public static String SEARCH_PRODUCTS = API_BASE_URL + "/api/search_products.php?query=";
    public static String GET_BANNERS = API_BASE_URL + "/api/banners.php";
    public static String GET_PRODUCT_DETAILS = API_BASE_URL + "/api/product_details.php?id=";
    public static String POST_ORDER = API_BASE_URL + "/api/product_order.php";

    public static String GET_UPLOADS = "https://abramovicluka-diplomski.com/Domus/web/uploads/";
    public static String GET_BANNER_IMG = "https://abramovicluka-diplomski.com/Domus/web/banners/";
}
