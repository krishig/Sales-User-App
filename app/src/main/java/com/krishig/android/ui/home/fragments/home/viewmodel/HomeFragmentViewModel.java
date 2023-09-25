package com.krishig.android.ui.home.fragments.home.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.krishig.android.data.remote.ApiCallback;
import com.krishig.android.data.remote.ApiResponseArray;
import com.krishig.android.data.remote.ApiResponseObject;
import com.krishig.android.data.remote.exception.NetworkException;
import com.krishig.android.data.repository.LocalRepository;
import com.krishig.android.data.repository.RemoteRepository;
import com.krishig.android.model.Cart;
import com.krishig.android.model.Customer;
import com.krishig.android.model.Home;
import com.krishig.android.model.Market;
import com.krishig.android.model.Product;
import com.krishig.android.model.User;
import com.krishig.android.ui.home.fragments.home.adapter.SliderItem;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import okhttp3.RequestBody;
import retrofit2.Response;

@HiltViewModel
public class HomeFragmentViewModel extends ViewModel {

    private LocalRepository localRepository;
    private RemoteRepository remoteRepository;

    private MutableLiveData<ArrayList<SliderItem>> userSuccess = new MutableLiveData<>();
    private MutableLiveData<String> userError = new MutableLiveData<>();


    @Inject
    public HomeFragmentViewModel(LocalRepository localRepository, RemoteRepository remoteRepository) {
        this.localRepository = localRepository;
        this.remoteRepository = remoteRepository;
    }

    public MutableLiveData<ArrayList<SliderItem>> userSuccess() {
        return userSuccess;
    }

    public MutableLiveData<String> userError() {
        return userError;
    }


    //ADD
    private MutableLiveData<Customer> addCustomerUserSuccess = new MutableLiveData<>();
    private MutableLiveData<String> addCustomerUserError = new MutableLiveData<>();

    public MutableLiveData<Customer> addCustomerUserSuccess() {
        return addCustomerUserSuccess;
    }

    public MutableLiveData<String> addCustomerUserError() {
        return addCustomerUserError;
    }

