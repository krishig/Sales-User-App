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
import com.krishig.android.model.Category;
import com.krishig.android.model.Customer;
import com.krishig.android.model.Order;
import com.krishig.android.model.Product;
import com.krishig.android.model.ProductBrands;
import com.krishig.android.model.SubCategory;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import okhttp3.RequestBody;
import retrofit2.Response;

@HiltViewModel
public class SubCategoryModel extends ViewModel {

    private LocalRepository localRepository;
    private RemoteRepository remoteRepository;


    @Inject
    public SubCategoryModel(LocalRepository localRepository, RemoteRepository remoteRepository) {
        this.localRepository = localRepository;
        this.remoteRepository = remoteRepository;
    }


    //Get
    private MutableLiveData<SubCategory> getSubCategoryUserSuccess = new MutableLiveData<>();
    private MutableLiveData<String> getSubCategoryUserError = new MutableLiveData<>();

    public MutableLiveData<SubCategory> getSubCategoryUserSuccess() {
        return getSubCategoryUserSuccess;
    }

    public MutableLiveData<String> getSubCategoryUserError() {
        return getSubCategoryUserError;
    }

    public void getSubCategories(String category_id, String accept, String authorisation, String token) {
        remoteRepository.getSubCategories(category_id, accept, authorisation, token).enqueue(new ApiCallback<ApiResponseObject<SubCategory>>() {
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

    //Get
    private MutableLiveData<ArrayList<Category>> getCategoryUserSuccess = new MutableLiveData<>();
    private MutableLiveData<String> getCategoryUserError = new MutableLiveData<>();

    public MutableLiveData<ArrayList<Category>> getCategoryUserSuccess() {
        return getCategoryUserSuccess;
    }

    public MutableLiveData<String> getCategoryUserError() {
        return getCategoryUserError;
    }

    public void getCategories(String accept, String authorisation, String token) {
        remoteRepository.getCategories(accept, authorisation, token).enqueue(new ApiCallback<ApiResponseArray<Category>>() {
            @Override
            public void onSuccess(Response<ApiResponseArray<Category>> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        if (response.body().isError()) {
                            getCategoryUserError.setValue(response.body().getMessage());
                        } else {
                            getCategoryUserSuccess.setValue(response.body().getData());
                        }
                    } else {
                        getCategoryUserError.setValue(response.body().getMessage());
                    }
                } else
                    getCategoryUserError.setValue(null);
            }

            @Override
            public void onFailure(NetworkException networkException) {
                getCategoryUserError.setValue(networkException.getDisplayMessage());
            }
        });

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


    //GetFilter
    private MutableLiveData<Product> getFilterSearchUserSuccess = new MutableLiveData<>();
    private MutableLiveData<String> getFilterSearchUserError = new MutableLiveData<>();

    public MutableLiveData<Product> getFilterSearchUserSuccess() {
        return getFilterSearchUserSuccess;
    }

    public MutableLiveData<String> getFilterSearchUserError() {
        return getFilterSearchUserError;
    }

    public void productFilter(String category_id,
                              String accept, String authorisation, String token) {
        remoteRepository.productFilter(category_id, accept, authorisation, token).enqueue(new ApiCallback<ApiResponseObject<Product>>() {
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

    private MutableLiveData<Customer> searchUserSuccess = new MutableLiveData<>();
    private MutableLiveData<String> searchUserError = new MutableLiveData<>();

    public MutableLiveData<Customer> searchUserSuccess() {
        return searchUserSuccess;
    }

    public MutableLiveData<String> searchUserError() {
        return searchUserError;
    }

    public void searchCustomer(String pageNumber,
                               String pageSize,String findByMobile, String accept, String authorisation, String token) {
        remoteRepository.searchCustomer(pageNumber,pageSize,findByMobile, accept, authorisation, token).enqueue(new ApiCallback<ApiResponseObject<Customer>>() {
            @Override
            public void onSuccess(Response<ApiResponseObject<Customer>> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        if (response.body().isError()) {
                            searchUserError.setValue(response.body().getMessage());
                        } else {
                            searchUserSuccess.setValue(response.body().getData());
                        }
                    } else {
                        searchUserError.setValue(response.body().getMessage());
                    }
                } else
                    searchUserError.setValue(null);
            }

            @Override
            public void onFailure(NetworkException networkException) {
                searchUserError.setValue(networkException.getDisplayMessage());
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


    //ADDTOCart
    private MutableLiveData<Product> addCartUserSuccess = new MutableLiveData<>();
    private MutableLiveData<String> addCartUserError = new MutableLiveData<>();

    public MutableLiveData<Product> addCartUserSuccess() {
        return addCartUserSuccess;
    }

    public MutableLiveData<String> addCartUserError() {
        return addCartUserError;
    }

    public void addProductToCart(RequestBody body, String accept, String authorisation, String token) {
        remoteRepository.addProductToCart(body, accept, authorisation, token).enqueue(new ApiCallback<ApiResponseObject<Product>>() {
            @Override
            public void onSuccess(Response<ApiResponseObject<Product>> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        if (response.body().isError()) {
                            addCartUserError.setValue(response.body().getMessage());
                        } else {
                            addCartUserSuccess.setValue(response.body().getData());
                        }
                    } else {
                        addCartUserError.setValue(response.body().getMessage());
                    }
                } else
                    addCartUserError.setValue(null);
            }

            @Override
            public void onFailure(NetworkException networkException) {
                addCartUserError.setValue(networkException.getDisplayMessage());
            }
        });

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


    //DELETE CART
    private MutableLiveData<Cart> deleteCartUserSuccess = new MutableLiveData<>();
    private MutableLiveData<String> deleteCartUserError = new MutableLiveData<>();

    public MutableLiveData<Cart> deleteCartUserSuccess() {
        return deleteCartUserSuccess;
    }

    public MutableLiveData<String> deleteCartUserError() {
        return deleteCartUserError;
    }

    public void deleteCartProduct(String cartId, String customerId, String accept, String authorisation, String token) {
        remoteRepository.deleteCartProduct(cartId, customerId, accept, authorisation, token).enqueue(new ApiCallback<ApiResponseObject<Cart>>() {
            @Override
            public void onSuccess(Response<ApiResponseObject<Cart>> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        if (response.body().isError()) {
                            deleteCartUserError.setValue(response.body().getMessage());
                        } else {
                            deleteCartUserSuccess.setValue(response.body().getData());
                        }
                    } else {
                        deleteCartUserError.setValue(response.body().getMessage());
                    }
                } else
                    deleteCartUserError.setValue(null);
            }

            @Override
            public void onFailure(NetworkException networkException) {
                deleteCartUserError.setValue(networkException.getDisplayMessage());
            }
        });

    }

    //ADDAddress
    private MutableLiveData<Customer> addAddressUserSuccess = new MutableLiveData<>();
    private MutableLiveData<String> addAddressUserError = new MutableLiveData<>();

    public MutableLiveData<Customer> addAddressUserSuccess() {
        return addAddressUserSuccess;
    }

    public MutableLiveData<String> addAddressUserError() {
        return addAddressUserError;
    }

    public void addAddress(RequestBody body, String accept, String authorisation, String token) {
        remoteRepository.addAddress(body, accept, authorisation, token).enqueue(new ApiCallback<ApiResponseObject<Customer>>() {
            @Override
            public void onSuccess(Response<ApiResponseObject<Customer>> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        if (response.body().isError()) {
                            addAddressUserError.setValue(response.body().getMessage());
                        } else {
                            addAddressUserSuccess.setValue(response.body().getData());
                        }
                    } else {
                        addAddressUserError.setValue(response.body().getMessage());
                    }
                } else
                    addAddressUserError.setValue(null);
            }

            @Override
            public void onFailure(NetworkException networkException) {
                addAddressUserError.setValue(networkException.getDisplayMessage());
            }
        });

    }


    //DELETE ADDRESS
    private MutableLiveData<Cart> deleteAddressUserSuccess = new MutableLiveData<>();
    private MutableLiveData<String> deleteAddressUserError = new MutableLiveData<>();

    public MutableLiveData<Cart> deleteAddressUserSuccess() {
        return deleteAddressUserSuccess;
    }

    public MutableLiveData<String> deleteAddressUserError() {
        return deleteAddressUserError;
    }

    public void deleteAddress(String addressId, String accept, String authorisation, String token) {
        remoteRepository.deleteAddress(addressId, accept, authorisation, token).enqueue(new ApiCallback<ApiResponseObject<Cart>>() {
            @Override
            public void onSuccess(Response<ApiResponseObject<Cart>> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        if (response.body().isError()) {
                            deleteAddressUserError.setValue(response.body().getMessage());
                        } else {
                            deleteAddressUserSuccess.setValue(response.body().getData());
                        }
                    } else {
                        deleteAddressUserError.setValue(response.body().getMessage());
                    }
                } else
                    deleteAddressUserError.setValue(null);
            }

            @Override
            public void onFailure(NetworkException networkException) {
                deleteAddressUserError.setValue(networkException.getDisplayMessage());
            }
        });

    }


