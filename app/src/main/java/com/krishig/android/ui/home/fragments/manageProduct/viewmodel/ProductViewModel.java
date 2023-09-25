package com.krishig.android.ui.home.fragments.manageProduct.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.krishig.android.data.remote.ApiCallback;
import com.krishig.android.data.remote.ApiResponseArray;
import com.krishig.android.data.remote.ApiResponseObject;
import com.krishig.android.data.remote.exception.NetworkException;
import com.krishig.android.data.repository.LocalRepository;
import com.krishig.android.data.repository.RemoteRepository;
import com.krishig.android.model.Category;
import com.krishig.android.model.Product;
import com.krishig.android.model.ProductBrands;
import com.krishig.android.model.ProductId;
import com.krishig.android.model.SubCategory;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

@HiltViewModel
public class ProductViewModel extends ViewModel {

    private LocalRepository localRepository;
    private RemoteRepository remoteRepository;


    @Inject
    public ProductViewModel(LocalRepository localRepository, RemoteRepository remoteRepository) {
        this.localRepository = localRepository;
        this.remoteRepository = remoteRepository;
    }


    ////GetProductBrands
    private MutableLiveData<ProductBrands> getProductBrandsUserSuccess = new MutableLiveData<>();
    private MutableLiveData<String> getProductBrandsUserError = new MutableLiveData<>();

    public MutableLiveData<ProductBrands> getProductBrandsUserSuccess() {
        return getProductBrandsUserSuccess;
    }

    public MutableLiveData<String> getProductBrandsUserError() {
        return getProductBrandsUserError;
    }

    public void getProductBrands(String accept, String authorisation, String token) {
        remoteRepository.getProductBrands(accept, authorisation, token).enqueue(new ApiCallback<ApiResponseObject<ProductBrands>>() {
            @Override
            public void onSuccess(Response<ApiResponseObject<ProductBrands>> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        if (response.body().isError()) {
                            getProductBrandsUserError.setValue(response.body().getMessage());
                        } else {
                            getProductBrandsUserSuccess.setValue(response.body().getData());
                        }
                    } else {
                        getProductBrandsUserError.setValue(response.body().getMessage());
                    }
                } else
                    getProductBrandsUserError.setValue(null);
            }

            @Override
            public void onFailure(NetworkException networkException) {
                getProductBrandsUserError.setValue(networkException.getDisplayMessage());
            }
        });

    }
