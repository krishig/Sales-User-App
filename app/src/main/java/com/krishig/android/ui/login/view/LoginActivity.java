package com.krishig.android.ui.login.view;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.krishig.android.Navigator.activity.ActivityNavigator;
import com.krishig.android.Navigator.fragment.FragmentNavigator;
import com.krishig.android.R;
import com.krishig.android.data.local.sharedpreferences.SharedPreferencesHelper;
import com.krishig.android.data.remote.ApiResponseObject;
import com.krishig.android.data.remote.RequestUtils;
import com.krishig.android.data.remote.api.ApiService;
import com.krishig.android.data.remote.helper.RetrofitClient;
import com.krishig.android.databinding.ActivityLoginBinding;
import com.krishig.android.model.Category;
import com.krishig.android.model.User;
import com.krishig.android.ui.base.BaseActivity;
import com.krishig.android.ui.forgetPassword.view.ForgetPasswordActivity;
import com.krishig.android.ui.home.view.HomeActivity;
import com.krishig.android.ui.login.viewmodel.LoginViewModel;
import com.library.utilities.ValidationUtils;
import com.library.utilities.activity.ActivityUtils;

import org.json.JSONObject;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class LoginActivity extends BaseActivity<ActivityLoginBinding> {

    LoginViewModel viewModel;
    @Inject
    SharedPreferencesHelper sharedPreferencesHelper;

    private ApiService apiService;

    @Override
    protected ActivityLoginBinding getViewBinding() {
        return ActivityLoginBinding.inflate(getLayoutInflater());
    }

    @Override
    protected ActivityNavigator getActivityNavigator() {
        return new ActivityNavigator(LoginActivity.this);
    }

    @Override
    protected FragmentNavigator getFragmentNavigator() {
        return null;
    }

    @Override
    protected void initializeViewModel() {
        viewModel = createViewModel(LoginViewModel.class);
    }

    @Override
    protected void initializeToolBar() {

    }

    @Override
    protected void initializeObject() {
        // getDeviceToken();

    }

    @Override
    protected void addTextChangedListener() {
        viewBinding.passwordTextInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence text, int start, int count, int after) {

                if (text.length() < 1) {
                    // viewBinding.passwordTextInputLayout.setErrorEnabled(false);
                    viewBinding.passwordTextInputLayout.setError(getString(R.string.login_error_password_message_one));
                }
                if (text.length() > 0) {
                    viewBinding.passwordTextInputLayout.setError(null);
                    // viewBinding.passwordTextInputLayout.setErrorEnabled(false);
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
    }

    @Override
    protected void setOnFocusChangeListener() {
/*
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
*/

    }

    @Override
    protected void observeViewModel() {
        Observer<String> userError = new Observer<String>() {
            @Override
            public void onChanged(@Nullable String error) {
                hideProgressDialog();
                if (error == null) {
                    showToast("Incorrect UserName Or Password");
                } else {
                    showToast(error);
                }
            }
        };
        viewModel.userError().observe(this, userError);

        final Observer<User> userSuccessObserver = new Observer<User>() {
            @Override
            public void onChanged(User user) {
                hideProgressDialog();
                sharedPreferencesHelper.setKeyToken(user.getToken());
                sharedPreferencesHelper.setRemember(true);
                Intent intent = ActivityUtils.launchActivityWithClearBackStack(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
                System.out.println(user.getToken());
            }
        };
        viewModel.userSuccess().observe(this, userSuccessObserver);

    }

    @Override
    protected void setOnClickListener() {
        viewBinding.scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                viewBinding.scrollView.post(new Runnable() {
                    public void run() {
                        viewBinding.scrollView.scrollTo(0, viewBinding.scrollView.getBottom() + viewBinding.scrollView.getScrollY());
                    }
                });
            }
        });

        viewBinding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = viewBinding.emailTextInputEditText.getText().toString();
                String password = viewBinding.passwordTextInputEditText.getText().toString();
                if (loginRegisterValidation(userName, password)) {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("username", userName);
                        jsonObject.put("password", password);
                        RequestBody requestBody = RequestUtils.createRequestBodyForString(jsonObject.toString());
                        login("Sales",requestBody, "application/json", "application/json");
                        showProgressDialog();
                        //viewModel.login(requestBody, "application/json", "application/json");
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("Exception= ", "" + e);
                    }
                }
            }
        });


        viewBinding.forgetTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });

        viewBinding.icBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }

    }

    @Override
    public void hideProgressDialog() {
        if (viewBinding.progressDialog.pleaseWaitProgressBar.getVisibility() == View.VISIBLE) {
            viewBinding.progressDialog.pleaseWaitProgressBar.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    private boolean loginRegisterValidation(String userName, String password) {
        boolean isValid = true;
        int emailValidCode = ValidationUtils.isValidEmail(userName);
        int passwordValidCode = ValidationUtils.isValidPassword(password);

/*
        if (emailValidCode > 0) {
            if (emailValidCode == 1) {
                viewBinding.emailTextInputLayout.setError(getString(R.string.email_message_one));
                isValid = false;
            } else if (emailValidCode == 2) {
                viewBinding.emailTextInputLayout.setError(getString(R.string.email_message_two));
                isValid = false;
            }
        } else {
            viewBinding.emailTextInputLayout.setError(null);
            viewBinding.emailTextInputLayout.setErrorEnabled(false);
        }
*/

        if (userName.equalsIgnoreCase("")) {
            viewBinding.emailInputLayout.setError("Please Enter UserName");
            viewBinding.emailInputLayout.setErrorEnabled(true);
        } else {
            viewBinding.emailInputLayout.setError("");
            viewBinding.emailInputLayout.setErrorEnabled(false);
        }

        if (passwordValidCode > 0) {
            if (passwordValidCode == 1) {
                viewBinding.passwordTextInputLayout.setError(getString(R.string.login_error_password_message_one));
                isValid = false;
            } else if (passwordValidCode == 2) {
                viewBinding.passwordTextInputLayout.setError(getString(R.string.login_error_password_message_two));
                isValid = false;
            } else if (passwordValidCode == 3) {
                viewBinding.passwordTextInputLayout.setError(getString(R.string.login_error_password_message_three));
                isValid = false;
            } else if (passwordValidCode == 4) {
                viewBinding.passwordTextInputLayout.setError(getString(R.string.login_error_password_message_four));
                isValid = false;
            } else if (passwordValidCode == 5) {
                viewBinding.passwordTextInputLayout.setError(getString(R.string.login_error_password_message_five));
                isValid = false;
            } else if (passwordValidCode == 6) {
                viewBinding.passwordTextInputLayout.setError(getString(R.string.login_error_password_message_six));
                isValid = false;
            } else if (passwordValidCode == 7) {
                viewBinding.passwordTextInputLayout.setError(getString(R.string.login_error_password_message_seven));
                isValid = false;
            } else if (passwordValidCode == 8) {
                viewBinding.passwordTextInputLayout.setError(getString(R.string.login_error_password_message_eight));
                isValid = false;
            }
        } else {
            viewBinding.passwordTextInputLayout.setError(null);
            viewBinding.passwordTextInputLayout.setErrorEnabled(false);
        }
        return isValid;
    }


/*    private void getDeviceToken() {
        FirebaseMessaging.getInstance().subscribeToTopic("weather")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Success";
                        if (!task.isSuccessful()) {
                            msg = "Failed";
                        }
                        //  Toast.makeText(SplashActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

        //For Registration Token
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        //  fcmToken = task.getResult();
                        sharedPreferencesHelper.setFcmToken(task.getResult());
                        // fcmToken=token;
                        // Log.e("==TAGToken==", token);
                        Log.e("token////", "" + sharedPreferencesHelper.getFcmToken());

                    }
                });
    }*/


    private void login(String role,RequestBody requestBody, String accept, String authorisation) {
        apiService = RetrofitClient.getRetrofitInstance2().create(ApiService.class);
        Call<ApiResponseObject<User>> call = apiService.login(requestBody,role, accept, authorisation);
        call.enqueue(new Callback<ApiResponseObject<User>>() {
            @Override
            public void onResponse(Call<ApiResponseObject<User>> call, Response<ApiResponseObject<User>> response) {
                ApiResponseObject<User> categories = response.body();
                hideProgressDialog();
                if (categories != null) {
                    hideProgressDialog();
                    sharedPreferencesHelper.setKeyToken(categories.getData().getToken());
                    sharedPreferencesHelper.setRemember(true);
                    Intent intent = ActivityUtils.launchActivityWithClearBackStack(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    System.out.println(categories.getData().getToken());
                }else{
                    showToast("Incorrect UserName Or Password");
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


}