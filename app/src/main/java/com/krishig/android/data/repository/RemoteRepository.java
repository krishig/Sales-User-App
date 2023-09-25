package com.krishig.android.data.repository;

import com.krishig.android.data.remote.ApiResponseArray;
import com.krishig.android.data.remote.ApiResponseObject;
import com.krishig.android.data.remote.api.ApiService;
import com.krishig.android.model.Cart;
import com.krishig.android.model.Category;
import com.krishig.android.model.Customer;
import com.krishig.android.model.Order;
import com.krishig.android.model.Product;
import com.krishig.android.model.ProductBrands;
import com.krishig.android.model.ProductId;
import com.krishig.android.model.SubCategory;
import com.krishig.android.model.User;
import com.krishig.android.ui.home.fragments.home.adapter.SliderItem;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

@Singleton
public class RemoteRepository {

    private ApiService apiService;

    @Inject
    public RemoteRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public Call<User> signUp(RequestBody jsonObject, String accept, String authorisation) {
        return apiService.signUp(jsonObject, accept, authorisation);
    }


    public Call<ApiResponseObject<User>> login(RequestBody body, String accept, String authorisation) {
        return apiService.login(body, accept, authorisation);
    }

    public Call<ApiResponseObject<Customer>> addCustomer(RequestBody body, String accept, String authorisation, String token) {
        return apiService.addCustomer(body, accept, authorisation, token);
    }

    public Call<ApiResponseObject<Customer>> addAddress(RequestBody body, String accept, String authorisation, String token) {
        return apiService.addAddress(body, accept, authorisation, token);
    }

    public Call<ApiResponseObject<Product>> addProductToCart(RequestBody body, String accept, String authorisation, String token) {
        return apiService.addProductToCart(body, accept, authorisation, token);
    }

    public Call<ApiResponseObject<Cart>> orderBook(RequestBody body, String accept, String authorisation, String token) {
        return apiService.orderBook(body, accept, authorisation, token);
    }

    public Call<ApiResponseObject<Order>> getAllOrder(String page_number,String items_per_page,
                                                      String accept, String authorisation, String token) {
        return apiService.getAllOrder(page_number,items_per_page,accept, authorisation, token);
    }

    public Call<ApiResponseObject<Customer>> putCustomer(String customerId, RequestBody body, String accept, String authorisation, String token) {
        return apiService.putCustomer(customerId, body, accept, authorisation, token);
    }


    public Call<ApiResponseObject<Customer>> getCustomer(String pageNumber, String pageSize, String accept, String authorisation, String token) {
        return apiService.getCustomer(pageNumber, pageSize, accept, authorisation, token);
    }

    public Call<ApiResponseArray<Category>> getCategories(String accept, String authorisation, String token) {
        return apiService.getCategories(accept, authorisation, token);
    }

    public Call<ApiResponseObject<Cart>> getCartProduct(String customerId, String accept, String authorisation, String token) {
        return apiService.getCartProduct(customerId, accept, authorisation, token);
    }


    public Call<ApiResponseObject<Cart>> deleteCartProduct(String cartId, String customerId, String accept, String authorisation, String token) {
        return apiService.deleteCartProduct(cartId, customerId, accept, authorisation, token);
    }

    public Call<ApiResponseObject<Cart>> deleteAddress(String addressId, String accept, String authorisation, String token) {
        return apiService.deleteAddress(addressId, accept, authorisation, token);
    }

    public Call<ApiResponseObject<Customer.Content>> getCustomerWithId(String customerId, String accept, String authorisation, String token) {
        return apiService.getCustomerWithId(customerId, accept, authorisation, token);
    }

    public Call<ApiResponseObject<Order>> searchOrder(String items_per_page,
                                                      String page_number,String orderId, String accept, String authorisation, String token) {
        return apiService.searchOrder(items_per_page,page_number,orderId, accept, authorisation, token);
    }

    public Call<ApiResponseObject<Order>> fetchOrderDetail(String orderId, String accept, String authorisation, String token) {
        return apiService.fetchOrderDetail(orderId, accept, authorisation, token);
    }

    public Call<ApiResponseObject<Customer>> searchCustomer(String findByMobile, String accept, String authorisation, String token) {
        return apiService.searchCustomer(findByMobile, accept, authorisation, token);
    }


    public Call<ApiResponseObject<SubCategory>> getSubCategories(String category_id, String accept, String authorisation, String token) {
        return apiService.getSubCategories(category_id, accept, authorisation, token);
    }

    public Call<ApiResponseObject<ProductBrands>> getProductBrands(String accept, String authorisation, String token) {
        return apiService.getProductBrands(accept, authorisation, token);
    }

    public Call<ApiResponseObject<Product>> productFilter(String category_id,
                                                          String accept, String authorisation, String token) {
        return apiService.productFilter(category_id, accept, authorisation, token);
    }


}