    public void addCustomer(RequestBody body, String accept, String authorisation, String token) {
        remoteRepository.addCustomer(body, accept, authorisation, token).enqueue(new ApiCallback<ApiResponseObject<Customer>>() {
            @Override
            public void onSuccess(Response<ApiResponseObject<Customer>> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        if (response.body().isError()) {
                            addCustomerUserError.setValue(response.body().getMessage());
                        } else {
                            addCustomerUserSuccess.setValue(response.body().getData());
                        }
                    } else {
                        addCustomerUserError.setValue(response.body().getMessage());
                    }
                } else
                    addCustomerUserError.setValue(null);
            }

            @Override
            public void onFailure(NetworkException networkException) {
                addCustomerUserError.setValue(networkException.getDisplayMessage());
            }
        });

    }

    //GetCustomerId
    private MutableLiveData<Customer.Content> getCustomerWithIdUserSuccess = new MutableLiveData<>();
    private MutableLiveData<String> getCustomerWithIdUserError = new MutableLiveData<>();

    public MutableLiveData<Customer.Content> getCustomerWithIdUserSuccess() {
        return getCustomerWithIdUserSuccess;
    }

    public MutableLiveData<String> getCustomerWithIdUserError() {
        return getCustomerWithIdUserError;
    }

    public void getCustomerWithId(String customerId, String accept, String authorisation, String token) {
        remoteRepository.getCustomerWithId(customerId, accept, authorisation, token).enqueue(new ApiCallback<ApiResponseObject<Customer.Content>>() {
            @Override
            public void onSuccess(Response<ApiResponseObject<Customer.Content>> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        if (response.body().isError()) {
                            getCustomerWithIdUserError.setValue(response.body().getMessage());
                        } else {
                            getCustomerWithIdUserSuccess.setValue(response.body().getData());
                        }
                    } else {
                        getCustomerWithIdUserError.setValue(response.body().getMessage());
                    }
                } else
                    getCustomerWithIdUserError.setValue(null);
            }

            @Override
            public void onFailure(NetworkException networkException) {
                getCustomerWithIdUserError.setValue(networkException.getDisplayMessage());
            }
        });

    }


    //PUT
    private MutableLiveData<Customer> updateCustomerUserSuccess = new MutableLiveData<>();
    private MutableLiveData<String> updateCustomerUserError = new MutableLiveData<>();

    public MutableLiveData<Customer> updateCustomerUserSuccess() {
        return updateCustomerUserSuccess;
    }

    public MutableLiveData<String> updateCustomerUserError() {
        return updateCustomerUserError;
    }

    public void putCustomer(String customerId, RequestBody body, String accept, String authorisation, String token) {
        remoteRepository.putCustomer(customerId, body, accept, authorisation, token).enqueue(new ApiCallback<ApiResponseObject<Customer>>() {
            @Override
            public void onSuccess(Response<ApiResponseObject<Customer>> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        if (response.body().isError()) {
                            updateCustomerUserError.setValue(response.body().getMessage());
                        } else {
                            updateCustomerUserSuccess.setValue(response.body().getData());
                        }
                    } else {
                        updateCustomerUserError.setValue(response.body().getMessage());
                    }
                } else
                    updateCustomerUserError.setValue(null);
            }

            @Override
            public void onFailure(NetworkException networkException) {
                updateCustomerUserError.setValue(networkException.getDisplayMessage());
            }
        });

    }



    //GetCustomer
    private MutableLiveData<Customer> getCustomerUserSuccess = new MutableLiveData<>();
    private MutableLiveData<String> getCustomerUserError = new MutableLiveData<>();

    public MutableLiveData<Customer> getCustomerUserSuccess() {
        return getCustomerUserSuccess;
    }

    public MutableLiveData<String> getCustomerUserError() {
        return getCustomerUserError;
    }

    public void getCustomer(String pageNumber,
                            String pageSize, String accept, String authorisation, String token) {
        remoteRepository.getCustomer(pageNumber,
                pageSize, accept, authorisation, token).enqueue(new ApiCallback<ApiResponseObject<Customer>>() {
            @Override
            public void onSuccess(Response<ApiResponseObject<Customer>> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        if (response.body().isError()) {
                            getCustomerUserError.setValue(response.body().getMessage());
                        } else {
                            getCustomerUserSuccess.setValue(response.body().getData());
                        }
                    } else {
                        getCustomerUserError.setValue(response.body().getMessage());
                    }
                } else
                    getCustomerUserError.setValue(null);
            }

            @Override
            public void onFailure(NetworkException networkException) {
                getCustomerUserError.setValue(networkException.getDisplayMessage());
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

    //GET CART
    private MutableLiveData<Cart> getCartUserSuccess = new MutableLiveData<>();
    private MutableLiveData<String> getCartUserError = new MutableLiveData<>();

    public MutableLiveData<Cart> getCartUserSuccess() {
        return getCartUserSuccess;
    }

    public MutableLiveData<String> getCartUserError() {
        return getCartUserError;
    }

    public void getCartProduct(String customerId, String accept, String authorisation, String token) {
        remoteRepository.getCartProduct(customerId, accept, authorisation, token).enqueue(new ApiCallback<ApiResponseObject<Cart>>() {
            @Override
            public void onSuccess(Response<ApiResponseObject<Cart>> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        if (response.body().isError()) {
                            getCartUserError.setValue(response.body().getMessage());
                        } else {
                            getCartUserSuccess.setValue(response.body().getData());
                        }
                    } else {
                        getCartUserError.setValue(response.body().getMessage());
                    }
                } else
                    getCartUserError.setValue(null);
            }

            @Override
            public void onFailure(NetworkException networkException) {
                getCartUserError.setValue(networkException.getDisplayMessage());
            }
        });

    }


}