/*
    //Get
    private MutableLiveData<SubCategory> getSubCategoryUserSuccess = new MutableLiveData<>();
    private MutableLiveData<String> getSubCategoryUserError = new MutableLiveData<>();

    public MutableLiveData<SubCategory> getSubCategoryUserSuccess() {
        return getSubCategoryUserSuccess;
    }

    public MutableLiveData<String> getSubCategoryUserError() {
        return getSubCategoryUserError;
    }

    public void getSubCategories(String accept, String authorisation, String token) {
        remoteRepository.getSubCategories(accept, authorisation, token).enqueue(new ApiCallback<ApiResponseObject<SubCategory>>() {
            @Override
            public void onSuccess(Response<ApiResponseObject<SubCategory>> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        if (response.body().isError()) {
                            getSubCategoryUserError.setValue(response.body().getMessage());
                        } else {
                            getSubCategoryUserSuccess.setValue(response.body().getData());
                        }
                    } else {
                        getSubCategoryUserError.setValue(response.body().getMessage());
                    }
                } else
                    getSubCategoryUserError.setValue(null);
            }

            @Override
            public void onFailure(NetworkException networkException) {
                getSubCategoryUserError.setValue(networkException.getDisplayMessage());
            }
        });

    }


    //GetProduct
    private MutableLiveData<Product> getProductListUserSuccess = new MutableLiveData<>();
    private MutableLiveData<String> getProductListUserError = new MutableLiveData<>();

    public MutableLiveData<Product> getProductListUserSuccess() {
        return getProductListUserSuccess;
    }

    public MutableLiveData<String> getProductListUserError() {
        return getProductListUserError;
    }

    public void getProduct(
            String items_per_page,
            String page_number, String accept, String authorisation, String token) {
        remoteRepository.getProduct(items_per_page, page_number, accept, authorisation, token).enqueue(new ApiCallback<ApiResponseObject<Product>>() {
            @Override
            public void onSuccess(Response<ApiResponseObject<Product>> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        if (response.body().isError()) {
                            getProductListUserError.setValue(response.body().getMessage());
                        } else {
                            getProductListUserSuccess.setValue(response.body().getData());
                        }
                    } else {
                        getProductListUserError.setValue(response.body().getMessage());
                    }
                } else
                    getProductListUserError.setValue(null);
            }

            @Override
            public void onFailure(NetworkException networkException) {
                getProductListUserError.setValue(networkException.getDisplayMessage());
            }
        });

    }


    //GetProductWithID
    private MutableLiveData<ProductId> getProductIdListUserSuccess = new MutableLiveData<>();
    private MutableLiveData<String> getProductIdListUserError = new MutableLiveData<>();

    public MutableLiveData<ProductId> getProductIdListUserSuccess() {
        return getProductIdListUserSuccess;
    }

    public MutableLiveData<String> getProductIdListUserError() {
        return getProductIdListUserError;
    }

    public void getProductWithId(
            String id, String accept, String authorisation, String token) {
        remoteRepository.getProductWithId(id, accept, authorisation, token).enqueue(new ApiCallback<ApiResponseObject<ProductId>>() {
            @Override
            public void onSuccess(Response<ApiResponseObject<ProductId>> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        if (response.body().isError()) {
                            getProductIdListUserError.setValue(response.body().getMessage());
                        } else {
                            getProductIdListUserSuccess.setValue(response.body().getData());
                        }
                    } else {
                        getProductIdListUserError.setValue(response.body().getMessage());
                    }
                } else
                    getProductIdListUserError.setValue(null);
            }

            @Override
            public void onFailure(NetworkException networkException) {
                getProductIdListUserError.setValue(networkException.getDisplayMessage());
            }
        });

    }






    //GetSearch
    private MutableLiveData<Product> getSubCategorySearchUserSuccess = new MutableLiveData<>();
    private MutableLiveData<String> getSubCategorySearchUserError = new MutableLiveData<>();

    public MutableLiveData<Product> getSubCategorySearchUserSuccess() {
        return getSubCategorySearchUserSuccess;
    }

    public MutableLiveData<String> getSubCategorySearchUserError() {
        return getSubCategorySearchUserError;
    }

    public void productSearch(String items_per_page,
                              String page_number,String search_sub_category,String accept, String authorisation, String token) {
        remoteRepository.productSearch(items_per_page,page_number,search_sub_category,accept, authorisation, token).enqueue(new ApiCallback<ApiResponseObject<Product>>() {
            @Override
            public void onSuccess(Response<ApiResponseObject<Product>> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        if (response.body().isError()) {
                            getSubCategorySearchUserError.setValue(response.body().getMessage());
                        } else {
                            getSubCategorySearchUserSuccess.setValue(response.body().getData());
                        }
                    } else {
                        getSubCategorySearchUserError.setValue(response.body().getMessage());
                    }
                } else
                    getSubCategorySearchUserError.setValue(null);
            }

            @Override
            public void onFailure(NetworkException networkException) {
                getSubCategorySearchUserError.setValue(networkException.getDisplayMessage());
            }
        });

    }




    //GetFilter
    private MutableLiveData<Product> getFilterSearchUserSuccess = new MutableLiveData<>();
    private MutableLiveData<String>  getFilterSearchUserError = new MutableLiveData<>();

    public MutableLiveData<Product> getFilterSearchUserSuccess() {
        return getFilterSearchUserSuccess;
    }

    public MutableLiveData<String> getFilterSearchUserError() {
        return getFilterSearchUserError;
    }

    public void productFilter(String sub_category_id,
                              String brand_id,
                              String items_per_page,
                              String page_number,
                              String accept, String authorisation, String token) {
        remoteRepository.productFilter(sub_category_id,brand_id,items_per_page,page_number,accept, authorisation, token).enqueue(new ApiCallback<ApiResponseObject<Product>>() {
            @Override
            public void onSuccess(Response<ApiResponseObject<Product>> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        if (response.body().isError()) {
                            getFilterSearchUserError.setValue(response.body().getMessage());
                        } else {
                            getFilterSearchUserSuccess.setValue(response.body().getData());
                        }
                    } else {
                        getFilterSearchUserError.setValue(response.body().getMessage());
                    }
                } else
                    getFilterSearchUserError.setValue(null);
            }

            @Override
            public void onFailure(NetworkException networkException) {
                getFilterSearchUserError.setValue(networkException.getDisplayMessage());
            }
        });

    }*/


}
