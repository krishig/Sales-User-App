package com.krishig.android.ui.splash.view;

import android.content.Intent;
import android.os.Handler;

import com.krishig.android.Navigator.activity.ActivityNavigator;
import com.krishig.android.Navigator.fragment.FragmentNavigator;
import com.krishig.android.data.local.sharedpreferences.SharedPreferencesHelper;
import com.krishig.android.databinding.ActivitySplashBinding;
import com.krishig.android.ui.AppConstants;
import com.krishig.android.ui.base.BaseActivity;
import com.krishig.android.ui.home.view.HomeActivity;
import com.krishig.android.ui.login.view.LoginActivity;
import com.krishig.android.ui.welcome.view.WelcomeActivity;
import com.library.utilities.activity.ActivityUtils;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SplashActivity extends BaseActivity<ActivitySplashBinding> {
    private Handler handler;
    private Runnable runnable;

    @Inject
    SharedPreferencesHelper sharedPreferencesHelper;

    @Override
    protected ActivitySplashBinding getViewBinding() {
        return ActivitySplashBinding.inflate(getLayoutInflater());
    }

    @Override
    protected ActivityNavigator getActivityNavigator() {
        return new ActivityNavigator(SplashActivity.this);
    }

    @Override
    protected FragmentNavigator getFragmentNavigator() {
        return null;
    }

    @Override
    protected void initializeViewModel() {

    }

    @Override
    protected void initializeToolBar() {

    }

    @Override
    protected void initializeObject() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                sharedPreferencesHelper.setCustomerName(null);
                sharedPreferencesHelper.setCustomerCartId(null);
                sharedPreferencesHelper.setCustomerID(null);
                sharedPreferencesHelper.setCustomerMobileNumber(null);

                if (sharedPreferencesHelper.getRemember()) {
                    Intent intent = ActivityUtils.launchActivityWithClearBackStack(SplashActivity.this, HomeActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = ActivityUtils.launchActivityWithClearBackStack(SplashActivity.this, WelcomeActivity.class);
                    startActivity(intent);
                }
            }
        };
    }

    @Override
    protected void addTextChangedListener() {

    }

    @Override
    protected void setOnFocusChangeListener() {

    }

    @Override
    protected void observeViewModel() {

    }

    @Override
    protected void setOnClickListener() {

    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public void showProgressDialog() {

    }

    @Override
    public void hideProgressDialog() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (handler != null) {
            handler.postDelayed(runnable, AppConstants.Delay.SPLASH);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
    }
}