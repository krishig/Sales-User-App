package com.krishig.android.data.remote.api;

import android.util.Log;

import com.krishig.android.data.remote.ApiConfiguration;
import com.krishig.android.data.remote.ApiResponseArray;
import com.krishig.android.data.remote.ApiResponseObject;
import com.krishig.android.model.Cart;
import com.krishig.android.model.Category;
import com.krishig.android.model.Customer;
import com.krishig.android.model.Order;
import com.krishig.android.model.PostalPincodeResponse;
import com.krishig.android.model.Product;
import com.krishig.android.model.ProductBrands;
import com.krishig.android.model.ProductId;
import com.krishig.android.model.SubCategory;
import com.krishig.android.model.User;
import com.krishig.android.ui.home.fragments.home.adapter.SliderItem;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    String BASE_URL = "https://api.postalpincode.in/";

    @GET("pincode/{pincode}")
    Call<ArrayList<PostalPincodeResponse>> getPincodeDetails(@Path("pincode") String pincode);


    @POST(ApiConfiguration.user)
    Call<User> signUp(@Body RequestBody body,
                      @Header("accept") String accept,
                      @Header("Content-Type") String authorisation
    );

    @POST(ApiConfiguration.login)
    Call<ApiResponseObject<User>> login(@Body RequestBody body,
                                        @Header("accept") String accept,
                                        @Header("Content-Type") String authorisation
    );


    @POST(ApiConfiguration.addCustomer)
    Call<ApiResponseObject<Customer>> addCustomer(@Body RequestBody body,
                                                  @Header("accept") String accept,
                                                  @Header("Content-Type") String authorisation,
                                                  @Header("Authorization") String token
    );

    @POST(ApiConfiguration.addAddress)
    Call<ApiResponseObject<Customer>> addAddress(@Body RequestBody body,
                                                 @Header("accept") String accept,
                                                 @Header("Content-Type") String authorisation,
                                                 @Header("Authorization") String token
    );

    @POST(ApiConfiguration.addProductToCart)
    Call<ApiResponseObject<Product>> addProductToCart(@Body RequestBody body,
                                                      @Header("accept") String accept,
                                                      @Header("Content-Type") String authorisation,
                                                      @Header("Authorization") String token
    );

    @POST(ApiConfiguration.orderBook)
    Call<ApiResponseObject<Cart>> orderBook(@Body RequestBody body,
                                            @Header("accept") String accept,
                                            @Header("Content-Type") String authorisation,
                                            @Header("Authorization") String token
    );

    @GET(ApiConfiguration.getAllOrder)
    Call<ApiResponseObject<Order>> getAllOrder(@Query("pageNumber") String pageNumber,
                                               @Query("pageSize") String pageSize,
                                               @Header("accept") String accept,
                                               @Header("Content-Type") String authorisation,
                                               @Header("Authorization") String token
    );

    @PUT(ApiConfiguration.putCustomer)
    Call<ApiResponseObject<Customer>> putCustomer(@Path("id") String customerId,
                                                  @Body RequestBody body,
                                                  @Header("accept") String accept,
                                                  @Header("Content-Type") String authorisation,
                                                  @Header("Authorization") String token
    );

    @GET(ApiConfiguration.categories)
    Call<ApiResponseArray<Category>> getCategories(@Header("accept") String accept,
                                                   @Header("Content-Type") String authorisation,
                                                   @Header("Authorization") String token
    );

    @GET(ApiConfiguration.getCartProduct)
    Call<ApiResponseObject<Cart>> getCartProduct(@Path("id") String customerId,
                                                 @Header("accept") String accept,
                                                 @Header("Content-Type") String authorisation,
                                                 @Header("Authorization") String token
    );

    @GET(ApiConfiguration.slider_image)
    Call<ApiResponseArray<SliderItem>> slider_image(
            @Header("accept") String accept,
            @Header("Content-Type") String authorisation,
            @Header("Authorization") String token
    );

    @DELETE(ApiConfiguration.deleteCartProduct)
    Call<ApiResponseObject<Cart>> deleteCartProduct(@Path("id") String cartId,
                                                    @Path("ids") String customerId,
                                                    @Header("accept") String accept,
                                                    @Header("Content-Type") String authorisation,
                                                    @Header("Authorization") String token
    );

    @DELETE(ApiConfiguration.deleteAddress)
    Call<ApiResponseObject<Cart>> deleteAddress(@Path("addressId") String addressId,
                                                @Header("accept") String accept,
                                                @Header("Content-Type") String authorisation,
                                                @Header("Authorization") String token
    );

    @GET(ApiConfiguration.getCustomer)
    Call<ApiResponseObject<Customer>> getCustomer(@Query("pageNumber") String pageNumber,
                                                  @Query("pageSize") String pageSize,
                                                  @Header("accept") String accept,
                                                  @Header("Content-Type") String authorisation,
                                                  @Header("Authorization") String token
    );

    @GET(ApiConfiguration.getCustomerWithId)
    Call<ApiResponseObject<Customer.Content>> getCustomerWithId(@Path("id") String customerId,
                                                                @Header("accept") String accept,
                                                                @Header("Content-Type") String authorisation,
                                                                @Header("Authorization") String token
    );

    @GET(ApiConfiguration.searchOrder)
    Call<ApiResponseObject<Order>> searchOrder(@Query("pageSize") String items_per_page,
                                               @Query("pageNumber") String page_number,
                                               @Query("orderId") String orderId,
                                               @Header("accept") String accept,
                                               @Header("Content-Type") String authorisation,
                                               @Header("Authorization") String token
    );

    @GET(ApiConfiguration.fetchOrderDetail)
    Call<ApiResponseObject<Order>> fetchOrderDetail(@Path("orderId") String orderId,
                                                    @Header("accept") String accept,
                                                    @Header("Content-Type") String authorisation,
                                                    @Header("Authorization") String token
    );

    @GET(ApiConfiguration.searchCustomer)
    Call<ApiResponseObject<Customer>> searchCustomer(@Query("mobileNumber") String findByMobile,
                                                     @Header("accept") String accept,
                                                     @Header("Content-Type") String authorisation,
                                                     @Header("Authorization") String token
    );


    @GET(ApiConfiguration.subCategory)
    Call<ApiResponseObject<SubCategory>> getSubCategories(@Query("category_id") String category_id,
                                                          @Header("accept") String accept,
                                                          @Header("Content-Type") String authorisation,
                                                          @Header("Authorization") String token
    );

    @GET(ApiConfiguration.subCategory)
    Call<ApiResponseObject<SubCategory>> getSubCategories(
            @Header("accept") String accept,
            @Header("Content-Type") String authorisation,
            @Header("Authorization") String token
    );


    @GET(ApiConfiguration.productBrands)
    Call<ApiResponseObject<ProductBrands>> getProductBrands(@Header("accept") String accept,
                                                            @Header("Content-Type") String authorisation,
                                                            @Header("Authorization") String token
    );

    @GET(ApiConfiguration.productBrands)
    Call<ApiResponseObject<ProductBrands>> getProductBrands(@Query("items_per_page") String items_per_page,
                                                            @Query("page_number") String page_number,
                                                            @Header("accept") String accept,
                                                            @Header("Content-Type") String authorisation,
                                                            @Header("Authorization") String token
    );


    @GET(ApiConfiguration.productFilter)
    Call<ApiResponseObject<Product>> productFilter(@Query("category_id") String category_id,
                                                   @Header("accept") String accept,
                                                   @Header("Content-Type") String authorisation,
                                                   @Header("Authorization") String token
    );

    @GET(ApiConfiguration.productFilter)
    Call<ApiResponseObject<Product>> productFilter(@Query("items_per_page") String items_per_page,
                                                   @Query("page_number") String page_number,
                                                   @Query("category_id") String category_id,
                                                   @Header("accept") String accept,
                                                   @Header("Content-Type") String authorisation,
                                                   @Header("Authorization") String token
    );

    @GET(ApiConfiguration.productFilter)
    Call<ApiResponseObject<Product>> productFilterWithBrandId(@Query("category_id") String category_id,
                                                              @Query("sub_category_id") String sub_category_id,
                                                              @Query("brand_id") String brand_id,
                                                              @Query("items_per_page") String items_per_page,
                                                              @Query("page_number") String page_number,
                                                              @Header("accept") String accept,
                                                              @Header("Content-Type") String authorisation,
                                                              @Header("Authorization") String token
    );

    @GET(ApiConfiguration.productFilter)
    Call<ApiResponseObject<Product>> productFilter(@Query("sub_category_id") String sub_category_id,
                                                   @Query("brand_id") String brand_id,
                                                   @Query("items_per_page") String items_per_page,
                                                   @Query("page_number") String page_number,
                                                   @Header("accept") String accept,
                                                   @Header("Content-Type") String authorisation,
                                                   @Header("Authorization") String token
    );

    @GET(ApiConfiguration.product)
    Call<ApiResponseObject<ProductId>> getProductWithId(
            @Query("id") String id,
            @Header("accept") String accept,
            @Header("Content-Type") String authorisation,
            @Header("Authorization") String token
    );

    @GET(ApiConfiguration.product)
    Call<ApiResponseObject<Product>> getProduct(
            @Query("items_per_page") String items_per_page,
            @Query("page_number") String page_number,
            @Header("accept") String accept,
            @Header("Content-Type") String authorisation,
            @Header("Authorization") String token
    );

    @GET(ApiConfiguration.productSearch)
    Call<ApiResponseObject<Product>> productSearch(@Query("items_per_page") String items_per_page,
                                                   @Query("page_number") String page_number,
                                                   @Query("search_product") String search_sub_category,
                                                   @Header("accept") String accept,
                                                   @Header("Content-Type") String authorisation,
                                                   @Header("Authorization") String token
    );

    @GET(ApiConfiguration.user)
    Call<ApiResponseObject<User>> getUserWithId(@Query("id") String id,
                                                @Header("accept") String accept,
                                                @Header("Content-Type") String authorisation,
                                                @Header("Authorization") String token
    );

    @PATCH(ApiConfiguration.user)
    Call<ApiResponseObject<User>> editUser(@Query("id") String id,
                                           @Body RequestBody body,
                                           @Header("accept") String accept,
                                           @Header("Content-Type") String authorisation,
                                           @Header("Authorization") String token
    );

    @GET(ApiConfiguration.subCategorySearch)
    Call<ApiResponseObject<SubCategory>> subCategorySearch(@Query("search_sub_category") String search_sub_category,
                                                           @Header("accept") String accept,
                                                           @Header("Content-Type") String authorisation,
                                                           @Header("Authorization") String token
    );


    @GET(ApiConfiguration.productBrandsSearch)
    Call<ApiResponseObject<ProductBrands>> subProductBrandsSearch(@Query("search_brand") String search_brand,
                                                                  @Header("accept") String accept,
                                                                  @Header("Content-Type") String authorisation,
                                                                  @Header("Authorization") String token
    );

    @GET(ApiConfiguration.subCategory)
    Call<ApiResponseObject<SubCategory>> getSubCategories(@Query("category_id") String category_id,
                                                          @Query("items_per_page") String items_per_page,
                                                          @Query("page_number") String page_number,
                                                          @Header("accept") String accept,
                                                          @Header("Content-Type") String authorisation,
                                                          @Header("Authorization") String token
    );
}
