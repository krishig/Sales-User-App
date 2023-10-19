package com.krishig.android.ui.home.fragments.manageProfile;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.Observer;

import com.krishig.android.Navigator.activity.ActivityNavigator;
import com.krishig.android.Navigator.fragment.FragmentNavigator;
import com.krishig.android.R;
import com.krishig.android.data.local.sharedpreferences.SharedPreferencesHelper;
import com.krishig.android.data.remote.ApiResponseObject;
import com.krishig.android.data.remote.RequestUtils;
import com.krishig.android.data.remote.api.ApiService;
import com.krishig.android.data.remote.helper.RetrofitClient;
import com.krishig.android.databinding.FragmentManageSettingBinding;
import com.krishig.android.model.PostalPincodeResponse;
import com.krishig.android.model.Product;
import com.krishig.android.model.User;
import com.krishig.android.ui.KeyboardVisibility;
import com.krishig.android.ui.base.BaseFragment;
import com.krishig.android.ui.home.fragments.home.viewmodel.HomeFragmentViewModel;
import com.krishig.android.ui.home.view.HomeActivity;
import com.krishig.android.ui.login.view.LoginActivity;
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
public class ManageProfileFragment extends BaseFragment<FragmentManageSettingBinding> {
    HomeFragmentViewModel viewModel;


    String userId = "";

    @Inject
    SharedPreferencesHelper sharedPreferencesHelper;

    private ApiService postalPincodeApi;
    private ApiService apiService;
    String data="";
    @Override
    protected ActivityNavigator getActivityNavigator() {
        return new ActivityNavigator(getActivity());
    }

    @Override
    protected FragmentNavigator getFragmentNavigator() {
        return null;
    }

