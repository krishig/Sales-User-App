package com.krishig.android.ui.addToBag.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.krishig.android.Navigator.activity.ActivityNavigator;
import com.krishig.android.Navigator.fragment.FragmentNavigator;
import com.krishig.android.R;
import com.krishig.android.data.local.sharedpreferences.SharedPreferencesHelper;
import com.krishig.android.data.remote.RequestUtils;
import com.krishig.android.data.remote.api.ApiService;
import com.krishig.android.data.remote.helper.RetrofitClient;
import com.krishig.android.databinding.FragmentAddToBinding;
import com.krishig.android.model.Cart;
import com.krishig.android.model.Customer;
import com.krishig.android.model.PostalPincodeResponse;
import com.krishig.android.model.Product;
import com.krishig.android.ui.AppConstants;
import com.krishig.android.ui.KeyboardVisibility;
import com.krishig.android.ui.Order.OrderActivity;
import com.krishig.android.ui.addToBag.adapter.AddToBagAdapter;
import com.krishig.android.ui.addToBag.adapter.MainAdapter;
import com.krishig.android.ui.addToBag.adapter.RadioButtonCheck;
import com.krishig.android.ui.addToBag.adapter.deleteAddress;
import com.krishig.android.ui.base.BaseFragment;
import com.krishig.android.ui.home.fragments.home.viewmodel.SubCategoryModel;
import com.krishig.android.ui.home.fragments.manageProduct.view.ManageProductFragment;
import com.krishig.android.ui.home.view.HomeActivity;
import com.library.adapter.recyclerview.listener.OnRecyclerViewItemChildClick;
import com.library.utilities.ValidationUtils;
import com.library.utilities.activity.ActivityUtils;

