package com.krishig.android.data.remote;


import com.krishig.android.ui.AppConstants;

public class ApiConfiguration {

    public static final int CUSTOM_HTTP_CONNECT_TIMEOUT_IN_SECONDS = 2 * 60; /* 2 minutes */
    public static final int CUSTOM_HTTP_WRITE_TIMEOUT_IN_SECONDS = 25; /* 25 seconds */
    public static final int CUSTOM_HTTP_READ_TIMEOUT_IN_SECONDS = 40; /* 40 seconds */

    public static final String CUSTOM_OK_HTTP_CACHE_FILE_NAME = "customOkHttpClientCache";
    public static final long CUSTOM_OK_HTTP_CACHE_SIZE = 20 * 1024 * 1024; /* 20 MB Cache size */

    public static final int CUSTOM_CACHE_DURATION_WITH_NETWORK_IN_SECONDS = 10;
    public static final int CUSTOM_CACHE_DURATION_WITHOUT_NETWORK_IN_SECONDS = 14 * 24 * 60 * 60; /* Expired in two week. */

    public static final String BASE_URL = AppConstants.APIPath.BASE_URL;
    public static final String API_KEY = AppConstants.APIPath.API_KEY;
    public static final String BEARER_AUTHENTICATION_TOKEN = AppConstants.APIPath.BEARER_AUTHENTICATION_TOKEN;

    /*
     * End points
     */
    public static final String user = "user";
    public static final String login = "user/login";
    public static final String categories = "categories";
    public static final String subCategory = "sub_category/filter";
    public static final String productBrands = "product_brands";
    public static final String productFilter = "product/filter";
    public static final String product = "product";
    public static final String productSearch = "product/search";
    public static final String slider_image = "slider_image";
    public static final String subCategorySearch = "sub_category/search";
    public static final String productBrandsSearch = "product_brand/search";


    public static final String addCustomer = "customer/addCustomer";
    public static final String getCustomer = "customer/getAll";
    public static final String addAddress = "customer/address";
    public static final String putCustomer = "customer/{id}";
    public static final String deleteAddress = "customer/address/{addressId}";
    public static final String getCustomerWithId = "customer/findById/{id}";
    public static final String searchOrder = "order/filter";
    public static final String fetchOrderDetail = "order/{orderId}";
    public static final String searchCustomer = "customer/findByMobile";
    public static final String addProductToCart = "cart/addProduct";
    public static final String getCartProduct = "cart/getCartProduct/{id}";
    public static final String deleteCartProduct = "cart/{id}/{ids}";
    public static final String orderBook = "order/book";
    public static final String getAllOrder = "order/getAllOrder";


}
