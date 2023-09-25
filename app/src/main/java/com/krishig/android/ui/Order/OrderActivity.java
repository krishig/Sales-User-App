package com.krishig.android.ui.Order;

import android.content.Intent;
import android.view.View;

import com.krishig.android.Navigator.activity.ActivityNavigator;
import com.krishig.android.Navigator.fragment.FragmentNavigator;
import com.krishig.android.data.local.sharedpreferences.SharedPreferencesHelper;
import com.krishig.android.databinding.ActivityOrderBinding;
import com.krishig.android.ui.AppConstants;
import com.krishig.android.ui.base.BaseActivity;
import com.krishig.android.ui.home.view.HomeActivity;
import com.library.utilities.activity.ActivityUtils;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class OrderActivity extends BaseActivity<ActivityOrderBinding> {

    String orderId = "";
    @Inject
    SharedPreferencesHelper sharedPreferencesHelper;
    @Override
    protected ActivityOrderBinding getViewBinding() {
        return ActivityOrderBinding.inflate(getLayoutInflater());
    }

    @Override
    protected ActivityNavigator getActivityNavigator() {
        return new ActivityNavigator(OrderActivity.this);
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
        sharedPreferencesHelper.setCustomerName(null);
        sharedPreferencesHelper.setCustomerCartId(null);
        sharedPreferencesHelper.setCustomerID(null);
        sharedPreferencesHelper.setCustomerMobileNumber(null);
        if (getIntent().getExtras() != null) {
            orderId = getIntent().getExtras().getString(AppConstants.Extras.ORDER_ID);
        }
        if (orderId != null) {
            viewBinding.paymentMethodTextView.setText("Your Order has been Accepted And its OrderId is " + orderId);
        }
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
        viewBinding.icBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = ActivityUtils.launchActivityWithClearBackStack(OrderActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        viewBinding.trackOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = ActivityUtils.launchActivityWithClearBackStack(OrderActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        viewBinding.backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = ActivityUtils.launchActivityWithClearBackStack(OrderActivity.this, HomeActivity.class);
                startActivity(intent);
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

    }

    @Override
    public void hideProgressDialog() {

    }

}