    @Override
    protected FragmentManageSettingBinding getViewBinding() {
        return FragmentManageSettingBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initializeViewModel() {
        viewModel = createViewModel(HomeFragmentViewModel.class);

    }

    @Override
    protected void initializeToolBar() {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void initializeObject() {
        registerKeyboardVisibilityListener(getActivity());
        userId = extractPublicIdFromJWT(sharedPreferencesHelper.getKeyToken());
        if (userId != null) {
           getUserWithId(userId, "application/json", "application/json",
                    sharedPreferencesHelper.getKeyToken());
            showProgressDialog();
        }

    }

    public static String extractPublicIdFromJWT(String jwt) {
        try {
            String[] jwtParts = jwt.split("\\.");
            if (jwtParts.length != 3) {
                return null;
            }
            // Decode the payload
            String payloadJson = new String(Base64.decode(jwtParts[1], Base64.URL_SAFE), "UTF-8");
            // Parse the payload as a JSON object
            JSONObject payload = new JSONObject(payloadJson);
            // Extract the "public_id" claim
            int publicId = payload.optInt("public_id", -1);
            // Return the "public_id" value as a String
            return String.valueOf(publicId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void addTextChangedListener() {
        viewBinding.passwordTextInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence text, int start, int count, int after) {

                if (text.length() < 1) {
                    viewBinding.passwordTextInputLayout.setErrorEnabled(true);
                    viewBinding.passwordTextInputLayout.setError(getString(R.string.login_error_password_message_one));
                }
                if (text.length() > 0) {
                    viewBinding.passwordTextInputLayout.setError(null);
                    viewBinding.passwordTextInputLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int passwordValidCode = ValidationUtils.isValidPassword(viewBinding.passwordTextInputEditText.getText().toString());
                if (passwordValidCode > 0) {
                    if (passwordValidCode == 1) {
                        viewBinding.passwordTextInputLayout.setError(getString(R.string.login_error_password_message_one));
                    } else if (passwordValidCode == 2) {
                        viewBinding.passwordTextInputLayout.setError(getString(R.string.login_error_password_message_two));
                    } else if (passwordValidCode == 3) {
                        viewBinding.passwordTextInputLayout.setError(getString(R.string.login_error_password_message_three));
                    } else if (passwordValidCode == 4) {
                        viewBinding.passwordTextInputLayout.setError(getString(R.string.login_error_password_message_four));
                    } else if (passwordValidCode == 5) {
                        viewBinding.passwordTextInputLayout.setError(getString(R.string.login_error_password_message_five));
                    } else if (passwordValidCode == 6) {
                        viewBinding.passwordTextInputLayout.setError(getString(R.string.login_error_password_message_six));
                    } else if (passwordValidCode == 7) {
                        viewBinding.passwordTextInputLayout.setError(getString(R.string.login_error_password_message_seven));
                    } else if (passwordValidCode == 8) {
                        viewBinding.passwordTextInputLayout.setError(getString(R.string.login_error_password_message_eight));
                    }
                }
            }
        });

        viewBinding.pinCodeInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence text, int start, int count, int after) {
                if (text.length() < 1) {
                    viewBinding.pinCodeInputLayout.setErrorEnabled(true);
                    viewBinding.pinCodeInputLayout.setError("Please Enter PinCode Number");
                }
                if (text.length() > 0) {
                    viewBinding.pinCodeInputLayout.setError(null);
                    viewBinding.pinCodeInputLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                 data = s.toString();
            }
        });

    }

    @Override
    protected void setOnFocusChangeListener() {
        viewBinding.emailTextInputEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Log.e("mTag", "If view having focus");
                } else {
                    Log.e("mTag", "If view not having focus. You can validate here");
                    String input = viewBinding.emailTextInputEditText.getText().toString();
                    int emailValidCode = ValidationUtils.isValidEmail(input);
                    if (emailValidCode > 0) {
                        if (emailValidCode == 1) {
                            viewBinding.emailTextInputLayout.setError(getString(R.string.email_message_one));
                        } else if (emailValidCode == 2) {
                            viewBinding.emailTextInputLayout.setError(getString(R.string.email_message_two));
                        }
                    } else {
                        viewBinding.emailTextInputLayout.setError(null);
                        viewBinding.emailTextInputLayout.setErrorEnabled(false);
                    }
                }
            }
        });
    }

    @Override
    protected void observeViewModel() {


    }

    @Override
    protected void setOnClickListener() {
        viewBinding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = viewBinding.userNameTextInputEditText.getText().toString();
                String fullName = viewBinding.fullNameTextInputEditText.getText().toString();
                String email = viewBinding.emailTextInputEditText.getText().toString();
                String gender = viewBinding.genderAppCompatAutoCompleteTextView.getText().toString();
                String mobile = viewBinding.mobileNumberTextInputEditText.getText().toString();
                String aadhaar = viewBinding.aadhaarTextInputEditText.getText().toString();
                String password = viewBinding.passwordTextInputEditText.getText().toString();
                String houseNumber = viewBinding.houseNumberTextInputEditText.getText().toString();
                String landMark = viewBinding.landMarkTextInputEditText.getText().toString();
                String pinCode = viewBinding.pinCodeTextInputEditText.getText().toString();
                String district = viewBinding.districtTextInputEditText.getText().toString();
                String city = viewBinding.cityTextInputEditText.getText().toString();
                String state = viewBinding.stateTextInputEditText.getText().toString();


                if (loginRegisterValidation(username,fullName,email,gender,mobile,aadhaar,password,houseNumber
                        ,landMark,pinCode,district,city,state)) {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("username", username);
                        jsonObject.put("fullname", fullName);
                        jsonObject.put("email", email);
                        jsonObject.put("gender", gender);
                        jsonObject.put("mobile_number", mobile);
                        jsonObject.put("aadhaar_number", aadhaar);
                        jsonObject.put("password", password);
                        jsonObject.put("house_number", houseNumber);
                        jsonObject.put("landmark", landMark);
                        jsonObject.put("pincode", pinCode);
                        jsonObject.put("district", district);
                        jsonObject.put("city", city);
                        jsonObject.put("state", state);
                        jsonObject.put("Role", "2");

                        RequestBody requestBody1 = RequestUtils.createRequestBodyForString(jsonObject.toString());

                        editUser(userId, requestBody1, "application/json", "application/json", sharedPreferencesHelper.getKeyToken());
                        showProgressDialog();
                        Log.e("jsonObject= ", "" + jsonObject.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("Exception= ", "" + e);
                    }
                }
            }
        });

        viewBinding.pinCodeInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSuperHeroes(data);
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


    private boolean loginRegisterValidation(String username, String fullName, String email, String gender,
                                            String mobile, String aadhaar, String password, String houseNumber,
                                            String landMark, String pinCode, String district, String city,
                                            String state) {
        boolean isValid = true;
        int phoneValidCode = ValidationUtils.isPhoneNumberValid(mobile);
        int passwordValidCode = ValidationUtils.isValidPassword(password);
        int emailValidCode = ValidationUtils.isValidEmail(email);


        if (username.equalsIgnoreCase("")) {
            viewBinding.userNameInputLayout.setError("Please Enter UserName");
            viewBinding.userNameInputLayout.setErrorEnabled(true);
            isValid = false;
        } else {
            viewBinding.userNameInputLayout.setError("");
            viewBinding.userNameInputLayout.setErrorEnabled(false);
        }
        if (fullName.equalsIgnoreCase("")) {
            viewBinding.fullNameInputLayout.setError("Please Enter FullName");
            viewBinding.fullNameInputLayout.setErrorEnabled(true);
            isValid = false;
        } else {
            viewBinding.fullNameInputLayout.setError("");
            viewBinding.fullNameInputLayout.setErrorEnabled(false);
        }
        if (emailValidCode > 0) {
            if (emailValidCode == 1) {
                viewBinding.emailTextInputLayout.setError(getString(R.string.email_message_one));
                showSnackBar(viewBinding.signUpRelativeLayout, getString(R.string.email_message_one));
                isValid = false;
            } else if (emailValidCode == 2) {
                viewBinding.emailTextInputLayout.setError(getString(R.string.email_message_two));
                showSnackBar(viewBinding.signUpRelativeLayout, getString(R.string.email_message_two));
                isValid = false;
            }
        } else {
            viewBinding.emailTextInputLayout.setError(null);
            viewBinding.emailTextInputLayout.setErrorEnabled(false);
        }

        if (gender.equalsIgnoreCase("")) {
            viewBinding.genderTextInputLayout.setError("Please Select Gender");
            viewBinding.genderTextInputLayout.setErrorEnabled(true);
            isValid = false;
        } else {
            viewBinding.genderTextInputLayout.setError("");
            viewBinding.genderTextInputLayout.setErrorEnabled(false);
        }

        if (phoneValidCode > 0) {
            if (phoneValidCode == 1) {
                viewBinding.mobileNumberInputLayout.setError(getString(R.string.login_register_error_phone_number_one));
                showSnackBar(viewBinding.signUpRelativeLayout, getString(R.string.login_register_error_phone_number_one));
                isValid = false;
            } else if (phoneValidCode == 2) {
                viewBinding.mobileNumberInputLayout.setError(getString(R.string.login_register_error_phone_number_two));
                showSnackBar(viewBinding.signUpRelativeLayout, getString(R.string.login_register_error_phone_number_two));
                isValid = false;
            }
        } else {
            viewBinding.mobileNumberInputLayout.setError(null);
            viewBinding.mobileNumberInputLayout.setErrorEnabled(false);
        }

        if (aadhaar.equalsIgnoreCase("")) {
            viewBinding.aadhaarInputLayout.setError("Please Enter Aadhaar Number");
            viewBinding.aadhaarInputLayout.setErrorEnabled(true);
            isValid = false;
        } else {
            viewBinding.aadhaarInputLayout.setError("");
            viewBinding.aadhaarInputLayout.setErrorEnabled(false);
        }


        if (passwordValidCode > 0) {
            if (passwordValidCode == 1) {
                viewBinding.passwordTextInputLayout.setError(getString(R.string.login_error_password_message_one));
                showSnackBar(viewBinding.signUpRelativeLayout, getString(R.string.login_error_password_message_one));
                isValid = false;
            } else if (passwordValidCode == 2) {
                viewBinding.passwordTextInputLayout.setError(getString(R.string.login_error_password_message_two));
                showSnackBar(viewBinding.signUpRelativeLayout, getString(R.string.login_error_password_message_two));

                isValid = false;
            } else if (passwordValidCode == 3) {
                viewBinding.passwordTextInputLayout.setError(getString(R.string.login_error_password_message_three));
                showSnackBar(viewBinding.signUpRelativeLayout, getString(R.string.login_error_password_message_three));

                isValid = false;
            } else if (passwordValidCode == 4) {
                viewBinding.passwordTextInputLayout.setError(getString(R.string.login_error_password_message_four));
                showSnackBar(viewBinding.signUpRelativeLayout, getString(R.string.login_error_password_message_four));

                isValid = false;
            } else if (passwordValidCode == 5) {
                viewBinding.passwordTextInputLayout.setError(getString(R.string.login_error_password_message_five));
                showSnackBar(viewBinding.signUpRelativeLayout, getString(R.string.login_error_password_message_five));

                isValid = false;
            } else if (passwordValidCode == 6) {
                viewBinding.passwordTextInputLayout.setError(getString(R.string.login_error_password_message_six));
                showSnackBar(viewBinding.signUpRelativeLayout, getString(R.string.login_error_password_message_six));

                isValid = false;
            } else if (passwordValidCode == 7) {
                viewBinding.passwordTextInputLayout.setError(getString(R.string.login_error_password_message_seven));
                showSnackBar(viewBinding.signUpRelativeLayout, getString(R.string.login_error_password_message_seven));

                isValid = false;
            } else if (passwordValidCode == 8) {
                viewBinding.passwordTextInputLayout.setError(getString(R.string.login_error_password_message_eight));
                showSnackBar(viewBinding.signUpRelativeLayout, getString(R.string.login_error_password_message_eight));

                isValid = false;
            }
        } else {
            viewBinding.passwordTextInputLayout.setError(null);
            viewBinding.passwordTextInputLayout.setErrorEnabled(false);
        }

        if (houseNumber.equalsIgnoreCase("")) {
            viewBinding.houseNumberInputLayout.setError("Please Enter House Number");
            viewBinding.houseNumberInputLayout.setErrorEnabled(true);
            isValid = false;
        } else {
            viewBinding.houseNumberInputLayout.setError("");
            viewBinding.houseNumberInputLayout.setErrorEnabled(false);
        }
        if (landMark.equalsIgnoreCase("")) {
            viewBinding.landMarkInputLayout.setError("Please Enter LandMark");
            viewBinding.landMarkInputLayout.setErrorEnabled(true);
            isValid = false;
        } else {
            viewBinding.landMarkInputLayout.setError("");
            viewBinding.landMarkInputLayout.setErrorEnabled(false);
        }

        if (pinCode.equalsIgnoreCase("")) {
            viewBinding.pinCodeInputLayout.setError("Please Enter PinCode Number");
            viewBinding.pinCodeInputLayout.setErrorEnabled(true);
            isValid = false;
        } else {
            viewBinding.pinCodeInputLayout.setError("");
            viewBinding.pinCodeInputLayout.setErrorEnabled(false);
        }
        if (district.equalsIgnoreCase("")) {
            viewBinding.districtInputLayout.setError("Please Enter District");
            viewBinding.districtInputLayout.setErrorEnabled(true);
            isValid = false;
        } else {
            viewBinding.districtInputLayout.setError("");
            viewBinding.districtInputLayout.setErrorEnabled(false);
        }
        if (city.equalsIgnoreCase("")) {
            viewBinding.cityInputLayout.setError("Please Enter City");
            viewBinding.cityInputLayout.setErrorEnabled(true);
            isValid = false;
        } else {
            viewBinding.cityInputLayout.setError("");
            viewBinding.cityInputLayout.setErrorEnabled(false);
        }
        if (state.equalsIgnoreCase("")) {
            viewBinding.stateInputLayout.setError("Please Enter State");
            viewBinding.stateInputLayout.setErrorEnabled(true);
            isValid = false;
        } else {
            viewBinding.stateInputLayout.setError("");
            viewBinding.stateInputLayout.setErrorEnabled(false);
        }
        return isValid;
    }

    private static final String[] gender = new String[]{
            "Male", "Female", "Other"
    };

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
                        viewBinding.districtTextInputEditText.setText(postalPincodeResponse.get(0).getPostOffice().get(0).getDistrict());
                        viewBinding.cityTextInputEditText.setText(postalPincodeResponse.get(0).getPostOffice().get(0).getDivision());
                        viewBinding.stateTextInputEditText.setText(postalPincodeResponse.get(0).getPostOffice().get(0).getState());
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


    private void getUserWithId(String id,
                               String accept,
                               String authorisation,
                               String token) {
        apiService = RetrofitClient.getRetrofitInstance2().create(ApiService.class);
        // Example API call
        Call<ApiResponseObject<User>> call = apiService.getUserWithId(id, accept, authorisation, token);
        call.enqueue(new Callback<ApiResponseObject<User>>() {
            @Override
            public void onResponse(Call<ApiResponseObject<User>> call, Response<ApiResponseObject<User>> response) {
                ApiResponseObject<User> categories = response.body();
                hideProgressDialog();
                if (categories.getData() != null) {
                    viewBinding.userNameTextInputEditText.setText(categories.getData().getUsername());
                    viewBinding.fullNameTextInputEditText.setText(categories.getData().getFullname());
                    viewBinding.emailTextInputEditText.setText(categories.getData().getEmail());
                    viewBinding.genderAppCompatAutoCompleteTextView.setText(categories.getData().getGender());
                    viewBinding.mobileNumberTextInputEditText.setText(categories.getData().getMobileNumber());
                    viewBinding.aadhaarTextInputEditText.setText(categories.getData().getAdhaarNumber());
                    viewBinding.passwordTextInputEditText.setText(categories.getData().getPassword());
                    viewBinding.houseNumberTextInputEditText.setText(categories.getData().getHouseNumber());
                    viewBinding.landMarkTextInputEditText.setText(categories.getData().getLandmark());
                    viewBinding.pinCodeTextInputEditText.setText(categories.getData().getPincode());
                    viewBinding.districtTextInputEditText.setText(categories.getData().getDistrict2());
                    viewBinding.cityTextInputEditText.setText(categories.getData().getCity());
                    viewBinding.stateTextInputEditText.setText(categories.getData().getState2());
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, gender);
                    viewBinding.genderAppCompatAutoCompleteTextView.setAdapter(adapter);
                }else{
                    if(response.body().getMessage().equalsIgnoreCase("Please login again!"));{
                        sharedPreferencesHelper.setCustomerName(null);
                        sharedPreferencesHelper.setCustomerCartId(null);
                        sharedPreferencesHelper.setCustomerID(null);
                        sharedPreferencesHelper.setCustomerMobileNumber(null);
                        sharedPreferencesHelper.setRemember(false);
                        Intent intent = ActivityUtils.launchActivityWithClearBackStack(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponseObject<User>> call, Throwable t) {
                // Handle network or API call failure
                Log.e("", "onFailure  =" + call.toString());
                Log.e("", "onFailure  =" + t);

            }
        });
    }

    private void editUser(String id, RequestBody jsonObject, String accept, String authorisation, String token) {
        apiService = RetrofitClient.getRetrofitInstance2().create(ApiService.class);
        // Example API call
        Call<ApiResponseObject<User>> call = apiService.editUser(id, jsonObject, accept, authorisation, token);
        call.enqueue(new Callback<ApiResponseObject<User>>() {
            @Override
            public void onResponse(Call<ApiResponseObject<User>> call, Response<ApiResponseObject<User>> response) {
                ApiResponseObject<User> categories = response.body();
                hideProgressDialog();
                if (categories != null) {

                }
            }

            @Override
            public void onFailure(Call<ApiResponseObject<User>> call, Throwable t) {
                // Handle network or API call failure
                Log.e("", "onFailure  =" + call.toString());
                Log.e("", "onFailure  =" + t);

            }
        });
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