import org.json.JSONObject;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class AddToFragment extends BaseFragment<FragmentAddToBinding> implements RadioButtonCheck, deleteAddress {

    AddToBagAdapter addToBagAdapter;
    MainAdapter addressAdapter;
    ArrayList<Cart.CartProductResponseDto> cartArrayList = new ArrayList<>();
    ArrayList<Customer.Address> addressArraylist = new ArrayList<>();
    SubCategoryModel viewModel;
    private ApiService postalPincodeApi;
    Dialog dialog;
    @Inject
    SharedPreferencesHelper sharedPreferencesHelper;


    //DialogId
    Button materialButton;
    ImageView imageView;

    TextInputLayout houseNumberInputLayout;
    TextInputLayout streetNameInputLayout;
    TextInputLayout villageNameInputLayout;
    TextInputLayout pinCodeInputLayout;
    TextInputLayout districtInputLayout;
    TextInputLayout stateInputLayout;

    TextInputEditText houseNumberTextInputEditText;
    TextInputEditText streetNameTextInputEditText;
    TextInputEditText villageNameTextInputEditText;
    TextInputEditText pinCodeTextInputEditText;
    TextInputEditText districtTextInputEditText;
    TextInputEditText stateTextInputEditText;

    String mobileNumber = "", customerId = "", addressId = "", totalPrice = "";
    int size;


    Dialog failedDialog;

    @Override
    protected ActivityNavigator getActivityNavigator() {
        return new ActivityNavigator(getActivity());
    }

    @Override
    protected FragmentNavigator getFragmentNavigator() {
        return new FragmentNavigator(getActivity().getSupportFragmentManager(), R.id.nav_host_fragment_content_main);

    }

    @Override
    protected FragmentAddToBinding getViewBinding() {
        return FragmentAddToBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initializeViewModel() {
        viewModel = createViewModel(SubCategoryModel.class);
    }

    @Override
    protected void initializeToolBar() {

    }

    @Override
    protected void initializeObject() {
        registerKeyboardVisibilityListener(getActivity());

        setRecyclerView();
        dialog = new Dialog(getContext(), R.style.MyDialogThemePhoneNumber);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.mobile_verification_layout);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.curve_phone_number);
        materialButton = dialog.findViewById(R.id.submitButton);
        imageView = dialog.findViewById(R.id.icBack);

        houseNumberInputLayout = dialog.findViewById(R.id.houseNumberInputLayout);
        streetNameInputLayout = dialog.findViewById(R.id.streetNameInputLayout);
        villageNameInputLayout = dialog.findViewById(R.id.villageNameInputLayout);
        pinCodeInputLayout = dialog.findViewById(R.id.pinCodeInputLayout);
        districtInputLayout = dialog.findViewById(R.id.districtInputLayout);
        stateInputLayout = dialog.findViewById(R.id.stateInputLayout);

        houseNumberTextInputEditText = dialog.findViewById(R.id.houseNumberTextInputEditText);
        streetNameTextInputEditText = dialog.findViewById(R.id.streetNameTextInputEditText);
        villageNameTextInputEditText = dialog.findViewById(R.id.villageNameTextInputEditText);
        pinCodeTextInputEditText = dialog.findViewById(R.id.pinCodeTextInputEditText);
        districtTextInputEditText = dialog.findViewById(R.id.districtTextInputEditText);
        stateTextInputEditText = dialog.findViewById(R.id.stateTextInputEditText);

        failedDialog = new Dialog(getContext(), R.style.MyDialogThemePhoneNumber);
        failedDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        failedDialog.setCancelable(false);
        failedDialog.setContentView(R.layout.order_failed);
        failedDialog.getWindow().setBackgroundDrawableResource(R.drawable.curve_phone_number);
    }

    private void setRecyclerView() {
        addToBagAdapter = new AddToBagAdapter(getContext());
        viewBinding.recyclerView.setHasFixedSize(true);
        viewBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        addToBagAdapter.addArrayList(cartArrayList);
        viewBinding.recyclerView.setAdapter(addToBagAdapter);
    }

    private void setRecyclerViewAddress() {
        addressAdapter = new MainAdapter(addressArraylist, this, this);
        viewBinding.recyclerViewAddress.setHasFixedSize(true);
        viewBinding.recyclerViewAddress.setLayoutManager(new LinearLayoutManager(getContext()));
        viewBinding.recyclerViewAddress.setAdapter(addressAdapter);
    }

    @Override
    protected void addTextChangedListener() {

    }

    @Override
    protected void setOnFocusChangeListener() {

    }

    @Override
    protected void observeViewModel() {
        Observer<String> getCustomerWithIdUserError = new Observer<String>() {
            @Override
            public void onChanged(@Nullable String error) {
                hideProgressDialog();
                if (error == null) {
                    //showToast(getString(R.string.something_went_wrong_please_try_again));
                } else {
                    //  showToast(error);
                }
            }
        };
        viewModel.getCustomerWithIdUserError().observe(this, getCustomerWithIdUserError);

        final Observer<Customer.Content> getCustomerWithIdUserSuccess = new Observer<Customer.Content>() {
            @Override
            public void onChanged(Customer.Content list) {
                hideProgressDialog();
                addressArraylist.clear();
                addressArraylist = list.getAddress();
                setRecyclerViewAddress();

                customerId = list.getCustomerCartResponseDto().getCustomerId();
            }
        };
        viewModel.getCustomerWithIdUserSuccess().observe(this, getCustomerWithIdUserSuccess);

        Observer<String> getCartUserError = new Observer<String>() {
            @Override
            public void onChanged(@Nullable String error) {
                hideProgressDialog();
                if (error == null) {
                    //  showToast(getString(R.string.something_went_wrong_please_try_again));
                } else {
                    // showToast(error);
                }
            }
        };
        viewModel.getCartUserError().observe(this, getCartUserError);

        final Observer<Cart> getCartUserSuccess = new Observer<Cart>() {
            @Override
            public void onChanged(Cart cart) {
                hideProgressDialog();
                size = cart.getCartProductResponseDtoList().size();
                if (size == 0) {
                    viewBinding.recyclerView.setVisibility(View.GONE);
                    viewBinding.errorImageView.setVisibility(View.VISIBLE);
                    viewBinding.errorTextView.setVisibility(View.VISIBLE);
                } else {
                    viewBinding.recyclerView.setVisibility(View.VISIBLE);
                    viewBinding.errorImageView.setVisibility(View.GONE);
                    viewBinding.errorTextView.setVisibility(View.GONE);
                    cartArrayList = cart.getCartProductResponseDtoList();
                    addToBagAdapter.clearAllItem();
                    addToBagAdapter.addArrayList(cartArrayList);
                }
                totalPrice = cart.getTotalPrice();
                viewBinding.doubleDigitDefineTextView.setText("Rs. " + cart.getTotalPrice());
            }
        };
        viewModel.getCartUserSuccess().observe(this, getCartUserSuccess);


        Observer<String> deleteCartUserError = new Observer<String>() {
            @Override
            public void onChanged(@Nullable String error) {
                hideProgressDialog();
                if (error == null) {
                    //showToast(getString(R.string.something_went_wrong_please_try_again));
                } else {
                    //showToast(error);
                }
            }
        };
        viewModel.deleteCartUserError().observe(this, deleteCartUserError);

        final Observer<Cart> deleteCartUserSuccess = new Observer<Cart>() {
            @Override
            public void onChanged(Cart cart) {
                hideProgressDialog();
                viewModel.getCartProduct(sharedPreferencesHelper.getCustomerCartId(), "application/json", "application/json",
                        sharedPreferencesHelper.getKeyToken());
                showProgressDialog();

            }
        };
        viewModel.deleteCartUserSuccess().observe(this, deleteCartUserSuccess);

        Observer<String> addCartUserError = new Observer<String>() {
            @Override
            public void onChanged(@Nullable String error) {
                hideProgressDialog();
                if (error == null) {
                    //showToast(getString(R.string.something_went_wrong_please_try_again));
                } else {
                    //showToast(error);
                }
            }
        };
        viewModel.addCartUserError().observe(this, addCartUserError);

        final Observer<Product> addCartUserSuccess = new Observer<Product>() {
            @Override
            public void onChanged(Product product) {
                hideProgressDialog();
                totalPrice = product.getTotalPrice();
                viewBinding.doubleDigitDefineTextView.setText("Rs. " + product.getTotalPrice());

            }
        };
        viewModel.addCartUserSuccess().observe(this, addCartUserSuccess);


        Observer<String> addAddressUserError = new Observer<String>() {
            @Override
            public void onChanged(@Nullable String error) {
                hideProgressDialog();
                if (error == null) {
                    //showToast(getString(R.string.something_went_wrong_please_try_again));
                } else {
                    //showToast(error);
                }
            }
        };
        viewModel.addAddressUserError().observe(this, addAddressUserError);

        final Observer<Customer> addAddressUserSuccess = new Observer<Customer>() {
            @Override
            public void onChanged(Customer employees) {
                hideProgressDialog();
                dialog.dismiss();
                viewModel.getCustomerWithId(sharedPreferencesHelper.getCustomerId(), "application/json", "application/json",
                        sharedPreferencesHelper.getKeyToken());
                showProgressDialog();
            }
        };
        viewModel.addAddressUserSuccess().observe(this, addAddressUserSuccess);


        Observer<String> deleteAddressUserError = new Observer<String>() {
            @Override
            public void onChanged(@Nullable String error) {
                hideProgressDialog();
                if (error == null) {
                    //showToast(getString(R.string.something_went_wrong_please_try_again));
                } else {
                    //showToast(error);
                }
            }
        };
        viewModel.deleteAddressUserError().observe(this, deleteAddressUserError);

        final Observer<Cart> deleteAddressUserSuccess = new Observer<Cart>() {
            @Override
            public void onChanged(Cart cart) {
                hideProgressDialog();
                viewModel.getCustomerWithId(sharedPreferencesHelper.getCustomerId(), "application/json", "application/json",
                        sharedPreferencesHelper.getKeyToken());
                showProgressDialog();
            }
        };
        viewModel.deleteAddressUserSuccess().observe(this, deleteAddressUserSuccess);

        Observer<String> orderBookUserError = new Observer<String>() {
            @Override
            public void onChanged(@Nullable String error) {
                hideProgressDialog();
                if (error == null) {
                    //showToast(getString(R.string.something_went_wrong_please_try_again));
                } else {
                    //showToast(error);
                }
                dialogOrderFailed();
            }
        };
        viewModel.orderBookUserError().observe(this, orderBookUserError);

        final Observer<Cart> orderBookUserSuccess = new Observer<Cart>() {
            @Override
            public void onChanged(Cart cart) {
                hideProgressDialog();
                Intent intent = ActivityUtils.launchActivityWithClearBackStack(getActivity(), OrderActivity.class);
                intent.putExtra(AppConstants.Extras.ORDER_ID, cart.getOrderId());
                startActivity(intent);
            }
        };
        viewModel.orderBookUserSuccess().observe(this, orderBookUserSuccess);

    }

    @Override
    protected void setOnClickListener() {

        viewBinding.changeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog();
            }
        });

        addToBagAdapter.setOnRecyclerViewItemChildClick(new OnRecyclerViewItemChildClick<Cart.CartProductResponseDto>() {
            @Override
            public void OnItemChildClick(View viewChild, Cart.CartProductResponseDto cartProductResponseDto, int position) {
                switch (viewChild.getId()) {
                    case R.id.addMaterialCardView:
                        int count = Integer.parseInt(cartProductResponseDto.getProductQuantity());
                        ++count;
                        try {
                            JSONObject jsonObject = new JSONObject();
                            JSONObject jsonObject1 = new JSONObject();
                            jsonObject1.put("id", cartProductResponseDto.getProductResponseDto().getId());
                            jsonObject.put("id", cartProductResponseDto.getId());
                            jsonObject.put("cartId", sharedPreferencesHelper.getCustomerCartId());
                            jsonObject.put("product", jsonObject1);
                            jsonObject.put("productQuantity", count);
                            RequestBody requestBody = RequestUtils.createRequestBodyForString(jsonObject.toString());

                            viewModel.addProductToCart(requestBody, "application/json", "application/json",
                                    sharedPreferencesHelper.getKeyToken());
                            showProgressDialog();
                        } catch (Exception exception) {
                            Log.e("", "" + exception);
                        }
                        cartProductResponseDto.setProductQuantity(String.valueOf(count));
                        addToBagAdapter.notifyItemChanged(position);
                        break;
                    case R.id.minusMaterialCardView:
                        int count2 = Integer.parseInt(cartProductResponseDto.getProductQuantity());
                        if (!String.valueOf(count2).equalsIgnoreCase("1")) {
                            --count2;
                            try {
                                JSONObject jsonObject = new JSONObject();
                                JSONObject jsonObject1 = new JSONObject();
                                jsonObject1.put("id", cartProductResponseDto.getProductResponseDto().getId());
                                jsonObject.put("id", cartProductResponseDto.getId());
                                jsonObject.put("cartId", sharedPreferencesHelper.getCustomerCartId());
                                jsonObject.put("product", jsonObject1);
                                jsonObject.put("productQuantity", count2);
                                RequestBody requestBody = RequestUtils.createRequestBodyForString(jsonObject.toString());

                                viewModel.addProductToCart(requestBody, "application/json", "application/json",
                                        sharedPreferencesHelper.getKeyToken());
                                showProgressDialog();
                            } catch (Exception exception) {
                                Log.e("", "" + exception);
                            }
                            cartProductResponseDto.setProductQuantity(String.valueOf(count2));
                            addToBagAdapter.notifyItemChanged(position);
                        } else {
                            viewModel.deleteCartProduct(sharedPreferencesHelper.getCustomerCartId(), cartProductResponseDto.getId(), "application/json", "application/json",
                                    sharedPreferencesHelper.getKeyToken());
                            showProgressDialog();
                        }
                        break;
                    default:
                        break;
                }
            }
        });

        viewBinding.placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobileNumber = viewBinding.mobileNumberTextInputEditText.getText().toString();
                if (loginRegisterValidation(mobileNumber)) {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        JSONObject jsonObject1 = new JSONObject();

                        jsonObject.put("status", "OPEN");
                        jsonObject.put("customerId", jsonObject1);
                        jsonObject.put("totalPrice", totalPrice);
                        jsonObject.put("paymentMethod", "101");
                        jsonObject.put("contactNumber", mobileNumber);
                        jsonObject.put("addressId", addressId);

                        jsonObject1.put("id", customerId);


                        RequestBody requestBody = RequestUtils.createRequestBodyForString(jsonObject.toString());
                        viewModel.orderBook(requestBody, "application/json", "application/json",
                                sharedPreferencesHelper.getKeyToken());
                        showProgressDialog();
                    } catch (Exception exception) {
                        Log.e("", "" + exception);
                    }
                }

            }
        });