    //BookOrder
    private MutableLiveData<Cart> orderBookUserSuccess = new MutableLiveData<>();
    private MutableLiveData<String> orderBookUserError = new MutableLiveData<>();

    public MutableLiveData<Cart> orderBookUserSuccess() {
        return orderBookUserSuccess;
    }

    public MutableLiveData<String> orderBookUserError() {
        return orderBookUserError;
    }

    public void orderBook(RequestBody body, String accept, String authorisation, String token) {
        remoteRepository.orderBook(body, accept, authorisation, token).enqueue(new ApiCallback<ApiResponseObject<Cart>>() {
            @Override
            public void onSuccess(Response<ApiResponseObject<Cart>> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        if (response.body().isError()) {
                            orderBookUserError.setValue(response.body().getMessage());
                        } else {
                            orderBookUserSuccess.setValue(response.body().getData());
                        }
                    } else {
                        orderBookUserError.setValue(response.body().getMessage());
                    }
                } else
                    orderBookUserError.setValue(null);
            }

            @Override
            public void onFailure(NetworkException networkException) {
                orderBookUserError.setValue(networkException.getDisplayMessage());
            }
        });

    }


    //getAllOrder
    private MutableLiveData<Order> getOrderBookUserSuccess = new MutableLiveData<>();
    private MutableLiveData<String> getOrderBookUserError = new MutableLiveData<>();

    public MutableLiveData<Order> getOrderBookUserSuccess() {
        return getOrderBookUserSuccess;
    }

    public MutableLiveData<String> getOrderBookUserError() {
        return getOrderBookUserError;
    }

    public void getAllOrder(String customerId,String page_number, String items_per_page,
                            String accept, String authorisation, String token) {
        remoteRepository.getAllOrder(customerId,page_number,items_per_page , accept, authorisation, token).enqueue(new ApiCallback<ApiResponseObject<Order>>() {
            @Override
            public void onSuccess(Response<ApiResponseObject<Order>> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        if (response.body().isError()) {
                            getOrderBookUserError.setValue(response.body().getMessage());
                        } else {
                            getOrderBookUserSuccess.setValue(response.body().getData());
                        }
                    } else {
                        getOrderBookUserError.setValue(response.body().getMessage());
                    }
                } else
                    getOrderBookUserError.setValue(null);
            }

            @Override
            public void onFailure(NetworkException networkException) {
                getOrderBookUserError.setValue(networkException.getDisplayMessage());
            }
        });

    }


    //searchAllOrder
    private MutableLiveData<Order> searchOrderUserSuccess = new MutableLiveData<>();
    private MutableLiveData<String> searchOrderUserError = new MutableLiveData<>();

    public MutableLiveData<Order> searchOrderUserSuccess() {
        return searchOrderUserSuccess;
    }

    public MutableLiveData<String> searchOrderUserError() {
        return searchOrderUserError;
    }

    public void searchOrder(String items_per_page,
                            String page_number, String orderId, String accept, String authorisation, String token) {
        remoteRepository.searchOrder(items_per_page, page_number, orderId, accept, authorisation, token).enqueue(new ApiCallback<ApiResponseObject<Order>>() {
            @Override
            public void onSuccess(Response<ApiResponseObject<Order>> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        if (response.body().isError()) {
                            searchOrderUserError.setValue(response.body().getMessage());
                        } else {
                            searchOrderUserSuccess.setValue(response.body().getData());
                        }
                    } else {
                        searchOrderUserError.setValue(response.body().getMessage());
                    }
                } else
                    searchOrderUserError.setValue(null);
            }

            @Override
            public void onFailure(NetworkException networkException) {
                searchOrderUserError.setValue(networkException.getDisplayMessage());
            }
        });

    }


    //searchAllOrder
    private MutableLiveData<Order> fetchOrderUserSuccess = new MutableLiveData<>();
    private MutableLiveData<String> fetchOrderUserError = new MutableLiveData<>();

    public MutableLiveData<Order> fetchOrderUserSuccess() {
        return fetchOrderUserSuccess;
    }

    public MutableLiveData<String> fetchOrderUserError() {
        return fetchOrderUserError;
    }

    public void fetchOrderDetail(String orderId, String accept, String authorisation, String token) {
        remoteRepository.fetchOrderDetail(orderId, accept, authorisation, token).enqueue(new ApiCallback<ApiResponseObject<Order>>() {
            @Override
            public void onSuccess(Response<ApiResponseObject<Order>> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        if (response.body().isError()) {
                            fetchOrderUserError.setValue(response.body().getMessage());
                        } else {
                            fetchOrderUserSuccess.setValue(response.body().getData());
                        }
                    } else {
                        fetchOrderUserError.setValue(response.body().getMessage());
                    }
                } else
                    fetchOrderUserError.setValue(null);
            }

            @Override
            public void onFailure(NetworkException networkException) {
                fetchOrderUserError.setValue(networkException.getDisplayMessage());
            }
        });

    }

}