/*
        addressAdapter.setOnRecyclerViewItemChildClick(new OnRecyclerViewItemChildClick<Customer.Address>() {
            @Override
            public void OnItemChildClick(View viewChild, Customer.Address address, int position) {
                switch (viewChild.getId()) {
                    case R.id.imageDelete:
                        alertDialogConfirmExit(getActivity(), address.getId());
                        break;
                    default:
                        break;
                }
            }
        });
*/

        viewBinding.addMoreProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentNavigator.popAll();
                ManageProductFragment manageProductFragment = new ManageProductFragment();
                fragmentNavigator.push(manageProductFragment, false, true);
            }
        });

    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public void showProgressDialog() {
        if (viewBinding.progressDialog.pleaseWaitProgressBar.getVisibility() == View.GONE) {
            viewBinding.progressDialog.pleaseWaitProgressBar.setVisibility(View.VISIBLE);
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }

    }

    @Override
    public void hideProgressDialog() {
        if (viewBinding.progressDialog.pleaseWaitProgressBar.getVisibility() == View.VISIBLE) {
            viewBinding.progressDialog.pleaseWaitProgressBar.setVisibility(View.GONE);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    public void dialog() {
        houseNumberTextInputEditText.setText("");
        streetNameTextInputEditText.setText("");
        villageNameTextInputEditText.setText("");
        pinCodeTextInputEditText.setText("");
        districtTextInputEditText.setText("");
        stateTextInputEditText.setText("");
        pinCodeInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence text, int start, int count, int after) {
                if (text.length() < 1) {
                    pinCodeInputLayout.setErrorEnabled(true);
                    pinCodeInputLayout.setError("Please Enter PinCode Number");
                }
                if (text.length() > 0) {
                    pinCodeInputLayout.setError(null);
                    pinCodeInputLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String data = s.toString();
                getSuperHeroes(data);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });

        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String houseNumber = houseNumberTextInputEditText.getText().toString().trim();
                String streetName = streetNameTextInputEditText.getText().toString().trim();
                String villageName = villageNameTextInputEditText.getText().toString().trim();
                String pinCode = pinCodeTextInputEditText.getText().toString().trim();
                String district = districtTextInputEditText.getText().toString().trim();
                String state = stateTextInputEditText.getText().toString().trim();
                if (loginRegisterValidation(houseNumber, streetName, villageName, pinCode, district, state)) {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        JSONObject jsonObject1 = new JSONObject();
                        jsonObject.put("houseNumber", houseNumber);
                        jsonObject.put("streetName", streetName);
                        jsonObject.put("villageName", villageName);
                        jsonObject.put("district", district);
                        jsonObject.put("state", state);
                        jsonObject.put("postalCode", pinCode);
                        jsonObject.put("customer", jsonObject1);

                        jsonObject1.put("id", sharedPreferencesHelper.getCustomerId());

                        RequestBody requestBody = RequestUtils.createRequestBodyForString(jsonObject.toString());
                        viewModel.addAddress(requestBody, "application/json", "application/json",
                                sharedPreferencesHelper.getKeyToken());
                    } catch (Exception exception) {
                        System.out.println(exception);
                    }
                }

            }
        });

        dialog.show();
    }

    public void dialogOrderFailed() {
        MaterialButton materialButton = failedDialog.findViewById(R.id.loginButton);
        ImageView imageView = failedDialog.findViewById(R.id.icBack);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                failedDialog.dismiss();

            }
        });


        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                failedDialog.dismiss();

            }
        });


        failedDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.getCartProduct(sharedPreferencesHelper.getCustomerCartId(), "application/json", "application/json",
                sharedPreferencesHelper.getKeyToken());
        showProgressDialog();

        viewModel.getCustomerWithId(sharedPreferencesHelper.getCustomerId(), "application/json", "application/json",
                sharedPreferencesHelper.getKeyToken());
        showProgressDialog();
    }

    private void getSuperHeroes(String data) {
        postalPincodeApi = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        // Example API call
        Call<ArrayList<PostalPincodeResponse>> call = postalPincodeApi.getPincodeDetails(data);
        Log.e("", "call  =" + call);
        String requestUrl = call.request().url().toString();
        String requestParameters = call.request().url().query();

        call.enqueue(new Callback<ArrayList<PostalPincodeResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<PostalPincodeResponse>> call, Response<ArrayList<PostalPincodeResponse>> response) {

                ArrayList<PostalPincodeResponse> postalPincodeResponse = response.body();

                if (postalPincodeResponse != null) {
                    if (postalPincodeResponse.get(0).getPostOffice() != null) {
                        districtTextInputEditText.setText(postalPincodeResponse.get(0).getPostOffice().get(0).getDistrict());
                        stateTextInputEditText.setText(postalPincodeResponse.get(0).getPostOffice().get(0).getState());
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<PostalPincodeResponse>> call, Throwable t) {
                // Handle network or API call failure
                Log.e("", "onFailure  =" + call.toString());
                Log.e("", "onFailure  =" + t);

            }
        });
    }


    private boolean loginRegisterValidation(String houseNumber, String streetName, String villageName,
                                            String pinCode, String district, String state) {
        boolean isValid = true;

        if (houseNumber.equalsIgnoreCase("")) {
            houseNumberInputLayout.setError("Please Enter House Number");
            isValid = false;
        } else {
            houseNumberInputLayout.setError("");
        }
        if (streetName.equalsIgnoreCase("")) {
            streetNameInputLayout.setError("Please Enter Street Name");
            isValid = false;
        } else {
            streetNameInputLayout.setError("");
        }
        if (villageName.equalsIgnoreCase("")) {
            villageNameInputLayout.setError("Please Enter Village Name");
            isValid = false;
        } else {
            villageNameInputLayout.setError("");
        }
        if (pinCode.equalsIgnoreCase("")) {
            pinCodeInputLayout.setError("Please Enter PinCode Number");
            isValid = false;
        } else {
            pinCodeInputLayout.setError("");
        }
        if (district.equalsIgnoreCase("")) {
            districtInputLayout.setError("Please Enter District");
            isValid = false;
        } else {
            districtInputLayout.setError("");
        }
        if (state.equalsIgnoreCase("")) {
            stateInputLayout.setError("Please Enter State");
            isValid = false;
        } else {
            stateInputLayout.setError("");
        }

        return isValid;
    }

    private void alertDialogConfirmExit(Activity activity, String id) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);

        alertDialogBuilder.setIcon(R.drawable.ic_black_question_mark);
        alertDialogBuilder.setTitle("Confirm Exit");
        alertDialogBuilder.setMessage("Are you sure you want to Delete?");


        alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                if (addressId.equalsIgnoreCase(id)) {
                    addressId = "";
                }
                viewModel.deleteAddress(id, "application/json", "application/json",
                        sharedPreferencesHelper.getKeyToken());
                showProgressDialog();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

        Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);

        positiveButton.setTextColor(Color.parseColor("#FFFFFF"));
        positiveButton.setBackgroundColor(Color.parseColor("#000000"));

        negativeButton.setTextColor(Color.parseColor("#FFFFFF"));
        negativeButton.setBackgroundColor(Color.parseColor("#000000"));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        params.setMargins(20, 0, 0, 0);
        positiveButton.setLayoutParams(params);
    }

    private boolean loginRegisterValidation(String mobile) {
        boolean isValid = true;
        int phoneValidCode = ValidationUtils.isPhoneNumberValid(mobile);

        if (size == 0) {
            showSnackBar(viewBinding.addFragment, "First Add Product To Cart");
            //showToast("First Add Product To Cart");
            isValid = false;
        }
        if (addressId.equalsIgnoreCase("")) {
            isValid = false;
            showSnackBar(viewBinding.addFragment, "Please First Select Address");
            //showToast("Please First Select Address");
        }
        if (phoneValidCode > 0) {
            if (phoneValidCode == 1) {
                viewBinding.mobileNumberInputLayout.setError(getString(R.string.login_register_error_phone_number_one));
                isValid = false;
            } else if (phoneValidCode == 2) {
                viewBinding.mobileNumberInputLayout.setError(getString(R.string.login_register_error_phone_number_two));
                isValid = false;
            }
        } else {
            viewBinding.mobileNumberInputLayout.setError(null);
            viewBinding.mobileNumberInputLayout.setErrorEnabled(false);
        }


        return isValid;
    }

    @Override
    public void radio(String addressId) {
        this.addressId = addressId;
        addressAdapter.notifyDataSetChanged();
    }

    @Override
    public void check(String id) {
        alertDialogConfirmExit(getActivity(), id);
    }

    public void registerKeyboardVisibilityListener(Activity activity) {
        unRegisterKeyboardVisibilityListener();
        KeyboardVisibility.addKeyboardToggleListener(activity, new KeyboardVisibility.SoftKeyboardToggleListener() {
            @Override
            public void onToggleSoftKeyboard(boolean isVisible) {
                if (isVisible) {
                    if (getActivity() instanceof HomeActivity) {
                        HomeActivity leftNavDrawerActivity = (HomeActivity) getActivity();
                        leftNavDrawerActivity.hideBottomNavigation();
                    }
                } else {
                    if (getActivity() instanceof HomeActivity) {
                        HomeActivity leftNavDrawerActivity = (HomeActivity) getActivity();
                        leftNavDrawerActivity.showBottomNavigation();
                    }
                }
            }
        });
    }

    public void unRegisterKeyboardVisibilityListener() {
        KeyboardVisibility.removeAllKeyboardToggleListeners();
    }
}